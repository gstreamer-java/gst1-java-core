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

//import org.freedesktop.gstreamer.controller.ControlSource;
import org.freedesktop.gstreamer.lowlevel.GObjectAPI.GParamSpec;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValue;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import java.util.Arrays;
import java.util.List;
import org.freedesktop.gstreamer.GstObject;
import static org.freedesktop.gstreamer.lowlevel.GstAPI.GST_PADDING;
import org.freedesktop.gstreamer.lowlevel.GstObjectAPI.GstObjectClass;

/**
 * GstControlSource methods and structures
 * @see https://cgit.freedesktop.org/gstreamer/gstreamer/tree/gst/gstcontrolsource.h?h=1.8
 */

// @TODO review in line with https://gitlab.freedesktop.org/gstreamer/gstreamer/tree/master/libs/gst/controller

public interface GstControlSourceAPI extends Library {
//    
//    GstControlSourceAPI GSTCONTROLSOURCE_API = GstNative.load("gstcontroller", GstControlSourceAPI.class);    
//	
//    /**    
//    * GstTimedValue:
//    * @timestamp: timestamp of the value change
//    * @value: the corresponding value
//    *
//    * Structure for saving a timestamp and a value.
//    */
//    public static final class TimedValue extends com.sun.jna.Structure {
//    	public static final String GTYPE_NAME = "GstTimedValue";
//        
//        public volatile long timestamp;
//        public volatile double value;
//
//        @Override
//        protected List<String> getFieldOrder() {
//            return Arrays.asList(new String[]{
//                "timestamp", "value"
//            });
//        }
//    }
//	
//    public static interface GstControlSourceGetValue extends Callback {
//        public boolean callback(ControlSource self, long timestamp, DoubleByReference value);
//    }
//    public static interface GstControlSourceGetValueArray extends Callback {
//        public boolean callback(ControlSource self, long timestamp, long interval, int n_values, DoubleByReference values);
//    }
//    public static interface GstControlSourceBind extends Callback {
//        public boolean callback(ControlSource self, GParamSpec pspec);
//    }
//    
//    /**
//    * GstControlSource:
//    * @get_value: Function for returning a value for a given timestamp
//    * @get_value_array: Function for returning a values array for a given timestamp
//    *
//    * The instance structure of #GstControlSource.
//    */
//    public static final class GstControlSourceStruct extends com.sun.jna.Structure {
//        public volatile GstObject parent;
//
//        /*< public >*/
//        public volatile GstControlSourceGetValue get_value;             /* Returns the value for a property at a given timestamp */
//        public volatile GstControlSourceGetValueArray get_value_array;  /* Returns values for a property in a given timespan */
//
//        /*< private >*/
//        public volatile Pointer[] _gst_reserved = new Pointer[GST_PADDING];
//
//        @Override
//        protected List<String> getFieldOrder() {
//            return Arrays.asList(new String[]{
//                "parent", "get_value", "get_value_array",
//                "_gst_reserved"
//            });
//        }
//    }
//
//    /**
//    * GstControlSourceClass:
//    * @parent_class: Parent class
//    *
//    * The class structure of #GstControlSource.
//    */
//    public static final class GstControlSourceClass extends com.sun.jna.Structure {
//        public volatile GstObjectClass parent_class;
//
//        /*< private >*/
//        public volatile Pointer[] _gst_reserved = new Pointer[GST_PADDING];
//
//        @Override
//        protected List<String> getFieldOrder() {
//            return Arrays.asList(new String[]{
//                "parent_class", "_gst_reserved"
//            });
//        }
//    }
//	
//    GType gst_control_source_get_type();
//
//    /* Functions */
//    boolean gst_control_source_get_value(ControlSource self, long timestamp, GValue value);
//    boolean gst_control_source_get_value_array(ControlSource self, long timestamp, long interval, int n_values, DoubleByReference values);
}
