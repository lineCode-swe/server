/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.api.message;

public class SpeedFromUnit extends Message{
    private final String id;
    private final int speed;

    public SpeedFromUnit(String id, int speed) {
        super("SpeedFromUnit");
        this.id = id;
        this.speed = speed;
    }

    public String getId() {
        return id;
    }

    public int getSpeed() {
        return speed;
    }
}
