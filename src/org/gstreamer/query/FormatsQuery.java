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

import java.util.AbstractList;
import java.util.List;

import org.gstreamer.Format;
import org.gstreamer.Query;
import org.gstreamer.lowlevel.GstNative;

import com.sun.jna.Pointer;

/**
 * Used for querying formats of the stream. 
 */
public class FormatsQuery extends Query {
    private static interface API extends com.sun.jna.Library {
        Pointer gst_query_new_formats();
        void gst_query_set_formats(Query query, int n_formats, Format... formats);
        void gst_query_set_formatsv(Query query, int n_formats, /* GstFormat * */ int[] formats);
        void gst_query_parse_formats_length(Query query, int[] n_formats);
        void gst_query_parse_formats_nth(Query query, int nth, /*GstFormat * */ int[] format);
    }
    private static final API gst = GstNative.load(API.class);
    
    /**
     * Constructs a new query object for querying formats of the stream. 
     */
    public FormatsQuery() {
        this(initializer(gst.gst_query_new_formats()));
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
        gst.gst_query_set_formats(this, formats.length, formats);
    }
    
    /**
     * Gets the number of formats in this query.
     * 
     * @return the number of formats in this query.
     */
    public int getCount() {
        int[] count = { 0 };
        gst.gst_query_parse_formats_length(this, count);
        return count[0];
    }
    
    /**
     * Gets a format at {@code index}.
     * @param index the index of the format to retrieve.
     * @return the format.
     */
    public Format getFormat(int index) {
        int[] fmt = { 0 };
        gst.gst_query_parse_formats_nth(this, index, fmt);
        return Format.valueOf(fmt[0]);
    }
    
    /**
     * Gets all formats in this query.
     * 
     * @return a {@code List} of {@link Format}.
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
