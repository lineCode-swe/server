/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.business;

import com.github.msteinbeck.sig4j.slot.Slot1;
import com.github.msteinbeck.sig4j.slot.Slot2;
import org.linecode.server.Position;

import java.util.List;

public interface UnitService {
    public void newUnit(String id, String name, Position base);
    public void delUnit(String id);
    public boolean isUnit(String id);
    public List<Unit> getUnits();
    //TODO
    public List<Position> getUnitsPosition();
    public List<Position> getPoiList(String id);
    public Position getPosition(String id);
    public Position getBase(String id);
    public UnitStatus getStatus(String id);
    //TODO
    public int getSpeed(String id);
    //TODO
    public int getError(String id);
    public void setPoiList(String id,List<Position> pois);
    public void newPosition(String id,Position position);
    public void newStatus(String id,UnitStatus status);
    public void newError(String id,int error);
    public void newSpeed(String id,int speed);
    public void start(String id,List<Position> poiList);
    public void stop(String id);
    public void base(String id);
    public void shutdown(String id);
    public void connectUnitCloseSignal(Slot1<String> slot);
    public void connectPositionSignal(Slot2<String,Position> slot);
    public void connectStatusSignal(Slot2<String,UnitStatus> slot);
    public void connectErrorSignal(Slot2<String,Integer> slot);
    public void connectSpeedSignal(Slot2<String,Integer> slot);
    public void connectStartSignal(Slot1<String> slot);
    public void connectStopSignal(Slot1<String> slot);
    public void connectBaseSignal(Slot1<String> slot);
    public void connectPoiListSignal(Slot2<String, List<Position>> slot);
    public void connectUnitSignal(Slot1<List<Unit>> slot);
    public void connectShutdownSignal(Slot1<String> slot);
}
