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

import java.util.List;
import java.util.Objects;

public class Grid {

    private final List<Cell> cells;
    private final int length;
    private final int height;

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
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("[");
        for(Cell cella : cells){
            toReturn.append(cella.toString());
        }
        toReturn.append("]");
        return toReturn.toString();
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

    @Override
    public int hashCode() {
        return Objects.hash(cells, length, height);
    }
}
