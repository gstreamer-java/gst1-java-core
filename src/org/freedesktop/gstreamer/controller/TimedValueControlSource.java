/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer.controller;

import org.freedesktop.gstreamer.ControlSource;
import static org.freedesktop.gstreamer.lowlevel.GstControllerAPI.GSTCONTROLLER_API;
import org.freedesktop.gstreamer.lowlevel.GstTimedValueControlSourcePtr;

/**
 * Timed value control source base class.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer-libs/html/GstTimedValueControlSource.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer-libs/html/GstTimedValueControlSource.html</a>
 * <p>
 * Base class for {@link ControlSource} that use time-stamped values.
 * <p>
 * When overriding bind, chain up first to give this bind implementation a
 * chance to setup things.
 * <p>
 * All functions are MT-safe.
 */
public class TimedValueControlSource extends ControlSource {
    
    private final Handle handle;

    protected TimedValueControlSource(Handle handle, boolean needRef) {
        super(handle, needRef);
        this.handle = handle;
    }

    TimedValueControlSource(Initializer init) {
        this(new Handle(
                init.ptr.as(GstTimedValueControlSourcePtr.class,
                        GstTimedValueControlSourcePtr::new),
                init.ownsHandle),
                init.needRef);
    }

    /**
     * Set the value of given controller-handled property at a certain time.
     *
     * @param timestamp the time the control-change is scheduled for
     * @param value the control value
     * @return false if the the value could not be set
     */
    public boolean set(long timestamp, double value) {
        return GSTCONTROLLER_API.gst_timed_value_control_source_set(
                handle.getPointer(), timestamp, value);
    }
    
    /**
     * Used to remove the value of given controller-handled property at a
     * certain time.
     * 
     * @param timestamp the time the control-change should be removed from
     * @return FALSE if the value couldn't be unset (i.e. not found)
     */
    public boolean unset(long timestamp) {
        return GSTCONTROLLER_API.gst_timed_value_control_source_unset(
                handle.getPointer(), timestamp);
    }
    
    /**
     * Used to remove all time-stamped values of given controller-handled
     * property.
     */
    public void unsetAll() {
        GSTCONTROLLER_API.gst_timed_value_control_source_unset_all(handle.getPointer());
    }
    
    /**
     * Get the number of control points that are set.
     * 
     * @return the number of control points that are set
     */
    public int getCount() {
        return GSTCONTROLLER_API.gst_timed_value_control_source_get_count(handle.getPointer());
    }
    
    /**
     * Reset the controlled value cache.
     */
    public void invalidateCache() {
        GSTCONTROLLER_API.gst_timed_value_control_invalidate_cache(handle.getPointer());
    }
    

    protected static class Handle extends ControlSource.Handle {

        public Handle(GstTimedValueControlSourcePtr ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected GstTimedValueControlSourcePtr getPointer() {
            return (GstTimedValueControlSourcePtr) super.getPointer();
        }

    }

}
