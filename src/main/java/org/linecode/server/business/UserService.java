/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.business;

import com.github.msteinbeck.sig4j.slot.Slot1;

import java.util.List;

public interface UserService {
    public void newUser(String user,String password,Boolean admin);
    public void delUser(String user);
    public List<User> getUsers();
    public AuthStatus login(String user,String password);
    public void connectUsersSignal(Slot1<List<User>> slot);
}
