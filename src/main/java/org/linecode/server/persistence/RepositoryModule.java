/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.persistence;

import com.google.inject.AbstractModule;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RepositoryModule extends AbstractModule {

    private static JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost:6379");

    protected void configure() {
        bind(Jedis.class).toInstance(pool.getResource());
    }
}
