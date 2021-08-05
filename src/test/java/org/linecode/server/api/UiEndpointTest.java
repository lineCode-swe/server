package org.linecode.server.api;
import org.junit.Before;
import org.junit.Test;
import org.linecode.server.business.MapService;
import org.linecode.server.business.UnitService;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.util.Timer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UiEndpointTest {
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
}
