/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.business;

import com.github.msteinbeck.sig4j.signal.Signal1;
import com.github.msteinbeck.sig4j.slot.Slot1;
import org.linecode.server.persistence.UserRepository;

import java.util.ArrayList;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final Signal1<List<User>> userSignal;

    @Inject
    public UserServiceImpl(UserRepository repo, Signal1<List<User>> userSignal) {
        this.repo = repo;
        this.userSignal = userSignal;
    }

    @Override
    public void newUser(String user, String password, Boolean admin) {
        repo.newUser(user, password, admin);
        userSignal.emit(getUsers());
    }

    @Override
    public void delUser(String user) {
        repo.delUser(user);
        userSignal.emit(getUsers());
    }

    @Override
    public List<User> getUsers() {
        Set<String> keys = repo.getUsers();
        List<User> result = new ArrayList<>(keys.size());

        for (String username : keys) {
            result.add(new User(username, repo.isAdmin(username)));
        }

        return result;
    }

    @Override
    public AuthStatus login(String user, String password) {
        if (password.equals(repo.getPassword(user))) {
            if(repo.isAdmin(user)) {
                return AuthStatus.ADMIN;
            } else {
                return AuthStatus.AUTH;
            }
        } else {
            return AuthStatus.NO_AUTH;
        }
    }

    @Override
    public void connectUsersSignal(Slot1<List<User>> slot) {
        userSignal.connect(slot);
    }

}
