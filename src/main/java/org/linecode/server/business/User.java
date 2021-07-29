/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source license (see accompanying file LICENSE).
 ******************************************************************************/
package org.linecode.server.business;

public class User {
    private String username;
    private Boolean admin;

    public User(String username, Boolean admin) {
        this.username = username;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public Boolean isAdmin() {
        return admin;
    }

}
