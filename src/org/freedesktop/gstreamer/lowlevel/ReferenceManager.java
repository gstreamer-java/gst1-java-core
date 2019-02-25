/* 
 * Copyright (C) 2008 Wayne Meissner
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

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.freedesktop.gstreamer.glib.RefCountedObject;

/**
 * Manages keep alive links from one object to another.
 */
public class ReferenceManager {
    private ReferenceManager() {}
    /**
     * Adds a link from <tt>ref</tt> to <tt>target</tt> that keeps target alive
     * whilst <tt>ref</tt> is alive.
     * 
     * @param ref
     * @param target
     */
    public static <T extends Object> T addKeepAliveReference(T ref, RefCountedObject target) {
        if (ref != null) {
            StaticData.map.put(new WeakReference<Object>(ref, StaticData.queue), target);
        }
        return ref;
    }
    
    /**
     * Holds static data for lazy loading.
     */
    private static class StaticData {
        private static final Map<Object, Object> map = new ConcurrentHashMap<Object, Object>();
        private static final ReferenceQueue<Object> queue = new ReferenceQueue<Object>();
        static {
            Thread t = new Thread(new Runnable() {

                public void run() {
                    while (true) {
                        try {
                            map.remove(queue.remove());
                        } catch (InterruptedException ex) {
                            break;
                        } catch (Throwable ex) {
                            // Don't break out of the loop for any other reason
                            continue;
                        }
                    }
                }
            });
            t.setName("Reference reaper");
            t.setDaemon(true);
            t.start();
        }
    }
}
