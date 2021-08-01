/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/
package org.linecode.server.api.message;

import org.linecode.server.Position;

import java.util.List;

public class StartToUnit extends Message{
    public final List<Position> path;

    public StartToUnit(List<Position> path) {
        super("StartToUnit");
        this.path = path;
    }

    public List<Position> getPath() {
        return path;
    }
}
