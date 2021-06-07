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

import javax.inject.Inject;
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

    public Cell getCell(Position p) {
        Cell toReturn = null;
        for (Cell cell : cells) {
            if (cell.getPosition().equals(p)) {
                toReturn = cell;
            }
        }
       return toReturn;
    }

    public List<Cell> getCells() {
       return cells;
    }

    public String toString(){
        String toReturn="[";
        for(Cell cella : cells){
            toReturn += cella.toString();
        }
        toReturn+="]";
        return toReturn;
    }

    @Override
    public boolean equals(Object x){

        if(x == null) {
            return false;
        }
        if(x.getClass() != this.getClass()) {
            return false;
        }

        final Grid cmp = (Grid) x;
        return this.cells.equals(cmp.getCells()) && this.length==cmp.getLength() && this.height==cmp.getHeight();
    }


}
