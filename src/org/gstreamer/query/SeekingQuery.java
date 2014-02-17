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

package org.gstreamer.query;

import org.gstreamer.Format;
import org.gstreamer.Query;
import org.gstreamer.lowlevel.GstNative;

import com.sun.jna.Pointer;

/**
 * Used for querying the seeking properties of the stream.
 */
public class SeekingQuery extends Query {
    private static interface API extends com.sun.jna.Library {
        Pointer gst_query_new_seeking(Format format);
        void gst_query_set_seeking(Query query, Format format,
            boolean seekable, /* gint64 */ long segment_start, /* gint64 */ long segment_end);
        void gst_query_parse_seeking(Query query, Format[] format,
            boolean[] seekable, /* gint64 * */ long[] segment_start, /* gint64 * */ long[] segment_end);
    }
    private static final API gst = GstNative.load(API.class);
    public SeekingQuery(Initializer init) {
        super(init);
    }
    
    /**
     * Constructs a new query object for querying seeking properties of the stream. 
     * 
     * @param format the default {@link Format} for the new query.
     */
    public SeekingQuery(Format format) {
        this(initializer(gst.gst_query_new_seeking(format)));
    }
    
    /**
     * Sets the seeking query result fields.
     * 
     * @param format the format to set for the {@code start} and {@code end} values.
     * @param seekable the seekable flag to set
     * @param start the start of the segment.
     * @param end the end of the segment.
     */
    public void setSeeking(Format format, boolean seekable, long start, long end) {
        gst.gst_query_set_seeking(this, format, seekable, start, end);
    }
    
    /**
     * Checks if the stream is seekable.
     * 
     * @return {@code true} if the stream is seekable.
     */
    public boolean isSeekable() {
        boolean[] value = { false };
        gst.gst_query_parse_seeking(this, null, value, null, null);
        return value[0];
    }
    
    /**
     * Gets the {@link Format} of the start and end values for the segment.
     * 
     * @return the format of the start and end values.
     */
    public Format getFormat() {
        Format[] value = { Format.UNDEFINED };
        gst.gst_query_parse_seeking(this, value, null, null, null);
        return value[0];
    }
    
    /**
     * Gets the start of the segment.
     * 
     * @return the start of the segment.
     */
    public long getStart() {
        long[] value = { 0 };
        gst.gst_query_parse_seeking(this, null, null, value, null);
        return value[0];
    }
    
    /**
     * Gets the end of the segment.
     * 
     * @return the end of the segment.
     */
    public long getEnd() {
        long[] value = { 0 };
        gst.gst_query_parse_seeking(this, null, null, null, value);
        return value[0];
    }
}
