/*
 * Copyright (c) 2010 David Hoyt <dhoyt@hoytsoft.org>
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

/**
 * A representation of the values used in querying the pipeline using
 * gst_query_new_segment() and subsequently gst_query_parse_segment().
 */
public final class Segment {
    //--------------------------------------------------------------------------
    // Instance variables
    //
    private final double rate;
    private final Format format;
    private final long startValue;
    private final long stopValue;

   /**
    * Creates a new instance of {@link Segment}.
    *
    * @param rate the rate of the segment.
    * @param format the {@link Format} of the segment values.
    * @param startValue the start value.
    * @param stopValue the stop value.
    */
    Segment(double rate, Format format, long startValue, long stopValue) {
        this.rate = rate;
        this.format = format;
        this.stopValue = stopValue;
        this.startValue = startValue;
    }

    /**
     * Gets the rate of the segment.
     *
     * @return The rate of the segment.
     */
    public double getRate() {
        return rate;
    }

    /**
     * Gets the {@link Format} of the segment values.
     *
     * @return The {@link Format} of the segment values.
     */
    public Format getFormat() {
        return format;
    }

    /**
     * Gets the start value.
     * 
     * @return The start value.
     */
    public long getStartValue() {
        return startValue;
    }

    /**
     * Gets the stop value.
     * 
     * @return The stop value.
     */
    public long getStopValue() {
        return stopValue;
    }
}
