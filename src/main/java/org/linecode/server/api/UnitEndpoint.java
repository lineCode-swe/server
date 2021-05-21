/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.linecode.server.business.MapService;
import org.linecode.server.business.UnitService;

import javax.websocket.Session;

public class UnitEndpoint {
    private Session session;
    private final ResetTimer timer;
    private final UnitService unitService;
    private final MapService mapService;
    private final ObjectMapper mapper;

    public UnitEndpoint(Session session, ResetTimer timer, UnitService unitService,
                        MapService mapService, ObjectMapper mapper) {
        this.session = session;
        this.timer = timer;
        this.unitService = unitService;
        this.mapService = mapService;
        this.mapper = mapper;
    }

    public void onOpen(Session session) { }

    public void onClose(Session session) { }

    public void onMessage(Session session, String message) { }

    public void onError(Session session, Throwable throwable) { }

    private void keepAlive() { }

    private void sendStart(String id) { }

    private void sendStop(String id) { }

    private void sendShutdown(String id) { }

    private void closeConnection(String id) { }
}
