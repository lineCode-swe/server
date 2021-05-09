package org.linecode.server;

public class Position {
    private final int x,y;

    public Position() {
        this.x=0;
        this.y=0;
    }
    public Position(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString(){
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object x){
        if(x == null)
            return false;
        if(x.getClass() != this.getClass())
            return false;

        final Position cmp = (Position) x;
        return this.x == cmp.getX() && this.y == cmp.getY();
    }

}
