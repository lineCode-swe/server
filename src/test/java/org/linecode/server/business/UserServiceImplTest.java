package org.linecode.server.business;

import com.github.msteinbeck.sig4j.signal.Signal1;

import org.junit.Before;
import org.junit.Test;
import org.linecode.server.persistence.UserRepository;
import org.mockito.Mockito;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceImplTest{

    private UserServiceImpl test;
    @Before
    public void setUp(){
        test = new UserServiceImpl(Mockito.mock(UserRepository.class),Mockito.mock(Signal1.class));
    }

    @Test
    public void login_Admin_ReturnAdminAUTH() {
        when(test.repo.getPassword(Mockito.anyString())).thenReturn("password");
        when(test.repo.isAdmin(Mockito.anyString())).thenReturn(true);

        assertEquals(AuthStatus.ADMIN,test.login("ciao","password"));

    }

    @Test
    public void login_NoAuth_ReturnNoAuth() {
        when(test.repo.getPassword(Mockito.anyString())).thenReturn("password");
        when(test.repo.isAdmin(Mockito.anyString())).thenReturn(true);

        assertEquals(AuthStatus.NO_AUTH,test.login("ciao","passwor"));

    }

    @Test
    public void login_Auth_ReturnAuth() {
        when(test.repo.getPassword(Mockito.anyString())).thenReturn("password");
        when(test.repo.isAdmin(Mockito.anyString())).thenReturn(false);

        assertEquals(AuthStatus.AUTH,test.login("ciao","password"));

    }
}