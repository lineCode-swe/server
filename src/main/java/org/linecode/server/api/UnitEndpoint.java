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
import org.linecode.server.api.message.ObstacleListFromUnit;
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

import javax.websocket.Session;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
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

            System.out.println("UnitEndpoint: Opened connection: " + id);
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
        System.out.println("UnitEndpoint: Closed connection: " + id);
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        switch (message.getType()) {
            case "ErrorFromUnit":
                ErrorFromUnit errorFromUnit = (ErrorFromUnit) message;
                unitService.newError(id, errorFromUnit.getError());
                break;

            case "ObstacleListFromUnit":
                ObstacleListFromUnit obstacleListFromUnit = (ObstacleListFromUnit) message;
                mapService.newObstacleList(obstacleListFromUnit.getObstacleList());
                break;

            case "PathRequestFromUnit":
                List<Position> path = mapService.getNextPath(id);
                send(new StartToUnit(path));
                break;

            case "PositionFromUnit":
                PositionFromUnit positionFromUnit = (PositionFromUnit) message;
                unitService.newPosition(id, positionFromUnit.getPosition());
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
        System.out.println("UnitEndpoint (" + id + "): Exception " + throwable.getClass().getName() +
                " has been thrown: " + throwable.getMessage() +
                "\nStack trace:" + Arrays.toString(throwable.getStackTrace()));
    }

    private void keepAlive() {
        send(new KeepAliveToUnit("keepalive"));
    }

    public void send(Message message) {
        System.out.println("Sending " + message.getType() + " to " + id);
        try {
            session.getBasicRemote().sendObject(message);
        } catch (Throwable e) {
            onError(session, e);
        }
    }

    public void sendStart(String id) {
        if (this.id.equals(id)){
            send(new StartToUnit(mapService.getNextPath(id)));
        }
    }

    public void sendStop(String id) {
        if (this.id.equals(id)){
            send(new CommandToUnit(UnitStopCommand.STOP));
        }
    }

    public void sendBase(String id){
        if (this.id.equals(id)) {
            send(new CommandToUnit(UnitStopCommand.BASE));
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
