/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.api.message;

import org.linecode.server.business.User;

import java.util.List;

public class UsersToUi extends Message {
    private final List<User> users;

    public UsersToUi(List<User> users) {
        super("UsersToUi");
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
