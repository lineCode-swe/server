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
import java.util.Set;

public class UnitRepositoryRedis implements UnitRepository {

    private final Jedis db;

    @Inject
    public UnitRepositoryRedis(Jedis db) {
        this.db = db;
    }


    @Override
    public void newUnit(String id, String name, Position base) {

    }

    @Override
    public void delUnit(String id) {

    }

    @Override
    public Set<String> getUnits() {
        return null;
    }

    @Override
    public String getName(String id) {
        return null;
    }

    @Override
    public Position getBase(String id) {
        return null;
    }

    @Override
    public Position getPosition(String id) {
        return null;
    }

    @Override
    public void setPosition(String id, Position position) {

    }

    @Override
    public void setStatus(String id, int status) {

    }

    @Override
    public void setError(String id, int error) {

    }

    @Override
    public void setPoiList(String id, List<Position> pois) {

    }

    @Override
    public void setSpeed(String id, int speed) {

    }
}

