/*
 * Copyright (C) 2008 Wayne Meissner
 * Copyright (C) 2004 Wim Taymans <wim@fluendo.com>
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


import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.Message;
import org.freedesktop.gstreamer.lowlevel.GstMessageAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.NativeObject;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

/**
 * This message is generated and posted in the sink elements of a {@link Bin}.
 * The bin will only forward the EOS message to the application if all sinks
 * have posted an EOS message.
 */
public class EOSMessage extends Message {
    private static interface API extends GstMessageAPI {
    	@CallerOwnsReturn Pointer ptr_gst_message_new_eos(GstObject src);
    }
    private static final API gst = GstNative.load(API.class);

    /**
     * Creates a new eos message.
     * @param init internal initialization data.
     */
    public EOSMessage(Initializer init) {
        super(init);
    }

    /**
     * Creates a new eos message.
     * @param src The object originating the message.
     */
    public EOSMessage(GstObject src) {
        this(NativeObject.initializer(EOSMessage.gst.ptr_gst_message_new_eos(src)));
    }
}
