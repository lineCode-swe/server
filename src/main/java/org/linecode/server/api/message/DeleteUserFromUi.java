/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.api.message;

public class DeleteUserFromUi extends Message {
    private final String user;

    public DeleteUserFromUi(String user) {
        super("DeleteUserFromUi");
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}
