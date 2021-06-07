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
    public void getLength() {
        when(db.get("length")).thenReturn("5");
        assertEquals(5, test.getLength());
        verify(db,times(1)).get("length");
    }

    @Test
    public void getHeight() {
        when(db.get("height")).thenReturn("5");
        assertEquals(5, test.getHeight());
        verify(db,times(1)).get("height");
    }


    // TODO: rifare test
//    @Test
//    public void getCell() {
//        when(db.hget("cell:0:0","locked")).thenReturn("false");
//        when(db.hget("cell:0:0","base")).thenReturn("false");
//        when(db.hget("cell:0:0","direction")).thenReturn("RIGHT");
//        Cell cell= test.getCell(0,0);
//        assertFalse(cell.isLocked());
//        assertFalse(cell.isBase());
//        assertEquals(Direction.RIGHT,cell.getDirection());
//    }

    // TODO: rifare test
//    @Test
//    public void setCells() {
//        List<Cell> cellList = new ArrayList<Cell>();
//        cellList.add(new Cell(new Position(0,0), false, false, Direction.RIGHT, true));
//        cellList.add(new Cell(new Position(0,1), true, false, Direction.RIGHT, false));
//        cellList.add(new Cell(new Position(1,0), false, true, Direction.ALL, false));
//        cellList.add(new Cell(new Position(1,1), false, true, Direction.ALL, false));
//        test.setCells(cellList, 2, 2);
//        verify(db, times(1)).del(anyString());
//        verify(db, times(3)).hmset(anyString(), anyMap());
//        verify(db, times(1)).bgsave();
//    }
}
