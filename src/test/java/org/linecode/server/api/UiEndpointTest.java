package org.linecode.server.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.linecode.server.business.MapService;
import org.linecode.server.business.UnitService;
import org.linecode.server.business.UserService;

import javax.websocket.Session;

import java.util.TimerTask;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UiEndpointTest {
    private UiEndpoint endpoint;
    private ResetTimer resetTimer;
    private UserService userService;
    private UnitService unitService;
    private MapService mapService;
    private Session session;

    @Before
    public void setUp() throws Exception {
        resetTimer = mock(ResetTimer.class);
        userService = mock(UserService.class);
        unitService = mock(UnitService.class);
        mapService = mock(MapService.class);
        session = mock(Session.class);
        endpoint = new UiEndpoint(resetTimer, userService, unitService, mapService);
    }

    @Test
    public void onOpen_mockSessionAsParameter_resetTimerScheduleCalledCorrectly() {
        endpoint.onOpen(session);
        verify(resetTimer, times(1)).schedule(any(TimerTask.class), eq(25000L));
    }

    @Test
    public void onClose() {
    }

    @Test
    public void onMessage() {
    }

    @Test
    public void onError() {
    }

    @Test
    public void keepAlive() {
    }

    @Test
    public void sendAuth() {
    }

    @Test
    public void sendMap() {
    }

    @Test
    public void sendObstacle() {
    }

    @Test
    public void sendUsers() {
    }

    @Test
    public void sendUnits() {
    }

    @Test
    public void sendUnitPosition() {
    }

    @Test
    public void sendUnitStatus() {
    }

    @Test
    public void sendUnitError() {
    }

    @Test
    public void sendUnitSpeed() {
    }
}
