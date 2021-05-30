/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */


package org.linecode.server.persistence;

import org.linecode.server.Position;

import java.util.Set;

public interface ObstacleRepository {
    public Set<String> getObstaclesKey();
    public void setObstacle(Position obstacle);
    public Position getPosition(String id);
    public boolean checkObstacle(Position p);

}
