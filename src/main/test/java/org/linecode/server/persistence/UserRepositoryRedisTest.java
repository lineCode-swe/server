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

public class UserRepositoryRedisTest extends TestCase {
    private Jedis db= Mockito.mock(Jedis.class);
    private UserRepositoryRedis test= new UserRepositoryRedis(db);

    @Before
    public void setup() {
//         db = Mockito.mock(Jedis.class);
//         test = new UserRepositoryRedis(db);
    }

    @Test
    public void testNewUser() {
        String user = "valton";
        String password = "password";
        Boolean admin = true;
        System.out.println(Objects.isNull(test));
        System.out.println(Objects.isNull(db));
        test.newUser(user, password, admin);

        verify(db,times(1)).sadd("user", user);
        verify(db,times(1)).hmset(user,anyMapOf(String.class, String.class));
        verify(db,times(1)).bgsave();
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