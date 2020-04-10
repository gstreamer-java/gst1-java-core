/* 
 * Copyright (c) 2020 Neil C Smith
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2007, 2008 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wtay@chello.be>
 *                    2005 Wim Taymans <wim@fluendo.com>
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

import com.sun.jna.Pointer;
import java.util.logging.Logger;
import org.freedesktop.gstreamer.glib.GObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GObjectAPI;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GstControlBindingPtr;
import org.freedesktop.gstreamer.lowlevel.GstObjectPtr;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValue;

import static org.freedesktop.gstreamer.lowlevel.GObjectAPI.GOBJECT_API;
import static org.freedesktop.gstreamer.lowlevel.GValueAPI.GVALUE_API;
import static org.freedesktop.gstreamer.lowlevel.GlibAPI.GLIB_API;
import static org.freedesktop.gstreamer.lowlevel.GstObjectAPI.GSTOBJECT_API;
import static org.freedesktop.gstreamer.lowlevel.GstValueAPI.GSTVALUE_API;

/**
 * Base class for the GStreamer object hierarchy
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstObject.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstObject.html</a>
 * <p>
 * GstObject provides a root for the object hierarchy tree filed in by the
 * GStreamer library. It is currently a thin wrapper on top of {@link GObject}.
 * It is an abstract class that is not very usable on its own.
 *
 */
public class GstObject extends GObject {

    private static Logger LOG = Logger.getLogger(GstObject.class.getName());

    private final Handle handle;

    /**
     * Wraps an underlying C GstObject with a Java proxy
     *
     * @param init Initialization data
     */
    protected GstObject(Initializer init) {
        this(new Handle(init.ptr.as(GstObjectPtr.class, GstObjectPtr::new), init.ownsHandle), init.needRef);
    }

    protected GstObject(Handle handle, boolean needRef) {
        super(handle, needRef);
        this.handle = handle;
    }

