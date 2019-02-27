/* 
 * Copyright (c) 2019 Neil C Smith
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

import org.freedesktop.gstreamer.glib.RefCountedObject;
import org.freedesktop.gstreamer.lowlevel.GPointer;

import static org.freedesktop.gstreamer.lowlevel.GstClockAPI.GSTCLOCK_API;

/**
 * A datatype to hold the handle to an outstanding sync or async clock callback.
 */
public class ClockID extends RefCountedObject implements Comparable<ClockID> {

    ClockID(Initializer init) {
        super(new Handle(init.ptr, init.ownsHandle), init.needRef);
    }

    /**
     * Cancel an outstanding request. This can either be an outstanding async
     * notification or a pending sync notification. After this call, @id cannot
     * be used anymore to receive sync or async notifications, you need to
     * create a new #GstClockID.
     */
    public void unschedule() {
        GSTCLOCK_API.gst_clock_id_unschedule(this);
    }

    /**
     * Gets the time of the clock ID
     * <p>
     * Thread safe.
     *
     * @return The time of this clock id.
     */
    public long getTime() {
        return GSTCLOCK_API.gst_clock_id_get_time(this);
    }

    /**
     * Compares this ClockID to another.
     *
     * @param other The other ClockID to compare to
     * @return negative value if a < b; zero if a = b; positive value if a > b
     */
    @Override
    public int compareTo(ClockID other) {
        return GSTCLOCK_API.gst_clock_id_compare_func(this, other);
    }

    private static final class Handle extends RefCountedObject.Handle {

        public Handle(GPointer ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            GSTCLOCK_API.gst_clock_id_unref(ptr);
        }

        @Override
        protected void ref() {
            GSTCLOCK_API.gst_clock_id_ref(getPointer());
        }

        @Override
        protected void unref() {
            GSTCLOCK_API.gst_clock_id_unref(getPointer());
        }

    }

}
