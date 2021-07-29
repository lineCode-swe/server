/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright (c) lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source license (see accompanying file LICENSE).
 ******************************************************************************/
package org.linecode.server.api;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

public class ResetTimerImpl implements ResetTimer {
    private TimerTask task;
    private long period;
    private Timer timer;

    @Inject
    public ResetTimerImpl() {
        this.task = null;
        this.period = 0;
        this.timer = null;
    }

    @Override
    public synchronized void reset() {
        timer.cancel();
        timer = new Timer();
        timer.schedule(task, period, period);
    }

    @Override
    public synchronized void schedule(TimerTask task, long period) {
        timer = new Timer();
        this.period = period;
        this.task = task;
        timer.schedule(task, period, period);
    }
}
