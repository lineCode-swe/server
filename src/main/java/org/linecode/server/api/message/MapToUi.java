/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

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
