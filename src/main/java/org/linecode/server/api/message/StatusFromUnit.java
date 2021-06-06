/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.api.message;

import org.linecode.server.business.UnitStatus;

public class StatusFromUnit extends Message{
    private final String id;
    private final UnitStatus status;

    public StatusFromUnit(String id, UnitStatus status) {
        super("StatusFromUnit");
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public UnitStatus getStatus() {
        return status;
    }
}
