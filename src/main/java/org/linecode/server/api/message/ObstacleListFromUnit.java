/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.api.message;

import org.linecode.server.Position;

import java.util.List;

public class ObstacleListFromUnit extends Message{
    private final String id;
    private final List<Position> obstacleList;

    public ObstacleListFromUnit(String id, List<Position> obstacleList) {
        super("ObstacleListFromUnit");
        this.id = id;
        this.obstacleList = obstacleList;
    }

    public String getId() {
        return id;
    }

    public List<Position> getObstacleList() {
        return obstacleList;
    }
}
