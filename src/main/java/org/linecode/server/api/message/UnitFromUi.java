/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.api.message;

import org.linecode.server.Position;

public class UnitFromUi extends Message {
    private final String id;
    private final String name;
    private final Position base;

    public UnitFromUi(String id, String name, Position base) {
        super("UnitFromUi");
        this.id = id;
        this.name = name;
        this.base = base;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Position getBase() {
        return base;
    }
}
