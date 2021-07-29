/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source license (see accompanying file LICENSE).
 ******************************************************************************/
package org.linecode.server.api.message;

import org.linecode.server.Position;

import java.util.List;

public class ObstacleListFromUnit extends Message{
    private final List<Position> obstacleList;

    public ObstacleListFromUnit(List<Position> obstacleList) {
        super("ObstacleListFromUnit");
        this.obstacleList = obstacleList;
    }

    public List<Position> getObstacleList() {
        return obstacleList;
    }
}
