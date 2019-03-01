/* 
 * Copyright (c) 2019 Neil C Smith
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
import org.freedesktop.gstreamer.Segment;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstAPI;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstSegmentStruct;
import static org.freedesktop.gstreamer.lowlevel.GstEventAPI.GSTEVENT_API;

/**
 *
 * An event for {@link Segment}
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-segment"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-segment</a>
 * <p>
 * The segment event marks the range of buffers to be processed. All data not
 * within the segment range is not to be processed. This can be used
 * intelligently by plugins to apply more efficient methods of skipping unneeded
 * data. The valid range is expressed with the start and stop values.
 * <p>
 * The time value of the segment is used in conjunction with the start value to
 * convert the buffer timestamps into the stream time. This is usually done in
 * sinks to report the current stream_time. time represents the stream_time of a
 * buffer carrying a timestamp of start . time cannot be -1.
 * <p>
 * start cannot be -1, stop can be -1. If there is a valid stop given, it must
 * be greater or equal the start , including when the indicated playback rate is
 * {@literal < 0}
 * <p>
 * The applied_rate value provides information about any rate adjustment that
 * has already been made to the timestamps and content on the buffers of the
 * stream. (rate * applied_rate ) should always equal the rate that has been
 * requested for playback. For example, if an element has an input segment with
 * intended playback rate of 2.0 and applied_rate of 1.0, it can adjust incoming
 * timestamps and buffer content by half and output a segment event with rate of
 * 1.0 and applied_rate of 2.0
 * <p>
 * After a segment event, the buffer stream time is calculated with:
 * <p>
 * time + (TIMESTAMP(buf) - start) * ABS (rate * applied_rate)
 */
//@TODO needs work to parse segments
public class SegmentEvent extends Event {

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
        this(Natives.initializer(GSTEVENT_API.ptr_gst_event_new_segment(segment)));
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
        GSTEVENT_API.gst_event_parse_segment(this, segmentPointer);
        GstSegmentStruct result = new GstAPI.GstSegmentStruct(segmentPointer[0]);
        result.read();
        return result;
    }
}
