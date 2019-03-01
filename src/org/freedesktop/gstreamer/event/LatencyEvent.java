/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer.event;

import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstEventAPI.GSTEVENT_API;

/**
 * Notification of new latency adjustment.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-latency"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-latency</a>
 * <p>
 * The event is sent upstream from the sinks and notifies elements that they
 * should add an additional latency to the timestamps before synchronising
 * against the clock.
 * <p>
 * The latency is mostly used in live sinks and is always expressed in the time
 * format.
 */
public class LatencyEvent extends Event {

    /**
     * This constructor is for internal use only.
     *
     * @param init initialization data.
     */
    LatencyEvent(Initializer init) {
        super(init);
    }

    /**
     * Create a new latency event.
     *
     * @param latency the new latency value to add to timestamps.
     */
    public LatencyEvent(long latency) {
        super(Natives.initializer(GSTEVENT_API.ptr_gst_event_new_latency(latency)));
    }

    /**
     * Gets the latency in the latency event.
     *
     * @return the latency.
     */
    public long getLatency() {
        long[] latency = new long[1];
        GSTEVENT_API.gst_event_parse_latency(this, latency);
        return latency[0];
    }
}
