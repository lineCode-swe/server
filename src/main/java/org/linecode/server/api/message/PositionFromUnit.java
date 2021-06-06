/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */


package org.linecode.server.api.message;

import org.linecode.server.Position;

public class PositionFromUnit extends Message {
    private final String id;
    private final Position position;

    public PositionFromUnit(String id, Position position){
        super("PositionFromUnit");
        this.id=id;
        this.position=position;
    }
    public Position getPosition() {
        return position;
    }

    public String getId() {
        return id;
    }
}
