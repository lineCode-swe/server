/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */



package org.linecode.server.persistence;

import java.util.List;

public interface MapRepository {
    public int getLength();
    public int getHeight();
    public Cell getCell(int length, int height);
    public void setCells(List<Cell> cellList, int length, int height);
    public List<Cell> getCells();
}
