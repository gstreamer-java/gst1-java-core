/*
 * Copyright (c) 2015 Christophe Lafolet
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
import org.freedesktop.gstreamer.Query;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.GstQueryAPI;
import org.freedesktop.gstreamer.lowlevel.NativeObject;

import com.sun.jna.Pointer;

/**
 * Used for querying formats of the stream.
 */
public class FormatsQuery extends Query {
    private static interface API extends GstQueryAPI {
        Pointer ptr_gst_query_new_formats();
    }
    private static final API gst = GstNative.load(API.class);

    /**
     * Constructs a new query object for querying formats of the stream.
     */
    public FormatsQuery() {
        this(NativeObject.initializer(FormatsQuery.gst.ptr_gst_query_new_formats()));
    }

    public FormatsQuery(Initializer init) {
        super(init);
    }

    /**
     * Sets the formats query result fields.
     *
     * @param formats the formats to set.
     */
    public void setFormats(Format... formats) {
        FormatsQuery.gst.gst_query_set_formats(this, formats.length, formats);
    }

    /**
     * Gets the number of formats in this query.
     *
     * @return the number of formats in this query.
     */
    public int getCount() {
        int[] count = { 0 };
        FormatsQuery.gst.gst_query_parse_n_formats(this, count);
        return count[0];
    }

    /**
     * Gets a format at {@code index}.
     * @param index the index of the format to retrieve.
     * @return the format.
     */
    public Format getFormat(int index) {
    	Format[] fmt = { Format.UNDEFINED };
        FormatsQuery.gst.gst_query_parse_nth_format(this, index, fmt);
        return fmt[0];
    }

    /**
     * Gets all formats in this query.
     *
     * @return a {@code List} of {@link Format}.
     */
    public List<Format> getFormats() {
        final int count = this.getCount();
        return new AbstractList<Format>() {
            @Override
            public Format get(int index) {
                return FormatsQuery.this.getFormat(index);
            }

            @Override
            public int size() {
                return count;
            }
        };
    }
}
