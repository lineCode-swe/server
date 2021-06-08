/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.api.message;

public class ErrorFromUnit extends Message{
    private final String id;
    private final int error;

    public ErrorFromUnit(String id, int error) {
        super("ErrorFromUnit");
        this.id = id;
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public int getError() {
        return error;
    }
}
