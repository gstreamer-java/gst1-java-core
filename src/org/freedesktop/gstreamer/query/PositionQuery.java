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
 * Used to query an element for the current position in the stream.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstQuery.html#gst-query-new-position"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstQuery.html#gst-query-new-position</a>
 * <p>
 */
public class PositionQuery extends Query {

    PositionQuery(Initializer init) {
        super(init);
    }

    /**
     * Constructs a new query stream position query object. A position query is
     * used to query the current position of playback in the streams, in some
     * format.
     *
     * @param format the default {@link Format} for the new query
     */
    public PositionQuery(Format format) {
        super(Natives.initializer(GstQueryAPI.GSTQUERY_API.ptr_gst_query_new_position(format)));
    }

    /**
     * Answers a position query by setting the requested value in the given
     * format.
     *
     * @param format the requested {@link Format}
     * @param position the position to set in the answer
     */
    public void setPosition(Format format, long position) {
        GstQueryAPI.GSTQUERY_API.gst_query_set_position(this, format, position);
    }

    /**
     * Gets the {@link Format} of this position query.
     *
     * @return The format of the query.
     */
    public Format getFormat() {
        Format[] fmt = new Format[1];
        GstQueryAPI.GSTQUERY_API.gst_query_parse_position(this, fmt, null);
        return fmt[0];
    }

    /**
     * Gets the position in terms of the {@link Format} of the query.
     *
     * @return the position.
     */
    public long getPosition() {
        long[] pos = new long[1];
        GstQueryAPI.GSTQUERY_API.gst_query_parse_position(this, null, pos);
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
