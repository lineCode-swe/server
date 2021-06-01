package org.linecode.server.persistence;

import org.junit.Before;
import org.junit.Test;
import org.linecode.server.Position;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;
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
        verify(db,times(1)).bgsave();
    }

    @Test
    public void getUnits() {
        test.getUnits();
        verify(db,times(1)).smembers("unit");
    }

    @Test
    public void getName() {
        test.getName(id);
        verify(db,times(1)).hget(id,"name");
    }

    @Test
    public void getBase() {
        when(db.hget(id,"base_x")).thenReturn("5");
        when(db.hget(id,"base_y")).thenReturn("5");
        test.getBase(id);
        verify(db,times(1)).hget(id,"base_x");
        verify(db,times(1)).hget(id,"base_y");
    }

    @Test
    public void getPosition() {
        when(db.hget(id,"position_x")).thenReturn("5");
        when(db.hget(id,"position_y")).thenReturn("5");
        test.getPosition(id);
        verify(db,times(1)).hget(id,"position_x");
        verify(db,times(1)).hget(id,"position_y");
    }

    @Test
    public void getPoiList() {
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
        test.setPoiList(eq("id"), anyList());
//        come faccio a contare il numero di volte passandogli anyList???
        verify(db,times(1)).bgsave();
    }

    @Test
    public void setSpeed() {
        test.setSpeed(id,0);
        verify(db,times(1)).hset(id,"speed","0");
        verify(db,times(1)).bgsave();
    }

    @Test
    public void checkUnit() {
    }
}