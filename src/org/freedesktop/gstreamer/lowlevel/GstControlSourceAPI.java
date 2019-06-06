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
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * GstControlSource API
 *
 * https://gstreamer.freedesktop.org/data/doc/gstreamer/head/gstreamer/html/GstControlBinding.html
 * https://gitlab.freedesktop.org/gstreamer/gstreamer/tree/master/libs/gst/controller
 */

public interface GstControlSourceAPI extends Library {

    GstControlSourceAPI GSTCONTROLSOURCE_API = GstNative.load(GstControlSourceAPI.class);    


    boolean gst_control_source_get_value(GstControlSourcePtr self, long timestamp, double[] value);
    boolean gst_control_source_get_value_array(GstControlSourcePtr self, long timestamp, long interval, int n_values, double[] values);

//    static class Direct implements GstControlSourceAPI {
//
//        @Override
//        public native boolean gst_control_source_get_value(GstControlSourcePtr self, long timestamp, double[] value);
//
//        @Override
//        public native boolean gst_control_source_get_value_array(GstControlSourcePtr self, long timestamp, long interval, int n_values, double[] values);
//        
//    }

    @Structure.FieldOrder({"timestamp", "value"})
    public static final class GstTimedValue extends Structure {
        
        public volatile long timestamp;
        public volatile double value;
        
        public GstTimedValue() {
            super();
        }
        
        public GstTimedValue(Pointer ptr) {
            super(ptr);
        }
        
    } 
    
    

}
