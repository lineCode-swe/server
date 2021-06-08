/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api.message;

public class KeepAliveToUnit extends Message {
    public final String keepalive;

    public KeepAliveToUnit(String keepalive) {
        super("KeepAliveToUnit");
        this.keepalive = keepalive;
    }
}