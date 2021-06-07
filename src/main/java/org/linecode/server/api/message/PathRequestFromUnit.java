/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.api.message;

public class PathRequestFromUnit extends Message{
    private final String id;

    public PathRequestFromUnit(String id) {
        super("PathRequestFromUnit");
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
