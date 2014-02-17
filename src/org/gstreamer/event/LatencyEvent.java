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

import org.gstreamer.ClockTime;
import org.gstreamer.Event;
import org.gstreamer.lowlevel.GstNative;

import com.sun.jna.Pointer;

/**
 * Notification of new latency adjustment.
 * <p>
 * The event is sent upstream from the sinks and
 * notifies elements that they should add an additional latency to the
 * timestamps before synchronising against the clock.
 * <p>
 * The latency is mostly used in live sinks and is always expressed in
 * the time format.
 */
public class LatencyEvent extends Event {
    private static interface API extends com.sun.jna.Library {
        Pointer ptr_gst_event_new_latency(ClockTime latency);
        void gst_event_parse_latency(Event event, ClockTime[] latency);
    }
    private static final API gst = GstNative.load(API.class);
    
    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    public LatencyEvent(Initializer init) {
        super(init);
    }
    
    /**
     * Create a new latency event. 
     * 
     * @param latency the new latency value to add to timestamps.
     */
    public LatencyEvent(ClockTime latency) {
        super(initializer(gst.ptr_gst_event_new_latency(latency)));
    }
    
    /**
     * Gets the latency in the latency event.
     * 
     * @return the latency.
     */
    public ClockTime getLatency() {
        ClockTime[] latency = new ClockTime[1];
        gst.gst_event_parse_latency(this, latency);
        return latency[0];
    }
}
