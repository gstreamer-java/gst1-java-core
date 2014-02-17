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
 * Used to query an element for the current position in the stream.
 */
public class PositionQuery extends Query {
    private static interface API extends com.sun.jna.Library { 
        /* position query */
        Pointer ptr_gst_query_new_position(Format format);
        void gst_query_set_position(Query query, Format format, /* gint64 */ long cur);
        void gst_query_parse_position(Query query, int[] format, /* gint64 * */ long[] cur);
    }
    private static final API gst = GstNative.load(API.class);
    
    public PositionQuery(Initializer init) {
        super(init);
    }
    
    /**
     * Constructs a new query stream position query object. A position query is 
     * used to query the current position of playback in the streams, in some format.
     * 
     * @param format the default {@link Format} for the new query
     */
    public PositionQuery(Format format) {
        super(initializer(gst.ptr_gst_query_new_position(format)));
    }
    
    /**
     * Answers a position query by setting the requested value in the given format.
     * 
     * @param format the requested {@link Format}
     * @param position the position to set in the answer
     */
    public void setPosition(Format format, long position) {
        gst.gst_query_set_position(this, format, position);
    }
    
    /**
     * Gets the {@link Format} of this position query.
     * 
     * @return The format of the query.
     */
    public Format getFormat() {
        int[] fmt = new int[1];
        gst.gst_query_parse_position(this, fmt, null);
        return Format.valueOf(fmt[0]);
    }
    
    /**
     * Gets the position in terms of the {@link Format} of the query.
     * 
     * @return the position.
     */
    public long getPosition() {
        long[] pos = new long[1];
        gst.gst_query_parse_position(this, null, pos);
        return pos[0];
    }
    
    /**
     * Gets the position as a user-readable string.
     * 
     * @return A string representation of the position.
     */
    @Override
    public String toString() {
        return String.format("position: [format=%s, position=%d]", getFormat(), getPosition());
    }
}
