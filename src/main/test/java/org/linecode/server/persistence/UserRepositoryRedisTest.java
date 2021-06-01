package org.linecode.server.persistence;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserRepositoryRedisTest {
    private Jedis db;
    private UserRepositoryRedis test;

    @Before
    public void setup() {
         db = Mockito.mock(Jedis.class);
         test = new UserRepositoryRedis(db);
    }

    @Test
    public void testNewUser() {
        String user = "test";
        String password = "password";
        Boolean admin = true;
        test.newUser(user, password, admin);

        verify(db,times(1)).sadd("user", user);
        verify(db,times(1)).hmset(eq(user), anyMap());
        verify(db,times(1)).bgsave();
    }

    @Test
    public void testDelUser() {
        test.delUser("test");
        verify(db,times(1)).srem("user","test");
        verify(db,times(1)).bgsave();
    }

    @Test
    public void testGetPassword() {
        test.getPassword("test");
        verify(db,times(1)).hget("test","password");
    }

    @Test
    public void testIsAdmin() {
        test.isAdmin("test");
        verify(db,times(1)).hget("test","admin");
    }

    @Test
    public void testGetUsers() {
        test.getUsers();
        verify(db,times(1)).smembers("user");
    }
}
