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

import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstMessageAPI.GSTMESSAGE_API;

/**
 * This message is posted by elements that finish playback of a segment as a 
 * result of a segment seek. 
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-segment-done"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-segment-done</a>
 * <p>
 * This message is received by the application after all elements that posted a segment_start
 * have posted the segment_done.
 */
public class SegmentDoneMessage extends Message {

    /**
     * Creates a new segment-done message.
     * 
     * @param init internal initialization data.
     */
    SegmentDoneMessage(Initializer init) {
        super(init);
    }
    
    /**
     * Creates a new segment done message.
     * 
     * @param src the object originating the message.
     * @param format the format of the position being done
     * @param position the position of the segment being done
     */
    public SegmentDoneMessage(GstObject src, Format format, long position) {
        this(Natives.initializer(GSTMESSAGE_API.ptr_gst_message_new_segment_done(src, format, position)));
    }
    
    /**
     * Gets the format of the position in this message.
     * 
     * @return the format of the position.
     */
    public Format getFormat() {
        Format[] format = new Format[1];
        GSTMESSAGE_API.gst_message_parse_segment_done(this, format, null);
        return format[0];
    }
    
    /**
     * Gets the position of the segment that is done.
     * 
     * @return the position.
     */
    public long getPosition() {
        long[] position = { 0 };
        GSTMESSAGE_API.gst_message_parse_segment_done(this, null, position);
        return position[0];
    }
}
