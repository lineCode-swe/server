/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source license (see accompanying file LICENSE).
 ******************************************************************************/
package org.linecode.server.api;

import java.util.TimerTask;

public interface ResetTimer {
    public void reset();
    public void schedule(TimerTask task, long period);
}
