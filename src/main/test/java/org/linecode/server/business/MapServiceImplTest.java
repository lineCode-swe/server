package org.linecode.server.business;

import com.github.msteinbeck.sig4j.signal.Signal1;
import junit.framework.TestCase;
import org.junit.Test;
import org.linecode.server.Position;
import org.linecode.server.persistence.MapRepository;
import org.linecode.server.persistence.ObstacleRepository;
import org.linecode.server.persistence.UnitRepository;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.when;


public class MapServiceImplTest extends TestCase {

    Grid map = Mockito.mock(Grid.class);
    UnitRepository unitRepo = Mockito.mock(UnitRepository.class);
    ObstacleRepository obsRepo= Mockito.mock(ObstacleRepository.class);
    MapRepository mapRepo= Mockito.mock(MapRepository.class);
    UnitRepository unitRepository= Mockito.mock(UnitRepository.class);
    Signal1<Grid> mapSignal= Mockito.mock(Signal1.class);
    Signal1<List<Position>> obstaclesSignal= Mockito.mock(Signal1.class);

    MapServiceImpl test = new MapServiceImpl(map,unitRepo,obsRepo,mapRepo,unitRepository,mapSignal,obstaclesSignal);

  /*  @Test
    public void testNewMap() {
        String mappa = new String(">_^<BP\nxx>_^+");

    }*/

    @Test
    public void testIsValidBetweenRange() {
        when(map.getLength()).thenReturn(5);
        when(map.getHeight()).thenReturn(5);
        assertEquals(true,test.isValid(3,3));

    }

    @Test
    public void testIsValidOutsideRangeX(){
        when(map.getLength()).thenReturn(5);
        when(map.getHeight()).thenReturn(5);
        assertEquals(false,test.isValid(6,4));
    }
    @Test
    public void testIsValidOutsideRangeY(){
        when(map.getLength()).thenReturn(5);
        when(map.getHeight()).thenReturn(5);
        assertEquals(false,test.isValid(4,6));
    }






}