package org.linecode.server.persistence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import redis.clients.jedis.Jedis;
import junit.framework.TestCase;




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
        test.getLength();
        verify(db,times(1)).get("length");
    }

    @Test
    public void getHeight() {
        when(db.get("height")).thenReturn("5");
        test.getHeight();
        verify(db,times(1)).get("height");
    }

    @Test
    public void setLength() {
        test.setLength(5);
        verify(db,times(1)).set(anyString(),anyString());
        verify(db,times(1)).bgsave();
    }

    @Test
    public void setHeight() {
        test.setHeight(5);
        verify(db,times(1)).set("height","5");
        verify(db,times(1)).bgsave();
    }

    @Test
    public void getCell() {
        when(db.hget("cell:0:0","locked")).thenReturn("false");
        when(db.hget("cell:0:0","base")).thenReturn("false");
        when(db.hget("cell:0:0","direction")).thenReturn("RIGHT");
        Cell cell= test.getCell(0,0);
        assertEquals(false,cell.isLocked());
        assertEquals(false,cell.isBase());
        assertEquals(Direction.RIGHT,cell.getDirection());
    }

    @Test
    public void setCells() {
    }
}