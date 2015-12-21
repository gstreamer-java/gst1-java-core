/*
 * Copyright (c) 2015 Christophe Lafolet
 * Copyright (c) 2010 David Hoyt <dhoyt@hoytsoft.org>
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

package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.GstSegmentAPI;

public final class Segment extends GstObject {

    static interface API extends GstSegmentAPI {}
    private static final GstSegmentAPI gst = GstNative.load(API.class);

    /**
     * Creates a new instance of {@link Segment}.
     */
    public Segment(Initializer init) {
        super(init);
    }

    /**
     * Creates a new instance of {@link Segment}.
     */
    public Segment(Format format) {
        super(GstObject.initializer(Segment.gst.ptr_gst_segment_new()));
        Segment.gst.gst_segment_init(this, format);
    }

    public boolean seek(double rate, Format format, int flags,
            SeekType startType, long start, SeekType stopType, long stop) {
    	boolean[] update = new boolean[1];
        Segment.gst.gst_segment_do_seek(this, rate, format, flags, startType, start, stopType, stop, update);
        return update[0];
    }

    // TODO
//   /**
//    * Creates a new instance of {@link Segment}.
//    *
//    * @param rate the rate of the segment.
//    * @param format the {@link Format} of the segment values.
//    * @param startValue the start value.
//    * @param stopValue the stop value.
//    */
//    Segment(double rate, Format format, long startValue, long stopValue) {
//        this.rate = rate;
//        this.format = format;
//        this.stopValue = stopValue;
//        this.startValue = startValue;
//    }
//
//    /**
//     * Gets the rate of the segment.
//     *
//     * @return The rate of the segment.
//     */
//    public double getRate() {
//        return this.rate;
//    }
//
//    /**
//     * Gets the {@link Format} of the segment values.
//     *
//     * @return The {@link Format} of the segment values.
//     */
//    public Format getFormat() {
//        return this.format;
//    }
//
//    /**
//     * Gets the start value.
//     *
//     * @return The start value.
//     */
//    public long getStartValue() {
//        return this.startValue;
//    }
//
//    /**
//     * Gets the stop value.
//     *
//     * @return The stop value.
//     */
//    public long getStopValue() {
//        return this.stopValue;
//    }
}
