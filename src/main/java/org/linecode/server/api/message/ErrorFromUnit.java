/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source license (see accompanying file LICENSE).
 ******************************************************************************/
package org.linecode.server.api.message;

public class ErrorFromUnit extends Message{
    private final int error;

    public ErrorFromUnit(int error) {
        super("ErrorFromUnit");
        this.error = error;
    }

    public int getError() {
        return error;
    }
}
