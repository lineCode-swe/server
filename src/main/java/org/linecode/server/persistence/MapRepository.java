/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/



package org.linecode.server.persistence;

import java.util.List;

public interface MapRepository {
    public int getLength();
    public int getHeight();
    public Cell getCell(int length, int height);
    public void setCells(List<Cell> cellList, int length, int height);
    public List<Cell> getCells();
}
