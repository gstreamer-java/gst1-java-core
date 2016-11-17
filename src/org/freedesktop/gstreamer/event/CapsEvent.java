/*
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


import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Event;
import org.freedesktop.gstreamer.lowlevel.GstEventAPI;


public class CapsEvent extends Event {
	
    private static final GstEventAPI gst = GstEventAPI.GSTEVENT_API;

    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    public CapsEvent(Initializer init) {
        super(init);
    }

    /**
     * Creates a new caps event.
     */
    public CapsEvent(final Caps caps) {
        super(initializer(gst.ptr_gst_event_new_caps(caps)));
    }
}