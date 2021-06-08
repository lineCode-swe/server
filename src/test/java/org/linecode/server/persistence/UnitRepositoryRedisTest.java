package org.linecode.server.persistence;

import org.junit.Before;
import org.junit.Test;
import org.linecode.server.Position;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class UnitRepositoryRedisTest {
    private Jedis db;
    private UnitRepositoryRedis test;
    private String id = "Unit:1";
    private String name = "Unità";
    private Position position = new Position(0,0);

    @Before
    public void setUp() throws Exception {
        db = Mockito.mock(Jedis.class);
        test = new UnitRepositoryRedis(db);
    }

    @Test
    public void newUnit() {
        test.newUnit(id,name,position);
        verify(db,times(1)).sadd("unit",id);
        verify(db,times(1)).hmset(eq(id),anyMap());
        verify(db,times(1)).bgsave();
    }

    @Test
    public void delUnit() {
        test.delUnit(id);
        verify(db,times(1)).srem("unit",id);
        // TODO: aggiungere test di db.del()
        verify(db,times(1)).bgsave();
    }

    @Test
    public void getUnits() {
        test.getUnits();
        verify(db,times(1)).smembers("unit");
    }

    @Test
    public void getName() {
        when(db.hget(id,"name")).thenReturn("nome");
        assertEquals("nome", test.getName(id));
        verify(db,times(1)).hget(id,"name");
    }

    @Test
    public void getBase() {
        when(db.hget(id,"base_x")).thenReturn("5");
        when(db.hget(id,"base_y")).thenReturn("5");
        assertEquals(new Position(5,5), test.getBase(id));
        verify(db,times(1)).hget(id,"base_x");
        verify(db,times(1)).hget(id,"base_y");
    }

    @Test
    public void getPosition() {
        when(db.hget(id,"position_x")).thenReturn("5");
        when(db.hget(id,"position_y")).thenReturn("5");
        assertEquals(new Position(5,5), test.getPosition(id));
        verify(db,times(1)).hget(id,"position_x");
        verify(db,times(1)).hget(id,"position_y");
    }

    @Test
    public void getPoiList() {
        when(db.llen(anyString())).thenReturn(3L);
        when(db.lindex("poi:" + id,0L)).thenReturn("(0:1)");
        when(db.lindex("poi:" + id,1L)).thenReturn("(2:3)");
        when(db.lindex("poi:" + id,2L)).thenReturn("(4:5)");
        List<Position> PoiList = test.getPoiList(id);
        assertEquals(PoiList.get(0),new Position(0,1));
        assertEquals(PoiList.get(1),new Position(2,3));
        assertEquals(PoiList.get(2),new Position(4,5));
        verify(db,times(3)).lindex(anyString(),anyLong());
    }

    @Test
    public void setPosition() {
        test.setPosition(id,position);
        verify(db,times(1)).hmset(eq(id),anyMap());
        verify(db,times(1)).bgsave();
    }

    @Test
    public void setStatus() {
        test.setStatus(id,0);
        verify(db,times(1)).hset(id,"status","0");
        verify(db,times(1)).bgsave();
    }

    @Test
    public void setError() {
        test.setError(id,0);
        verify(db,times(1)).hset(id,"error","0");
        verify(db,times(1)).bgsave();
    }

    @Test
    public void setPoiList() {
        List<Position> poilist = new ArrayList<Position>();
        poilist.add(new Position(0,0));
        poilist.add(new Position(1,1));
        test.setPoiList(id, poilist);
        verify(db,times(2)).rpush(anyString(),anyString());
        verify(db,times(1)).bgsave();
    }

    @Test
    public void setSpeed() {
        test.setSpeed(id,0);
        verify(db,times(1)).hset(id,"speed","0");
        verify(db,times(1)).bgsave();
    }
}
