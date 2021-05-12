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

public class MapRepositoryRedis implements MapRepository{

    private final Jedis db;

    @Inject
    public MapRepositoryRedis(Jedis db) {
        this.db = db;
    }

    @Override
    public void setNewMap(String mapSchema) {
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void setLength(int length) {

    }

    @Override
    public void setHeight(int height) {

    }

    @Override
    public Cell getCell(int length, int height) {
        return null;
    }
}
