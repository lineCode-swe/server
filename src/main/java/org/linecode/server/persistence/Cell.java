package org.linecode.server.persistence;

import org.linecode.server.Position;

public class Cell {
    private final Position position;
    private final boolean locked = false;
    private final boolean obstacle = false;
    private final boolean unit = false;
    private final boolean poi = false;
    private final boolean baseRicarica=false;
    private Direction senso ; // "UP,DOWN,LEFT,RIGHT"

    public boolean isBaseRicarica() {
        return baseRicarica;
    }

    public Cell(int x, int y) {
        this.position = new Position(x,y);
    }

    public Cell(Position position){
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isObstacle() {
        return obstacle;
    }

    public boolean isUnit() {
        return unit;
    }

    public boolean isPoi() {
        return poi;
    }

    public Direction getSenso() {
        return senso;
    }


    @Override
    public String toString() {
        return position.toString() + " , Locked: " + locked + " , Obstacle : " + obstacle + " , Unit : " + unit + " , Poi " + poi + " , Base di ricarica " + baseRicarica + " , Senso = " + senso +"}";
    }
}
