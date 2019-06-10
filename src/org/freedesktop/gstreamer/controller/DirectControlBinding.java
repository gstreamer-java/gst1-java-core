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

import org.freedesktop.gstreamer.ControlBinding;
import org.freedesktop.gstreamer.ControlSource;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.glib.GObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstControlSourcePtr;
import org.freedesktop.gstreamer.lowlevel.GstDirectControlBindingPtr;

import static org.freedesktop.gstreamer.lowlevel.GstControllerAPI.GSTCONTROLLER_API;
import org.freedesktop.gstreamer.lowlevel.GstObjectPtr;

/**
 * Direct attachment for control sources.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer-libs/html/GstDirectControlBinding.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer-libs/html/GstDirectControlBinding.html</a>
 * <p>
 */
public class DirectControlBinding extends ControlBinding {
    
    public static final String GTYPE_NAME = "GstDirectControlBinding";

    DirectControlBinding(Initializer init) {
        this(new Handle(
                init.ptr.as(GstDirectControlBindingPtr.class, GstDirectControlBindingPtr::new),
                init.ownsHandle),
                init.needRef);
    }

    private DirectControlBinding(Handle handle, boolean needRef) {
        super(handle, needRef);
    }

    /**
     * Create a new control-binding that attaches the {@link ControlSource } to
     * the {@link GObject} property. It will map the control source range [0.0
     * ... 1.0] to the full target property range, and clip all values outside
     * this range.
     *
     * @param object the object of the property
     * @param propertyName the property-name to attach the control source
     * @param controlSource the control source
     * @return new DirectControlBinding
     */
    public static DirectControlBinding create(GstObject object, String propertyName, ControlSource controlSource) {
        GstDirectControlBindingPtr ptr = GSTCONTROLLER_API.gst_direct_control_binding_new(
                Natives.getPointer(object).as(GstObjectPtr.class, GstObjectPtr::new),
                propertyName,
                Natives.getPointer(controlSource).as(GstControlSourcePtr.class, GstControlSourcePtr::new));
        return new DirectControlBinding(new Handle(ptr, true), false);
    }
    
    /**
     * Create a new control-binding that attaches the {@link ControlSource } to
     * the {@link GObject} property. It will directly map the control source
     * values to the target property range without any transformations.
     *
     * @param object the object of the property
     * @param propertyName the property-name to attach the control source
     * @param controlSource the control source
     * @return new DirectControlBinding
     */
    public static DirectControlBinding createAbsolute(GstObject object,
            String propertyName, ControlSource controlSource) {
        GstDirectControlBindingPtr ptr = GSTCONTROLLER_API.gst_direct_control_binding_new_absolute(
                Natives.getPointer(object).as(GstObjectPtr.class, GstObjectPtr::new),
                propertyName,
                Natives.getPointer(controlSource).as(GstControlSourcePtr.class, GstControlSourcePtr::new));
        return new DirectControlBinding(new Handle(ptr, true), false);
    }

}
