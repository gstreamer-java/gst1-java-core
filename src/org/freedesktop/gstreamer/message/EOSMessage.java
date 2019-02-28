/* 
 * Copyright (C) 2019 Neil C Smith
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

import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstMessageAPI.GSTMESSAGE_API;

/**
 * An End-of-Stream Message.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-eos"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-eos</a>
 * <p>
 * This message is generated and posted in the sink elements of a {@link Bin}.
 * The bin will only forward the EOS message to the application if all sinks
 * have posted an EOS message.
 */
public class EOSMessage extends Message {

    /**
     * Creates a new eos message.
     *
     * @param init internal initialization data.
     */
    EOSMessage(Initializer init) {
        super(init);
    }

    /**
     * Creates a new eos message.
     *
     * @param src The object originating the message.
     */
    public EOSMessage(GstObject src) {
        this(Natives.initializer(GSTMESSAGE_API.ptr_gst_message_new_eos(src)));
    }
}
