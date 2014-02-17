/* 
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2007, 2008 Wayne Meissner
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

import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.PadDirection;
import org.gstreamer.lowlevel.GlibAPI.GList;
import org.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.gstreamer.lowlevel.annotations.Const;

import com.sun.jna.Pointer;

/**
 * GstElementFactory methods
 */
public interface GstElementFactoryAPI extends com.sun.jna.Library {
    GstElementFactoryAPI GSTELEMENTFACTORY_API = GstNative.load(GstElementFactoryAPI.class);

    GType gst_element_factory_get_type();
    ElementFactory gst_element_factory_find(String factoryName);
    @CallerOwnsReturn Pointer ptr_gst_element_factory_make(String factoryName, String elementName);
    @CallerOwnsReturn Pointer ptr_gst_element_factory_create(ElementFactory factory, String elementName);
    @CallerOwnsReturn Element gst_element_factory_make(String factoryName, String elementName);
    @CallerOwnsReturn Element gst_element_factory_create(ElementFactory factory, String elementName);
    GType gst_element_factory_get_element_type(ElementFactory factory);
    String gst_element_factory_get_longname(ElementFactory factory);
    String gst_element_factory_get_klass(ElementFactory factory);
    String gst_element_factory_get_description(ElementFactory factory);
    String gst_element_factory_get_author(ElementFactory factory);
    int gst_element_factory_get_num_pad_templates(ElementFactory factory);
    int gst_element_factory_get_uri_type(ElementFactory factory);
    GList gst_element_factory_get_static_pad_templates(ElementFactory factory);

    GList gst_element_factory_list_get_elements(long type, int minrank);
    GList gst_element_factory_list_filter(GList list, @Const Caps caps, PadDirection direction,
            boolean subsetonly);
    
    /* util elementfactory functions */
    boolean gst_element_factory_can_src_caps(ElementFactory factory, @Const Caps caps);
    boolean gst_element_factory_can_sink_caps(ElementFactory factory, @Const Caps caps);
}
