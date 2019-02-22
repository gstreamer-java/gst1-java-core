/*
 * 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2015 Andres Colubri <andres.colubri@gmail.com>
 * Copyright (C) 2013 Olivier Crete <olivier.crete@collabora.com>
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
package org.freedesktop.gstreamer.device;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.Structure;

import static org.freedesktop.gstreamer.lowlevel.GlibAPI.GLIB_API;
import static org.freedesktop.gstreamer.lowlevel.GstDeviceAPI.GSTDEVICE_API;

/**
 * Devices are objects representing a device, they contain relevant metadata
 * about the device, such as its class and the GstCaps representing the media
 * types it can produce or handle.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/gstreamer-GstDevice.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/gstreamer-GstDevice.html</a>
 * <p>
 * Device objects are created by {@link DeviceProvider} objects, which can be
 * aggregated by {@link DeviceMonitor} objects.
 */
public class Device extends GstObject {

    public static final String GTYPE_NAME = "GstDevice";

    Device(Initializer init) {
        super(init);
    }

    /**
     * Creates the element with all of the required parameters set to use this
     * device.
     *
     * @param name name of new element, or NULL to automatically create a unique
     * name
     * @return a new {@link Element} configured to use this device.
     */
    public Element createElement(String name) {
        return GSTDEVICE_API.gst_device_create_element(this, name);
    }

    /**
     * Get the {@link Caps} that this device supports.
     *
     * @return The Caps supported by this device.
     */
    public Caps getCaps() {
        return GSTDEVICE_API.gst_device_get_caps(this);
    }

    /**
     * Gets the "class" of a device. This is a "/" separated list of classes
     * that represent this device. They are a subset of the classes of the
     * {@link DeviceProvider} that produced this device.
     *
     * @return class the device class
     */
    public String getDeviceClass() {
        Pointer ptr = GSTDEVICE_API.gst_device_get_device_class(this);
        String ret = ptr.getString(0);
        GLIB_API.g_free(ptr);
        return ret;
    }

    /**
     * Gets the user-friendly name of the device.
     *
     * @return name of the device
     */
    public String getDisplayName() {
        Pointer ptr = GSTDEVICE_API.gst_device_get_display_name(this);
        String ret = ptr.getString(0);
        GLIB_API.g_free(ptr);
        return ret;
    }

    /**
     * Check if device matches all of the given classes.
     *
     * @param classes a "/"-separated list of device classes to match, only
     * match if all classes are matched
     * @return true if device matches
     */
    public boolean hasClasses(String classes) {
        return GSTDEVICE_API.gst_device_has_classes(this, classes);
    }

    /**
     * Check if device matches all of the given classes.
     *
     * @param classes an array of classes to match, only match if all classes
     * are matched.
     * @return true if device matches
     */
    public boolean hasClasses(String[] classes) {
        return GSTDEVICE_API.gst_device_has_classesv(this, classes);
    }

    /**
     * Tries to reconfigure an existing element to use the device. If this
     * function fails, then one must destroy the element and create a new one
     * using {@link #createElement(java.lang.String) }
     * <p>
     * Note: This should only be implemented for elements can change their
     * device in the PLAYING state.
     * 
     * @param element the element to be configured
     * @return true if the element could be reconfigured to use this device
     */
    public boolean reconfigureElement(Element element) {
        return GSTDEVICE_API.gst_device_reconfigure_element(this, element);
    }

    /**
     * Gets the extra properties of a device.
     * 
     * @return The extra properties or NULL when there are none.
     */
    public Structure getProperties() {
        return GSTDEVICE_API.gst_device_get_properties(this);
    }
}
