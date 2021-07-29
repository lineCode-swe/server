/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.api.message;

import org.linecode.server.Position;

import java.util.List;

public class UnitPoiToUi extends Message {
    private final String id;
    private final List<Position> poi;

    public UnitPoiToUi(String id, List<Position> poi) {
        super("UnitPoiToUi");
        this.id = id;
        this.poi = poi;
    }

    public String getId() {
        return id;
    }

    public List<Position> getPoi() {
        return poi;
    }
}