    /**
     * Set the value of a GstObject property from a String representation.
     * <p>
     * The data value is deserialized using <code>gst_value_deserialize</code>.
     *
     * @param property the property to set
     * @param data the value as a valid String representation
     * @throws IllegalArgumentException if the data cannot be deserialized to
     * the required type.
     */
    public void setAsString(String property, String data) {
        GObjectAPI.GParamSpec propertySpec = findProperty(property);
        if (propertySpec == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        final GType propType = propertySpec.value_type;

        GValue propValue = new GValue();
        GVALUE_API.g_value_init(propValue, propType);

        boolean success = GSTVALUE_API.gst_value_deserialize(propValue, data);

        if (success) {
            GOBJECT_API.g_param_value_validate(propertySpec, propValue);
            GOBJECT_API.g_object_set_property(this, property, propValue);
        }

        GVALUE_API.g_value_unset(propValue); // Release any memory

        if (!success) {
            throw new IllegalArgumentException(
                    "Unable to deserialize data to required type: "
                            + propType.getTypeName());
        }

    }

    /**
     * Get the value of a GstObject property as a serialized String
     * representation of its value.
     * <p>
     * The data value is serialized using <code>gst_value_serialize</code>.
     *
     * @param property the property to get
     * @return serialized String value of property
     */
    public String getAsString(String property) {
        GObjectAPI.GParamSpec propertySpec = findProperty(property);
        if (propertySpec == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        final GType propType = propertySpec.value_type;
        GValue propValue = new GValue();
        GVALUE_API.g_value_init(propValue, propType);
        GOBJECT_API.g_object_get_property(this, property, propValue);
        Pointer ptr = GSTVALUE_API.gst_value_serialize(propValue);
        String ret = ptr.getString(0);
        GLIB_API.g_free(ptr);
        return ret;
    }

    private GObjectAPI.GParamSpec findProperty(String propertyName) {
        Pointer ptr = GOBJECT_API.g_object_class_find_property(
                getRawPointer().getPointer(0), propertyName);
        if (ptr == null) {
            return null;
        }
        return new GObjectAPI.GParamSpec(ptr);
    }

    /**
     * Sets the name of this object, or gives this object a guaranteed unique
     * name (if name is null).
     *
     * Returns: TRUE if the name could be set. Since Objects that have a parent
     * cannot be renamed, this function returns FALSE in those cases.
     *
     * MT safe.
     *
     * @param name new name of object
     * @return true if the name was set. Since Objects that have a parent cannot
     * be renamed, this function returns false in those cases.
     */
    public boolean setName(String name) {
        LOG.entering("GstObject", "setName", name);
        return GSTOBJECT_API.gst_object_set_name(this, name);
    }

    /**
     * Returns a copy of the name of this object.
     *
     * For a nameless object, this returns null.
     *
     * @return the name of this object.
     */
    public String getName() {
        LOG.entering("GstObject", "getName");
        return GSTOBJECT_API.gst_object_get_name(this);
    }

    /**
     * Returns this object's parent, if there is one.
     *
     * @return parent or <code>null</code>
     */
    public GstObject getParent() {
        return GSTOBJECT_API.gst_object_get_parent(this);
    }

    /**
     * Returns a suggestion for timestamps where buffers should be split to get
     * best controller results.
     *
     * @return the suggested timestamp or {@link ClockTime#NONE} if no
     * control-rate was set.
     */
    public long suggestNextSync() {
        return GSTOBJECT_API.gst_object_suggest_next_sync(handle.getPointer());
    }

    /**
     * Sets the properties of the object, according to the {@link ControlSource}
     * that (maybe) handle them and for the given timestamp.
     * <p>
     * If this function fails, it is most likely the application developers
     * fault. Most probably the control sources are not setup correctly.
     *
     * @param timestamp the time that should be processed
     * @return true if the controller values have been applied to the object
     * properties
     */
    public boolean syncValues(long timestamp) {
        return GSTOBJECT_API.gst_object_sync_values(handle.getPointer(), timestamp);
    }

    /**
     * Check if this object has active controlled properties.
     *
     * @return TRUE if the object has active controlled properties
     */
    public boolean hasActiveControlBindings() {
        return GSTOBJECT_API.gst_object_has_active_control_bindings(handle.getPointer());
    }

    /**
     * This function is used to disable all controlled properties of the object
     * for some time, i.e. {@link #syncValues(long) } will do nothing.
     *
     * @param disabled whether to disable the controllers or not
     */
    public void setControlBindingsDisabled(boolean disabled) {
        GSTOBJECT_API.gst_object_set_control_bindings_disabled(handle.getPointer(), disabled);
    }

    /**
     * This function is used to disable the control bindings on a property for
     * some time, i.e. {@link #syncValues(long) } will do nothing for the
     * property.
     *
     * @param propertyName property to disable
     * @param disabled whether to disable the controller or not
     */
    public void setControlBindingDisabled(String propertyName, boolean disabled) {
        GSTOBJECT_API.gst_object_set_control_binding_disabled(handle.getPointer(), propertyName, disabled);
    }

    /**
     * Attach a {@link ControlBinding } to this object. If there was already a
     * binding for this property it will be replaced.
     *
     * @param binding the ControlBinding that should be used
     * @throws IllegalStateException if the binding has not been setup for this
     * object
     */
    public void addControlBinding(ControlBinding binding) {
        GstControlBindingPtr bindingPtr = Natives.getPointer(binding)
                .as(GstControlBindingPtr.class, GstControlBindingPtr::new);
        boolean ok = GSTOBJECT_API.gst_object_add_control_binding(handle.getPointer(), bindingPtr);
        if (!ok) {
            throw new IllegalStateException();
        }
    }

    /**
     * Gets the corresponding {@link ControlBinding} for the property.
     *
     * @param propertyName name of the property
     * @return control binding for the property or NULL if the property is not
     * controlled
     */
    public ControlBinding getControlBinding(String propertyName) {
        GstControlBindingPtr ptr = GSTOBJECT_API.gst_object_get_control_binding(
                handle.getPointer(), propertyName);
        return ptr == null ? null : Natives.callerOwnsReturn(ptr, ControlBinding.class);
    }

    /**
     * Removes the corresponding {@link ControlBinding }.
     *
     * @param binding the binding to remove
     * @return true if the binding could be removed
     */
    public boolean removeControlBinding(ControlBinding binding) {
        GstControlBindingPtr bindingPtr = Natives.getPointer(binding)
                .as(GstControlBindingPtr.class, GstControlBindingPtr::new);
        return GSTOBJECT_API.gst_object_remove_control_binding(handle.getPointer(), bindingPtr);
    }

    @Override
    public String toString() {
        return String.format("%s: [%s]", getClass().getSimpleName(), getName());
    }
//    protected static Initializer initializer(Pointer ptr) {
//        return initializer(ptr, true, true);
//    }
//    
//    protected static Initializer initializer(Pointer ptr, boolean needRef) {
//        return initializer(ptr, needRef, true);
//    }

    protected static class Handle extends GObject.Handle {

        public Handle(GstObjectPtr ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected void ref() {
            GSTOBJECT_API.gst_object_ref(getPointer());
        }

        @Override
        protected void sink() {
            GSTOBJECT_API.gst_object_ref_sink(getPointer());
        }

        @Override
        protected void unref() {
            GSTOBJECT_API.gst_object_unref(getPointer());
        }

        @Override
        protected GstObjectPtr getPointer() {
            return (GstObjectPtr) super.getPointer();
        }

    }

}
