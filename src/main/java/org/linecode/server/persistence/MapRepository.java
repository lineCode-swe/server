/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */



package org.linecode.server.persistence;

public interface MapRepository {
    public void setNewMap(String mapSchema);
    public int getLength();
    public int getHeight();
    public void setLength(int length);
    public void setHeight(int height);
    public Cell getCell(int length, int height);
}
