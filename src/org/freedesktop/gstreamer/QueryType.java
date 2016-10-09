/* 
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

package org.freedesktop.gstreamer;

import java.util.ArrayList;
import java.util.List;

import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.GstQueryAPI;

/**
 * Standard predefined Query types
 */
public enum QueryType {
    /** invalid query type */
    UNKNOWN,
     /** current position in stream */
    POSITION,
    /** total duration of the stream */
    DURATION,
    /** latency of stream */
    LATENCY,
    /** current jitter of stream */
    JITTER,
    /** current rate of the stream */
    RATE,
    /** seeking capabilities */
    SEEKING,
    /** segment start/stop positions */
    SEGMENT,
    /** convert values between formats */
    CONVERT,
    /** query supported formats for convert */
    FORMATS,
    /** query available media for efficient seeking */
    BUFFERING,
    /** a custom application or element defined query */
    CUSTOM,
    /** query the URI of the source or sink */
    URI,
    /** the buffer allocation properties */
    ALLOCATION,
    /** the scheduling properties */
    SCHEDULING,
    /** the accept caps query */
    ACCEPT_CAPS,
    /** the caps query */
    CAPS,
    /** wait till all serialized data is consumed downstream */
    DRAIN,
    /** query the pipeline-local context from downstream or upstream */
    CONTEXT,
    ;
    
    private static final GstQueryAPI gst = GstNative.load(GstQueryAPI.class);

    /**
     * gets the name of this type from runtime.
     * 
     * @return the gstreamer name for this type.
     */
    public String getAPIName() {
        return gst.gst_query_type_get_name(this);
    }
}
