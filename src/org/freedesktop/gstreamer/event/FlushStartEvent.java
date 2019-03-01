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

import org.freedesktop.gstreamer.FlowReturn;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstEventAPI.GSTEVENT_API;

/**
 * Start a flush operation.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-flush-start"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-flush-start</a>
 * <p>
 * The flush start event can be sent upstream and downstream and travels
 * out-of-bounds with the dataflow.
 * <p>
 * It marks pads as being flushing and will make them return
 * {@link FlowReturn#FLUSHING} when used for data flow with {@link Pad#pushEvent},
 * {@link Pad#chain}, {@link Pad#getRange} and {@link Pad#pullRange}. Any event
 * (except a {@link FlushStopEvent}) received on a flushing pad will return
 * {@code false} immediately.
 * <p>
 * Elements should unlock any blocking functions and exit their streaming
 * functions as fast as possible when this event is received.
 * <p>
 * This event is typically generated after a seek to flush out all queued data
 * in the pipeline so that the new media is played as soon as possible.
 *
 */
public class FlushStartEvent extends Event {


    /**
     * This constructor is for internal use only.
     *
     * @param init initialization data.
     */
    FlushStartEvent(Initializer init) {
        super(init);
    }

    /**
     * Creates a new flush start event.
     */
    public FlushStartEvent() {
        super(Natives.initializer(GSTEVENT_API.ptr_gst_event_new_flush_start()));
    }
}
