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
 * A convert query used to ask for a conversion between one format and another.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstQuery.html#gst-query-new-convert"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstQuery.html#gst-query-new-convert</a>
 * <p>
 */
public class ConvertQuery extends Query {

    ConvertQuery(Initializer init) {
        super(init);
    }

    /**
     * Construct a new convert query object.
     *
     * @param srcFormat the source {@link Format} for the new query
     * @param value the value to convert
     * @param destFormat the target {@link Format}
     */
    public ConvertQuery(Format srcFormat, long value, Format destFormat) {
        this(Natives.initializer(GstQueryAPI.GSTQUERY_API.ptr_gst_query_new_convert(srcFormat, value, destFormat)));
    }

    /**
     * Answer a convert query by setting the requested values.
     *
     * @param srcFormat the source {@link Format}
     * @param srcValue the source value
     * @param dstFormat the destination {@link Format}
     * @param dstValue the destination value
     */
    public void setConvert(Format srcFormat, long srcValue, Format dstFormat, long dstValue) {
        GstQueryAPI.GSTQUERY_API.gst_query_set_convert(this, srcFormat, srcValue, dstFormat, dstValue);
    }

    /**
     * Get the source {@link Format} of this query.
     *
     * @return source Format
     */
    public Format getSourceFormat() {
        Format[] fmt = new Format[1];
        GstQueryAPI.GSTQUERY_API.gst_query_parse_convert(this, fmt, null, null, null);
        return fmt[0];
    }

    /**
     * Get the destination {@link Format} of this query.
     *
     * @return destination Format
     */
    public Format getDestinationFormat() {
        Format[] fmt = new Format[1];
        GstQueryAPI.GSTQUERY_API.gst_query_parse_convert(this, null, null, fmt, null);
        return fmt[0];
    }

    /**
     * Get the source value of this query.
     *
     * @return source value
     */
    public long getSourceValue() {
        long[] value = new long[1];
        GstQueryAPI.GSTQUERY_API.gst_query_parse_convert(this, null, value, null, null);
        return value[0];
    }

    /**
     * Get the destination value of this query.
     *
     * @return destination value
     */
    public long getDestinationValue() {
        long[] value = new long[1];
        GstQueryAPI.GSTQUERY_API.gst_query_parse_convert(this, null, null, null, value);
        return value[0];
    }
}
