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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

public class UnitRepositoryRedis implements UnitRepository {

    private final Jedis db;

    @Inject
    public UnitRepositoryRedis(Jedis db) {
        this.db = new Jedis("localhost");
    }

    @Override
    public void newUnit(String id, String name, Position base) {
        Map<String, String> keyValue= new HashMap<>();
        keyValue.put("name",name);
        keyValue.put("base_x",String.valueOf(base.getX()));
        keyValue.put("base_y",String.valueOf(base.getY()));
        keyValue.put("position_x","0");
        keyValue.put("position_y","0");
        keyValue.put("status","0");
        keyValue.put("error","0");
        keyValue.put("speed","0");
        db.sadd("unit",id);
        db.hmset(id,keyValue);
    }

    @Override
    public void delUnit(String id) {
        db.srem("unit", id);
    }

    @Override
    public Set<String> getUnits() {
        return db.smembers("unit");

    }

    @Override
    public String getName(String id) {
        return db.hget(id,"name");
    }

    @Override
    public Position getBase(String id) {
        int x=Integer.parseInt(db.hget(id,"base_x"));
        int y=Integer.parseInt(db.hget(id,"base_y"));
        return new Position(x,y);
    }

    @Override
    public Position getPosition(String id) {
        int x=Integer.parseInt(db.hget(id,"position_x"));
        int y=Integer.parseInt(db.hget(id,"position_y"));
        return new Position(x,y);
    }

    @Override
    public List<Position> getPoiList(String id) {
        List<Position> POIlist= new ArrayList<Position>();
        for(int i=0 ; i<db.llen("poi"+id) ; i++) {
            POIlist.add(new Position(db.lindex("poi:"+id,i)));
        }
        return POIlist;
    }

    @Override
    public void setPosition(String id, Position position) {
        Map<String, String> keyValue= new HashMap<>();
        keyValue.put("position_x",String.valueOf(position.getX()));
        keyValue.put("position_y",String.valueOf(position.getY()));
        db.hmset(id,keyValue);
    }

    @Override
    public void setStatus(String id, int status) {
        db.hset(id,"status",String.valueOf(status));
    }

    @Override
    public void setError(String id, int error) {
        db.hset(id,"error",String.valueOf(error));
    }

    @Override
    public void setPoiList(String id, List<Position> pois) {
        for(Position POIlist:pois) {
            db.rpush("poi:" + id, POIlist.toString());
        }
    }

    @Override
    public void setSpeed(String id, int speed) {
        db.hset(id,"speed",String.valueOf(speed));
    }

    public boolean checkUnit(Position p){
        return false;
        //TODO: Data una posizione ritornare se vi è un unità in quella posizione
    }
}

