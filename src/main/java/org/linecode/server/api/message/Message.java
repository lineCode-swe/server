/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.api.message;

public abstract class Message {
    private final String type;

    protected Message(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
