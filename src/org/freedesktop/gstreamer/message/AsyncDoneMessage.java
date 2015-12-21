/*
 * Copyright (c) 2015 Christophe Lafolet
 * Copyright (C) 2008 Wayne Meissner
 * Copyright (C) 2004 Wim Taymans <wim@fluendo.com>
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


import org.freedesktop.gstreamer.ClockTime;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.Message;
import org.freedesktop.gstreamer.lowlevel.GstMessageAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.NativeObject;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

public class AsyncDoneMessage extends Message {
    private static interface API extends GstMessageAPI {
    	@CallerOwnsReturn Pointer ptr_gst_message_new_async_done(GstObject src, ClockTime running_time);
    }
    private static final API gst = GstNative.load(API.class);

    /**
     * Creates a new async done message.
     *
     * @param init internal initialization data.
     */
    public AsyncDoneMessage(Initializer init) {
        super(init);
    }

    /**
     * Creates a async done message.
     * @param src the object originating the message.
     *
     */
    public AsyncDoneMessage(GstObject src, ClockTime running_time) {
        this(NativeObject.initializer(AsyncDoneMessage.gst.ptr_gst_message_new_async_done(src, running_time)));
    }

    /**
     * Gets the clock time of this message.
     *
     * @return the clock
     */
    public ClockTime getClockTime() {
    	ClockTime[] clockTime = new ClockTime[1];
        AsyncDoneMessage.gst.gst_message_parse_async_done(this, clockTime);
        return clockTime[0];
    }

}
