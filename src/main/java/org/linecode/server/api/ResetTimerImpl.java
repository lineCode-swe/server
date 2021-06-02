/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.api;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

public class ResetTimerImpl implements ResetTimer {
    private TimerTask task;
    private final Timer timer;

    @Inject
    public ResetTimerImpl(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void reset() {

    }

    @Override
    public void schedule(TimerTask task, long period) {

    }
}
