/*
 * Copyright (c) 2010 Levente Farkas
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

package org.freedesktop.gstreamer.controller;

import java.util.logging.Logger;

import org.freedesktop.gstreamer.GObject;
import org.freedesktop.gstreamer.GstException;
import org.freedesktop.gstreamer.Gst.NativeArgs;
import org.freedesktop.gstreamer.lowlevel.GstControllerAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;

import com.sun.jna.Pointer;

/**
 * The controller subsystem offers a lightweight way to adjust gobject properties over stream-time.
 * It works by using time-stamped value pairs that are queued for element-properties.
 * At run-time the elements continously pull values changes for the current stream-time.
 * <p>
 * What needs to be changed in a GstElement? Very little - it is just two steps to make a plugin controllable!
 * <ul>
 * <li> mark gobject-properties paramspecs that make sense to be controlled, by GST_PARAM_CONTROLLABLE.
 * <li> when processing data (get, chain, loop function) at the beginning call gst_object_sync_values(element,timestamp).
 * </ul> 
 * This will made the controller to update all gobject properties that are under control with the current values 
 * based on timestamp.
 * <p>
 * What needs to be done in applications? Again its not a lot to change.
 * <li> first put some properties under control, by calling controller = gst_object_control_properties (object, "prop1", "prop2",...);
 * <li> Get a GstControlSource for the property and set it up. 
 * csource = gst_interpolation_control_source_new(); 
 * gst_interpolation_control_source_set_interpolation_mode(csource, mode); 
 * gst_interpolation_control_source_set (csource,0 * GST_SECOND, value1); 
 * gst_interpolation_control_source_set (csource,1 * GST_SECOND, value2);
 * <li> Set the GstControlSource in the controller. gst_controller_set_control_source (controller, "prop1", csource);
 * <li> start your pipeline
 * <ul>
 */
public class Controller extends GObject {
    private static Logger logger = Logger.getLogger(Controller.class.getName());
    
    private static final GstControllerAPI gst = GstNative.load(GstControllerAPI.class);
    
    static {
        NativeArgs argv = new NativeArgs("gstreamer-java", new String[] {});
        if (!gst.gst_controller_init(argv.argcRef, argv.argvRef)) {
            throw new GstException("Can't initialize GstController");
        }       
        logger.fine("after gst_init, argc=" + argv.argcRef.getValue());
    }
    /**
     * For internal gstreamer-java use only
     *
     * @param init initialization data
     */
    public Controller(Initializer init) {
        super(init);
        throw new IllegalArgumentException("Cannot instantiate this class");
    }

    public Controller(Pointer ptr, boolean needRef, boolean ownsHandle) {
        super(initializer(ptr, needRef, ownsHandle));
    }
}
