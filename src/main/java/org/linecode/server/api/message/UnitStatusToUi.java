/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.api.message;

import org.linecode.server.business.UnitStatus;

public class UnitStatusToUi extends Message {
    private final String id;
    private final UnitStatus status;

    public UnitStatusToUi(String id, UnitStatus status) {
        super("UnitStatusToUi");
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
