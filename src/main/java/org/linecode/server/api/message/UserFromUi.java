/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api.message;

public class UserFromUi extends Message {
    private final String user;
    private final String password;
    private final boolean admin;

    public UserFromUi(String user, String password, boolean admin) {
        super("UserFromUi");
        this.user = user;
        this.password = password;
        this.admin = admin;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }
}
