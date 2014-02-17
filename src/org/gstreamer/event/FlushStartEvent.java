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
import org.gstreamer.Pad;
import org.gstreamer.lowlevel.GstNative;

import com.sun.jna.Pointer;

/**
 * Start a flush operation.
 * <p>
 * The flush start event can be sent
 * upstream and downstream and travels out-of-bounds with the dataflow.
 * <p>
 * It marks pads as being flushing and will make them return
 * {@link org.gstreamer.FlowReturn#WRONG_STATE} when used for data flow with {@link Pad#pushEvent},
 * {@link Pad#chain}, Pad#allocBuffer, {@link Pad#getRange} and
 * {@link Pad#pullRange}. Any event (except a {@link FlushStopEvent}) received
 * on a flushing pad will return {@code false} immediately.
 * <p>
 * Elements should unlock any blocking functions and exit their streaming
 * functions as fast as possible when this event is received.
 * <p>
 * This event is typically generated after a seek to flush out all queued data
 * in the pipeline so that the new media is played as soon as possible.
 *
 */
public class FlushStartEvent extends Event {
    private static interface API extends com.sun.jna.Library {
        Pointer ptr_gst_event_new_flush_start();
    }
    private static final API gst = GstNative.load(API.class);
    
    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    public FlushStartEvent(Initializer init) {
        super(init);
    }
    
    /**
     * Creates a new flush start event.
     */
    public FlushStartEvent() {
        super(initializer(gst.ptr_gst_event_new_flush_start()));
    }
}
