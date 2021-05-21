/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */


package org.linecode.server.persistence;

import org.linecode.server.Position;

import java.util.List;

public interface ObstacleRepository {
    public List<String> getObstaclesKey();
    public void setObstacle(Position obstacle);
    public Position getPosition();
    public boolean checkObstacle(Position p);

}
