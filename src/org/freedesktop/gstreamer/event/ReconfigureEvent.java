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
 * A reconfigure event. The purpose of the reconfigure event is to travel
 * upstream and make elements renegotiate their caps or reconfigure their buffer
 * pools. This is useful when changing properties on elements or changing the
 * topology of the pipeline.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-reconfigure"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-reconfigure</a>
 * <p>
 */
public class ReconfigureEvent extends Event {

    /**
     * This constructor is for internal use only.
     *
     * @param init initialization data.
     */
    ReconfigureEvent(Initializer init) {
        super(init);
    }

    /**
     * Creates a new reconfigure event.
     */
    public ReconfigureEvent() {
        super(Natives.initializer(GSTEVENT_API.ptr_gst_event_new_reconfigure()));
    }

}
