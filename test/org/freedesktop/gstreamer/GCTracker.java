/* 
 * Copyright (C) 2007, 2008 Wayne Meissner
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

import org.freedesktop.gstreamer.glib.GObject;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

import org.freedesktop.gstreamer.lowlevel.GObjectAPI;
import org.freedesktop.gstreamer.lowlevel.IntPtr;

import com.sun.jna.Pointer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of GObject derived objects.
 */
public class GCTracker {

    private static Map<Integer,WeakReference<GCTracker>> objectMap =
            Collections.synchronizedMap(new HashMap<Integer,WeakReference<GCTracker>>());
    
    private static final GObjectAPI.GWeakNotify notify = new GObjectAPI.GWeakNotify() {

        @Override
        public void callback(IntPtr id, Pointer obj) {
            int identityHashCode = id.intValue();
            WeakReference<GCTracker> trackerRef = objectMap.get(identityHashCode);
            GCTracker tracker = trackerRef.get();
            if(tracker != null) {
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
    
    private final WeakReference<GObject> ref;
    private final AtomicBoolean destroyed = new AtomicBoolean(false);
    
    public GCTracker(GObject obj) {
        int identityHashCode = System.identityHashCode(this);
        ref = new WeakReference<GObject>(obj);
        objectMap.put(identityHashCode, new WeakReference<GCTracker>(this));
        GObjectAPI.GOBJECT_API.g_object_weak_ref(obj, notify, new IntPtr(identityHashCode));
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
}
