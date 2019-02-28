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

import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstMessageAPI.GSTMESSAGE_API;

/**
 * A duration changed message.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-duration-changed"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-duration-changed</a>
 * <p>
 * This message is posted by elements that know the duration of a stream when
 * the duration changes. This message is received by bins and is used to
 * calculate the total duration of a pipeline.
 */
public class DurationChangedMessage extends Message {

    /**
     * Creates a new DurationChanged message.
     *
     * @param init internal initialization data.
     */
    DurationChangedMessage(Initializer init) {
        super(init);
    }

    /**
     * Creates a new DurationChanged Message
     *
     * @param src The object originating the message.
     */
    public DurationChangedMessage(GstObject src) {
        this(Natives.initializer(GSTMESSAGE_API.ptr_gst_message_new_duration_changed(src)));
    }
}
