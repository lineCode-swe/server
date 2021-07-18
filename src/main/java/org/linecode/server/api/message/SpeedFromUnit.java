/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
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
