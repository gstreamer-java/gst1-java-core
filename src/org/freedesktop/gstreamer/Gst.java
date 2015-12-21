/*
 * Copyright (c) 2015 Neil C Smith
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

package org.freedesktop.gstreamer;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.freedesktop.gstreamer.elements.AppSink;
import org.freedesktop.gstreamer.elements.AppSrc;
import org.freedesktop.gstreamer.elements.BaseSink;
import org.freedesktop.gstreamer.elements.BaseSrc;
import org.freedesktop.gstreamer.elements.BaseTransform;
import org.freedesktop.gstreamer.elements.DecodeBin;
import org.freedesktop.gstreamer.elements.FakeSink;
import org.freedesktop.gstreamer.elements.FakeSrc;
import org.freedesktop.gstreamer.elements.PlayBin;
import org.freedesktop.gstreamer.elements.URIDecodeBin;
import org.freedesktop.gstreamer.event.BufferSizeEvent;
import org.freedesktop.gstreamer.event.BusSyncHandler;
import org.freedesktop.gstreamer.event.CapsEvent;
import org.freedesktop.gstreamer.event.EOSEvent;
import org.freedesktop.gstreamer.event.FlushStartEvent;
import org.freedesktop.gstreamer.event.FlushStopEvent;
import org.freedesktop.gstreamer.event.ForceKeyUnit;
import org.freedesktop.gstreamer.event.LatencyEvent;
import org.freedesktop.gstreamer.event.NavigationEvent;
import org.freedesktop.gstreamer.event.QOSEvent;
import org.freedesktop.gstreamer.event.ReconfigureEvent;
import org.freedesktop.gstreamer.event.SeekEvent;
import org.freedesktop.gstreamer.event.SegmentEvent;
import org.freedesktop.gstreamer.event.StepEvent;
import org.freedesktop.gstreamer.event.StreamStartEvent;
import org.freedesktop.gstreamer.event.TagEvent;
import org.freedesktop.gstreamer.glib.GDate;
import org.freedesktop.gstreamer.glib.MainContextExecutorService;
import org.freedesktop.gstreamer.lowlevel.GMainContext;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValue;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValueArray;
import org.freedesktop.gstreamer.lowlevel.GstAPI;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GErrorStruct;
import org.freedesktop.gstreamer.lowlevel.GstControlSourceAPI.TimedValue;
import org.freedesktop.gstreamer.lowlevel.GstControlSourceAPI.ValueArray;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.GstTypes;
import org.freedesktop.gstreamer.lowlevel.NativeObject;
import org.freedesktop.gstreamer.message.AsyncDoneMessage;
import org.freedesktop.gstreamer.message.AsyncStartMessage;
import org.freedesktop.gstreamer.message.BufferingMessage;
import org.freedesktop.gstreamer.message.DurationChangedMessage;
import org.freedesktop.gstreamer.message.EOSMessage;
import org.freedesktop.gstreamer.message.ErrorMessage;
import org.freedesktop.gstreamer.message.GErrorMessage;
import org.freedesktop.gstreamer.message.InfoMessage;
import org.freedesktop.gstreamer.message.LatencyMessage;
import org.freedesktop.gstreamer.message.NeedContextMessage;
import org.freedesktop.gstreamer.message.NewClockMessage;
import org.freedesktop.gstreamer.message.QosMessage;
import org.freedesktop.gstreamer.message.SegmentDoneMessage;
import org.freedesktop.gstreamer.message.StateChangedMessage;
import org.freedesktop.gstreamer.message.StreamStartMessage;
import org.freedesktop.gstreamer.message.StreamStatusMessage;
import org.freedesktop.gstreamer.message.TagMessage;
import org.freedesktop.gstreamer.message.WarningMessage;
import org.freedesktop.gstreamer.query.ContextQuery;
import org.freedesktop.gstreamer.query.ConvertQuery;
import org.freedesktop.gstreamer.query.CustomQuery;
import org.freedesktop.gstreamer.query.DurationQuery;
import org.freedesktop.gstreamer.query.FormatsQuery;
import org.freedesktop.gstreamer.query.LatencyQuery;
import org.freedesktop.gstreamer.query.PositionQuery;
import org.freedesktop.gstreamer.query.SeekingQuery;
import org.freedesktop.gstreamer.query.SegmentQuery;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Media library supporting arbitrary formats and filter graphs.
 *
 */
@SuppressWarnings("deprecation")
public final class Gst {
	private static Logger logger = Logger.getLogger(Gst.class.getName());

