/* 
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

package org.gstreamer.message;

import org.gstreamer.Format;
import org.gstreamer.GstObject;
import org.gstreamer.Message;
import org.gstreamer.lowlevel.GstMessageAPI;
import org.gstreamer.lowlevel.GstNative;

import com.sun.jna.Pointer;

/**
 * The duration of a pipeline has changed. The application can get the new 
 * duration with a duration query.
 */
public class DurationMessage extends Message {
    private static interface API extends GstMessageAPI {
        Pointer ptr_gst_message_new_duration(GstObject src, Format format, long duration);
    }
    private static final API gst = GstNative.load(API.class);
    
    /**
     * Creates a new Buffering message.
     * @param init internal initialization data.
     */
    public DurationMessage(Initializer init) {
        super(init);
    }
    
    /**
     * Creates a new Buffering message.
     * @param src the object originating the message.
     * @param format the format of the duration
     * @param duration the new duration.
     */
    public DurationMessage(GstObject src, Format format, long duration) {
        this(initializer(gst.ptr_gst_message_new_duration(src, format, duration)));
    }
    
    /**
     * Gets the format of the duration contained in this message.
     * 
     * @return the format of the duration.
     */
    public Format getFormat() {
        Format[] fmt = new Format[1];
        gst.gst_message_parse_duration(this, fmt, null);
        return fmt[0];
    }
    
    /**
     * Gets the duration of this message.
     * 
     * @return the new duration.
     */
    public long getDuration() {
        long[] duration = { 0 };
        gst.gst_message_parse_duration(this, null, duration);
        return duration[0];
    }
}
