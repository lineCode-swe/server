/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.business;

import com.github.msteinbeck.sig4j.signal.Signal1;
import com.github.msteinbeck.sig4j.signal.Signal2;
import com.github.msteinbeck.sig4j.slot.Slot1;
import com.github.msteinbeck.sig4j.slot.Slot2;
import org.linecode.server.Position;
import org.linecode.server.persistence.UnitRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UnitServiceImpl implements UnitService{
    private final UnitRepository repo;
    private final Signal1<String> unitCloseSignal;
    private final Signal2<String, Position> positionSignal;
    private final Signal2<String, UnitStatus> statusSignal;
    private final Signal2<String, Integer> errorSignal;
    private final Signal2<String, Integer> speedSignal;
    private final Signal1<String> startSignal;
    private final Signal1<String> stopSignal;
    private final Signal1<String> baseSignal;
    private final Signal1<String> poiSignal;
    private final Signal1<List<Unit>> unitSignal;

    public UnitServiceImpl(UnitRepository repo, Signal1<String> unitCloseSignal,
                           Signal2<String, Position> positionSignal, Signal2<String, UnitStatus> statusSignal,
                           Signal2<String, Integer> errorSignal,
                           Signal2<String, Integer> speedSignal,
                           Signal1<String> startSignal, Signal1<String> stopSignal,
                           Signal1<String> baseSignal, Signal1<String> poiSignal,Signal1<List<Unit>> unitSignal) {
        this.repo = repo;
        this.unitCloseSignal = unitCloseSignal;
        this.positionSignal = positionSignal;
        this.statusSignal = statusSignal;
        this.errorSignal = errorSignal;
        this.speedSignal = speedSignal;
        this.startSignal = startSignal;
        this.stopSignal = stopSignal;
        this.baseSignal = baseSignal;
        this.poiSignal = poiSignal;
        this.unitSignal=unitSignal;

    }


    @Override
    public void newUnit(String id, String name, Position base) {

        repo.newUnit(id,name,base);
        unitSignal.emit(this.getUnits());
    }

    @Override
    public void delUnit(String id) {

        unitCloseSignal.emit(id);
        repo.delUnit(id);
        unitSignal.emit(this.getUnits());

    }


    @Override
    public List<Unit> getUnits() {
        Set<String> temporal = repo.getUnits();
        List<Unit> units = new ArrayList<Unit>();
        for (String id: temporal) {
            units.add(new Unit(id,repo.getName(id),repo.getBase(id)));
        }
        return units;
    }

    @Override
    public List<Position> getPoiList(String id) {
        return repo.getPoiList(id);
    }

    @Override
    public void newPosition(String id, Position position) {
        repo.setPosition(id, position);
        positionSignal.emit(id,position);
    }

    @Override
    public void newStatus(String id, UnitStatus status) {
        repo.setStatus(id, status.ordinal());
        statusSignal.emit(id,status); //
    }

    @Override
    public void newError(String id, int error) {
        repo.setError(id, error);
        errorSignal.emit(id,error);
    }

    @Override
    public void newSpeed(String id, int speed) {
        repo.setSpeed(id, speed);
        speedSignal.emit(id,speed);
    }

    @Override
    public void start(String id, List<Position> poiList) {
        repo.setPoiList(id,poiList);
        startSignal.emit(id);

    }

    @Override
    public void stop(String id) {
        stopSignal.emit(id);

    }

    @Override
    public void base(String id) {
        baseSignal.emit(id);

    }

    @Override
    public void connectUnitCloseSignal(Slot1<String> slot) {
        unitCloseSignal.connect(slot);
    }

    @Override
    public void connectPositionSignal(Slot2<String, Position> slot) {
        positionSignal.connect(slot);
    }

    @Override
    public void connectStatusSignal(Slot2<String, UnitStatus> slot) {
        statusSignal.connect(slot);
    }

    @Override
    public void connectErrorSignal(Slot2<String, Integer> slot) {
        errorSignal.connect(slot);
    }

    @Override
    public void connectSpeedSignal(Slot2<String, Integer> slot) {
        speedSignal.connect(slot);
    }

    @Override
    public void connectStartSignal(Slot1<String> slot) {
        startSignal.connect(slot);
    }

    @Override
    public void connectStopSignal(Slot1<String> slot) {
        stopSignal.connect(slot);
    }

    @Override
    public void connectBaseSignal(Slot1<String> slot) {
        baseSignal.connect(slot);
    }

    @Override
    public void connectPoiList(Slot1<String> slot) {
        poiSignal.connect(slot);
    }

    @Override
    public void connectUnitSignal(Slot1<List<Unit>> slot) { unitSignal.connect(slot);}
}
