/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.persistence;

import org.linecode.server.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UnitRepositoryRedis implements UnitRepository {

    private final Jedis db;

    private final Logger logger = LoggerFactory.getLogger(UnitRepository.class);

    @Inject
    public UnitRepositoryRedis(@Named("UnitRepo") Jedis db) {
        this.db = db;
    }

    @Override
    public void newUnit(String id, String name, Position base) {
        Map<String, String> keyValue= new HashMap<>();
        keyValue.put("name",name);
        keyValue.put("base_x",String.valueOf(base.getX()));
        keyValue.put("base_y",String.valueOf(base.getY()));
        keyValue.put("position_x","-1");
        keyValue.put("position_y","-1");
        keyValue.put("status","4");
        keyValue.put("error","0");
        keyValue.put("speed","0");
        db.sadd("unit",id);
        db.hmset(id,keyValue);
        db.save();
    }

    @Override
    public void delUnit(String id) {
        db.srem("unit", id);
        db.del(id);
        db.save();
    }

    @Override
    public Set<String> getUnits() {
        return db.smembers("unit");
    }

    //TODO
    @Override
    public List<Position> getPositionUnits(){
        Set<String> units = getUnits();
        List<Position> toReturn = new ArrayList<Position>();
        for(String unit : units){
            toReturn.add(getPosition(unit));
        }
        return toReturn;
    }
    // TODO
    @Override
    public int getStatus(String id) {
        return Integer.parseInt(db.hget(id,"status"));
    }

    @Override
    // TODO
    public int getError(String id){
        return Integer.parseInt(db.hget(id,"error"));
    }

    // TODO
    @Override
    public int getSpeed(String id){
        return Integer.parseInt(db.hget(id,"speed"));
    }
    @Override
    public String getName(String id) {
        return db.hget(id,"name");
    }

    @Override
    public boolean isUnit(String id){
        return db.sismember("unit", id);
    }

    @Override
    public Position getBase(String id) {
        int x = Integer.parseInt(db.hget(id, "base_x"));
        int y = Integer.parseInt(db.hget(id, "base_y"));
        return new Position(x, y);
    }

    @Override
    public Position getPosition(String id) {
        int x=Integer.parseInt(db.hget(id,"position_x"));
        int y=Integer.parseInt(db.hget(id,"position_y"));
        return new Position(x,y);
    }

    @Override
    public List<String> getUnit(Position cella) {
        List<String> toReturn= new ArrayList<String>();
        Set<String> units = getUnits();
        for(String unit : units){
            if(getPosition(unit).equals(cella)){
                toReturn.add(unit);
            }
        }
        return toReturn;
    }

    @Override
    public List<Position> getPoiList(String id) {
        List<Position> POIlist= new ArrayList<Position>();
        for(int i=0 ; i<db.llen("poi:"+id) ; i++) {
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
        db.save();
    }

    @Override
    public void setStatus(String id, int status) {
        db.hset(id,"status",String.valueOf(status));
        db.save();
    }

    @Override
    public void setError(String id, int error) {
        db.hset(id,"error",String.valueOf(error));
        db.save();
    }

    @Override
    public void setPoiList(String id, List<Position> pois) {
        db.del("poi:"+id);
        for(Position POIlist:pois) {
            db.rpush("poi:" + id, POIlist.toString());
        }
        db.save();
    }

    @Override
    public void setSpeed(String id, int speed) {
        db.hset(id,"speed",String.valueOf(speed));
        db.save();
    }
}
