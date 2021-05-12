/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.business;

import com.github.msteinbeck.sig4j.slot.Slot1;
import org.linecode.server.Position;

import java.util.List;

public interface MapService {
    public void newObstacleList(List<Position> obstacles);
    public Grid getMap();
    public void newMap(String mapSchema);
    public List<Position> getNextPath(String id);
    public void connectMapSignal(Slot1<Grid> slot);
    public void connectObstaclesSignal(Slot1<List<Position>> slot);
}
