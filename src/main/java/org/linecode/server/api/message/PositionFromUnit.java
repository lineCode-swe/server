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

public class PositionFromUnit extends Message {
    private final Position position;
    private final List<Position> obstacles;

    public PositionFromUnit(Position position, List<Position> obstacles){
        super("PositionFromUnit");
        this.position = position;
        this.obstacles = obstacles;
    }
    public Position getPosition() {
        return position;
    }

    public List<Position> getObstacles() {
        return obstacles;
    }
}
