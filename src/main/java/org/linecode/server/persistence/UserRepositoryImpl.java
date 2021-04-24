/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.persistence;

import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private Jedis db;

    @Inject
    public UserRepositoryImpl(Jedis db) {
        this.db = db;
    }

    @Override
    public void newUser(String user, String password, Boolean admin) {}

    @Override
    public void delUser(String user) {}

    @Override
    public String getPassword(String user) {
        return "";
    }

    @Override
    public Boolean isAdmin(String user) {
        return false;
    }

    @Override
    public List<String> getUsers() {
        return new ArrayList<String>();
    }
}
