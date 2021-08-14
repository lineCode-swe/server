/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/
package org.linecode.server.business;

import com.github.msteinbeck.sig4j.slot.Slot1;
import org.linecode.server.Position;

import java.util.List;

public interface MapService {
    public void newObstacleList(List<Position> obstacles,Position p);
    public Grid getMap();
    //TODO
    public List<Position> getObstacles();
    public void newMap(String mapSchema);
    public List<Position> getNextPath(String id, List<Position> premesis);
    public void connectMapSignal(Slot1<Grid> slot);
    public void connectObstaclesSignal(Slot1<List<Position>> slot);

    public List<Position> checkPremises(Position position);
}
