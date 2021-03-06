/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/
package org.linecode.server.api.message;

public class SpeedFromUnit extends Message{
    private final int speed;

    public SpeedFromUnit(int speed) {
        super("SpeedFromUnit");
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
}
