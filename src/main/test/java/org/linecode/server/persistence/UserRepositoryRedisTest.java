package org.linecode.server.persistence;

import junit.framework.TestCase;
import org.junit.Test;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;
import static org.mockito.Matchers.any;
import java.util.Map;


import static org.mockito.Mockito.when;

public class UserRepositoryRedisTest extends TestCase {
    Jedis db = Mockito.mock(Jedis.class);
    UserRepositoryRedis test = new UserRepositoryRedis(db);

    @Test
    public void testNewUser() {
        String user= "valton";
        String password= "password";
        Boolean admin=true;

        when(db.sadd("user",user)).thenReturn(1L);
        when(db.hmset("valton",Mockito.mock(Map.class))).thenReturn("");
        when(db.bgsave()).thenReturn("");

        test.newUser(user,password,admin);

    }

    @Test
    public void testDelUser() {
    }

    @Test
    public void testGetPassword() {
    }

    @Test
    public void testIsAdmin() {
    }

    @Test
    public void testGetUsers() {
    }
}