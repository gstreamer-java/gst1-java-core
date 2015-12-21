/*
 * Copyright (c) 2015 Christophe Lafolet
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


import org.freedesktop.gstreamer.Event;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.NativeObject;

import com.sun.jna.Pointer;


public class ReconfigureEvent extends Event {
    private static interface API extends com.sun.jna.Library {
        Pointer ptr_gst_event_new_reconfigure();
    }
    private static final API gst = GstNative.load(API.class);

    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    public ReconfigureEvent(Initializer init) {
        super(init);
    }

    /**
     * Creates a new reconfigure event.
     */
    public ReconfigureEvent() {
        super(NativeObject.initializer(ReconfigureEvent.gst.ptr_gst_event_new_reconfigure()));
    }

}