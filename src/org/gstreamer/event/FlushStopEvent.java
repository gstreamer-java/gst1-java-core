/* 
 * Copyright (c) 2008 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wim.taymans@chello.be>
 *                    2005 Wim Taymans <wim@fluendo.com>
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

package org.gstreamer.event;

import org.gstreamer.Event;
import org.gstreamer.lowlevel.GstNative;

import com.sun.jna.Pointer;

/**
 * Stop a flush operation.
 * <p>
 * The flush stop event can be sent upstream and downstream and travels 
 * out-of-bounds with the dataflow.
 * <p>
 * It is typically sent after sending a {@link FlushStartEvent} event to make the
 * pads accept data again.
 * <p>
 * Elements can process this event synchronized with the dataflow since
 * the preceeding FLUSH_START event stopped the dataflow.
 * <p>
 * This event is typically generated to complete a seek and to resume
 * dataflow.
 */
public class FlushStopEvent extends Event {
    private static interface API extends com.sun.jna.Library {
        Pointer ptr_gst_event_new_flush_stop();
    }
    private static final API gst = GstNative.load(API.class);
    
    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    public FlushStopEvent(Initializer init) {
        super(init);
    }
    
    /**
     * Creates a new flush stop event. 
     */
    public FlushStopEvent() {
        super(initializer(gst.ptr_gst_event_new_flush_stop()));
    }
}
