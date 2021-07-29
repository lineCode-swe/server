/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source license (see accompanying file LICENSE).
 ******************************************************************************/
package org.linecode.server.business;

import org.linecode.server.Position;

public class Unit {
    private String id;
    private String name;
    private Position base;

    public Unit(String id, String name, Position base) {
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
