package org.linecode.server.api;

import org.junit.Before;
import org.junit.Test;
import org.linecode.server.api.message.AuthToUi;
import org.linecode.server.api.message.LoginFromUi;
import org.linecode.server.api.message.UserFromUi;
import org.linecode.server.business.AuthStatus;
import org.linecode.server.business.MapService;
import org.linecode.server.business.UnitService;
import org.linecode.server.business.UserService;
import org.mockito.ArgumentCaptor;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class UiEndpointTest {
    private UiEndpoint endpoint;
    private ResetTimer resetTimer;
    private UserService userService;
    private UnitService unitService;
    private MapService mapService;
    private Session session;
    private RemoteEndpoint.Basic remote;

//    @Before
//    public void setUp() throws Exception {
//        resetTimer = mock(ResetTimer.class);
//        userService = mock(UserService.class);
//        unitService = mock(UnitService.class);
//        mapService = mock(MapService.class);
//        session = mock(Session.class);
//        remote = mock(RemoteEndpoint.Basic.class);
//        when(session.getBasicRemote()).thenReturn(remote);
//        endpoint = new UiEndpoint(resetTimer, userService, unitService, mapService);
//    }
//
//    @Test
//    public void onOpen_mockSessionAsParameter_resetTimerScheduleCalledCorrectly() {
//        endpoint.onOpen(session);
//        verify(resetTimer, times(1)).schedule(any(TimerTask.class), eq(25000L));
//    }
//
//    @Test
//    public void logout_mockSession_NoAuthSent() throws EncodeException, IOException {
//        endpoint.onOpen(session);
//        endpoint.logout();
//
//        ArgumentCaptor<AuthToUi> captor = forClass(AuthToUi.class);
//        verify(remote, times(1)).sendObject(captor.capture());
//        assertEquals(AuthStatus.NO_AUTH, captor.getValue().getSession());
//    }
//
//
//    @Test
//    public void newUser_requestByAdmin_registersNewUser() {
//        when(userService.login(any(String.class), any(String.class))).thenReturn(AuthStatus.ADMIN);
//        endpoint.login(new LoginFromUi("test", "test"));
//
//        String user = "Valton";
//        String password = "Tahiraj";
//        boolean admin = true;
//        endpoint.newUser(new UserFromUi(user, password, admin));
//
//        verify(userService, times(1)).newUser(user, password, admin);
//    }

    @Test
    public void deleteUser() {
    }

    @Test
    public void newMap() {
    }

    @Test
    public void unitStop() {
    }

    @Test
    public void unitStart() {
    }

    @Test
    public void newUnit() {
    }

    @Test
    public void deleteUnit() {
    }

    @Test
    public void testKeepAlive() {
    }

    @Test
    public void testSendAuth() {
    }

    @Test
    public void testSendMap() {
    }

    @Test
    public void testSendObstacle() {
    }

    @Test
    public void testSendUsers() {
    }

    @Test
    public void testSendUnits() {
    }

    @Test
    public void testSendUnitPosition() {
    }

    @Test
    public void testSendUnitStatus() {
    }

    @Test
    public void testSendUnitError() {
    }

    @Test
    public void testSendUnitSpeed() {
    }

    @Test
    public void sendUnitPoi() {
    }
}
