/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/


package org.linecode.server.persistence;

import org.linecode.server.Position;

import java.util.List;
import java.util.Set;

public interface ObstacleRepository {
    public List<Position> getObstaclesList();
    public void setObstacle(Position position);
    public void delObstacle(Position position);
    public boolean checkObstacle(Position position);
}
