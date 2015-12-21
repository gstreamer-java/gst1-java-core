/*
 * Copyright (c) 2015 Christophe Lafolet
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

import org.freedesktop.gstreamer.Event;
import org.freedesktop.gstreamer.Segment;
import org.freedesktop.gstreamer.lowlevel.GstEventAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.NativeObject;

/**
 *
 * The segment event marks the range of buffers to be processed. All
 * data not within the segment range is not to be processed. This can be
 * used intelligently by plugins to apply more efficient methods of skipping
 * unneeded data.
 * <p>
 * The position value of the segment is used in conjunction with the start
 * value to convertTo the buffer timestamps into the stream time. This is
 * usually done in sinks to report the current stream_time.
 *
 *
 * @author wayne
 */
public class SegmentEvent extends Event {
    static interface API extends GstEventAPI {}
    private static final API gst = GstNative.load(API.class);

    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    public SegmentEvent(Initializer init) {
        super(init);
    }

    /**
     * Creates a new instance of {@link SegmentEvent}.
     */
    public SegmentEvent(Segment segment) {
    	super(NativeObject.initializer(SegmentEvent.gst.ptr_gst_event_new_segment(segment)));
    }


    public Segment getSegment() {
    	Segment[] segment = {};
    	SegmentEvent.gst.gst_event_parse_segment(this, segment);
    	return segment[0];
    }

//    /**
//     * Allocates a new segment event with the given format/values triplets.
//     *
//     * {@code position} represents the stream_time of a buffer carrying a timestamp of
//     * {@code start}.
//     * <p>
//     * {@code position} cannot be -1, {@code start} cannot be -1, {@code stop} can be -1.
//     * <p>If there
//     * is a valid {@code stop} given, it must be greater or equal to {@code start}, including
//     * when the indicated playback {@code rate} is < 0.
//     * <p>
//     * After a newsegment event, the buffer stream time is calculated with:
//     * <p>
//     * {@code
//     *   position + (TIMESTAMP(buf) - start) * ABS (rate * applied_rate)
//     * }
//     *
//     * @param update boolean holding whether position was updated.
//     * @param rate the new playback rate
//     * @param format the format of the seek values
//     * @param flags the optional seek flags
//     * @param startType the type and flags for the new start position
//     * @param start the value of the new start position
//     * @param stopType the type and flags for the new stop position
//     * @param stop the value of the new stop position
//     */
//    public SegmentEvent(boolean update, double rate, Format format, int flags, SeekType startType, long start, SeekType stopType, long stop) {
//        this(NativeObject.initializer(SegmentEvent.gst.ptr_gst_segment_new()));
//        boolean[] update = new boolean[1];
//        SegmentEvent.gst.gst_segment_do_seek(this, rate, format, flags, startType, start, stopType, stop, update);
//    }
//
//    public SegmentEvent(boolean update, double rate, ClockTime start, ClockTime stop, ClockTime position) {
////        this(NativeObject.initializer(SegmentEvent.gst.ptr_gst_event_new_new_segment(update, rate, Format.TIME, start.toNanos(), stop.toNanos(), position.toNanos())));
//        this(NativeObject.initializer(SegmentEvent.gst.ptr_gst_segment_new()));
//    }
//
//    /**
//     * Allocates a new newsegment event with the given format/values triplets.
//     *
//     * {@code position} represents the stream_time of a buffer carrying a timestamp of
//     * {@code start}.
//     * <p>
//     * {@code position} cannot be -1, {@code start} cannot be -1, {@code stop} can be -1.
//     * <p>
//     * If there is a valid {@code stop} given, it must be greater or equal to
//     * {@code start}, including when the indicated playback {@code rate} is < 0.
//     * <p>
//     * The {@code appliedRate} value provides information about any rate adjustment that
//     * has already been made to the timestamps and content on the buffers of the
//     * stream. ({@code rate * appliedRate}) should always equal the rate that has been
//     * requested for playback.
//     * <p>For example, if an element has an input segment
//     * with intended playback {@code rate} of 2.0 and {@code appliedRate} of 1.0, it can adjust
//     * incoming timestamps and buffer content by half and output a newsegment event
//     * with {@code rate} of 1.0 and {@code appliedRate} of 2.0
//     * <p>
//     * After a newsegment event, the buffer stream time is calculated with:
//     * <p>
//     * {@code
//     *   position + (TIMESTAMP(buf) - start) * ABS (rate * applied_rate)
//     * }
//     * @param update Whether this segment is an update to a previous one
//     * @param rate A new rate for playback
//     * @param appliedRate The rate factor which has already been applied
//     * @param format The format of the segment values
//     * @param start The start value of the segment
//     * @param stop The stop value of the segment
//     * @param position The stream position
//     */
//    public SegmentEvent(boolean update, double rate, double appliedRate, Format format, long start, long stop, long position) {
//        this(NativeObject.initializer(SegmentEvent.gst.ptr_gst_segment_new()));
//        SegmentEvent.gst.gst_event_set_new_segment(this, update, rate, format, start, stop, position);
//    }
//
//    /**
//     * Gets whether this new segment event is an update or not.
//     *
//     * @return true if this event is an update.
//     */
//    public boolean isUpdate() {
//        boolean[] update = new boolean[1];
//        SegmentEvent.gst.gst_event_parse_new_segment(this, update, null, null, null, null, null);
//        return update[0];
//    }
//
//    /**
//     * Gets the format of the start, stop and position values.
//     *
//     * @return The {@link Format} for the start, stop and position values.
//     */
//    public Format getFormat() {
//        Format[] format = new Format[1];
//        SegmentEvent.gst.gst_event_parse_new_segment(this, null, null, format, null, null, null);
//        return format[0];
//    }
//
//    /**
//     * Gets the playback rate.
//     *
//     * @return the playback rate.
//     */
//    public double getRate() {
//        double[] value = new double[1];
//        SegmentEvent.gst.gst_event_parse_new_segment(this, null, value, null, null, null, null);
//        return value[0];
//    }
//
//    /**
//     * Gets the applied playback rate.
//     *
//     * @return the applied playback rate.
//     */
//    public double getAppliedRate() {
//        double[] value = new double[1];
//        SegmentEvent.gst.gst_event_parse_new_segment_full(this, null, null, value, null, null, null, null);
//        return value[0];
//    }
//
//    /**
//     * Gets the start of the new segment.
//     *
//     * @return the start of the new segment in units of the event format.
//     */
//    public long getStart() {
//        long[] value = new long[1];
//        SegmentEvent.gst.gst_event_parse_new_segment(this, null, null, null, value, null, null);
//        return value[0];
//    }
//
//    /**
//     * Gets the end of the new segment.
//     *
//     * @return the end of the new segment.
//     */
//    public long getEnd() {
//        long[] value = new long[1];
//        SegmentEvent.gst.gst_event_parse_new_segment(this, null, null, null, null, value, null);
//        return value[0];
//    }
//
//    /**
//     * Gets the position of the new segment.
//     *
//     * @return the position of the new segment.
//     */
//    public long getPosition() {
//        long[] value = new long[1];
//        SegmentEvent.gst.gst_event_parse_new_segment(this, null, null, null, null, null, value);
//        return value[0];
//    }
}
