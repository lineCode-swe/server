/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.linecode.server.Position;
import org.linecode.server.business.AuthStatus;
import org.linecode.server.business.MapService;
import org.linecode.server.business.UnitService;
import org.linecode.server.business.UserService;
import org.linecode.server.business.Grid;
import org.linecode.server.business.User;
import org.linecode.server.business.Unit;
import org.linecode.server.business.UnitStatus;


import javax.websocket.Session;
import java.util.List;

public class UiEndpoint {
    private Session session;
    private AuthStatus logged;
    private final ResetTimer timer;
    private final UserService userService;
    private final UnitService unitService;
    private final MapService mapService;
    private final ObjectMapper mapper;

    public UiEndpoint(Session session, AuthStatus logged, ResetTimer timer, UserService userService,
                      UnitService unitService, MapService mapService, ObjectMapper mapper) {
        this.session = session;
        this.logged = logged;
        this.timer = timer;
        this.userService = userService;
        this.unitService = unitService;
        this.mapService = mapService;
        this.mapper = mapper;
    }

    public void onOpen(Session session) { }

    public void onClose(Session session) { }

    public void onMessage(Session session, String message) { }

    public void onError(Session session, Throwable throwable) { }

    private void keepAlive() { }

    private void sendAuth(AuthStatus logged) { }

    private void sendMap(Grid map) { }

    private void sendObstacle(List<Position> positionList) { }

    private void sendUsers(List<User> userList) { }

    private void sendUnits(List<Unit> unitList) { }

    private void sendUnitPosition(String id, Position position) { }

    private void sendUnitStatus(String id, UnitStatus status) { }

    private void sendUnitError(String id, int error) { }

    private void sendUnitSpeed(String id, int speed) { }
}