    private static ScheduledExecutorService executorService;
    private static volatile CountDownLatch quit = new CountDownLatch(1);
    private static GMainContext mainContext;
    private static boolean useDefaultContext = false;
    private static final AtomicInteger initCount = new AtomicInteger(0);
    private static List<Runnable> shutdownTasks = Collections.synchronizedList(new ArrayList<Runnable>());
    private static final GstAPI gst = GstNative.load(GstAPI.class);

    public static class NativeArgs {
        public IntByReference argcRef;
        public PointerByReference argvRef;
        Memory[] argsCopy;
        Memory argvMemory;
        public NativeArgs(String progname, String[] args) {
            //
            // Allocate some native memory to pass the args down to the native layer
            //
            this.argsCopy = new Memory[args.length + 2];
            this.argvMemory = new Memory(this.argsCopy.length * Pointer.SIZE);

            //
            // Insert the program name as argv[0]
            //
            Memory arg = new Memory(progname.getBytes().length + 4);
            arg.setString(0, progname, false);
            this.argsCopy[0] = arg;

            for (int i = 0; i < args.length; i++) {
                arg = new Memory(args[i].getBytes().length + 1);
                arg.setString(0, args[i], false);
                this.argsCopy[i + 1] = arg;
            }
            this.argvMemory.write(0, this.argsCopy, 0, this.argsCopy.length);
            this.argvRef = new PointerByReference(this.argvMemory);
            this.argcRef = new IntByReference(args.length + 1);
        }
        String[] toStringArray() {
            //
            // Unpack the native arguments back into a String array
            //
            List<String> args = new ArrayList<String>();
            Pointer argv = this.argvRef.getValue();
            for (int i = 1; i < this.argcRef.getValue(); i++) {
                Pointer arg = argv.getPointer(i * Pointer.SIZE);
                if (arg != null) {
                    args.add(arg.getString(0, false));
                }
            }
            return args.toArray(new String[args.size()]);
        }
    }

    /** Creates a new instance of Gst */
    private Gst() {
    }

    /**
     * Gets the version of gstreamer currently in use.
     *
     * @return the version of gstreamer
     */
    public static Version getVersion() {
        long[] major = { 0 }, minor = { 0 }, micro = { 0 }, nano = { 0 };
        Gst.gst.gst_version(major, minor, micro, nano);
        return new Version(major[0], minor[0], micro[0], nano[0]);
    }

    /**
     * Gets the the version of gstreamer currently in use, as a String.
     *
     * @return a string representation of the version.
     */
    public static String getVersionString() {
        return Gst.gst.gst_version_string();
    }
    /**
     * Get Segmentation Trap status.
     * @return Segmentation Trap status.
     */
    public static boolean isSegTrapEnabled() {
    	return Gst.gst.gst_segtrap_is_enabled();
    }
    /**
     * Set Segmentation Trap status.
     * @param enabled
     */
    public static void setSegTrap(boolean enabled) {
    	Gst.gst.gst_segtrap_set_enabled(enabled);
    }

    /**
     * Test whether the GStreamer library already initialized.
     *
     * @return true if the GStreamer library already initialized.
     */
    public static synchronized final boolean isInitialized() {
    	return Gst.initCount.get() > 0;
    }

    /**
     * Gets the common {@code Executor} used to execute background tasks.
     *
     * @return an executor that can be used for background tasks.
     */
    public static Executor getExecutor() {
        return Gst.getScheduledExecutorService();
    }

    /**
     * Gets the common {@code ExecutorService} used to execute background tasks.
     *
     * @return an executor that can be used for background tasks.
     */
    public static ExecutorService getExecutorService() {
        return Gst.getScheduledExecutorService();
    }

    /**
     * Gets the common {@code ScheduledExecutorService} used to execute
     * background tasks and schedule timeouts.
     *
     * @return an executor that can be used for background tasks.
     */
    public static ScheduledExecutorService getScheduledExecutorService() {
        return Gst.executorService;
    }

    /**
     * Signals the thread that called {@link #init} to return.
     */
    public static void quit() {
        Gst.quit.countDown();
    }

    /**
     * Waits for the gstreamer system to shutdown via a call to {@link #quit}.
     * <p> For most gui programs, this is of little use.  However, it can be
     * a convenient way of keeping the main thread alive whilst gstreamer
     * processing on other threads continues.
     */
    public static void main() {
        try {
            CountDownLatch latch = Gst.quit;
            if (latch != null) {
                latch.await();
            }
        } catch (InterruptedException ex) {
        } finally {
            Gst.quit = new CountDownLatch(1);
        }
    }

    /**
     * Schedules a task for execution on the gstreamer background
     * {@link java.util.concurrent.Executor}.
     *
     * @param task the task to execute.
     */
    public static void invokeLater(final Runnable task) {
        Gst.getExecutor().execute(task);
    }

