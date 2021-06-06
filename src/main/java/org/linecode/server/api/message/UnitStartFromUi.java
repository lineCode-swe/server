/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api.message;

import org.linecode.server.Position;

import java.util.List;

public class UnitStartFromUi extends Message {
    private final String id;
    private final List<Position> poiList;

    public UnitStartFromUi(String id, List<Position> poiList) {
        super("UnitStartFromUi");
        this.id = id;
        this.poiList = poiList;
    }

    public String getId() {
        return id;
    }

    public List<Position> getPoiList() {
        return poiList;
    }
}
