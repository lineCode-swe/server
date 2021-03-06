/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

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
    private String id;
    private String name;
    private Position position;

    @Before
    public void setUp() throws Exception {
        db = Mockito.mock(Jedis.class);
        test = new UnitRepositoryRedis(db);
        id = "Unit:1";
        name = "Unità";
        position = new Position(0, 0);
    }

    @Test
    public void newUnit_NewUnitToRegister_UnitSuccessfullyAddedToDB() {
        test.newUnit(id, name, position);
        verify(db, times(1)).sadd("unit", id);
        verify(db, times(1)).hmset(eq(id), anyMap());
        verify(db, times(1)).save();
    }

    @Test
    public void delUnit_UnitIdToDelete_UnitSuccessfullyDeletedToDB() {
        test.delUnit(id);
        verify(db, times(1)).srem("unit", id);
        verify(db, times(1)).del(id);
        verify(db, times(1)).save();
    }

    @Test
    public void getUnits_requestToGetUnits_UnitsCorrectlyReturned() {
        test.getUnits();
        verify(db, times(1)).smembers("unit");
    }

    @Test
    public void getName_UnitIdToGetName_ReturnNameCorrectlyFromDB() {
        when(db.hget(id, "name")).thenReturn("nome");
        assertEquals("nome", test.getName(id));
        verify(db, times(1)).hget(id, "name");
    }

    @Test
    public void isUnit_UnitId_ReturnTrue() {
        when(db.sismember("unit", id)).thenReturn(true);
        assertTrue(test.isUnit(id));
        verify(db, times(1)).sismember("unit", id);
    }

    @Test
    public void getBase_UnitIdToGetBase_ReturnBaseCorrectlyFromDB() {
        when(db.hget(id, "base_x")).thenReturn("5");
        when(db.hget(id, "base_y")).thenReturn("5");
        assertEquals(new Position(5, 5), test.getBase(id));
        verify(db, times(1)).hget(id, "base_x");
        verify(db, times(1)).hget(id, "base_y");
    }

    @Test
    public void getPosition_UnitIdToGetPosition_ReturnPositionCorrectlyFromDB() {
        when(db.hget(id, "position_x")).thenReturn("5");
        when(db.hget(id, "position_y")).thenReturn("5");
        assertEquals(new Position(5, 5), test.getPosition(id));
        verify(db, times(1)).hget(id, "position_x");
        verify(db, times(1)).hget(id, "position_y");
    }

    @Test
    public void getPoiList_UnitId_UnitPoiListCorrectlyReturned() {
        when(db.llen(anyString())).thenReturn(3L);
        when(db.lindex("poi:" + id, 0L)).thenReturn("(0:1)");
        when(db.lindex("poi:" + id, 1L)).thenReturn("(2:3)");
        when(db.lindex("poi:" + id, 2L)).thenReturn("(4:5)");
        List<Position> PoiList = test.getPoiList(id);
        assertEquals(PoiList.get(0), new Position(0, 1));
        assertEquals(PoiList.get(1), new Position(2, 3));
        assertEquals(PoiList.get(2), new Position(4, 5));
        verify(db, times(3)).lindex(anyString(), anyLong());
    }

    @Test
    public void setPosition_UnitIdAndNewPosition_UnitPositionSuccessfullyUpdateToDB() {
        test.setPosition(id, position);
        verify(db, times(1)).hmset(eq(id), anyMap());
        verify(db, times(1)).save();
    }

    @Test
    public void setStatus_UnitIdAndNewStatus_UnitStatusSuccessfullyUpdateToDB() {
        test.setStatus(id, 0);
        verify(db, times(1)).hset(id, "status", "0");
        verify(db, times(1)).save();
    }

    @Test
    public void setError_UnitIdAndNewError_UnitErrorSuccessfullyUpdateToDB() {
        test.setError(id, 0);
        verify(db, times(1)).hset(id, "error", "0");
        verify(db, times(1)).save();
    }

    @Test
    public void setPoiList_UnitIdAndPoiList_UnitPoiListSuccessfullyUpdateToDB() {
        List<Position> poilist = new ArrayList<Position>();
        poilist.add(new Position(0, 0));
        poilist.add(new Position(1, 1));
        test.setPoiList(id, poilist);
        verify(db, times(2)).rpush(anyString(), anyString());
        verify(db, times(1)).save();
    }

    @Test
    public void setSpeed_UnitIdAndNewSpeed_UnitSpeedSuccessfullyUpdateToDB() {
        test.setSpeed(id, 0);
        verify(db, times(1)).hset(id, "speed", "0");
        verify(db, times(1)).save();
    }
}
