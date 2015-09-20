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

import org.freedesktop.gstreamer.ClockTime;
import org.freedesktop.gstreamer.GObject;
import org.freedesktop.gstreamer.controller.ControlSource;
import org.freedesktop.gstreamer.controller.Controller;
import org.freedesktop.gstreamer.lowlevel.GObjectAPI.GObjectClass;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValue;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GSList;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.Arrays;
import java.util.List;

public interface GstControllerAPI extends Library {
	GstControllerAPI GSTCONTROLLER_API = GstNative.load("gstcontroller", GstControllerAPI.class);
    int GST_PADDING = GstAPI.GST_PADDING;
	
	public static final class GstControllerStruct extends com.sun.jna.Structure {
		public volatile GObjectAPI.GObjectStruct parent;
		
		public volatile GList properties;          /* List of GstControlledProperty */
		public volatile /* GMutex */ Pointer lock; /* Secure property access, elements will access from threads */
		public volatile GObject object;            /* the object we control */

		  /*< private >*/
		public volatile /* GstControllerPrivate */ Pointer priv;
		public volatile Pointer[] _gst_reserved = new Pointer[GST_PADDING - 1];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent", "properties", "lock",
                "object", "priv", "_gst_reserved"
            });
        }
	}
	
	public static final class GstControllerClass extends com.sun.jna.Structure {
		public GstControllerClass() {}
        public GstControllerClass(Pointer ptr) {
            useMemory(ptr);
            read();
        }

        public volatile GObjectClass parent_class;

		/*< private >*/
		public volatile Pointer[] _gst_reserved = new Pointer[GST_PADDING];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_class", "_gst_reserved"
            });
        }
	}	
	
	GType gst_controller_get_type();

	/* GstController functions */
	Controller gst_controller_new_list(GObject object, GList list);
	Controller gst_controller_new(GObject ... object) /*G_GNUC_NULL_TERMINATED*/;

	boolean gst_controller_remove_properties_list(Controller self, GList list);
	boolean gst_controller_remove_properties(Controller ... self) /*G_GNUC_NULL_TERMINATED*/;

	void gst_controller_set_disabled(Controller self, boolean disabled);
	void gst_controller_set_property_disabled(Controller self, String property_name, boolean disabled);
	boolean gst_controller_set_control_source(Controller self, String property_name, ControlSource csource);
	ControlSource gst_controller_get_control_source(Controller self, String property_name);

	ClockTime gst_controller_suggest_next_sync(Controller self);
	boolean gst_controller_sync_values(Controller self, ClockTime timestamp);

	GValue gst_controller_get(Controller self, String property_name, ClockTime timestamp);
	boolean gst_controller_get_value_arrays(Controller self, ClockTime timestamp, GSList value_arrays);
	//boolean gst_controller_get_value_array(Controller self, ClockTime timestamp, ValueArray * value_array);

	/* GObject convenience functions */
	Controller gst_object_control_properties(GObject ... object) /*G_GNUC_NULL_TERMINATED*/;
	boolean gst_object_uncontrol_properties(GObject ... object) /*G_GNUC_NULL_TERMINATED*/;

	Controller gst_object_get_controller(GObject object);
	boolean gst_object_set_controller(GObject object, Controller controller);

	ClockTime gst_object_suggest_next_sync(GObject object);
	boolean gst_object_sync_values(GObject object, ClockTime timestamp);

	boolean gst_object_set_control_source(GObject object, String property_name, ControlSource csource);
	ControlSource gst_object_get_control_source(GObject object, String property_name);

	boolean gst_object_get_value_arrays(GObject object, ClockTime timestamp, GSList value_arrays);
	//boolean gst_object_get_value_array(GObject object, ClockTime timestamp, ValueArray * value_array);

	ClockTime gst_object_get_control_rate(GObject object);
	void gst_object_set_control_rate(GObject object, ClockTime control_rate);
	/* lib init/done */
	boolean gst_controller_init(IntByReference argc, PointerByReference argv);	
}
