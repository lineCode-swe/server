/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api;

import org.linecode.server.Position;
import org.linecode.server.api.message.AuthToUi;
import org.linecode.server.api.message.AuthToUiEncoder;
import org.linecode.server.api.message.DeleteUnitFromUi;
import org.linecode.server.api.message.DeleteUserFromUi;
import org.linecode.server.api.message.KeepAliveToUi;
import org.linecode.server.api.message.KeepAliveToUiEncoder;
import org.linecode.server.api.message.LoginFromUi;
import org.linecode.server.api.message.MapFromUi;
import org.linecode.server.api.message.MapToUi;
import org.linecode.server.api.message.MapToUiEncoder;
import org.linecode.server.api.message.Message;
import org.linecode.server.api.message.ObstaclesToUi;
import org.linecode.server.api.message.ObstaclesToUiEncoder;
import org.linecode.server.api.message.UiMessageDecoder;
import org.linecode.server.api.message.UnitErrorToUi;
import org.linecode.server.api.message.UnitErrorToUiEncoder;
import org.linecode.server.api.message.UnitFromUi;
import org.linecode.server.api.message.UnitPoiToUi;
import org.linecode.server.api.message.UnitPoiToUiEncoder;
import org.linecode.server.api.message.UnitPositionToUi;
import org.linecode.server.api.message.UnitPositionToUiEncoder;
import org.linecode.server.api.message.UnitSpeedToUi;
import org.linecode.server.api.message.UnitSpeedToUiEncoder;
import org.linecode.server.api.message.UnitStartFromUi;
import org.linecode.server.api.message.UnitStatusToUi;
import org.linecode.server.api.message.UnitStatusToUiEncoder;
import org.linecode.server.api.message.UnitStopFromUi;
import org.linecode.server.api.message.UnitsToUi;
import org.linecode.server.api.message.UnitsToUiEncoder;
import org.linecode.server.api.message.UserFromUi;
import org.linecode.server.api.message.UsersToUi;
import org.linecode.server.api.message.UsersToUiEncoder;
import org.linecode.server.business.AuthStatus;
import org.linecode.server.business.MapService;
import org.linecode.server.business.UnitService;
import org.linecode.server.business.UserService;
import org.linecode.server.business.Grid;
import org.linecode.server.business.User;
import org.linecode.server.business.Unit;
import org.linecode.server.business.UnitStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@ServerEndpoint(
        value = "/ui",
        configurator = EndpointConfigurator.class,
        decoders = {
                UiMessageDecoder.class
        },
        encoders = {
                KeepAliveToUiEncoder.class,
                UnitsToUiEncoder.class,
                UnitStatusToUiEncoder.class,
                UnitPoiToUiEncoder.class,
                UnitStatusToUiEncoder.class,
                UnitSpeedToUiEncoder.class,
                UnitErrorToUiEncoder.class,
                UnitPositionToUiEncoder.class,
                UsersToUiEncoder.class,
                MapToUiEncoder.class,
                AuthToUiEncoder.class,
                ObstaclesToUiEncoder.class
        }
)
public class UiEndpoint {
    private final Logger logger = LoggerFactory.getLogger(UiEndpoint.class);

    private Session session;
    private AuthStatus logged;
    private final Timer timer;
    private final UserService userService;
    private final UnitService unitService;
    private final MapService mapService;

