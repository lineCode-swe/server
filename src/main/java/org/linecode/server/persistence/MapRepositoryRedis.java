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
    public Cell getCell(int length, int height) {
        String cellName="cell:" + length + ":" + height;
        return new Cell(new Position(length,height),Boolean.parseBoolean(db.hget(cellName,"locked")),
                Boolean.parseBoolean(db.hget(cellName,"base")),
                Direction.valueOf(db.hget(cellName,"direction")), Boolean.parseBoolean(db.hget(cellName,"poi")));
    }

    @Override
    public void setCells(List<Cell> cellList, int length, int height) {
        for (int i = 0; i < getLength(); ++i) {
            for (int j = 0; j < getHeight(); ++j) {
                db.del("cell:" + i + ":" + j);
            }
        }

        db.set("length", String.valueOf(length));
        db.set("height", String.valueOf(height));

        for(Cell cell:cellList) {
            String cellName = "cell:" + cell.getPosition().getX() + ":" + cell.getPosition().getY();
            Map<String, String> keyValue= new HashMap<>();
            keyValue.put("locked", Boolean.toString(cell.isLocked()));
            keyValue.put("base", Boolean.toString(cell.isBase()));
            keyValue.put("direction", cell.getDirection().toString());
            keyValue.put("poi", Boolean.toString(cell.isPoi()));
            db.hmset(cellName, keyValue);
        }

        db.bgsave();
    }

    @Override
    public List<Cell> getCells() {
        List<Cell> cellsList = new ArrayList<Cell>();
        for (int i = 0; i < getLength(); ++i) {
            for (int j = 0; j < getHeight(); ++j) {
                String cellKey = "cell:" + i + ":" + j;
                cellsList.add(new Cell(
                        new Position(i, j),
                        Boolean.parseBoolean(db.hget(cellKey, "locked")),
                        Boolean.parseBoolean(db.hget(cellKey, "base")),
                        Direction.valueOf(db.hget(cellKey, "direction")),
                        Boolean.parseBoolean(db.hget(cellKey, "poi"))
                ));
            }
        }
        return cellsList;
    }
}
