/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/


package org.linecode.server.api.message;

import org.linecode.server.Position;

public class PositionFromUnit extends Message {
    private final Position position;

    public PositionFromUnit(Position position){
        super("PositionFromUnit");
        this.position = position;
    }
    public Position getPosition() {
        return position;
    }
}
