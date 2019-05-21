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
package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.GstControlSourcePtr;

import static org.freedesktop.gstreamer.lowlevel.GstControlSourceAPI.GSTCONTROLSOURCE_API;

/**
 * Base class for control source sources.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstControlSource.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstControlSource.html</a>
 * <p>
 */
public class ControlSource extends GstObject {

    private final Handle handle;

    protected ControlSource(Handle handle, boolean needRef) {
        super(handle, needRef);
        this.handle = handle;
    }

    ControlSource(Initializer init) {
        this(new Handle(
                init.ptr.as(GstControlSourcePtr.class, GstControlSourcePtr::new),
                init.ownsHandle),
                init.needRef);
    }

    /**
     * Gets the value for this ControlSource at a given timestamp.
     *
     * @param timestamp the time for which the value should be returned
     * @return value
     * @throws IllegalStateException if the value could not be calculated
     */
    public double getValue(long timestamp) {
        double[] out = new double[1];
        boolean ok = GSTCONTROLSOURCE_API.gst_control_source_get_value(handle.getPointer(), timestamp, out);
        if (ok) {
            return out[0];
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Gets an array of values for for this ControlSource. Values that are
     * undefined contain NANs.
     *
     * @param timestamp the first timestamp
     * @param interval the time steps
     * @param values array to put control-values in
     * @return true if the values were successfully calculated
     */
    public boolean getValueArray(long timestamp, long interval, double[] values) {
        return GSTCONTROLSOURCE_API.gst_control_source_get_value_array(
                handle.getPointer(),
                timestamp,
                interval,
                values.length,
                values);
    }

    protected static class Handle extends GstObject.Handle {

        public Handle(GstControlSourcePtr ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected GstControlSourcePtr getPointer() {
            return (GstControlSourcePtr) super.getPointer();
        }

    }

}
