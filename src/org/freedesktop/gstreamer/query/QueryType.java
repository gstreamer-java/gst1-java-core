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

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.glib.NativeEnum;

/**
 * Standard predefined Query types
 */
public enum QueryType implements NativeEnum<QueryType> {

    /**
     * invalid query type
     */
    UNKNOWN(0, 0),
    /**
     * current position in stream
     */
    POSITION(10, Flags.BOTH),
    /**
     * total duration of the stream
     */
    DURATION(20, Flags.BOTH),
    /**
     * latency of stream
     */
    LATENCY(30, Flags.BOTH),
    /**
     * current jitter of stream
     */
    JITTER(40, Flags.BOTH),
    /**
     * current rate of the stream
     */
    RATE(50, Flags.BOTH),
    /**
     * seeking capabilities
     */
    SEEKING(60, Flags.BOTH),
    /**
     * segment start/stop positions
     */
    SEGMENT(70, Flags.BOTH),
    /**
     * convert values between formats
     */
    CONVERT(80, Flags.BOTH),
    /**
     * query supported formats for convert
     */
    FORMATS(90, Flags.BOTH),
    /**
     * query available media for efficient seeking
     */
    BUFFERING(110, Flags.BOTH),
    /**
     * a custom application or element defined query
     */
    CUSTOM(120, Flags.BOTH),
    /**
     * query the URI of the source or sink
     */
    URI(130, Flags.BOTH),
    /**
     * the buffer allocation properties
     */
    ALLOCATION(140, Flags.DOWNSTREAM | Flags.SERIALIZED),
    /**
     * the scheduling properties
     */
    SCHEDULING(150, Flags.UPSTREAM),
    /**
     * the accept caps query
     */
    ACCEPT_CAPS(160, Flags.BOTH),
    /**
     * the caps query
     */
    CAPS(170, Flags.BOTH),
    /**
     * wait till all serialized data is consumed downstream
     */
    DRAIN(180, Flags.DOWNSTREAM | Flags.SERIALIZED),
    /**
     * query the pipeline-local context from downstream or upstream (since 1.2)
     */
    CONTEXT(190, Flags.BOTH),
    
    /**
     * the bitrate query (since 1.16)
     */
    @Gst.Since(minor = 16)
    BITRATE(200, Flags.DOWNSTREAM);

    private static final int SHIFT = 8;

    private final int value;

    private QueryType(int num, int flags) {
        this.value = (num << SHIFT) | flags;
    }

    private QueryType(int value) {
        this.value = value;
    }

    /**
     * Gets the integer value of the enum.
     *
     * @return the integer value for this enum.
     */
    public int intValue() {
        return value;
    }

    private static final class Flags {

        public static final int UPSTREAM = 1 << 0;
        public static final int DOWNSTREAM = 1 << 1;
        public static final int SERIALIZED = 1 << 2;
        public static final int BOTH = UPSTREAM | DOWNSTREAM;
    }
}
