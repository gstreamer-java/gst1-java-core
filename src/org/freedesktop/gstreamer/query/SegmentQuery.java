/* 
 * Copyright (C) 2019 Neil C Smith
 * Copyright (C) 2008 Wayne Meissner
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
package org.freedesktop.gstreamer.query;

import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstQueryAPI;

/**
 * Used to discover information about the currently configured segment for
 * playback.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstQuery.html#gst-query-new-segment"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstQuery.html#gst-query-new-segment</a>
 * <p>
 */
public class SegmentQuery extends Query {

    SegmentQuery(Initializer init) {
        super(init);
    }

    /**
     * Constructs a new segment query object.
     *
     * @param format the {@link Format} for the new query.
     */
    public SegmentQuery(Format format) {
        this(Natives.initializer(GstQueryAPI.GSTQUERY_API.ptr_gst_query_new_segment(format)));
    }

    /**
     * Answers a segment query by setting the requested values.
     * <p>
     * The normal playback segment of a pipeline is 0 to duration at the default
     * rate of 1.0. If a seek was performed on the pipeline to play a different
     * segment, this query will return the range specified in the last seek.
     *
     * {@code startValue} and {@code stopValue} will respectively contain the
     * configured playback range start and stop values expressed in format. The
     * values are always between 0 and the duration of the media and
     * {@code startValue <= stopValue}. {@code rate} will contain the playback
     * rate. For negative rates, playback will actually happen from
     * {@code stopValue} to {@code startValue}.
     *
     * @param rate the rate of the segment.
     * @param format the {@link Format} of the segment values.
     * @param startValue the start value.
     * @param stopValue the stop value.
     */
    public void setSegment(double rate, Format format, long startValue, long stopValue) {
        GstQueryAPI.GSTQUERY_API.gst_query_set_segment(this, rate, format, startValue, stopValue);
    }

    /**
     * Gets the rate of the segment Query.
     *
     * @return the rate of the segment.
     */
    public double getRate() {
        double[] rate = new double[1];
        GstQueryAPI.GSTQUERY_API.gst_query_parse_segment(this, rate, null, null, null);
        return rate[0];
    }

    /**
     * Gets the format of the start and stop values in the segment query.
     *
     * @return The format for the start and stop values.
     */
    public Format getFormat() {
        Format[] fmt = new Format[1];
        GstQueryAPI.GSTQUERY_API.gst_query_parse_segment(this, null, fmt, null, null);
        return fmt[0];
    }

    /**
     * Gets the start of the playback range.
     *
     * @return the start of the playback range.
     */
    public long getStart() {
        long[] value = new long[1];
        GstQueryAPI.GSTQUERY_API.gst_query_parse_segment(this, null, null, value, null);
        return value[0];
    }

    /**
     * Gets the end of the playback range.
     *
     * @return the end of the playback range.
     */
    public long getEnd() {
        long[] value = new long[1];
        GstQueryAPI.GSTQUERY_API.gst_query_parse_segment(this, null, null, null, value);
        return value[0];
    }
}
