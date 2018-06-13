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

package org.freedesktop.gstreamer.lowlevel;

import com.sun.jna.Native;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.PadDirection;
import org.freedesktop.gstreamer.PadPresence;
import org.freedesktop.gstreamer.PadTemplate;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.IncRef;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

/**
 * GstPadTemplate functions
 */
public interface GstPadTemplateAPI extends com.sun.jna.Library {
    GstPadTemplateAPI GSTPADTEMPLATE_API = GstNative.load(GstPadTemplateAPI.class);

    /* element class pad templates */
    void gst_element_class_add_pad_template(Pointer klass, PadTemplate templ);
    PadTemplate gst_element_class_get_pad_template(Pointer /*GstElementClass*/ element_class, String name);
    
    /* templates and factories */
    GType gst_pad_template_get_type();
    GType gst_static_pad_template_get_type();

    @CallerOwnsReturn Pointer ptr_gst_pad_template_new(String name_template, PadDirection direction, 
            PadPresence presence, @IncRef Caps caps);
    @CallerOwnsReturn PadTemplate gst_pad_template_new(String name_template, PadDirection direction, 
            PadPresence presence, @IncRef Caps caps);
    @CallerOwnsReturn PadTemplate gst_static_pad_template_get(GstStaticPadTemplate pad_template);
    @CallerOwnsReturn Caps gst_static_pad_template_get_caps(GstStaticPadTemplate template);
    @CallerOwnsReturn Caps gst_pad_template_get_caps(PadTemplate template);
    void gst_pad_template_pad_created(PadTemplate templ, Pad pad);
    
    public static final class GstStaticPadTemplate extends PointerType {

        public GstStaticPadTemplate() {
        }

        public GstStaticPadTemplate(Pointer p) {
            super(p);
        }
        
        public String getName() {
            return getPointer().getPointer(0).getString(0);
        }
        
        public PadDirection getPadDirection() {
            return PadDirection.values()[getPointer().getInt(Native.POINTER_SIZE)];
        }
        
        public PadPresence getPadPresence() {
            return PadPresence.values()[getPointer().getInt(Native.POINTER_SIZE + 4)];
        }
    }  
    
}
