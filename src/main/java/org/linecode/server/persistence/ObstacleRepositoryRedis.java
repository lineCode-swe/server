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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ObstacleRepositoryRedis implements ObstacleRepository{
    private final Jedis db;

    @Inject
    public ObstacleRepositoryRedis(Jedis db) {
        this.db = db;
    }

    @Override
    public Set<String> getObstaclesKey() {
        return db.smembers("obs");
    }

    @Override
    public void setObstacle(Position obstacle) {
        Map<String, String> keyValue= new HashMap<>();
        keyValue.put("position_x",String.valueOf(obstacle.getX()));
        keyValue.put("position_y",String.valueOf(obstacle.getY()));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String strDate = sdf.format(date);
        keyValue.put("timestamp",strDate);
        int index= Math.toIntExact(db.llen("obs")); // se ne viene cancellato uno questa cosa potrebbe non funzionare
        db.sadd("obs","obs:"+index);
        db.hmset("obs:"+index,keyValue);
        db.bgsave();
    }


    @Override
    public Position getPosition(String id) { // mancava il parametro in ingresso, ho aggiunto l'id dell'obstacle
        int x=Integer.parseInt(db.hget(id,"position_x"));
        int y=Integer.parseInt(db.hget(id,"position_y"));
        return new Position(x,y);
    }


}
