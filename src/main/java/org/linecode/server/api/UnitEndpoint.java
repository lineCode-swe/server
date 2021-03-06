/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/
package org.linecode.server.api;

import org.linecode.server.Position;
import org.linecode.server.api.message.CommandToUnit;
import org.linecode.server.api.message.CommandToUnitEncoder;
import org.linecode.server.api.message.ErrorFromUnit;
import org.linecode.server.api.message.KeepAliveToUnit;
import org.linecode.server.api.message.KeepAliveToUnitEncoder;
import org.linecode.server.api.message.Message;
import org.linecode.server.api.message.PathRequestFromUnit;
import org.linecode.server.api.message.PositionFromUnit;
import org.linecode.server.api.message.SpeedFromUnit;
import org.linecode.server.api.message.StartToUnit;
import org.linecode.server.api.message.StartToUnitEncoder;
import org.linecode.server.api.message.StatusFromUnit;
import org.linecode.server.api.message.UnitMessageDecoder;
import org.linecode.server.api.message.UnitStopCommand;
import org.linecode.server.business.MapService;
import org.linecode.server.business.UnitService;
import org.linecode.server.business.UnitStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@ServerEndpoint(
        value = "/unit/{id}",
        configurator = EndpointConfigurator.class,
        decoders = {
                UnitMessageDecoder.class
        },
        encoders = {
                KeepAliveToUnitEncoder.class,
                StartToUnitEncoder.class,
                CommandToUnitEncoder.class
        }
)
public class UnitEndpoint {
    private final Logger logger = LoggerFactory.getLogger(UnitEndpoint.class);

    private Session session;
    private String id;
    private final Timer timer;
    private final UnitService unitService;
    private final MapService mapService;

    @Inject
    public UnitEndpoint(Timer timer, UnitService unitService,
                        MapService mapService) {

        this.timer = timer;
        this.unitService = unitService;
        this.mapService = mapService;
        this.session = null;
        this.id = null;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        if(unitService.isUnit(id)) {

            this.session = session;
            this.id = id;
            unitService.newStatus(id,UnitStatus.BASE);
            unitService.connectStartSignal(this::sendStart);
            unitService.connectStopSignal(this::sendStop);
            unitService.connectUnitCloseSignal(this::closeConnection);
            unitService.connectBaseSignal(this::sendBase);
            unitService.connectShutdownSignal(this::sendShutdown);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    keepAlive();
                }
            }, 25000L, 25000L);

            logger.info(String.format("UnitEndpoint: Opened connection: %s", id));
        } else {
            try {
                session.close();
            } catch (IOException e) {
                onError(session, e);
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        timer.cancel();
        unitService.newStatus(id,UnitStatus.DISCONNECTED);
        unitService.newPosition(id, new Position(-1,-1));
        logger.info(String.format("UnitEndpoint: Closed connection: %s", id));
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        switch (message.getType()) {
            case "ErrorFromUnit":
                ErrorFromUnit errorFromUnit = (ErrorFromUnit) message;
                unitService.newError(id, errorFromUnit.getError());
                break;
            case "PathRequestFromUnit":
                PathRequestFromUnit pathRequestFromUnit = (PathRequestFromUnit) message;
                List<Position> path = mapService.getNextPath(id,mapService.checkPremises(unitService.getPosition(id)));
                if (!path.isEmpty()) {
                    pathFound(path);
                } else {
                    unitError(404);
                }
                break;
            case "PositionFromUnit":
                PositionFromUnit positionFromUnit = (PositionFromUnit) message;
                Position unitPosition = positionFromUnit.getPosition();
                List<Position> obstacles = positionFromUnit.getObstacles();
                if(unitService.getUnitsPosition().contains(unitPosition) &&
                        !unitService.getBase(id).equals(unitPosition)){
                    mapService.newObstacleList(obstacles, unitPosition);
                    unitService.newPosition(id, unitPosition);
                    unitError(123);
                } else {
                    unitService.newPosition(id, unitPosition);
                    List<Position> pois = unitService.getPoiList(id);
                    if (!pois.isEmpty()) {
                        if (pois.get(0).equals(unitPosition)) {
                            pois.remove(0);
                            unitService.setPoiList(id, pois);
                        }
                    }

                    mapService.newObstacleList(obstacles, unitPosition);
                    List<Position> premises = mapService.checkPremises(unitPosition);
                    if (!obstacles.isEmpty() || !premises.isEmpty()) {
                        logger.info(String.format("UnitEndpoint: Rilevati ostacoli nelle vicinanze di : %s", id));
                        List<Position> newPath = mapService.getNextPath(id, premises);
                        if (!newPath.isEmpty()) {
                            logger.info(String.format("UnitEndpoint: Ricalcolo e invio del percorso a : %s", id));
                            pathFound(newPath);
                        } else {
                            unitError(404);
                        }
                    }
                }
                break;
            case "SpeedFromUnit":
                SpeedFromUnit speedFromUnit = (SpeedFromUnit) message;
                unitService.newSpeed(id, speedFromUnit.getSpeed());
                break;
            case "StatusFromUnit":
                StatusFromUnit statusFromUnit = (StatusFromUnit) message;
                unitService.newStatus(id, statusFromUnit.getStatus());
                break;
            case "":
            default:
                onError(session, new Exception("UnitEndpoint: unrecognized type of message"));
                break;
        }
    }
    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error(String.format("UnitEndpoint (%s): Exception %s has been thrown: %s\nStack trace: %s",
                session.getId(),
                throwable.getClass().getName(),
                throwable.getMessage(),
                Arrays.toString(throwable.getStackTrace())));
    }

    private void keepAlive() {
        send(new KeepAliveToUnit("keepalive"));
    }

    public void send(Message message) {
        logger.info(String.format("Sending %s to %s", message.getType(), session.getId()));
        try {
            session.getBasicRemote().sendObject(message);
        } catch (Exception e) {
            onError(session, e);
        }
    }

    public void sendStart(String id) {
        if (this.id.equals(id)){
            List<Position> path = mapService.getNextPath(id,mapService.checkPremises(unitService.getPosition(id)));
            logger.info(path.toString());
            if (!path.isEmpty()) {
                logger.info("PATH CALCOLATO");
                pathFound(path);
            } else {
                unitError(404);
            }
        }
    }

    public void sendStop(String id) {
        if (this.id.equals(id)){
            unitService.newStatus(id,UnitStatus.STOP);
            send(new CommandToUnit(UnitStopCommand.STOP));

        }
    }

    public void sendBase(String id){
        if (this.id.equals(id)) {
            send(new CommandToUnit(UnitStopCommand.STOP));
            unitService.newStatus(id,UnitStatus.STOP);
            unitService.setPoiList(id, new ArrayList<Position>());
            List<Position> path = mapService.getNextPath(id,mapService.checkPremises(unitService.getPosition(id)));
            if (!path.isEmpty()) {
                pathFound(path);
            } else {
                unitError(404);
            }
        }
    }

    public void sendShutdown(String id) {
        if (this.id.equals(id)) {
            send(new CommandToUnit(UnitStopCommand.SHUTDOWN));
        }
    }

    public void closeConnection(String id) {
        if (this.id.equals(id)) {
            try {
                this.session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void unitError(int error){
        send(new CommandToUnit(UnitStopCommand.ERROR));
        unitService.newStatus(id, UnitStatus.ERROR);
        unitService.newError(id,error);
    }

    public void pathFound(List<Position> path){
        unitService.newError(id,0);
        unitService.newStatus(id,UnitStatus.GOINGTO);
        send(new StartToUnit(path));
    }
}
