/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */

package org.linecode.server.persistence;

import java.util.List;

public interface UserRepository {
    public void newUser(String user, String password, Boolean admin);
    public void delUser(String user);
    public String getPassword(String user);
    public Boolean isAdmin(String user);
    public List<String> getUsers();
}
