/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.api;

import org.linecode.server.Position;
import org.linecode.server.api.message.CommandToUnit;
import org.linecode.server.api.message.CommandToUnitEncoder;
import org.linecode.server.api.message.ErrorFromUnit;
import org.linecode.server.api.message.KeepAliveToUnit;
import org.linecode.server.api.message.KeepAliveToUnitEncoder;
import org.linecode.server.api.message.Message;
import org.linecode.server.api.message.PositionFromUnit;
import org.linecode.server.api.message.SpeedFromUnit;
import org.linecode.server.api.message.StartToUnit;
import org.linecode.server.api.message.StartToUnitEncoder;
import org.linecode.server.api.message.StatusFromUnit;
import org.linecode.server.api.message.UnitMessageDecoder;
import org.linecode.server.api.message.UnitStopCommand;
import org.linecode.server.business.MapService;
import org.linecode.server.business.UnitService;
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
                List<Position> path = mapService.getNextPath(id);
                if (!path.isEmpty()) {
                    send(new StartToUnit(path));
                } else {
                    send(new ErrorFromUnit(404));
                    unitService.newError(id,404);
                }
                break;
            case "PositionFromUnit":
                PositionFromUnit positionFromUnit = (PositionFromUnit) message;
                logger.info("Arrivata posizione da unit");
                logger.info(positionFromUnit.getObstacles().toString());
                unitService.newPosition(id, positionFromUnit.getPosition());
                List<Position> pois = unitService.getPoiList(id);
                if(!pois.isEmpty()) {
                    if (pois.get(0).equals(positionFromUnit.getPosition())) {
                        logger.info(String.format("UnitEndpoint: Unità è arrivata al poi : ", id));
                        pois.remove(0);
                        unitService.setPoiList(id, pois);
                    }
                }
                List<Position> obstacles = positionFromUnit.getObstacles();
                mapService.newObstacleList(obstacles,positionFromUnit.getPosition());
                if(!obstacles.isEmpty() || mapService.checkPremises(positionFromUnit.getPosition())){
                    logger.info(String.format("UnitEndpoint: Rilevati ostacoli nelle vicinanze di : ", id));
                    logger.info(String.format("UnitEndpoint: Invio segnale stop a : ", id));
                    sendStop(id);
                    List<Position> newPath = mapService.getNextPath(id);
                    if (!newPath.isEmpty()) {
                        logger.info(String.format("UnitEndpoint: Ricalcolo e invio del percorso a : ", id));
                        send(new StartToUnit(newPath));
                    } else {
                        logger.info(String.format("UnitEndpoint: Percorso incalcolabile, errore inviato a : ", id));
                        send(new ErrorFromUnit(404));
                        unitService.newError(id,404);
                    }
                } logger.info("Nessun ostacolo rilevato");
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
            List<Position> path = mapService.getNextPath(id);
            if (!path.isEmpty()) {
                send(new StartToUnit(path));
            } else {
                send(new ErrorFromUnit(404));
                unitService.newError(id,404);
            }
        }
    }

    public void sendStop(String id) {
        if (this.id.equals(id)){
            send(new CommandToUnit(UnitStopCommand.STOP));
        }
    }

    public void sendBase(String id){
        if (this.id.equals(id)) {
            send(new CommandToUnit(UnitStopCommand.STOP));
            unitService.setPoiList(id, new ArrayList<Position>());
            List<Position> path = mapService.getNextPath(id);
            if (!path.isEmpty()) {
                send(new StartToUnit(path));
            } else {
                send(new ErrorFromUnit(404));
                unitService.newError(id, 404);
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
}
