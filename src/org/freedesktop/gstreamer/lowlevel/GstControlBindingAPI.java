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

package org.freedesktop.gstreamer.lowlevel;

import com.sun.jna.Library;

/**
 * GstControlBinding API
 * 
 * https://gstreamer.freedesktop.org/data/doc/gstreamer/head/gstreamer/html/GstControlBinding.html
 * https://gitlab.freedesktop.org/gstreamer/gstreamer/tree/master/libs/gst/controller
 */

public interface GstControlBindingAPI extends Library {

    GstControlBindingAPI GSTCONTROLBINDING_API = GstNative.load(GstControlBindingAPI.class);    


    boolean gst_control_binding_sync_values(GstControlBindingPtr binding,
            GstObjectPtr object,
            long timestamp,
            long lastSync);
    
    GValueAPI.GValue gst_control_binding_get_value(GstControlBindingPtr binding,
            long timestamp);
    
    boolean gst_control_binding_get_g_value_array(GstControlBindingPtr binding,
            long timestamp,
            long internal,
            int n_values,
            GValueAPI.GValue[] values);
    
    void gst_control_binding_set_disabled(GstControlBindingPtr binding,
            boolean disabled);
    
    boolean gst_control_binding_is_disabled(GstControlBindingPtr binding);

}
