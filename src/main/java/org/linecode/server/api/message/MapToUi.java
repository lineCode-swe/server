/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api.message;

import org.linecode.server.business.Grid;

public class MapToUi extends Message {
    private final Grid map;

    public MapToUi(Grid map) {
        super("MapToUi");
        this.map = map;
    }

    public Grid getMap() {
        return map;
    }
}
