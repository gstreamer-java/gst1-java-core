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

package org.freedesktop.gstreamer.message;


import org.freedesktop.gstreamer.Clock;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.Message;
import org.freedesktop.gstreamer.lowlevel.GstMessageAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.NativeObject;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

public class NewClockMessage extends Message {
    private static interface API extends GstMessageAPI {
    	@CallerOwnsReturn Pointer ptr_gst_message_new_new_clock(GstObject src, Clock clock);
    }
    private static final API gst = GstNative.load(API.class);

    /**
     * Creates a new new clock message.
     *
     * @param init internal initialization data.
     */
    public NewClockMessage(Initializer init) {
        super(init);
    }

    /**
     * Creates a new clock message.
     * @param src the object originating the message.
     *
     */
    public NewClockMessage(GstObject src, Clock clock) {
        this(NativeObject.initializer(NewClockMessage.gst.ptr_gst_message_new_new_clock(src, clock)));
    }

    /**
     * Gets the clock of this message.
     *
     * @return the clock
     */
    public Clock getClock() {
    	Clock[] clock = new Clock[1];
        NewClockMessage.gst.gst_message_parse_new_clock(this, clock);
        return clock[0];
    }

}
