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
 * Stop a flush operation.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-flush-stop"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-flush-stop</a>
 * <p>
 * The flush stop event can be sent upstream and downstream and travels
 * out-of-bounds with the dataflow. It is typically sent after sending a
 * {@link FlushStartEvent} event to make the pads accept data again.
 * <p>
 * Elements can process this event synchronized with the dataflow since the
 * preceeding FLUSH_START event stopped the dataflow.
 * <p>
 * This event is typically generated to complete a seek and to resume dataflow.
 */
public class FlushStopEvent extends Event {

    /**
     * This constructor is for internal use only.
     *
     * @param init initialization data.
     */
    FlushStopEvent(Initializer init) {
        super(init);
    }

    /**
     * Creates a new flush stop event.
     */
    public FlushStopEvent() {
        super(Natives.initializer(GSTEVENT_API.ptr_gst_event_new_flush_stop()));
    }
}
