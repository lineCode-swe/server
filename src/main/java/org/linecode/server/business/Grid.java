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

    private final Cell[][] g;
    private final int length, height;

    private int unitx = 0, unity = 0;
    private int poix = 0, poiy = 0;

    public Grid(int lenght, int height) {
        g = new Cell[lenght][height];
        this.length = lenght;
        this.height = height;
        for (int x = 0; x < lenght; ++x ) {
            for (int y = 0; y < height; ++y) {
                g[x][y] = new Cell(x, y);
            }
        }
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    //public Cell getCell(int x, int y) { return g[x][y]; }

    public Cell getCell(Position p) {
        return g[p.getX()][p.getY()];
    }


    public Position getPoi() {
        return new Position(poix, poiy);
    }

    public List<Cell> getGrid() {
        List<Cell> l = new ArrayList<Cell>();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < length; ++x) {
                l.add(g[height][length]);
            }
        }

        return l;
    }
}
