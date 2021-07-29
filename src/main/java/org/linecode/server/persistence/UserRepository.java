/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.persistence;

import java.util.Set;

public interface UserRepository {
    public void newUser(String user, String password, Boolean admin);
    public void delUser(String user);
    public String getPassword(String user);
    public Boolean isAdmin(String user);
    public Set<String> getUsers();
}
