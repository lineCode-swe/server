package org.linecode.server.business;

import com.github.msteinbeck.sig4j.signal.Signal1;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.linecode.server.Position;
import org.linecode.server.persistence.*;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
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




    @Test
    public void testNewMap() {
        List<Cell> lista = new ArrayList<Cell>();
        lista.add(new Cell(new Position(0, 0), false, false, Direction.RIGHT));
        lista.add(new Cell(new Position(1, 0), false, false, Direction.DOWN));
        lista.add(new Cell(new Position(2, 0), false, false, Direction.UP));
        lista.add(new Cell(new Position(3, 0), false, false, Direction.LEFT));
        lista.add(new Cell(new Position(4, 0), false, true, Direction.ALL));
        lista.add(new Cell(new Position(5, 0), false, false, Direction.ALL).createPoi(true));

        lista.add(new Cell(new Position(0, 1), true, false, Direction.NONE));
        lista.add(new Cell(new Position(1, 1), true, false, Direction.NONE));
        lista.add(new Cell(new Position(2, 1), false, false, Direction.RIGHT));
        lista.add(new Cell(new Position(3, 1), false, false, Direction.DOWN));
        lista.add(new Cell(new Position(4, 1), false, false, Direction.UP));
        lista.add(new Cell(new Position(5, 1), false, false, Direction.ALL));
        String mappa = new String(">_^<BP\nxx>_^+");
        test.newMap(mappa);
        assertEquals(new Grid(lista,lista.get(lista.size()-1).getPosition().getX(),
                lista.get(lista.size()-1).getPosition().getY()).getGrid(),test.getMap().getGrid());


    }

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


    @Test
    public void testAddNeighborsAll(){

        Cell cellina = Mockito.mock(Cell.class);
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.getDirection()).thenReturn(Direction.ALL);
        List<Position> input = new ArrayList<Position>();
        List<Position> expected = new ArrayList<Position>();
        expected.add(new Position(4,5));
        expected.add(new Position(6,5));
        expected.add(new Position(5,4));
        expected.add(new Position(5,6));
        test.addNeighbors(new Position(5,5),input);
        assertEquals(expected,input);
    }

    @Test
    public void testAddNeighborsRight(){

        Cell cellina = Mockito.mock(Cell.class);
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.getDirection()).thenReturn(Direction.RIGHT);
        List<Position> input = new ArrayList<Position>();
        List<Position> expected = new ArrayList<Position>();
        expected.add(new Position(6,5));
        expected.add(new Position(5,4));
        expected.add(new Position(5,6));
        test.addNeighbors(new Position(5,5),input);
        assertEquals(expected,input);
    }

    @Test
    public void testAddNeighborsLeft(){

        Cell cellina = Mockito.mock(Cell.class);
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.getDirection()).thenReturn(Direction.LEFT);
        List<Position> input = new ArrayList<Position>();
        List<Position> expected = new ArrayList<Position>();
        expected.add(new Position(4,5));
        expected.add(new Position(5,4));
        expected.add(new Position(5,6));
        test.addNeighbors(new Position(5,5),input);
        assertEquals(expected,input);
    }

    @Test
    public void testAddNeighborsUP(){

        Cell cellina = Mockito.mock(Cell.class);
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.getDirection()).thenReturn(Direction.UP);
        List<Position> input = new ArrayList<Position>();
        List<Position> expected = new ArrayList<Position>();
        expected.add(new Position(4,5));
        expected.add(new Position(6,5));
        expected.add(new Position(5,6));
        test.addNeighbors(new Position(5,5),input);
        assertEquals(expected,input);
    }

    @Test
    public void testAddNeighborsDown(){

        Cell cellina = Mockito.mock(Cell.class);
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.getDirection()).thenReturn(Direction.DOWN);
        List<Position> input = new ArrayList<Position>();
        List<Position> expected = new ArrayList<Position>();
        expected.add(new Position(4,5));
        expected.add(new Position(6,5));
        expected.add(new Position(5,4));
        test.addNeighbors(new Position(5,5),input);
        assertEquals(expected,input);
    }

    @Test
    public void testAddNeighborsNone(){

        Cell cellina = Mockito.mock(Cell.class);
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.getDirection()).thenReturn(Direction.NONE);
        List<Position> input = new ArrayList<Position>();
        List<Position> expected = new ArrayList<Position>();
        test.addNeighbors(new Position(5,5),input);
        assertEquals(expected,input);
    }


    // TODO: Checkare questo test
    @Test
    public void testGetNeighborReturnPosition(){
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        Position cell = new Position(2,2);
        int distance = 5;
        int[][] distances = {{1,1,1,1},{1,1,5,1},{1,1,1,1},{1,1,1,1}};
        assertEquals(new Position(1,2),test.getNeighbor(cell,distance,distances));
    }


    @Test
    public void testGetNeighborReturnNull(){
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        Position cell = new Position(2,2);
        int distance = 5;
        int[][] distances = {{1,1,1,1},{1,1,1,1},{1,1,1,1},{1,1,1,1}};
        assertEquals(null,test.getNeighbor(cell,distance,distances));
    }

    @Test
    public void testGetPathAllFree(){
        test.newMap("+++++\n+++++\n+++++");
        Cell cellina = Mockito.mock(Cell.class);
        when(obsRepo.checkObstacle(any(Position.class))).thenReturn(false);
        when(unitRepo.checkUnit(any(Position.class))).thenReturn(false);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.isLocked()).thenReturn(false);
        Position cell = new Position(0,0);
        List<Position> path = new ArrayList<Position>();
        assertEquals(6,test.getPath(cell,new Position(4,2),path));


    }

    @Test
    public void testGetPathSomeObstacle(){
        test.newMap("+xxxx\n+++xx\n+++++\nxxxx+");
        Cell cellina = Mockito.mock(Cell.class);
        when(obsRepo.checkObstacle(any(Position.class))).thenReturn(false);
        when(unitRepo.checkUnit(any(Position.class))).thenReturn(false);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.isLocked()).thenReturn(false);
        Position cell = new Position(0,0);
        List<Position> path = new ArrayList<Position>();
        assertEquals(7,test.getPath(cell,new Position(4,3),path));

    }


    @Test
    public void testGetPathSomeWeirdDirection(){
        test.newMap("x+++>>xxx+\n++^>x>xxx<\n^^<+x>+xx>\n<xxx+^+_+>");
        Cell cellina = Mockito.mock(Cell.class);
        when(obsRepo.checkObstacle(any(Position.class))).thenReturn(false);
        when(obsRepo.checkObstacle(new Position(6,1))).thenReturn(true);
        when(unitRepo.checkUnit(any(Position.class))).thenReturn(false);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.isLocked()).thenReturn(false);
        Position cell = new Position(0,0);
        List<Position> path = new ArrayList<Position>();
        assertEquals(1,test.getPath(cell,new Position(9,3),path));

    }




}