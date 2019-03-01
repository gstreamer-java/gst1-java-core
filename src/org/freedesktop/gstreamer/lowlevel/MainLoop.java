/* 
 * Copyright (c) 2007 Wayne Meissner
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
package org.freedesktop.gstreamer.lowlevel;

import static org.freedesktop.gstreamer.lowlevel.GlibAPI.GLIB_API;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.freedesktop.gstreamer.Gst;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.glib.GMainContext;
import org.freedesktop.gstreamer.glib.GSource;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.glib.RefCountedObject;

/**
 * The GLib main loop.
 */
public class MainLoop extends RefCountedObject {

    private static final List<Runnable> bgTasks = new LinkedList<Runnable>();

    private Thread bgThread;

    /**
     * Creates a new instance of {@code MainLoop}
     *
     * <p>
     * This will create a new main loop on the default gstreamer main context.
     *
     */
    public MainLoop() {
        this(Natives.initializer(GLIB_API.g_main_loop_new(Gst.getMainContext(), false)));
    }

    public MainLoop(GMainContext ctx) {
        this(Natives.initializer(GLIB_API.g_main_loop_new(ctx, false)));
    }

    /**
     * Creates a new instance of {@code MainLoop}
     *
     * <p>
     * This variant is used internally.
     *
     * @param init internal initialization data.
     */
    public MainLoop(Initializer init) {
        super(new Handle(init.ptr, init.ownsHandle), init.needRef);
    }

    /**
     * Instructs a main loop to stop processing and return from {@link #run}.
     */
    public void quit() {
        invokeLater(new Runnable() {
            public void run() {
                GLIB_API.g_main_loop_quit(MainLoop.this);
            }
        });
    }

    /**
     * Enter a loop, processing all events.
     * <p>
     * The loop will continue processing events until {@link #quit} is called.
     */
    public void run() {
        GLIB_API.g_main_loop_run(this);
    }

    /**
     * Returns whether this main loop is currently processing or not.
     *
     * @return <tt>true</tt> if the main loop is currently being run.
     */
    public boolean isRunning() {
        return GLIB_API.g_main_loop_is_running(this);
    }

    /**
     * Gets the main context for this main loop.
     *
     * @return a main context.
     */
    public GMainContext getMainContext() {
        return GLIB_API.g_main_loop_get_context(this);
    }

    /**
     * Runs the main loop in a background thread.
     */
    public void startInBackground() {
        bgThread = new java.lang.Thread(new Runnable() {

            public void run() {
                MainLoop.this.run();
            }
        });
        bgThread.setDaemon(true);
        bgThread.setName("gmainloop");
        bgThread.start();
    }

    /**
     * Invokes a task on the main loop thread.
     * <p>
     * This method will wait until the task has completed before returning.
     *
     * @param r the task to invoke.
     */
    public void invokeAndWait(Runnable r) {
        FutureTask<Object> task = new FutureTask<Object>(r, null);
        invokeLater(task);
        try {
            task.get();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex.getCause());
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
    private static final GlibAPI.GSourceFunc bgCallback = new GlibAPI.GSourceFunc() {
        public boolean callback(Pointer source) {
            //            System.out.println("Running g_idle callbacks");
            List<Runnable> tasks = new ArrayList<Runnable>();
            synchronized (bgTasks) {
                tasks.addAll(bgTasks);
                bgTasks.clear();
            }
            for (Runnable r : tasks) {
                r.run();
            }
            GLIB_API.g_source_unref(source);
            return false;
        }
    };

    /**
     * Invokes a task on the main loop thread.
     * <p>
     * This method returns immediately, without waiting for the task to
     * complete.
     *
     * @param r the task to invoke.
     */
    public void invokeLater(final Runnable r) {
        //        System.out.println("Scheduling idle callbacks");
        synchronized (bgTasks) {
            boolean empty = bgTasks.isEmpty();
            bgTasks.add(r);
            // Only trigger the callback if there were no existing elements in the list
            // otherwise it is already triggered
            if (empty) {
                GSource source = GLIB_API.g_idle_source_new();
                GLIB_API.g_source_set_callback(source, bgCallback, source, null);
                source.attach(Gst.getMainContext());
                source.disown(); // gets destroyed in the callback
            }
        }
    }

    private final static class Handle extends RefCountedObject.Handle {

        public Handle(GPointer ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            GLIB_API.g_main_loop_unref(ptr);
        }

        @Override
        protected void ref() {
            GLIB_API.g_main_loop_ref(getPointer());
        }

        @Override
        protected void unref() {
            GLIB_API.g_main_loop_unref(getPointer());
        }

    }

}
