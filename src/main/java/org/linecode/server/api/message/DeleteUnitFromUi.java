/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.api.message;

public class DeleteUnitFromUi extends Message {
    private final String id;

    public DeleteUnitFromUi(String id) {
        super("DeleteUnitFromUi");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
