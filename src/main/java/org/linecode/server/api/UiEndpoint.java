/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api;

import org.linecode.server.Position;
import org.linecode.server.api.message.KeepAliveToUi;
import org.linecode.server.api.message.KeepAliveToUiEncoder;
import org.linecode.server.api.message.LoginFromUi;
import org.linecode.server.api.message.Message;
import org.linecode.server.api.message.MessageDecoder;
import org.linecode.server.api.message.UserFromUi;
import org.linecode.server.business.AuthStatus;
import org.linecode.server.business.MapService;
import org.linecode.server.business.UnitService;
import org.linecode.server.business.UserService;
import org.linecode.server.business.Grid;
import org.linecode.server.business.User;
import org.linecode.server.business.Unit;
import org.linecode.server.business.UnitStatus;


import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

@ServerEndpoint(
        value = "/ui",
        configurator = EndpointConfigurator.class,
        decoders = {
                MessageDecoder.class
        },
        encoders = {
                KeepAliveToUiEncoder.class
        }
)
public class UiEndpoint {
    private Session session;
    private AuthStatus logged;
    private final ResetTimer timer;
    private final UserService userService;
    private final UnitService unitService;
    private final MapService mapService;

    @Inject
    public UiEndpoint(ResetTimer timer, UserService userService,
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
        this.mapService.connectMapSignal(this::sendMap);
        this.mapService.connectObstaclesSignal(this::sendObstacle);
        this.unitService.connectPositionSignal(this::sendUnitPosition);
        this.unitService.connectStatusSignal(this::sendUnitStatus);
        this.unitService.connectErrorSignal(this::sendUnitError);
        this.unitService.connectSpeedSignal(this::sendUnitSpeed);
        this.userService.connectUsersSignal(this::sendUsers);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                keepAlive();
            }
        }, 25000);
    }

    @OnClose
    public void onClose(Session session) { }

    @OnMessage
    public void onMessage(Session session, Message message) {
        switch (message.getType()) {
            case "LoginFromUi":
                LoginFromUi loginFromUi = (LoginFromUi) message;
                logged = userService.login(loginFromUi.getUser(), loginFromUi.getPassword());
                sendAuth(logged);
                break;

            case "LogoutFromUi":
                logged = AuthStatus.NO_AUTH;
                sendAuth(logged);
                break;

            case "UserFromUi":
                UserFromUi userFromUi = (UserFromUi) message;
                userService.newUser(userFromUi.getUser(), userFromUi.getPassword(), userFromUi.isAdmin());
                break;
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println(Arrays.toString(throwable.getStackTrace()));
    }

    public void keepAlive() {
        try {
            session.getBasicRemote().sendObject(new KeepAliveToUi("keepalive"));
        } catch (Throwable e) {
            onError(session, e);
        }
    }

    public void sendAuth(AuthStatus logged) {
        //session.getBasicRemote().sendObject();
    }

    public void sendMap(Grid map) { }

    public void sendObstacle(List<Position> positionList) { }

    public void sendUsers(List<User> userList) { }

    public void sendUnits(List<Unit> unitList) { }

    public void sendUnitPosition(String id, Position position) { }

    public void sendUnitStatus(String id, UnitStatus status) { }

    public void sendUnitError(String id, int error) { }

    public void sendUnitSpeed(String id, int speed) { }
}
