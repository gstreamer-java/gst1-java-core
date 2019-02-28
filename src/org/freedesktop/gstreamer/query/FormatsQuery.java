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

import java.util.AbstractList;
import java.util.List;

import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstQueryAPI;

/**
 * Used for querying formats of the stream.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstQuery.html#gst-query-new-formats"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstQuery.html#gst-query-new-formats</a>
 * <p>
 * @see Format
 */
public class FormatsQuery extends Query {

    /**
     * Constructs a new query object for querying formats of the stream.
     */
    public FormatsQuery() {
        this(Natives.initializer(GstQueryAPI.GSTQUERY_API.ptr_gst_query_new_formats()));
    }

    FormatsQuery(Initializer init) {
        super(init);
    }

    /**
     * Sets the formats query result fields.
     *
     * @param formats the formats to set.
     */
    public void setFormats(Format... formats) {
        GstQueryAPI.GSTQUERY_API.gst_query_set_formats(this, formats.length, formats);
    }

    /**
     * Gets the number of formats in this query.
     *
     * @return the number of formats in this query.
     */
    public int getCount() {
        int[] count = {0};
        GstQueryAPI.GSTQUERY_API.gst_query_parse_n_formats(this, count);
        return count[0];
    }

    /**
     * Gets a format at {@code index}.
     *
     * @param index the index of the format to retrieve.
     * @return the format.
     */
    public Format getFormat(int index) {
        Format[] fmt = new Format[1];
        GstQueryAPI.GSTQUERY_API.gst_query_parse_nth_format(this, index, fmt);
        return fmt[0];
    }

    /**
     * Gets all formats in this query.
     *
     * @return a {@link List} of {@link Format}.
     */
    public List<Format> getFormats() {
        final int count = getCount();
        return new AbstractList<Format>() {
            @Override
            public Format get(int index) {
                return getFormat(index);
            }

            @Override
            public int size() {
                return count;
            }
        };
    }
}
