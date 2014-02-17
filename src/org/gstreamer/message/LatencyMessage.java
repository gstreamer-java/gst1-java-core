/* 
 * Copyright (C) 2008 Wayne Meissner
 * Copyright (C) 2004 Wim Taymans <wim@fluendo.com>
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

package org.gstreamer.message;

import org.gstreamer.GstObject;
import org.gstreamer.Message;
import org.gstreamer.lowlevel.GstMessageAPI;
import org.gstreamer.lowlevel.GstNative;

import com.sun.jna.Pointer;

/**
 * Message posted by elements when their latency requirements have changed.
 */
public class LatencyMessage extends Message {
    private static interface API extends GstMessageAPI {
        Pointer ptr_gst_message_new_latency(GstObject source);
    }
    private static final API gst = GstNative.load(API.class);
    
    /**
     * Creates a new Latency message.
     * 
     * @param init internal initialization data.
     */
    public LatencyMessage(Initializer init) {
        super(init);
    }
    
    /**
     * Creates a new Latency message.
     * 
     * @param source the object originating the message.
     */
    public LatencyMessage(GstObject source) {
        this(initializer(gst.ptr_gst_message_new_latency(source)));
    }
}
