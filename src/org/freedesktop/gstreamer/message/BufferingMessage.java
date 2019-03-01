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
 * This message can be posted by an element that
 * needs to buffer data before it can continue processing. {@code percent} should be a
 * value between 0 and 100. A value of 100 means that the buffering completed.
 * <p>
 * See upstream documentation
 * at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-buffering"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-buffering</a>
 * <p>
 * When <tt>percent</tt> is &lt; 100 the application should PAUSE a PLAYING pipeline. When
 * <tt>percent</tt> is 100, the application can set the pipeline (back) to PLAYING.
 * The application must be prepared to receive BUFFERING messages in the
 * PREROLLING state and may only set the pipeline to PLAYING after receiving a
 * message with <tt>percent</tt> set to 100, which can happen after the pipeline
 * completed prerolling. 
 */
public class BufferingMessage extends Message {

    /**
     * Creates a new Buffering message.
     * @param init internal initialization data.
     */
    BufferingMessage(Initializer init) {
        super(init);
    }
    
    /**
     * Creates a new Buffering message.
     * @param src The object originating the message.
     * @param percent The buffering percent
     */
    public BufferingMessage(GstObject src, int percent) {
        this(Natives.initializer(GSTMESSAGE_API.ptr_gst_message_new_buffering(src, percent)));
    }
    
    /**
     * Gets the buffering percentage.
     * 
     * @return the percentage that is being buffered.
     */
    public int getPercent() {
        int[] percent = { 0 };
        GSTMESSAGE_API.gst_message_parse_buffering(this, percent);
        return percent[0];
    }
}
