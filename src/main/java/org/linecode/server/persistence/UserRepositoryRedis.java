/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.persistence;

import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserRepositoryRedis implements UserRepository {

    private final Jedis db;

    @Inject
    public UserRepositoryRedis(@Named("UserRepo") Jedis db) {
        this.db = db;
    }

    @Override
    public void newUser(String user, String password, Boolean admin) {
        Map<String, String> keyValue= new HashMap<>();
        keyValue.put("password",password);
        keyValue.put("admin",Boolean.toString(admin));
        db.sadd("user",user);
        db.hmset(user,keyValue);
        db.save();
    }

    @Override
    public void delUser(String user) {
        db.srem("user", user);
        db.del(user);
        db.save();
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
    public Set<String> getUsers() {
        return db.smembers("user");
    }
}
