/* 
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

package org.gstreamer;

import org.gstreamer.lowlevel.GstNative;
import org.gstreamer.lowlevel.ReferenceManager;
import org.gstreamer.lowlevel.annotations.HasSubtype;

/**
 * Base type of all events.
 * 
 * <p> Events are passed between elements in parallel to the data stream. Some events
 * are serialized with buffers, others are not. Some events only travel downstream,
 * others only upstream. Some events can travel both upstream and downstream. 
 * 
 * <p> The events are used to signal special conditions in the datastream such as
 * EOS (end of stream) or the start of a new stream-segment.
 * 
 * <p> Events are also used to flush the pipeline of any pending data.
 * 
 * @see Pad#pushEvent
 * @see Pad#sendEvent
 * @see Element#sendEvent
 */
@HasSubtype
public class Event extends MiniObject {
    public static final String GTYPE_NAME = "GstEvent";

    private static interface API extends com.sun.jna.Library {
        Structure gst_event_get_structure(Event event);
    }
    private static final API gst = GstNative.load(API.class);
    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    public Event(Initializer init) { 
        super(init); 
    }
    
    /**
     * Gets the structure containing the data in this event.
     * 
     * @return a structure.
     */
    public Structure getStructure() {
    	return ReferenceManager.addKeepAliveReference(gst.gst_event_get_structure(this), this);
    }
}
