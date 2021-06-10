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
    private long period;
    private final Timer timer;

    @Inject
    public ResetTimerImpl(Timer timer) {
        this.task = null;
        this.period = 0;
        this.timer = timer;
    }

    @Override
    public void reset() {
        timer.cancel();
        timer.schedule(task, period, period);
    }

    @Override
    public void schedule(TimerTask task, long period) {
        this.period = period;
        this.task = task;
        timer.schedule(task, period, period);
    }

    @Override
    public void cancel() {
        timer.cancel();
    }
}
