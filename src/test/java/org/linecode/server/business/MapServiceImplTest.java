/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.business;

import com.github.msteinbeck.sig4j.signal.Signal1;

import org.junit.Before;
import org.junit.Test;
import org.linecode.server.Position;
import org.linecode.server.persistence.*;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;
//import static org.mockito.Matchers.any;

import static org.mockito.Mockito.*;



public class MapServiceImplTest{
    private Grid map;
    private UnitRepository unitRepo;
    private ObstacleRepository obsRepo;
    private MapRepository mapRepo;
    private Signal1<Grid> mapSignal;
    private Signal1<List<Position>> obstaclesSignal;
    private MapServiceImpl test;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp(){
         map = Mockito.mock(Grid.class);
         unitRepo = Mockito.mock(UnitRepository.class);
         obsRepo= Mockito.mock(ObstacleRepository.class);
         mapRepo= Mockito.mock(MapRepository.class);
         mapSignal= (Signal1<Grid>)Mockito.mock(Signal1.class);
         obstaclesSignal= (Signal1<List<Position>>)Mockito.mock(Signal1.class);
         test =new MapServiceImpl(unitRepo, obsRepo, mapRepo, mapSignal, obstaclesSignal);
         test.map=map;
    }

    @Test
    public void newMap_StringWithAllTypeOfCells_Calculated() {
        List<Cell> lista = new ArrayList<Cell>();
        lista.add(new Cell(new Position(0, 0), false, false, Direction.RIGHT,false));
        lista.add(new Cell(new Position(1, 0), false, false, Direction.DOWN,false));
        lista.add(new Cell(new Position(2, 0), false, false, Direction.UP,false));
        lista.add(new Cell(new Position(3, 0), false, false, Direction.LEFT,false));
        lista.add(new Cell(new Position(4, 0), false, true, Direction.ALL,false));
        lista.add(new Cell(new Position(5, 0), false, false, Direction.ALL,true));

        lista.add(new Cell(new Position(0, 1), true, false, Direction.NONE,false));
        lista.add(new Cell(new Position(1, 1), true, false, Direction.NONE,false));
        lista.add(new Cell(new Position(2, 1), false, false, Direction.RIGHT,false));
        lista.add(new Cell(new Position(3, 1), false, false, Direction.DOWN,false));
        lista.add(new Cell(new Position(4, 1), false, false, Direction.UP,false));
        lista.add(new Cell(new Position(5, 1), false, false, Direction.ALL,false));
        String mappa = new String(">_^<BP\nxx>_^+");
        test.newMap(mappa);
        verify(mapSignal,times(1)).emit(any(Grid.class));
        assertEquals((new Grid(lista,6,2).getCells()),(test.getMap().getCells()));


    }

    @Test
    public void isValid_BetweenRangeXAndY_ReturnTrue() {
        when(map.getLength()).thenReturn(5);
        when(map.getHeight()).thenReturn(5);
        assertTrue(test.isValid(3, 3));

    }

    @Test
    public void isValid_ValueOutsideRangeX_ReturnFalse(){
        when(map.getLength()).thenReturn(5);
        when(map.getHeight()).thenReturn(5);
        assertFalse(test.isValid(6, 4));
    }
    @Test
    public void isValid_ValueOutsideRangeY_ReturnFalse(){
        when(map.getLength()).thenReturn(5);
        when(map.getHeight()).thenReturn(5);
        assertFalse(test.isValid(4, 6));
    }


