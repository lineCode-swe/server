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
    private final UnitStatus status;

    public StatusFromUnit(UnitStatus status) {
        super("StatusFromUnit");
        this.status = status;
    }

    public UnitStatus getStatus() {
        return status;
    }
}
