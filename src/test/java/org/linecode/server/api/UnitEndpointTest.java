package org.linecode.server.api;

import org.junit.Before;
import org.junit.Test;

import org.linecode.server.Position;
import org.linecode.server.api.message.*;
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
import java.util.TimerTask;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

public class UnitEndpointTest {
    private UnitEndpoint endpoint;
    private ResetTimer resetTimer;
    private UnitService unitService;
    private MapService mapService;
    private Session session;
    private RemoteEndpoint.Basic remote;

    @Before
    public void setUp() throws Exception {
        resetTimer = mock(ResetTimer.class);
        unitService = mock(UnitService.class);
        mapService = mock(MapService.class);
        session = mock(Session.class);
        remote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(remote);
        endpoint = new UnitEndpoint(resetTimer,unitService, mapService);
    }

    @Test
    public void onOpen_mockSessionAsParameter_resetTimerScheduleCalledCorrectly() {
        when(unitService.isUnit("1")).thenReturn(true);
        endpoint.onOpen(session,"1");
        verify(resetTimer, times(1)).schedule(any(TimerTask.class), eq(25000L));
    }

    @Test
    public void onOpen_mockSessionAsParameter_connectionRefusedIdNotInDB() throws IOException {
        when(unitService.isUnit("1")).thenReturn(false);
        endpoint.onOpen(session,"1");
        verify(session, times(1)).close();
    }

    @Test
    public void onMessage_mockErrorFromUnit_unitServiceUpdatedWithNewError(){
        ErrorFromUnit message = new ErrorFromUnit("1",1);
        endpoint.onMessage(session,message);
        verify(unitService,times(1)).newError(message.getId(),message.getError());
    }

    @Test
    public void onMessage_mockObstacleListFromUnit_unitServiceUpdatedWithNewObstacleList(){
        ObstacleListFromUnit message = new ObstacleListFromUnit("1",new ArrayList<Position>());
        endpoint.onMessage(session,message);
        verify(mapService,times(1)).newObstacleList(message.getObstacleList());

    }

    @Test
    public void onMessage_mockPathRequestFromUnit_pathCalculated(){
        PathRequestFromUnit message = new PathRequestFromUnit("1");
        endpoint.onMessage(session,message);
        verify(mapService,times(1)).getNextPath(message.getId());

    }

    @Test
    public void onMessage_mockPositionFromUnit_unitServiceUpdatedWithNewPosition(){
        PositionFromUnit message = new PositionFromUnit("1",new Position(0,0));
        endpoint.onMessage(session,message);
        verify(unitService,times(1)).newPosition(message.getId(),message.getPosition());

    }

    @Test
    public void onMessage_mockSpeedFromUnit_unitServiceUpdatedWithNewSpeed(){
        SpeedFromUnit message = new SpeedFromUnit("1",50);
        endpoint.onMessage(session,message);
        verify(unitService,times(1)).newSpeed(message.getId(),message.getSpeed());

    }

    @Test
    public void onMessage_mockStatusFromUnit_unitServiceUpdatedWithNewStatus(){
        StatusFromUnit message = new StatusFromUnit("1", UnitStatus.BASE);
        endpoint.onMessage(session,message);
        verify(unitService,times(1)).newStatus(message.getId(),message.getStatus());
    }

    @Test
    public void sendStart_mockSession_StartSentToUnit() throws EncodeException, IOException {
        when(unitService.isUnit("1")).thenReturn(true);
        List<Position> mockPath = new ArrayList<>();
        mockPath.add(new Position(1,1));
        when(mapService.getNextPath("1")).thenReturn(mockPath);
        endpoint.onOpen(session,"1");
        endpoint.sendStart("1");

        ArgumentCaptor<StartToUnit> captor = forClass(StartToUnit.class);
        verify(remote,times(1)).sendObject(captor.capture());
        assertEquals(mockPath,captor.getValue().getPath());

    }

    @Test
    public void sendStop_mockSession_StopSentToUnit() throws EncodeException, IOException {
        when(unitService.isUnit("1")).thenReturn(true);
        endpoint.onOpen(session,"1");
        endpoint.sendStop("1");

        ArgumentCaptor<CommandToUnit> captor = forClass(CommandToUnit.class);
        verify(remote,times(1)).sendObject(captor.capture());
        assertEquals(UnitStopCommand.STOP,captor.getValue().getCommand());

    }

    @Test
    public void sendShutdown_mockSession_ShutdownSentToUnit() throws EncodeException, IOException {
        when(unitService.isUnit("1")).thenReturn(true);
        endpoint.onOpen(session,"1");
        endpoint.sendShutdown("1");

        ArgumentCaptor<CommandToUnit> captor = forClass(CommandToUnit.class);
        verify(remote,times(1)).sendObject(captor.capture());
        assertEquals(UnitStopCommand.SHUTDOWN,captor.getValue().getCommand());

    }

    @Test
    public void sendBase_mockSession_ShutdownSentToUnit() throws EncodeException, IOException {
        when(unitService.isUnit("1")).thenReturn(true);
        endpoint.onOpen(session,"1");
        endpoint.sendBase("1");

        ArgumentCaptor<CommandToUnit> captor = forClass(CommandToUnit.class);
        verify(remote,times(1)).sendObject(captor.capture());
        assertEquals(UnitStopCommand.BASE,captor.getValue().getCommand());

    }

    @Test
    public void closeConnection_mockSession_ConnectionClosed() throws IOException {
        when(unitService.isUnit("1")).thenReturn(true);
        endpoint.onOpen(session,"1");
        endpoint.closeConnection("1");

        verify(session,times(1)).close();
    }
}
