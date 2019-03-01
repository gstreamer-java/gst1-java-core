/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2007, 2008 Wayne Meissner
 * 
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.gstreamer.glib;

import static org.freedesktop.gstreamer.lowlevel.GlibAPI.GLIB_API;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Wraps the glib main loop/main context in a ScheduledExecutor interface.
 */
public class MainContextExecutorService extends AbstractExecutorService implements ScheduledExecutorService {
    private final List<Runnable> bgTasks = new LinkedList<Runnable>();
    private final GMainContext context;
    private final Callable<Boolean> idleCallback = new Callable<Boolean>() {

        public Boolean call() throws Exception {
            //            System.out.println("Running g_idle callbacks");
            List<Runnable> tasks = new ArrayList<Runnable>();
            synchronized (bgTasks) {
                tasks.addAll(bgTasks);
                bgTasks.clear();
            }
            for (Runnable r : tasks) {
                r.run();
            }
            return false;
        }
    };
    private GSource idleSource = null;
    private volatile boolean running = true;
    
    public MainContextExecutorService(GMainContext context) {
        this.context = context;
    }
    public boolean awaitTermination(long timeout, TimeUnit units) throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public void execute(Runnable runnable) {
        invokeLater(runnable);
    }


    public boolean isShutdown() {
        return !running;
    }

    public boolean isTerminated() {
        synchronized (bgTasks) {
            return !isShutdown() && bgTasks.isEmpty();
        }
    }

    public ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit units) {
        return new ScheduledTimeout<Object>(Executors.callable(runnable), delay, units);
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit units) {
        return new ScheduledTimeout<V>(callable, delay, units);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initialiDelay, long period, TimeUnit units) {
        return new ScheduledTimeout<Object>(Executors.callable(runnable), initialiDelay, period, units);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long initialiDelay, long delay, TimeUnit units) {
        return new ScheduledTimeout<Object>(Executors.callable(runnable), initialiDelay, delay, units);
    }
    public void shutdown() {
        shutdownNow();
    }
    public List<Runnable> shutdownNow() {
        List<Runnable> tasks = new ArrayList<Runnable>();
        synchronized (bgTasks) {
            tasks.addAll(bgTasks);
            bgTasks.clear();
        }
        return tasks;
    }
    private void invokeLater(final Runnable r) {
        //        System.out.println("Scheduling idle callbacks");
        synchronized (bgTasks) {
            boolean empty = bgTasks.isEmpty();
            bgTasks.add(r);
            // Only trigger the callback if there were no existing elements in the list
            // otherwise it is already triggered
            if (empty) {
                idleSource = GLIB_API.g_idle_source_new();
                idleSource.setCallback(idleCallback);
                idleSource.attach(context);
            }
        }
    }
    private class ScheduledTimeout<V> extends FutureTask<V> implements ScheduledFuture<V> {
        private volatile GSource source;
        private Callable<Boolean> delayCallback = new Callable<Boolean>() {
            public Boolean call() {
                // Now start the periodic timer
                if (period != 0 && !isCancelled()) {
                    start(period, periodCallback);
                }
                // If periodic, don't bother returning a result
                if (period != 0) {
                    runAndReset();
                } else {
                    run();
                }
                return false;
            }
        };
        private Callable<Boolean> periodCallback = new Callable<Boolean>() {
            public Boolean call() {
                runAndReset();
                return !isCancelled();
            }
        };
        private final long period;
        private final TimeUnit units;
        
        public ScheduledTimeout(Callable<V> call, long delay, TimeUnit units) {
            this(call, delay, 0, units);
        }
        public ScheduledTimeout(Callable<V> call, long delay, long period, TimeUnit units) {
            super(call);

            this.period = period;
            this.units = units;
            start(delay, delayCallback);
        }
        
        private final int getMilliseconds(long time) {
            return (int) units.toMillis(time);
        }
        
        private void start(long timeout, Callable<Boolean> callback) {
            int milliseconds = getMilliseconds(timeout);
            /*
            * If the timeout is a multiple of seconds, use the more efficient
            * g_timeout_add_seconds, if it is available.
            */
            if ((milliseconds % 1000) == 0) {
                try {
                    source = GLIB_API.g_timeout_source_new_seconds(milliseconds / 1000);
                } catch (UnsatisfiedLinkError e) {
                    source = GLIB_API.g_timeout_source_new(milliseconds);
                }
            } else {
                source = GLIB_API.g_timeout_source_new(milliseconds);
            }
            source.setCallback(callback);
            source.attach(context);
        }
        public long getDelay(TimeUnit units) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public int compareTo(Delayed delayed) {
            //return delayed.getDelay(TimeUnit.MILLISECONDS)
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
