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

public class ObstaclesToUi extends Message {
    private final List<Position> obsList;

    public ObstaclesToUi(List<Position> obsList) {
        super("ObstaclesToUi");
        this.obsList = obsList;
    }

    public List<Position> getObsList() {
        return obsList;
    }
}
