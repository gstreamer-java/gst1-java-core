/* 
 * Copyright (c) 2008 Wayne Meissner
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

package org.gstreamer.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;

/**
 * A ScheduledExecutorService that executes tasks and timers on the AWT/Swing EDT.
 */
public class SwingExecutorService extends AbstractExecutorService implements ScheduledExecutorService {
    private final AtomicBoolean isShutDown = new AtomicBoolean(false);
    
    /**
     * Creates a new <tt>ScheduledExecutorService</tt>.
     */
    public SwingExecutorService() {
        
    }
    
    /**
     * Creates a proxy version of <tt>interfaceClass</tt> that executes <tt>instance</tt>
     * on the Swing EDT when any of its methods are invoked.
     * 
     * @param interfaceClass the interface to generate.
     * @param instance the instance to delegate calls to.
     * @return a new instance of <tt>interfaceclass</tt>.
     */
    public <T> T wrap(Class<T> interfaceClass, T instance) {
        return interfaceClass.cast(Proxy.newProxyInstance(interfaceClass.getClassLoader(), 
                new Class[]{ interfaceClass }, 
                new ExecutorInvocationProxy(instance, this)));
    }
    
    /**
     * Stops this executor service from accepting any more tasks.
     */
    public void shutdown() {
        isShutDown.set(true);
    }

    public List<Runnable> shutdownNow() {
        return Collections.emptyList();
    }

    public boolean isShutdown() {
        return isShutDown.get();
    }

    public boolean isTerminated() {
        return isShutdown();
    }

    public boolean awaitTermination(long timeout, TimeUnit units) throws InterruptedException {
        return false;
    }

    public void execute(Runnable task) {
        SwingUtilities.invokeLater(task);
    }
    private static class SwingFuture<V> extends FutureTask<V> 
            implements ScheduledFuture<V>, ActionListener {
        private final javax.swing.Timer timer;
        SwingFuture(Callable<V> call, long initialDelay, long interval, TimeUnit unit) {
            super(call);
            timer = new javax.swing.Timer(1, this);
            
            timer.setInitialDelay(Math.max(1, (int) unit.toMillis(initialDelay)));
            // set the between-event delay
            timer.setDelay(Math.max(1, (int) unit.toMillis(interval)));
            
            // Don't coalesce events - some code will depend on the timer being
            // fired every time.
            timer.setCoalesce(false);
        }

        public long getDelay(TimeUnit unit) {
            return unit.convert(timer.getDelay(), TimeUnit.MILLISECONDS);
        }

        public int compareTo(Delayed delayed) {
            Long lhs = getDelay(TimeUnit.NANOSECONDS);
            Long rhs = delayed.getDelay(TimeUnit.NANOSECONDS);
            return lhs.compareTo(rhs);
        }
        
        public void actionPerformed(ActionEvent arg0) {
            if (timer.getDelay() != 0) {
                runAndReset();
            } else {
                run();
            }
        }
    }
    private <V> ScheduledFuture<V> schedule(Callable<V> callable, 
            long initialDelay, long interval, TimeUnit units) {
        SwingFuture<V> f = new SwingFuture<V>(callable, initialDelay, interval, units);
        f.timer.start();
        return f;
    }
    public ScheduledFuture<?> schedule(Runnable task, long initialDelay, TimeUnit units) {
        return schedule(Executors.callable(task), initialDelay, 0, units);
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> task, long initialDelay, 
            TimeUnit units) {
        return schedule(task, initialDelay, 0, units);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, 
            long interval, TimeUnit units) {
        return schedule(Executors.callable(task), initialDelay, interval, units);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, 
            long interval, TimeUnit units) {
        return schedule(Executors.callable(task), initialDelay, interval, units);
    }
    
    /**
     * Provides a way of automagically executing methods on an interface on a 
     * different thread.
     */
    private static class ExecutorInvocationProxy implements InvocationHandler {

        private final Executor executor;
        private final Object object;

        public ExecutorInvocationProxy(Object object, Executor executor) {
            this.object = object;
            this.executor = executor;
        }

        public Object invoke(Object self, final Method method, final Object[] argArray) throws Throwable {
            if (method.getName().equals("hashCode")) {
                return object.hashCode();
            } else if (method.getName().equals("equals")) {
                return object.equals(argArray[0]);
            }
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        method.invoke(object, argArray);
                    } catch (Throwable t) {}
                }
            });
            return null;
        }
    }
}
