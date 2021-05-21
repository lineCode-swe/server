/*
 * PORTACS
 * piattaforma di controllo mobilità autonoma
 *
 * Copyright lineCode group <linecode.swe@gmail.com> 2020 - 2021
 * Distributed under open-source licence (see accompanying file LICENCE).
 */
package org.linecode.server.api;

import java.util.Timer;
import java.util.TimerTask;

public class ResetTimerImpl {
    private TimerTask task;
    private final Timer timer;

    public ResetTimerImpl(TimerTask task, Timer timer) {
        this.task = task;
        this.timer = timer;
    }
}
