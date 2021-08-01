/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under ISC license (see accompanying file LICENSE).
 ******************************************************************************/

package org.linecode.server.api.message;

import org.linecode.server.business.AuthStatus;

public class AuthToUi extends Message {
    private final AuthStatus session;

    public AuthToUi(AuthStatus session) {
        super("AuthToUi");
        this.session = session;
    }

    public AuthStatus getSession() {
        return session;
    }
}
