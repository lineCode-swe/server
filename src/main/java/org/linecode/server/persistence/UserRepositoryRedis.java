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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepositoryRedis implements UserRepository {

    private Jedis db;

    @Inject
    public UserRepositoryRedis(Jedis db) {
        this.db = new Jedis("localhost");
    }

    @Override
    public void newUser(String user, String password, Boolean admin) {
        Map<String, String> keyValue= new HashMap<>();
        keyValue.put("password",password);
        keyValue.put("admin",Boolean.toString(admin));
        db.hmset(user,keyValue);
    }

    @Override
    public void delUser(String user) {
        db.srem("user", user);
    }

    @Override
    public String getPassword(String user) {
        return db.hget(user,"password");
    }

    @Override
    public Boolean isAdmin(String user) {
        return Boolean.parseBoolean(db.hget(user,"admin"));
    }

    @Override
    public List<String> getUsers() { // solo i nomi?
        return new ArrayList<String>();
    }
}
