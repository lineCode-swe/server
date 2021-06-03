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
import java.util.ArrayList;
import java.util.List;

public class ObstacleRepositoryRedis implements ObstacleRepository {
    private final Jedis db;

    @Inject
    public ObstacleRepositoryRedis(Jedis db) {
        this.db = db;
    }

    @Override
    public List<Position> getObstaclesList() {
        List<Position> obstacleList = new ArrayList<Position>();
        for(int i=0 ; i<db.llen("obs") ; i++) {
            obstacleList.add(new Position(db.lindex("obs",i)));
        }
        return obstacleList;
    }

    @Override
    public void setObstacle(Position position) {
        db.rpush("obs", position.toString());
        db.bgsave();
    }

    @Override
    public void delObstacle(Position position) {
        db.lrem("obs", 0L, position.toString());
        db.bgsave();
    }

    @Override
    public boolean checkObstacle(Position position){
        return db.lpos("obs", position.toString()) != null;
    }
}
