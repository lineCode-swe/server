/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.business;

import org.linecode.server.Position;
import org.linecode.server.persistence.Cell;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    private final List<Cell> cells;
    private final int length, height;

    public Grid(List<Cell> cells, int length, int height) {
        this.cells = cells;
        this.length = length;
        this.height = height;
    }


    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    //public Cell getCell(int x, int y) { return g[x][y]; }

    public Cell getCell(Position p) {
        Cell toReturn=null;
        for(int i=0; i< cells.size();++i){
            if(cells.get(i).getPosition().equals(p)){
                toReturn=cells.get(i);
            }
        }
        return toReturn; //TODO: Possibile nullpointer
    }


    public List<Cell> getGrid() {
       return cells;
    }
}
