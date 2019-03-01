/*
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2016 Christophe Lafolet
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
 * Stream Start event.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-stream-start"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-stream-start</a>
 * <p>
 */
public class StreamStartEvent extends Event {
	
    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    StreamStartEvent(Initializer init) {
        super(init);
    }

    /**
     * Creates a new EOS event.
     * @param stream_id identifier for this stream
     */
    public StreamStartEvent(final String stream_id) {
        super(Natives.initializer(GSTEVENT_API.ptr_gst_event_new_stream_start(stream_id)));
    }
}