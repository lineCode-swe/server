package org.linecode.server.persistence;

import org.junit.Before;
import org.junit.Test;
import org.linecode.server.Position;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObstacleRepositoryRedisTest {
    private Jedis db;
    private ObstacleRepositoryRedis test;
    private String id = "obs:1";
    private Position position = new Position(0,0);


    @Before
    public void setUp() throws Exception {
        db = Mockito.mock(Jedis.class);
        test = new ObstacleRepositoryRedis(db);
    }

    @Test
    public void getObstaclesList() {
        when(db.llen(anyString())).thenReturn(3L);
        when(db.lindex("obs",0L)).thenReturn("(0:1)");
        when(db.lindex("obs",1L)).thenReturn("(2:3)");
        when(db.lindex("obs",2L)).thenReturn("(4:5)");
        List<Position> obstaclesList = test.getObstaclesList();
        assertEquals(obstaclesList.get(0),new Position(0,1));
        assertEquals(obstaclesList.get(1),new Position(2,3));
        assertEquals(obstaclesList.get(2),new Position(4,5));
        verify(db,times(3)).lindex(anyString(),anyLong());
    }

    @Test
    public void setObstacle() {
        test.setObstacle(position);
        verify(db,times(1)).rpush(anyString(),anyString());
        verify(db,times(1)).bgsave();
    }

    @Test
    public void delObstacle() {
        test.delObstacle(position);
        verify(db,times(1)).lrem(anyString(),eq(0L), anyString());
        verify(db,times(1)).bgsave();
    }

    @Test
    public void checkObstacle() {
        when(db.lpos(anyString(),anyString())).thenReturn(null);
        assertFalse(test.checkObstacle(position));
        when(db.lpos(anyString(),anyString())).thenReturn(0L);
        assertTrue(test.checkObstacle(position));
        verify(db,times(2)).lpos(anyString(),anyString());
    }
}