/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.api.message;

public class UnitErrorToUi extends Message {
    private final String id;
    private final int error;

    public UnitErrorToUi(String id, int error) {
        super("UnitErrorToUi");
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
