/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.persistence;

import org.linecode.server.Position;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

public class ObstacleRepositoryRedis implements ObstacleRepository {
    private final Jedis db;

    @Inject
    public ObstacleRepositoryRedis(@Named("ObstacleRepo") Jedis db) {
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
        //db.save();
    }

    @Override
    public void delObstacle(Position position) {
        db.lrem("obs", 0L, position.toString());
        //db.save();
    }

    @Override
    public boolean checkObstacle(Position position){
        return db.lpos("obs", position.toString()) != null;
    }
}
