package org.linecode.server.api;

import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.linecode.server.Position;
import org.linecode.server.api.message.*;
import org.linecode.server.business.AuthStatus;
import org.linecode.server.business.MapService;
import org.linecode.server.business.UnitService;
import org.linecode.server.business.UserService;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class UiEndpointTest {
    private UiEndpoint endpoint;
    private Timer timer;
    private UserService userService;
    private UnitService unitService;
    private MapService mapService;
    private Session session;
    private RemoteEndpoint.Basic remote;

    @Before
    public void setUp() throws Exception {
        timer = mock(Timer.class);
        unitService = mock(UnitService.class);
        userService = mock(UserService.class);
        mapService = mock(MapService.class);
        session = mock(Session.class);
        remote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(remote);
        when(unitService.isUnit(anyString())).thenReturn(true);
        endpoint = new UiEndpoint(timer,userService,unitService,mapService);
        endpoint.onOpen(session);
    }

    @Test
    public void onOpen_mockSessionAsParameter_resetTimerScheduleCalledCorrectly() {
        verify(timer, times(1)).schedule(any(TimerTask.class), eq(25000L), eq(25000L));
    }

    @Test
    public void onClose_mockSessionAsParameter_resetTimerCancelledCorrectly() {
        endpoint.onClose(session);
        verify(timer, times(1)).cancel();
    }

    @Test
    public void login_validLoginFromUiReceived_AuthToUiIsSent() throws EncodeException, IOException {
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.AUTH);
        endpoint.login(loginFromUi);

        verify(userService,times(1)).login("user","password");
        verify(remote,times(5)).sendObject(any(Object.class));
    }

    @Test
    public void newUser_loggedInAsAdmin_newUserCreated(){
        UserFromUi userFromUi = new UserFromUi("user","password",true);
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.ADMIN);
        endpoint.login(loginFromUi);
        endpoint.newUser(userFromUi);
        endpoint.logout();
        verify(userService,times(1)).newUser("user","password",true);
    }

    @Test
    public void newUser_loggedInAsUser_error() throws IOException {
        UserFromUi userFromUi = new UserFromUi("user","password",true);
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.AUTH);
        endpoint.login(loginFromUi);
        endpoint.newUser(userFromUi);
        verify(userService,times(0)).newUser("user","password",true);
    }

    @Test
    public void deleteUser_loggedInAsAdmin_userDeleted(){
        DeleteUserFromUi deleteUserFromUi = new DeleteUserFromUi("1");
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.ADMIN);
        endpoint.login(loginFromUi);
        endpoint.deleteUser(deleteUserFromUi);
        verify(userService,times(1)).delUser("1");
    }

    @Test
    public void deleteUser_loggedInAsUser_error(){
        DeleteUserFromUi deleteUserFromUi = new DeleteUserFromUi("1");
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.AUTH);
        endpoint.login(loginFromUi);
        endpoint.deleteUser(deleteUserFromUi);
        verify(userService,times(0)).delUser("1");
    }

    @Test
    public void newMap_loggedInAsAdmin_setUpNewMap(){
        MapFromUi mapFromUi = new MapFromUi("+");
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.ADMIN);
        endpoint.login(loginFromUi);
        endpoint.newMap(mapFromUi);
        verify(mapService,times(1)).newMap("+");

    }

    @Test
    public void newMap_loggedInAsUser_error(){
        MapFromUi mapFromUi = new MapFromUi("+");
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.AUTH);
        endpoint.login(loginFromUi);
        endpoint.newMap(mapFromUi);
        verify(mapService,times(0)).newMap("+");
    }

    @Test
    public void unitStop_loggedInAsAdminAndRequestStop_requestedUnitStop(){
        UnitStopFromUi unitStopFromUi = new UnitStopFromUi("1",UnitStopCommand.STOP);
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.ADMIN);
        endpoint.login(loginFromUi);
        endpoint.unitStop(unitStopFromUi);
        verify(unitService,times(1)).stop("1");

    }

    @Test
    public void unitStop_loggedInAsAdminAndRequestShutdown_requestedUnitShutdown(){
        UnitStopFromUi unitStopFromUi = new UnitStopFromUi("1",UnitStopCommand.SHUTDOWN);
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.ADMIN);
        endpoint.login(loginFromUi);
        endpoint.unitStop(unitStopFromUi);
        verify(unitService,times(1)).shutdown("1");
    }

    @Test
    public void unitStop_loggedInAsUser_error(){
        UnitStopFromUi unitStopFromUi = new UnitStopFromUi("1",UnitStopCommand.STOP);
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.NO_AUTH);
        endpoint.login(loginFromUi);
        endpoint.unitStop(unitStopFromUi);
        verify(unitService,times(0)).stop("1");
    }

    @Test
    public void newUnit_loggedInAsAdmin_newUnitInserted(){
        UnitFromUi unitFromUi = new UnitFromUi("1","1",new Position(0,0));
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.ADMIN);
        endpoint.login(loginFromUi);
        endpoint.newUnit(unitFromUi);
        verify(unitService,times(1)).newUnit("1","1",new Position(0,0));
    }

    @Test
    public void newUnit_loggedInAsUser_error(){
        UnitFromUi unitFromUi = new UnitFromUi("1","1",new Position(0,0));
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.AUTH);
        endpoint.login(loginFromUi);
        endpoint.newUnit(unitFromUi);
        verify(unitService,times(0)).newUnit("1","1",new Position(0,0));
    }

    @Test
    public void deleteUnit_loggedInAsAdmin_unitDeleted(){
        DeleteUnitFromUi deleteUnitFromUi = new DeleteUnitFromUi("1");
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.ADMIN);
        endpoint.login(loginFromUi);
        endpoint.deleteUnit(deleteUnitFromUi);
        verify(unitService,times(1)).delUnit("1");

    }

    @Test
    public void deleteUnit_loggedInAsUser_error(){
        DeleteUnitFromUi deleteUnitFromUi = new DeleteUnitFromUi("1");
        LoginFromUi loginFromUi = new LoginFromUi("user","password");
        when(userService.login("user","password")).thenReturn(AuthStatus.AUTH);
        endpoint.login(loginFromUi);
        endpoint.deleteUnit(deleteUnitFromUi);
        verify(unitService,times(0)).delUnit("1");

    }




}
