/*
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2008 Andres Colubri
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

package org.gstreamer.lowlevel;

import org.gstreamer.Element;
import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface GstInterfacesAPI extends Library {
    GstInterfacesAPI GSTINTERFACES_API = GstNative.load(GstInterfacesAPI.class);

    GType  gst_implements_interface_get_type();
    Pointer gst_implements_interface_cast(NativeObject from, GType type);
    Pointer gst_implements_interface_check(NativeObject from, GType type);
    boolean gst_element_implements_interface(Element element, GType iface_type);
}
