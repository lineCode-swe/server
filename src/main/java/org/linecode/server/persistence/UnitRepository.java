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

public interface UnitRepository {
    public void newUnit(String id, String name, Position base);
    public void delUnit(String id);
    public boolean isUnit(String id);
    public Set<String> getUnits();
    public int getStatus(String id);
    public List<Position> getPositionUnits();

    // TODO
    int getError(String id);

    // TODO
    int getSpeed(String id);

    public String getName(String id);
    public Position getBase(String id);
    public Position getPosition(String id);
    public List<String> getUnit(Position cella);
    public List<Position> getPoiList(String id);
    public void setPosition(String id, Position position);
    public void setStatus(String id, int status);
    public void setError(String id, int error);
    public void setPoiList(String id, List<Position> pois);
    public void setSpeed(String id, int speed);


}
