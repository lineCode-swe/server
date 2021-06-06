/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api.message;

public class KeepAliveToUi extends Message {
    public final String keepalive;

    public KeepAliveToUi(String keepalive) {
        super("KeepAliveToUi");
        this.keepalive = keepalive;
    }
}
