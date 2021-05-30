package org.linecode.server.business;

import com.github.msteinbeck.sig4j.signal.Signal1;
import junit.framework.TestCase;
import org.junit.Test;
import org.linecode.server.persistence.UserRepository;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class UserServiceImplTest extends TestCase {

    UserServiceImpl test = new UserServiceImpl(Mockito.mock(UserRepository.class),Mockito.mock(Signal1.class));

    @Test
    public void testLoginAdmin() {
        when(test.repo.getPassword(Mockito.anyString())).thenReturn("password");
        when(test.repo.isAdmin(Mockito.anyString())).thenReturn(true);

        assertEquals(AuthStatus.ADMIN,test.login("ciao","password"));

    }

    @Test
    public void testLoginNoAuth() {
        when(test.repo.getPassword(Mockito.anyString())).thenReturn("password");
        when(test.repo.isAdmin(Mockito.anyString())).thenReturn(true);

        assertEquals(AuthStatus.NO_AUTH,test.login("ciao","passwor"));

    }

    @Test
    public void testLoginAuth() {
        when(test.repo.getPassword(Mockito.anyString())).thenReturn("password");
        when(test.repo.isAdmin(Mockito.anyString())).thenReturn(false);

        assertEquals(AuthStatus.AUTH,test.login("ciao","password"));

    }
}