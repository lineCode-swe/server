package org.linecode.server.persistence;

import org.junit.Before;
import org.junit.Test;
import org.linecode.server.Position;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MapRepositoryRedisTest {
    private Jedis db;
    private MapRepositoryRedis test;


    @Before
    public void setup() {
        db = Mockito.mock(Jedis.class);
        test = new MapRepositoryRedis(db);
    }

    @Test
    public void TestGetLength() {
        when(db.get("length")).thenReturn("5");
        assertEquals(5, test.getLength());
        verify(db,times(1)).get("length");
    }

    @Test
    public void TestGetHeight() {
        when(db.get("height")).thenReturn("5");
        assertEquals(5, test.getHeight());
        verify(db,times(1)).get("height");
    }

    @Test
    public void TestGetCell() {
        when(db.hget("cell:0:0","locked")).thenReturn("false");
        when(db.hget("cell:0:0","base")).thenReturn("false");
        when(db.hget("cell:0:0","direction")).thenReturn("RIGHT");
        when(db.hget("cell:0:0","poi")).thenReturn("false");
        Cell cell= test.getCell(0,0);
        assertFalse(cell.isLocked());
        assertFalse(cell.isBase());
        assertFalse(cell.isPoi());
        assertEquals(Direction.RIGHT,cell.getDirection());
        verify(db,times(4)).hget(anyString(),anyString());
    }

    @Test
    public void TestSetCells() {
        when(db.get("length")).thenReturn("5");
        when(db.get("height")).thenReturn("5");
        List<Cell> cellList = new ArrayList<Cell>();
        cellList.add(new Cell(new Position(0,0), false, false, Direction.ALL, false));
        cellList.add(new Cell(new Position(0,1), false, false, Direction.ALL, false));
        cellList.add(new Cell(new Position(1,0), false, false, Direction.ALL, false));
        cellList.add(new Cell(new Position(1,1), false, false, Direction.ALL, false));
        test.setCells(cellList, 2, 2);
        verify(db,times((5+1)*(5+1))).get(anyString());
        verify(db,times(5*5)).del(anyString());
        verify(db,times(2)).set(anyString(),anyString());
        verify(db,times(2*2)).hmset(anyString(),anyMap());
        verify(db,times(1)).bgsave();
    }

    @Test
    public void TestGetCells() {
        when(db.get("length")).thenReturn("5");
        when(db.get("height")).thenReturn("5");
        when(db.hget(anyString(),eq("locked"))).thenReturn("false");
        when(db.hget(anyString(),eq("base"))).thenReturn("false");
        when(db.hget(anyString(),eq("direction"))).thenReturn("RIGHT");
        when(db.hget(anyString(),eq("poi"))).thenReturn("false");
        List<Cell> cellList = test.getCells();
        verify(db,times((5+1)*(5+1))).get(anyString());
        verify(db,times(5*5*4)).hget(anyString(),anyString());
    }
}
