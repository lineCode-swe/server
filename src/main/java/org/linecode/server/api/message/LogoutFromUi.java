/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api.message;

public class LogoutFromUi extends Message {
    private final String user;

    public LogoutFromUi(String user) {
        super("LogoutFromUi");
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}
