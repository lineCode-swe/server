/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/


package org.linecode.server.persistence;

import org.linecode.server.Position;

import java.util.Objects;


public class Cell {
    private final Position position;
    private final boolean locked;
    private final boolean base;
    private final Direction direction;
    private final boolean poi;


    public Cell(Position position, boolean locked, boolean base, Direction direction, boolean poi) {
        this.position = position;
        this.locked = locked;
        this.base = base;
        this.direction = direction;
        this.poi=poi;
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

    @Override
    public int hashCode() {
        return Objects.hash(position, locked, base, direction, poi);
    }
}