    @Inject
    public UiEndpoint(Timer timer, UserService userService,
                      UnitService unitService, MapService mapService) {
        this.timer = timer;
        this.userService = userService;
        this.unitService = unitService;
        this.mapService = mapService;

        this.logged = AuthStatus.NO_AUTH;
        this.session = null;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                keepAlive();
            }
        }, 25000L, 25000L);

        mapService.connectMapSignal(this::sendMap);
        mapService.connectObstaclesSignal(this::sendObstacle);
        unitService.connectPositionSignal(this::sendUnitPosition);
        unitService.connectStatusSignal(this::sendUnitStatus);
        unitService.connectErrorSignal(this::sendUnitError);
        unitService.connectSpeedSignal(this::sendUnitSpeed);
        unitService.connectUnitSignal(this::sendUnits);
        unitService.connectPoiListSignal(this::sendUnitPoi);
        userService.connectUsersSignal(this::sendUsers);

        sendMap(mapService.getMap());
        sendUnits(unitService.getUnits());
        sendUsers(userService.getUsers());
        // TODO: inviare anche ostacoli

        logger.info("UIEndpoint: Opened connection: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        timer.cancel();
        logger.info("UIEndpoint: Closed connection: " + session.getId());
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        switch (message.getType()) {
            case "LoginFromUi":
                LoginFromUi loginFromUi = (LoginFromUi) message;
                login(loginFromUi);
                break;

            case "LogoutFromUi":
                logout();
                break;

            case "UserFromUi":
                UserFromUi userFromUi = (UserFromUi) message;
                newUser(userFromUi);
                break;

            case "DeleteUserFromUi":
                DeleteUserFromUi deleteUserFromUi = (DeleteUserFromUi) message;
                deleteUser(deleteUserFromUi);
                break;

            case "MapFromUi":
                MapFromUi mapFromUi = (MapFromUi) message;
                newMap(mapFromUi);
                break;

            case "UnitStopFromUi":
                UnitStopFromUi unitStopFromUi = (UnitStopFromUi) message;
                unitStop(unitStopFromUi);
                break;

            case "UnitStartFromUi":
                UnitStartFromUi unitStartFromUi = (UnitStartFromUi) message;
                unitStart(unitStartFromUi);
                break;

            case "UnitFromUi":
                UnitFromUi unitFromUi = (UnitFromUi) message;
                newUnit(unitFromUi);
                break;

            case "DeleteUnitFromUi":
                DeleteUnitFromUi deleteUnitFromUi = (DeleteUnitFromUi) message;
                deleteUnit(deleteUnitFromUi);
                break;

            case "":
            default:
                onError(session, new Exception("UIEndpoint: unrecognized type of message"));
        }

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("UIEndpoint (" + session.getId() + "): Exception " + throwable.getClass().getName() +
                " has been thrown: " + throwable.getMessage() +
                "\nStack trace:" + Arrays.toString(throwable.getStackTrace()));
    }

    public void login(LoginFromUi loginFromUi) {
        logged = userService.login(loginFromUi.getUser(), loginFromUi.getPassword());
        sendAuth(logged);
    }

    public void logout() {
        logged = AuthStatus.NO_AUTH;
        sendAuth(logged);
    }

    public void newUser(UserFromUi userFromUi) {
        if (logged == AuthStatus.ADMIN) {
            userService.newUser(userFromUi.getUser(), userFromUi.getPassword(), userFromUi.isAdmin());
        } else {
            onError(session, new Exception("Requested new user registration while not logged in as admin"));
        }
    }

    public void deleteUser(DeleteUserFromUi deleteUserFromUi) {
        if (logged == AuthStatus.ADMIN) {
            userService.delUser(deleteUserFromUi.getUser());
        } else {
            onError(session, new Exception("Requested user deletion while not logged in as admin"));
        }
    }

    public void newMap(MapFromUi mapFromUi) {
        if (logged == AuthStatus.ADMIN) {
            mapService.newMap(mapFromUi.getMapConfig());
        } else {
            onError(session, new Exception("Requested new map configuration while not logged in as admin"));
        }
    }

    public void unitStop(UnitStopFromUi unitStopFromUi) {
        if (logged == AuthStatus.AUTH || logged == AuthStatus.ADMIN) {
            switch (unitStopFromUi.getCommand()) {
                case STOP:
                    unitService.stop(unitStopFromUi.getId());
                    break;

                case BASE:
                    unitService.base(unitStopFromUi.getId());
                    break;

                case SHUTDOWN:
                    unitService.shutdown(unitStopFromUi.getId());
                    break;

                default:
                    onError(session, new Exception("Unrecognized UnitStopCommand sent by UI"));
                    break;
            }
        } else {
            onError(session, new Exception("Requested stop command while not logged"));
        }
    }

    public void unitStart(UnitStartFromUi unitStartFromUi) {
        if (logged == AuthStatus.AUTH || logged == AuthStatus.ADMIN) {
            unitService.start(unitStartFromUi.getId(), unitStartFromUi.getPoiList());
        } else {
            onError(session, new Exception("Requested start command while not logged"));
        }
    }

    public void newUnit(UnitFromUi unitFromUi) {
        if (logged == AuthStatus.ADMIN) {
            unitService.newUnit(unitFromUi.getId(), unitFromUi.getName(), unitFromUi.getBase());
        } else {
            onError(session, new Exception("Requested new unit while not logged as admin"));
        }
    }

    public void deleteUnit(DeleteUnitFromUi deleteUnitFromUi) {
        if (logged == AuthStatus.ADMIN) {
            unitService.delUnit(deleteUnitFromUi.getId());
        } else {
            onError(session, new Exception("Requested unit deletion while not logged as admin"));
        }
    }

    private void send(Message message) {
        logger.info("Sending " + message.getType() + " to " + session.getId());
        try {
            session.getBasicRemote().sendObject(message);
        } catch (Throwable e) {
            onError(session, e);
        }
    }

    public void keepAlive() {
        send(new KeepAliveToUi("keepalive"));
    }

    public void sendAuth(AuthStatus logged) {
        send(new AuthToUi(logged));
    }

    public void sendMap(Grid map) {
        send(new MapToUi(map));
    }

    public void sendObstacle(List<Position> positionList) {
        send(new ObstaclesToUi(positionList));
    }

    public void sendUsers(List<User> userList) {
        send(new UsersToUi(userList));
    }

    public void sendUnits(List<Unit> unitList) {
        send(new UnitsToUi(unitList));
    }

    public void sendUnitPosition(String id, Position position) {
        send(new UnitPositionToUi(id, position));
    }

    public void sendUnitStatus(String id, UnitStatus status) {
        send(new UnitStatusToUi(id, status));
    }

    public void sendUnitError(String id, int error) {
        send(new UnitErrorToUi(id, error));
    }

    public void sendUnitSpeed(String id, int speed) {
        send(new UnitSpeedToUi(id, speed));
    }

    public void sendUnitPoi(String id, List<Position> poiList) {
        send(new UnitPoiToUi(id, poiList));
    }
}
