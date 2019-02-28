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

import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.FlowReturn;
import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstEventAPI.GSTEVENT_API;

/**
 * End-Of-Stream event.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-eos"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-eos</a>
 * <p>
 * The eos event can only travel downstream synchronized with the buffer flow.
 * Elements that receive the EOS event on a pad can return
 * {@link FlowReturn#EOS} when data after the EOS event arrives.
 * <p>
 * The EOS event will travel down to the sink elements in the pipeline which
 * will then post the {@link Bus.EOS } message on the bus after they have
 * finished playing any buffered data.
 * <p>
 * When all sinks have posted an EOS message, an EOS message is forwarded to the
 * application.
 * <p>
 * The EOS event itself will not cause any state transitions of the pipeline.
 */
public class EOSEvent extends Event {

    /**
     * This constructor is for internal use only.
     *
     * @param init initialization data.
     */
    EOSEvent(Initializer init) {
        super(init);
    }

    /**
     * Creates a new EOS event.
     */
    public EOSEvent() {
        super(Natives.initializer(GSTEVENT_API.ptr_gst_event_new_eos()));
    }
}
