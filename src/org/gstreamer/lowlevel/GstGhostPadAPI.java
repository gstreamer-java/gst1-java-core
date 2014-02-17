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

import org.gstreamer.GhostPad;
import org.gstreamer.Pad;
import org.gstreamer.PadTemplate;
import org.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

/**
 * GstGhostPad functions
 */
public interface GstGhostPadAPI extends com.sun.jna.Library {
    GstGhostPadAPI GSTGHOSTPAD_API = GstNative.load(GstGhostPadAPI.class);

    GType gst_ghost_pad_get_type();
    
    @CallerOwnsReturn Pointer ptr_gst_ghost_pad_new(String name, Pad target);
    @CallerOwnsReturn Pointer ptr_gst_ghost_pad_new_no_target(String name, int direction);

    @CallerOwnsReturn Pointer ptr_gst_ghost_pad_new_from_template(String name, Pad target, PadTemplate templ);
    @CallerOwnsReturn Pointer ptr_gst_ghost_pad_new_no_target_from_template(String name, PadTemplate templ);
    @CallerOwnsReturn GhostPad gst_ghost_pad_new(String name, Pad target);
    @CallerOwnsReturn GhostPad gst_ghost_pad_new_no_target(String name, int direction);

    @CallerOwnsReturn GhostPad gst_ghost_pad_new_from_template(String name, Pad target, PadTemplate templ);
    @CallerOwnsReturn GhostPad gst_ghost_pad_new_no_target_from_template(String name, PadTemplate templ);
    @CallerOwnsReturn Pad gst_ghost_pad_get_target(GhostPad gpad);
    boolean gst_ghost_pad_set_target(GhostPad gpad, Pad newtarget);

    
}