    @Test
    public void addNeighbors_CellDirectionAll_ReturnAll(){

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
    public void addNeighbors_CellDirectionRight_ReturnAllExceptLeft(){

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
    public void addNeighbors_CellDirectionLeft_ReturnAllExceptRight(){

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
    public void addNeighbors_CellDirectionUP_ReturnAllExceptDown(){

        Cell cellina = Mockito.mock(Cell.class);
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.getDirection()).thenReturn(Direction.UP);
        List<Position> input = new ArrayList<Position>();
        List<Position> expected = new ArrayList<Position>();
        expected.add(new Position(4,5));
        expected.add(new Position(6,5));
        expected.add(new Position(5,4));
        test.addNeighbors(new Position(5,5),input);
        assertEquals(expected,input);
    }

    @Test
    public void addNeighbors_CellDirectionDown_ReturnAllExceptUp(){

        Cell cellina = Mockito.mock(Cell.class);
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.getDirection()).thenReturn(Direction.DOWN);
        List<Position> input = new ArrayList<Position>();
        List<Position> expected = new ArrayList<Position>();
        expected.add(new Position(4,5));
        expected.add(new Position(6,5));
        expected.add(new Position(5,6));
        test.addNeighbors(new Position(5,5),input);
        assertEquals(expected,input);
    }

    @Test
    public void addNeighbors_CellDirectionNone_ReturnNone(){

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


    @Test
    public void getNeighbor_AllNeighbors_ReturnNeighbors(){
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        Cell cellina = new Cell(new Position(2,2),false,false,Direction.ALL,false);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        Position cell = new Position(2,2);
        int distance = 5;
        int[][] distances = {{1,1,1,1},{1,1,5,1},{1,1,1,1},{1,1,1,1}};
        assertEquals(new Position(1,2),test.getNeighbor(cell,distance,distances));
    }


    @Test
    public void getNeighbor_NoNeighbor_ReturnNull(){
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        Cell cellina = new Cell(new Position(2,2),false,false,Direction.ALL,false);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        Position cell = new Position(2,2);
        int distance = 5;
        int[][] distances = {{1,1,1,1},{1,1,1,1},{1,1,1,1},{1,1,1,1}};
        assertEquals(null,test.getNeighbor(cell,distance,distances));
    }

    @Test
    public void getPath_OnlyFreeCells_Calculated(){
        test.newMap("+++++\n+++++\n+++++");
        Cell cellina = Mockito.mock(Cell.class);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.isLocked()).thenReturn(false);
        Position cell = new Position(0,0);
        List<Position> path = new ArrayList<Position>();
        assertEquals(6,test.getPath(cell,new Position(4,2),path,new ArrayList<Position>()));


    }

    @Test
    public void getPath_OnlyLockedAndFreeCells_Calculated(){
        test.newMap("+xxxx\n+++xx\n+++++\nxxxx+");
        Cell cellina = Mockito.mock(Cell.class);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.isLocked()).thenReturn(false);
        Position cell = new Position(0,0);
        List<Position> path = new ArrayList<Position>();
        assertEquals(7,test.getPath(cell,new Position(4,3),path,new ArrayList<Position>()));

    }


    @Test
    public void getPath_MapWithAllTypesOfCells_Calculated(){
        test.newMap("_xxxx\n+xxxx\n+xxxx\n^+xxx");
        Cell cellina = Mockito.mock(Cell.class);
        when(map.getCell(any(Position.class))).thenReturn(cellina);
        when(cellina.isLocked()).thenReturn(false);
        Position cell = new Position(0,0);
        List<Position> path = new ArrayList<Position>();
        assertEquals(4, test.getPath(cell, new Position(1, 3), path,new ArrayList<Position>()));
    }


    @Test
    public void newObstacleList_ListOfObstacles_EmitSignal(){
        List<Position> mockObstacles = new ArrayList<Position>();
        when(obsRepo.getObstaclesList()).thenReturn(new ArrayList<Position>());
        test.newObstacleList(mockObstacles,new Position(0,0));
        verify(obstaclesSignal,times(1)).emit(any(ArrayList.class));
    }

    @Test
    public void checkPremises_PositionOfUnit_UnitFoundInPremesis(){
        Position unita = new Position(2,2);
        List<Position> toReturn = new ArrayList<Position>();
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        toReturn.add(new Position(3,2));
        when(unitRepo.getPositionUnits()).thenReturn(toReturn);
        assertEquals(toReturn,test.checkPremises(unita));

    }

    @Test
    public void addInvaldis_invalidCell_invalidCellsCalculated(){
        Position unita = new Position(2,2);
        List<Position> toPass = new ArrayList<Position>();
        List<Position> toReturn = new ArrayList<Position>();
        when(map.getLength()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);
        when(map.getCell(unita)).thenReturn(new Cell(unita,true,true,Direction.ALL,true));
        toReturn.add(new Position(1,2));
        toReturn.add(new Position(3,2));
        toReturn.add(new Position(2,1));
        toReturn.add(new Position(2,3));
        toReturn.add(new Position(2,2));
        test.addInvalids(unita,toPass);
        assertEquals(toReturn,toPass);

    }


}
