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
    public Cell getCell(int length, int height) {
        return null;
    }
}
