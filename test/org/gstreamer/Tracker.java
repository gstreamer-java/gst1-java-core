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

package org.gstreamer;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

import org.gstreamer.lowlevel.GObjectAPI;
import org.gstreamer.lowlevel.IntPtr;

import com.sun.jna.Pointer;

/**
 * Keeps track of GObject derived objects.
 */
class Tracker {

    public Tracker(GObject obj) {
        GObjectAPI.GOBJECT_API.g_object_weak_ref(obj, notify, new IntPtr(System.identityHashCode(this)));
        ref = new WeakReference<GObject>(obj);
    }
    WeakReference<GObject> ref;
    private final AtomicBoolean destroyed = new AtomicBoolean(false);
    GObjectAPI.GWeakNotify notify = new GObjectAPI.GWeakNotify() {

        public void callback(IntPtr id, Pointer obj) {
            destroyed.set(true);
        }
    };

    public boolean waitGC() {
        return GarbageCollectionTest.waitGC(ref);
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
