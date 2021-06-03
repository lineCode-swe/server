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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapRepositoryRedis implements MapRepository{

    private final Jedis db;

    @Inject
    public MapRepositoryRedis(Jedis db) {
        this.db = db;
    }

    @Override
    public int getLength() {
        return Integer.parseInt(db.get("length"));
    }

    @Override
    public int getHeight() {
        return Integer.parseInt(db.get("height"));
    }

    @Override
    public void setLength(int length) {
        db.set("length", String.valueOf(length));
        db.bgsave();
    }

    @Override
    public void setHeight(int height) {
        db.set("height", String.valueOf(height));
        db.bgsave();
    }

    @Override
    public Cell getCell(int length, int height) {
        String cellName="cell:" + length + ":" + height;
        return new Cell(new Position(length,height),Boolean.parseBoolean(db.hget(cellName,"locked")),
                Boolean.parseBoolean(db.hget(cellName,"base")),
                Direction.valueOf(db.hget(cellName,"direction")), Boolean.parseBoolean(db.hget(cellName,"poi")));
    }

    @Override
    public void setCells(List<Cell> cellList) {
        Map<String, String> keyValue= new HashMap<>();
        for(Cell cell:cellList) {
            String cellName="cell:" + cell.getPosition().getX() + ":" + cell.getPosition().getY();
            db.sadd("cell",cellName);
            keyValue.put("locked",Boolean.toString(cell.isLocked()));
            keyValue.put("base",Boolean.toString(cell.isBase()));
            keyValue.put("direction",cell.getDirection().toString());
            keyValue.put("poi",Boolean.toString(cell.isPoi()));
            db.hmset(cellName,keyValue);
            keyValue.clear();
        }
        db.bgsave();
    }

    @Override
    public List<Cell> getCells() {
        List<Cell> cellsList = new ArrayList<Cell>();
        Set<String> cellsSet = db.smembers("cell");
        String position;
        int duePunti;
        for (String cellsId:cellsSet) {
            position = cellsId.substring(5);
            duePunti = position.indexOf(":");
            cellsList.add(getCell(Integer.parseInt(position.substring(0, duePunti)),
                    Integer.parseInt(position.substring(duePunti + 1))));
        }
        return cellsList;
    }
}
