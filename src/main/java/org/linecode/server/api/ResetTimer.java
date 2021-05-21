package org.linecode.server.api;

import java.util.TimerTask;

public interface ResetTimer {
    public void reset();
    public void schedule(TimerTask task, long period);
}
