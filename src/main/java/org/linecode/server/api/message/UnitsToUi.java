/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.api.message;

import org.linecode.server.business.Unit;

import java.util.List;

public class UnitsToUi extends Message {
    private final List<Unit> units;

    public UnitsToUi(List<Unit> units) {
        super("UnitsToUi");
        this.units = units;
    }

    public List<Unit> getUnits() {
        return units;
    }
}
