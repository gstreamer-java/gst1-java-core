/* 
 * Copyright (C) 2020 Neil C Smith
 * Copyright (C) 2008 Wayne Meissner
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
package org.freedesktop.gstreamer;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.glib.GObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectPtr;
import org.freedesktop.gstreamer.lowlevel.IntPtr;

import static org.freedesktop.gstreamer.lowlevel.GObjectAPI.*;
import static org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI.*;

/**
 * Tracks Java and/or native disposal of GObject and MiniObject derived objects.
 */
public class GCTracker {

    private static final Map<Integer, WeakReference<GCTracker>> OBJ_MAP
            = Collections.synchronizedMap(new HashMap<Integer, WeakReference<GCTracker>>());

    private final WeakReference<?> ref;
    private final AtomicBoolean destroyed = new AtomicBoolean(false);

    public GCTracker(GObject obj) {
        int identityHashCode = System.identityHashCode(this);
        ref = new WeakReference<>(obj);
        OBJ_MAP.put(identityHashCode, new WeakReference<>(this));
        GOBJECT_API.g_object_weak_ref(obj, GOBJECT_NOTIFY, new IntPtr(identityHashCode));
    }
    
    public GCTracker(MiniObject obj) {
        int identityHashCode = System.identityHashCode(this);
        ref = new WeakReference<>(obj);
        OBJ_MAP.put(identityHashCode, new WeakReference<>(this));
        GSTMINIOBJECT_API.gst_mini_object_weak_ref(
                Natives.getPointer(obj).as(GstMiniObjectPtr.class, GstMiniObjectPtr::new),
                GSTMINIOBJECT_NOTIFY,
                new IntPtr(identityHashCode));
    }

    public boolean waitGC() {
        return waitGC(ref);
    }

    public boolean waitDestroyed() {
        for (int i = 0; !destroyed.get() && i < 10; ++i) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
        return destroyed.get();
    }

    private static final GWeakNotify GOBJECT_NOTIFY = new GWeakNotify() {

        @Override
        public void callback(IntPtr id, Pointer obj) {
            int identityHashCode = id.intValue();
            WeakReference<GCTracker> trackerRef = OBJ_MAP.get(identityHashCode);
            GCTracker tracker = trackerRef.get();
            if (tracker != null) {
                tracker.destroyed.set(true);
            }
        }
    };

    private static final GstMiniObjectNotify GSTMINIOBJECT_NOTIFY
            = new GstMiniObjectNotify() {

        @Override
        public void callback(IntPtr id, Pointer obj) {
            int identityHashCode = id.intValue();
            WeakReference<GCTracker> trackerRef = OBJ_MAP.get(identityHashCode);
            GCTracker tracker = trackerRef.get();
            if (tracker != null) {
                tracker.destroyed.set(true);
            }
        }
    };

    public static boolean waitGC(WeakReference<? extends Object> ref) {
        System.gc();
        for (int i = 0; ref.get() != null && i < 10; ++i) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
            System.gc();
        }
        return ref.get() == null;
    }

}
