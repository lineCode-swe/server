/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */


package org.linecode.server.persistence;

import org.linecode.server.Position;



public class Cell {
    private final Position position;
    private final boolean locked;
    private boolean poi = false;
    private final boolean base;
    private final Direction direction ; // "UP,DOWN,LEFT,RIGHT" TODO: Chiedere a Achimetto se è final


    public Cell(Position position, boolean locked, boolean base, Direction direction) {
        this.position = position;
        this.locked = locked;
        this.base = base;
        this.direction = direction;
    }

    public boolean isBaseRicarica() {
        return base;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isPoi() {
        return poi;
    }

    public Direction getDirection() {
        return direction;
    }


    public Cell createPoi(boolean poi) {
        this.poi = poi;
        return this;
    }
    public void setPoi(boolean poi) {
        this.poi = poi;
    }

    @Override
    public String toString() {
        return position.toString() + " , Locked: " + locked + " , Poi " + poi +
                " , Base di ricarica " + base + " , Senso = " + direction +"}";
    }
}
