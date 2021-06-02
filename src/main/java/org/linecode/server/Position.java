/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */


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
    public Position(String position) {
        int duepunti=position.indexOf(":");
        this.x=Integer.parseInt(position.substring(1,duepunti));
        this.y=Integer.parseInt(position.substring(duepunti+1,position.length()-1));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString(){
        return "(" + x + ":" + y + ")";
    }

    @Override
    public boolean equals(Object x){
        if(x == null) {
            return false;
        }
        if(x.getClass() != this.getClass()) {
            return false;
        }

        final Position cmp = (Position) x;
        return this.x == cmp.getX() && this.y == cmp.getY();
    }

}
