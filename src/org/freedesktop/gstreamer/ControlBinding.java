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

import org.freedesktop.gstreamer.lowlevel.GValueAPI;
import org.freedesktop.gstreamer.lowlevel.GstControlBindingPtr;

import static org.freedesktop.gstreamer.lowlevel.GstControlBindingAPI.GSTCONTROLBINDING_API;

/**
 * Attachment for control source sources.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstControlBinding.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstControlBinding.html</a>
 * <p>
 */
public class ControlBinding extends GstObject {

    public static final String GTYPE_NAME = "GstControlBinding";
    
    private final Handle handle;

    protected ControlBinding(Handle handle, boolean needRef) {
        super(handle, needRef);
        this.handle = handle;
    }

    ControlBinding(Initializer init) {
        this(new Handle(
                init.ptr.as(GstControlBindingPtr.class, GstControlBindingPtr::new),
                init.ownsHandle),
                init.needRef);
    }

    /**
     * Gets the value for the given controlled property at the requested time.
     *
     * @param timestamp the time the control-change should be read from
     * @return the value of the property at the given time, or NULL if the
     * property isn't controlled
     */
    public Object getValue(long timestamp) {
        GValueAPI.GValue gValue = GSTCONTROLBINDING_API.gst_control_binding_get_value(
                handle.getPointer(), timestamp);
        return gValue == null ? null : gValue.getValue();
    }

    /**
     * Gets a number of values for the given controlled property starting at
     * the requested time.
     * <p>
     * This function is useful if one wants to e.g. draw a graph of the control
     * curve or apply a control curve sample by sample.
     * 
     * @param timestamp the time that should be processed
     * @param interval the time spacing between subsequent values
     * @param values array to fill with control values
     * @return false if the given array could not be filled
     */
    public boolean getValueArray(long timestamp, long interval, Object[] values) {
        GValueAPI.GValue[] gValues = new GValueAPI.GValue[values.length];
        boolean ok = GSTCONTROLBINDING_API.gst_control_binding_get_g_value_array(
                handle.getPointer(),
                timestamp,
                interval,
                gValues.length,
                gValues);
        if (ok) {
            for (int i = 0; i < values.length; i++) {
                values[i] = gValues[i].getValue();
            }
        }
        return ok;
    }
    
    /**
     * This function is used to disable a control binding for some time, i.e.
     * GstObject.syncValues() will do nothing.
     * 
     * @param disabled whether to disable the controller or not
     */
    public void setDisabled(boolean disabled) {
        GSTCONTROLBINDING_API.gst_control_binding_set_disabled(handle.getPointer(), disabled);
    }
    
    /**
     * Check if the control binding is disabled.
     * 
     * @return TRUE if the binding is inactive
     */
    public boolean isDisabled() {
        return GSTCONTROLBINDING_API.gst_control_binding_is_disabled(handle.getPointer());
        
    }
    

    protected static class Handle extends GstObject.Handle {

        public Handle(GstControlBindingPtr ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected GstControlBindingPtr getPointer() {
            return (GstControlBindingPtr) super.getPointer();
        }

    }

}
