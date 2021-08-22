/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.business;

import com.github.msteinbeck.sig4j.signal.Signal1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.linecode.server.api.message.CommandToUnit;
import org.linecode.server.api.message.UnitStopCommand;
import org.linecode.server.persistence.UserRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class UserServiceImplTest {
    private UserService test;
    private UserRepository userRepo;
    private Signal1<List<User>> signal;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        userRepo = mock(UserRepository.class);
        signal = mock(Signal1.class);
        test = new UserServiceImpl(userRepo, signal);
    }

    @Test
    public void login_Admin_ReturnAdminAUTH() {
        when(userRepo.getPassword(Mockito.anyString())).thenReturn("password");
        when(userRepo.isAdmin(Mockito.anyString())).thenReturn(true);
        assertEquals(AuthStatus.ADMIN, test.login("ciao","password"));
    }

    @Test
    public void login_NoAuth_ReturnNoAuth() {
        when(userRepo.getPassword(Mockito.anyString())).thenReturn("password");

        assertEquals(AuthStatus.NO_AUTH, test.login("ciao","wrong_passwd"));
    }

    @Test
    public void login_Auth_ReturnAuth() {
        when(userRepo.getPassword(Mockito.anyString())).thenReturn("password");
        when(userRepo.isAdmin(Mockito.anyString())).thenReturn(false);

        assertEquals(AuthStatus.AUTH, test.login("ciao","password"));
    }
    @Test
    public void newUser_newUserAdded_SignalEmitted(){
        test.newUser("Admin","admin",true);
        verify(signal, times(1)).emit(any(List.class));
    }

    @Test
    public void delUser_userDeleted_SignalEmitted(){
        test.delUser("Admin");
        verify(signal, times(1)).emit(any(List.class));
    }
}
