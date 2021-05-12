/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.business;

import com.github.msteinbeck.sig4j.signal.Signal1;
import com.github.msteinbeck.sig4j.slot.Slot1;
import org.linecode.server.persistence.UserRepository;

import java.util.List;

public class UserServiceImpl implements  UserService{

    private final UserRepository repo;
    private final Signal1<List<User>> userSignal;

    public UserServiceImpl(UserRepository repo, Signal1<List<User>> userSignal) {
        this.repo = repo;
        this.userSignal = userSignal;
    }


    @Override
    public void newUser(String user, String password, Boolean admin) {

    }

    @Override
    public void delUser(String user) {

    }

    @Override
    public AuthStatus login(String user, String password) {
        return null;
    }

    @Override
    public void connectUsersSignal(Slot1<List<User>> slot) {

    }
}