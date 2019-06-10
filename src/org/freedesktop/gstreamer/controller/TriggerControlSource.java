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
import org.freedesktop.gstreamer.glib.NativeEnum;
import org.freedesktop.gstreamer.lowlevel.GstInterpolationControlSourcePtr;

import static org.freedesktop.gstreamer.lowlevel.GstControllerAPI.GSTCONTROLLER_API;
import org.freedesktop.gstreamer.lowlevel.GstTriggerControlSourcePtr;

/**
 * Trigger control source.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer-libs/html/GstTriggerControlSource.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer-libs/html/GstTriggerControlSource.html</a>
 * <p>
 * TriggerControlSource is a {@link ControlSource}, that returns values from
 * user-given control points. It allows for a tolerance on time-stamps.
 * <p>
 * To use TriggerControlSource create a new instance, bind it to a GParamSpec
 * and set some control points by calling
 * {@link TimedValueControlSource#set(long, double)}.
 * <p>
 * All functions are MT-safe.
 */
public class TriggerControlSource extends TimedValueControlSource {

    public static final String GTYPE_NAME = "GstTriggerControlSource";

    /**
     * Create a new, unbound InterpolationControlSource.
     */
    public TriggerControlSource() {
        this(new Handle(GSTCONTROLLER_API.gst_trigger_control_source_new(), true), false);
    }

    TriggerControlSource(Initializer init) {
        this(new Handle(
                init.ptr.as(GstTriggerControlSourcePtr.class,
                        GstTriggerControlSourcePtr::new),
                init.ownsHandle),
                init.needRef);
    }

    private TriggerControlSource(Handle handle, boolean needRef) {
        super(handle, needRef);
    }

    /**
     * Amount of nanoseconds a control time can be off to still trigger.
     * <p>
     * Allowed values: >= 0
     * <p>
     * Default value: 0
     *
     * @param tolerance in nanoseconds
     * @return this
     */
    public TriggerControlSource setTolerance(long tolerance) {
        set("tolerance", tolerance);
        return this;
    }

    /**
     * Current tolerance in nanoseconds.
     *
     * @return tolerance in nanoseconds
     */
    public long getTolerance() {
        Object val = get("tolerance");
        if (val instanceof Long) {
            return (long) val;
        }
        return 0L;
    }

    private static class Handle extends TimedValueControlSource.Handle {

        public Handle(GstTriggerControlSourcePtr ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

    }

}
