/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */


package org.linecode.server.persistence;

import org.linecode.server.Position;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import java.util.List;

public class ObstacleRepositoryRedis implements ObstacleRepository{
    private final Jedis db;

    @Inject
    public ObstacleRepositoryRedis(Jedis db) { this.db = db; }

    @Override
    public List<String> getObstaclesKey() {
        return null;
    }

    @Override
    public void setObstacle(Position obstacle) {
    }

    @Override
    public Position getPosition() {
        return null;
    }
}
