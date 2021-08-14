package org.linecode.server.api;

import org.junit.Before;
import org.junit.Test;

import org.linecode.server.Position;
import org.linecode.server.api.message.CommandToUnit;
import org.linecode.server.api.message.ErrorFromUnit;
import org.linecode.server.api.message.PathRequestFromUnit;
import org.linecode.server.api.message.PositionFromUnit;
import org.linecode.server.api.message.SpeedFromUnit;
import org.linecode.server.api.message.StartToUnit;
import org.linecode.server.api.message.StatusFromUnit;
import org.linecode.server.api.message.UnitStopCommand;
import org.linecode.server.business.MapService;
import org.linecode.server.business.UnitService;
import org.linecode.server.business.UnitStatus;
import org.mockito.ArgumentCaptor;


import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

public class UnitEndpointTest {
    private UnitEndpoint endpoint;
    private Timer timer;
    private UnitService unitService;
    private MapService mapService;
    private Session session;
    private RemoteEndpoint.Basic remote;

    @Before
    public void setUp() throws Exception {
        timer = mock(Timer.class);
        unitService = mock(UnitService.class);
        mapService = mock(MapService.class);
        session = mock(Session.class);
        remote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(remote);
        when(unitService.isUnit(anyString())).thenReturn(true);
        endpoint = new UnitEndpoint(timer, unitService, mapService);
        endpoint.onOpen(session, "1");
    }

    @Test
    public void onOpen_mockSessionAsParameter_resetTimerScheduleCalledCorrectly() {
        verify(timer, times(1)).schedule(any(TimerTask.class), eq(25000L), eq(25000L));
    }

    @Test
    public void onOpen_mockSessionAsParameter_connectionRefusedIdNotInDB() throws IOException {
        when(unitService.isUnit("1")).thenReturn(false);
        endpoint.onOpen(session, "1");
        verify(session, times(1)).close();
    }

    @Test
    public void onMessage_mockErrorFromUnit_unitServiceUpdatedWithNewError(){
        ErrorFromUnit message = new ErrorFromUnit(1);
        endpoint.onMessage(session, message);
        verify(unitService, times(1)).newError(eq("1"), eq(message.getError()));
    }

    @Test
    public void onMessage_mockPathRequestFromUnit_pathCalculated(){
        PathRequestFromUnit message = new PathRequestFromUnit();
        endpoint.onMessage(session, message);
        verify(mapService, times(1)).getNextPath(eq("1"),eq(new ArrayList<Position>()));

    }

    @Test
    public void onMessage_mockPositionFromUnit_unitServiceUpdatedWithNewPosition(){
        List<Position> pois = new ArrayList<Position>();
        pois.add(new Position(1,1));
        when(unitService.getPoiList("1")).thenReturn(pois);
        PositionFromUnit message = new PositionFromUnit(new Position(0,0), new ArrayList<Position>());
        endpoint.onMessage(session, message);
        verify(unitService, times(1)).newPosition(eq("1"), eq(message.getPosition()));
        verify(mapService, times(1)).newObstacleList(eq(message.getObstacles()),eq(message.getPosition()));

    }

    @Test
    public void onMessage_mockSpeedFromUnit_unitServiceUpdatedWithNewSpeed(){
        SpeedFromUnit message = new SpeedFromUnit(50);
        endpoint.onMessage(session, message);
        verify(unitService, times(1)).newSpeed(eq("1"), eq(message.getSpeed()));

    }

    @Test
    public void onMessage_mockStatusFromUnit_unitServiceUpdatedWithNewStatus(){
        StatusFromUnit message = new StatusFromUnit(UnitStatus.BASE);
        endpoint.onMessage(session,message);
        verify(unitService, times(2)).newStatus(eq("1"), eq(message.getStatus()));
    }

    @Test
    public void sendStart_mockSession_StartSentToUnit() throws EncodeException, IOException {
        when(unitService.isUnit("1")).thenReturn(true);
        List<Position> mockPath = new ArrayList<>();
        mockPath.add(new Position(1,1));
        when(mapService.getNextPath("1",new ArrayList<Position>())).thenReturn(mockPath);
        endpoint.onOpen(session,"1");
        endpoint.sendStart("1");

        ArgumentCaptor<StartToUnit> captor = forClass(StartToUnit.class);
        verify(remote, times(1)).sendObject(captor.capture());
        assertEquals(mockPath,captor.getValue().getPath());

    }

    @Test
    public void sendStop_mockSession_StopSentToUnit() throws EncodeException, IOException {
        when(unitService.isUnit("1")).thenReturn(true);
        endpoint.onOpen(session,"1");
        endpoint.sendStop("1");

        ArgumentCaptor<CommandToUnit> captor = forClass(CommandToUnit.class);
        verify(remote, times(1)).sendObject(captor.capture());
        assertEquals(UnitStopCommand.STOP,captor.getValue().getCommand());

    }

    @Test
    public void sendShutdown_mockSession_ShutdownSentToUnit() throws EncodeException, IOException {
        when(unitService.isUnit("1")).thenReturn(true);
        endpoint.onOpen(session,"1");
        endpoint.sendShutdown("1");

        ArgumentCaptor<CommandToUnit> captor = forClass(CommandToUnit.class);
        verify(remote, times(1)).sendObject(captor.capture());
        assertEquals(UnitStopCommand.SHUTDOWN,captor.getValue().getCommand());

    }

    @Test
    public void sendBase_mockSession_ShutdownSentToUnit() throws EncodeException, IOException {
        when(unitService.isUnit("1")).thenReturn(true);
        endpoint.onOpen(session,"1");
        endpoint.sendBase("1");

        ArgumentCaptor<CommandToUnit> captor = forClass(CommandToUnit.class);
        verify(remote,times(2)).sendObject(captor.capture());

    }

    @Test
    public void closeConnection_mockSession_ConnectionClosed() throws IOException {
        when(unitService.isUnit("1")).thenReturn(true);
        endpoint.onOpen(session,"1");
        endpoint.closeConnection("1");

        verify(session,times(1)).close();
    }
}
