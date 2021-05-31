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
    private final Direction direction;


    public Cell(Position position, boolean locked, boolean base, Direction direction) {
        this.position = position;
        this.locked = locked;
        this.base = base;
        this.direction = direction;
    }

    public boolean isBase() {
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

    @Override
    public boolean equals(Object x){
        if(x == null) {
            return false;
        }
        if(x.getClass() != this.getClass()) {
            return false;
        }

        final Cell cmp = (Cell) x;
        return this.position.equals(cmp.getPosition()) && this.poi==cmp.isPoi() && this.base==cmp.isBase()
                && this.direction == cmp.getDirection() && this.locked==cmp.isLocked();
    }

}
