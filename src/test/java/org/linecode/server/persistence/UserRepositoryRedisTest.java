package org.linecode.server.persistence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class UserRepositoryRedisTest {
    private Jedis db;
    private UserRepositoryRedis test;
    private String user;
    private String password;
    private Boolean admin;

    @Before
    public void setup() {
         db = Mockito.mock(Jedis.class);
         test = new UserRepositoryRedis(db);
         user = "userTest";
         password = "userPassword";
         admin = true;
    }

    @Test
    public void testNewUser() { // newUser_inputExampleUser_dbColdWithExampleUser
        test.newUser(user, password, admin);
        verify(db,times(1)).sadd("user", user);
        verify(db,times(1)).hmset(eq(user), anyMap());
        verify(db,times(1)).bgsave();
    }

    @Test
    public void testDelUser() {
        test.delUser(user);
        verify(db,times(1)).srem("user",user);
        verify(db,times(1)).bgsave();
    }

    @Test
    public void testGetPassword() {
        when(db.hget(user, "password")).thenReturn("userPassword");
        assertEquals("userPassword", test.getPassword(user));
        verify(db,times(1)).hget(user,"password");
    }

    @Test
    public void testIsAdmin() {
        when(db.hget(user,"admin")).thenReturn("true");
        assertTrue(test.isAdmin(user));
        verify(db,times(1)).hget(user,"admin");
    }

    @Test
    public void testGetUsers() {
        test.getUsers();
        verify(db,times(1)).smembers("user");
    }
}
