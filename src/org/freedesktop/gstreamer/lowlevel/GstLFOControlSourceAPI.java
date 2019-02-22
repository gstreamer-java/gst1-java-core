/* 
 * Copyright (c) 2009 Levente Farkas
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

//import org.freedesktop.gstreamer.controller.LFOControlSource;
//import org.freedesktop.gstreamer.lowlevel.GstControlSourceAPI.GstControlSourceClass;
//import org.freedesktop.gstreamer.lowlevel.GstControlSourceAPI.GstControlSourceStruct;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import java.util.Arrays;
import java.util.List;

// @TODO review in line with https://gitlab.freedesktop.org/gstreamer/gstreamer/blob/master/libs/gst/controller/gstlfocontrolsource.h

public interface GstLFOControlSourceAPI extends Library {
//	GstLFOControlSourceAPI GSTLFOCONTROLSOURCE_API = GstNative.load("gstcontroller", GstLFOControlSourceAPI.class);
//    int GST_PADDING = GstAPI.GST_PADDING;
//	
//    public enum Waveform
//    {
//      SINE,
//      SQUARE,
//      SAW,
//      REVERSE_SAW,
//      TRIANGLE;
//    }
//    
//	public static final class GstLFOControlSourceStruct extends com.sun.jna.Structure {
//		public volatile GstControlSourceStruct parent;
//
//		  /* <private> */
//		public volatile Pointer /* GstLFOControlSourcePrivate */ priv;
//		public volatile Pointer /* GMutex */ lock;
//		public volatile Pointer[] _gst_reserved = new Pointer[GST_PADDING];
//
//        @Override
//        protected List<String> getFieldOrder() {
//            return Arrays.asList(new String[]{
//                "parent", "priv", "lock",
//                "_gst_reserved"
//            });
//        }
//	}
//	
//	public static final class GstLFOControlSourceClass extends com.sun.jna.Structure {
//		public volatile GstControlSourceClass parent_class;
//		  
//		  /*< private >*/
//		public volatile Pointer[] _gst_reserved = new Pointer[GST_PADDING];
//
//        @Override
//        protected List<String> getFieldOrder() {
//            return Arrays.asList(new String[]{
//                "parent_class", "_gst_reserved"
//            });
//        }
//	}
//	
//	GType gst_lfo_control_source_get_type();
//	GType gst_lfo_waveform_get_type();
//
//	/* Functions */
//	LFOControlSource gst_lfo_control_source_new();
}
