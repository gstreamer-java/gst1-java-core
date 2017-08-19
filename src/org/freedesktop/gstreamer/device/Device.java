/*
 * Copyright (c) 2015 Andres Colubri <andres.colubri@gmail.com>
*  Copyright (C) 2013 Olivier Crete <olivier.crete@collabora.com>
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

import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.lowlevel.GstDeviceAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;

/**
 *
 * @author andres
 */
public class Device extends GstObject {
    public static final String GTYPE_NAME = "GstDevice";
    
    private static final GstDeviceAPI gst = GstNative.load(GstDeviceAPI.class);
    
    public Device(Initializer init) {
        super(init);
    }
    
    public Element createElement(String name) {
        return gst.gst_device_create_element(this, name);    
    }
    
    public Caps getCaps() {
        return gst.gst_device_get_caps(this);
    }
    
    public String getDeviceClass() {
        return gst.gst_device_get_device_class(this);
    }
    
    public String getDisplayName() {
        return gst.gst_device_get_display_name(this);
    }
    
    public boolean hasClasses(String classes) {
        return gst.gst_device_has_classes(this, classes);
    }     

    public boolean hasClasses(String[] classes) {
        return gst.gst_device_has_classesv(this, classes);
    }    
    
    public boolean reconfigureElement(Element element) {
        return gst.gst_device_reconfigure_element(this, element);
    }
    
    public Structure getProperties() {
        return gst.gst_device_get_properties(this);
    }
}