    /**
     * Executes a task on the gstreamer background
     * {@link java.util.concurrent.Executor}, waiting until the task completes
     * before returning.
     *
     * @param task the task to execute.
     */
    public static void invokeAndWait(Runnable task) {
        try {
            Gst.getExecutorService().submit(task).get();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    /**
     * Gets the current main context used (if any).
     *
     * @return a main context.
     */
    public static GMainContext getMainContext() {
        return Gst.mainContext;
    }

    /**
     * Initializes the GStreamer library.
     * <p> This is a shortcut if no arguments are to be passed to gstreamer.
     *
     * @throws org.freedesktop.gstreamer.GstException
     */
    public static final void init() throws GstException {
        Gst.init("unknown", new String[] {});
    }

    /**
     * Initializes the GStreamer library.
     * <p> This sets up internal path lists, registers built-in elements, and
     * loads standard plugins.
     *
     * <p>
     * This method should be called before calling any other GLib functions. If
     * this is not an option, your program must initialise the GLib thread system
     * using g_thread_init() before any other GLib functions are called.
     *
     * <p>
     * <b>Note:</b><p>
     * This method will throw a GstException if it fails.
     *
     * @param progname the java program name.
     * @param args the java argument list.
     * @return the list of arguments with any gstreamer specific options stripped
     * out.
     * @throws org.freedesktop.gstreamer.GstException
     */
    public static synchronized final String[] init(String progname, String[] args) throws GstException {
        //
        // Only do real init the first time through
        //
        if (Gst.initCount.getAndIncrement() > 0) {
            return args;
        }
        NativeArgs argv = new NativeArgs(progname, args);

        Pointer[] error = { null };
        if (!Gst.gst.gst_init_check(argv.argcRef, argv.argvRef, error)) {
            Gst.initCount.decrementAndGet();
            throw new GstException(new GError(new GErrorStruct(error[0])));
        }

        Gst.logger.fine("after gst_init, argc=" + argv.argcRef.getValue());

        if (Gst.useDefaultContext) {
            Gst.mainContext = GMainContext.getDefaultContext();
            Gst.executorService = new MainContextExecutorService(Gst.mainContext);
        } else {
            Gst.mainContext = new GMainContext();
            Gst.executorService = Executors.newSingleThreadScheduledExecutor(Gst.threadFactory);
        }
        Gst.quit = new CountDownLatch(1);
        Gst.loadAllClasses();
        return argv.toStringArray();
    }

    /**
     * Undoes all the initialization done in {@link #init}.
     * <p> This will run any cleanup tasks, terminate any timers and other
     * asynchronous tasks, and de-initialize the gstreamer library.
     */
    public static synchronized final void deinit() {
        //
        // Only perform real shutdown if called as many times as Gst.init() is
        //
        if (Gst.initCount.decrementAndGet() > 0) {
            return;
        }
        // Perform any cleanup tasks
        for (Object task : Gst.shutdownTasks.toArray()) {
            ((Runnable) task).run();
        }

        // Stop any more tasks/timers from being scheduled
        Gst.executorService.shutdown();

        // Wake up the run thread.
        Gst.quit();

        // Wait for tasks to complete.
        try {
            if (!Gst.executorService.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                // Force-kill everything
                Gst.executorService.shutdownNow();
            }
        } catch (InterruptedException ex) { }

        Gst.mainContext = null;
        System.gc(); // Make sure any dangling objects are unreffed before calling deinit().
        Gst.gst.gst_deinit();
    }

    /**
     * Adds a task to be called when {@link Gst#deinit} is called.
     * <p> This is used internally, and is not recommended for other uses.
     *
     * @param task the task to execute.
     */
    public static void addStaticShutdownTask(Runnable task) {
        Gst.shutdownTasks.add(task);
    }

    /**
     * Instructs gstreamer-java to use the default main context.
     * <p>
     * This may be useful if integration with the GTK main loop is desirable,
     * as all {@link Bus} signals and timers will be executed in the context
     * of the GTK main loop.
     * <p>
     * For the majority of programs though, it is better to wrap the individual
     * listeners in a proxy which executes the listener in the appropriate
     * context.
     *
     * @param useDefault if true, use the default glib main context.
     */
    public static void setUseDefaultContext(boolean useDefault) {
        Gst.useDefaultContext = useDefault;
    }

    // Make the gstreamer executor threads daemon, so they don't stop the main
    // program from exiting
    private static final ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger counter = new AtomicInteger(0);
        /**
         * Determines if Gst has been started from an applet and returns
         * it's parent group.
         *
         * This is to avoid a problem where the service thread is killed when
         * an applet is destroyed.  If multiple applets are active simultaneously,
         * this could be a problem.
         *
         * @return Applet's parent ("main") thread group or null, if not
         * running inside an applet
         */
        private ThreadGroup getThreadGroup() {
            ThreadGroup tg = Thread.currentThread().getThreadGroup();
            try {
                Class<?> atgClass = Class.forName("sun.applet.AppletThreadGroup");
                return atgClass.isInstance(tg) ? tg.getParent() : null;
            } catch (ClassNotFoundException ex) {
                return null;
            }
        }
        @Override
		public Thread newThread(Runnable task) {
            final String name = "gstreamer service thread " + this.counter.incrementAndGet();
            Thread t = new Thread(this.getThreadGroup(), task, name);
            t.setDaemon(true);
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    };

    private static String getField(Class<? extends NativeObject> cls, String fieldName)
            throws SecurityException, IllegalArgumentException {
        try {
            Field f = cls.getDeclaredField(fieldName);
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isFinal(mod) && f.getType().equals(String.class)) {
                f.setAccessible(true);
                return (String)f.get(null);
            }
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Library getLibraryField(Class<? extends NativeObject> cls)
            throws SecurityException, IllegalArgumentException {
        try {
            for (Field field : cls.getDeclaredFields()) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) && Modifier.isFinal(mod) && Library.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    return (Library)field.get(null);
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static synchronized void registerClass(Class<? extends NativeObject> cls) {
        String value = null;
        value = Gst.getField(cls, "GTYPE_NAME");
        if (value != null)
            GstTypes.registerType(cls, value);
        value = Gst.getField(cls, "GST_NAME");

        if (Element.class.isAssignableFrom(cls) && value != null)
            ElementFactory.registerElement((Class<? extends Element>)cls, value);

    }

    @SuppressWarnings("unchecked")
    private static synchronized void loadAllClasses() {
        for(Class<?> cls : Gst.nativeClasses) {
            Gst.registerClass((Class<? extends NativeObject>)cls);

            // Force load of libraries at start-up to prevent deadlock
            Library lib = Gst.getLibraryField((Class<? extends NativeObject>)cls);
            if (lib != null) lib.toString();
        }
    }
    // to generate the list we use:
    // egrep -rl "GST_NAME|GTYPE_NAME" src 2>/dev/null | egrep -v ".svn|Gst.java" | sort
    // even though the best would be all subclasses of NativeObject
    @SuppressWarnings("rawtypes")
	private static Class[] nativeClasses = {
		Context.class,
		GDate.class,
		GValue.class,
		GValueArray.class,
		TimedValue.class,
		ValueArray.class,
		ValueList.class,
		TagList.class,
		// ----------- Base -------------
		Buffer.class,
		Bus.class,
		Caps.class,
		Clock.class,
		DateTime.class,
		Element.class,
		ElementFactory.class,
		Event.class,
		GhostPad.class,
		Message.class,
		Pad.class,
		PadTemplate.class,
		Plugin.class,
		PluginFeature.class,
		Query.class,
		Range.class,
		Registry.class,
		Sample.class,
		// ----------- Elements -------------
		AppSink.class,
		AppSrc.class,
		BaseSrc.class,
		BaseSink.class,
		BaseTransform.class,
		Bin.class,
		DecodeBin.class,
		FakeSrc.class,
		FakeSink.class,
		Pipeline.class,
		PlayBin.class,
		URIDecodeBin.class,
		// ----------- Messages -------------
		AsyncDoneMessage.class,
		AsyncStartMessage.class,
		BufferingMessage.class,
		DurationChangedMessage.class,
		EOSMessage.class,
		ErrorMessage.class,
		GErrorMessage.class,
		InfoMessage.class,
		LatencyMessage.class,
		NeedContextMessage.class,
		NewClockMessage.class,
		QosMessage.class,
		SegmentDoneMessage.class,
		StateChangedMessage.class,
		StreamStartMessage.class,
		StreamStatusMessage.class,
		TagMessage.class,
		WarningMessage.class,
		// ----------- Queries -------------
		ContextQuery.class,
		ConvertQuery.class,
		CustomQuery.class,
		DurationQuery.class,
		FormatsQuery.class,
		LatencyQuery.class,
		PositionQuery.class,
		SeekingQuery.class,
		SegmentQuery.class,
		// ----------- Events -------------
		BufferSizeEvent.class,
		BusSyncHandler.class,
		CapsEvent.class,
		EOSEvent.class,
		FlushStartEvent.class,
		FlushStopEvent.class,
		ForceKeyUnit.class,
		LatencyEvent.class,
		NavigationEvent.class,
		QOSEvent.class,
		ReconfigureEvent.class,
		SeekEvent.class,
		SegmentEvent.class,
		StepEvent.class,
		StreamStartEvent.class,
		TagEvent.class,
	};
}
