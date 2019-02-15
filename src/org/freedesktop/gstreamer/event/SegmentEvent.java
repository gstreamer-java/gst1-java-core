/* 
 * Copyright (c) 2008 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wim.taymans@chello.be>
 *                    2005 Wim Taymans <wim@fluendo.com>
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

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.lowlevel.GstAPI;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstSegmentStruct;
import org.freedesktop.gstreamer.lowlevel.GstEventAPI;

/**
 *
 * The newsegment event marks the range of buffers to be processed. All data not
 * within the segment range is not to be processed. This can be used
 * intelligently by plugins to apply more efficient methods of skipping unneeded
 * data.
 * <p>
 * The position value of the segment is used in conjunction with the start value
 * to convertTo the buffer timestamps into the stream time. This is usually done
 * in sinks to report the current stream_time.
 *
 *
 * @author wayne
 */
public class SegmentEvent extends Event {

    private static final GstEventAPI gst = GstEventAPI.GSTEVENT_API;

    /**
     * This constructor is for internal use only.
     *
     * @param init initialization data.
     */
    SegmentEvent(Initializer init) {
        super(init);
    }

    /**
     * Allocates a new segment event with the given segment.
     */
    SegmentEvent(GstSegmentStruct segment) {
        this(initializer(gst.ptr_gst_event_new_segment(segment)));
    }

    /**
     * Gets the {@link Segment} stored in this event.
     * <p>
     * <b>Note:</b> The Segment is owned by the event, so it should only be
     * accessed whilst holding a reference to this SegmentEvent.
     *
     * @return the Segment stored in this event.
     */
    GstAPI.GstSegmentStruct getSegment() {
        Pointer[] segmentPointer = new Pointer[1];
        gst.gst_event_parse_segment(this, segmentPointer);
        GstSegmentStruct result = new GstAPI.GstSegmentStruct(segmentPointer[0]);
        result.read();
        return result;
    }
}
