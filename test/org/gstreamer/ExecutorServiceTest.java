/* 
 * Copyright (c) 2008 Wayne Meissner
 * 
 * This file is part of gstreamer-java.
 *
 * gstreamer-java is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * gstreamer-java is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with gstreamer-java.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gstreamer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.gstreamer.glib.MainContextExecutorService;
import org.gstreamer.lowlevel.MainLoop;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author wayne
 */
public class ExecutorServiceTest {

    public ExecutorServiceTest() {
    }
    private static MainLoop loop;
    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("ExecutorServiceTest", new String[] {});
        (loop = new MainLoop()).startInBackground();
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        loop.quit();
        Gst.deinit();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    private static class TestExec {
        private final CountDownLatch latch = new CountDownLatch(1);
        final MainContextExecutorService exec = new MainContextExecutorService(Gst.getMainContext());
        final AtomicBoolean fired = new AtomicBoolean(false);
        public TestExec run() { 
            // Create a timer to quit out of the test so it does not hang
            try {
                latch.await(250, TimeUnit.MILLISECONDS);
            } catch (Exception ex) {}
            return this;
        }
        public void execute(Runnable run) {
            exec.execute(run);
        }
        public void quit() {
            latch.countDown();
        }
        public void fired() {
            fired.set(true);
            quit();
        }
        public boolean hasFired() {
            return fired.get();
        }
    }
    
    @Test public void execute() {
        final TestExec exec = new TestExec();
        exec.execute(new Runnable() {

            public void run() {
                exec.fired();
            }
        });
        exec.run();
        assertTrue("Runnable not called", exec.hasFired());
    }
    @Test public void submit() throws Exception {
        final TestExec exec = new TestExec();
        final Integer MAGIC = 0xdeadbeef;
        Callable<Integer> callable = new Callable<Integer>() {

            public Integer call() throws Exception {
                exec.fired();
                return MAGIC;
            }
        };
        Future<Integer> f = exec.exec.submit(callable);
        exec.run();
        assertTrue("Callable not called", exec.hasFired());
        assertEquals("Wrong value returned from Callable", MAGIC, f.get());
        
    }
    @Test public void oneShotTimeout() {
        final TestExec exec = new TestExec();
        exec.exec.schedule(new Runnable() {

            public void run() {
                exec.fired();
            }
        }, 100, TimeUnit.MILLISECONDS);
        
        exec.run();
        assertTrue("Runnable not called", exec.hasFired());
    }
    @Test public void timeoutWithReturnValue() throws Exception {
        final TestExec exec = new TestExec();
        final Integer MAGIC = 0xdeadbeef;
        Callable<Integer> callable = new Callable<Integer>() {

            public Integer call() throws Exception {
                exec.fired();
                return MAGIC;
            }
        };
        Future<Integer> f = exec.exec.schedule(callable, 100, TimeUnit.MILLISECONDS);
        
        exec.run();
        assertTrue("Runnable not called", exec.hasFired());
        assertEquals("Wrong value returned from Callable", MAGIC, f.get());
    }
    @Test public void periodicTimeout() {
        final TestExec exec = new TestExec();
        final AtomicBoolean called = new AtomicBoolean(false);
        exec.exec.scheduleAtFixedRate(new Runnable() {

            public void run() {
                if (called.getAndSet(true)) {
                    exec.fired();
                }
            }
        }, 10, 10, TimeUnit.MILLISECONDS);
        
        exec.run();
        assertTrue("Runnable not called", exec.hasFired());
    }
}