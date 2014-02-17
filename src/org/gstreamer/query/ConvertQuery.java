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
 * Convert values between formats
 */
public class ConvertQuery extends Query {
    private static interface API extends com.sun.jna.Library {
        Pointer ptr_gst_query_new_convert(Format src_format, /* gint64 */ long value, Format dest_format);
        void gst_query_set_convert(ConvertQuery query, Format src_format, /* gint64 */ long src_value,
            Format dest_format, /* gint64 */ long dest_value);
        void gst_query_parse_convert(ConvertQuery query, /* GstFormat **/ int[] src_format, /*gint64 **/ long[] src_value,
            /*GstFormat **/ int[] dest_format, /*gint64 **/ long[] dest_value);
    
    }
    private static final API gst = GstNative.load(API.class);
    public ConvertQuery(Initializer init) {
        super(init);
    }
    public ConvertQuery(Format srcFormat, long value, Format destFormat) {
        this(initializer(gst.ptr_gst_query_new_convert(srcFormat, value, destFormat)));
    }
    public void setConvert(Format srcFormat, long srcValue, Format dstFormat, long dstValue) {
        gst.gst_query_set_convert(this, srcFormat, srcValue, dstFormat, dstValue);
    }
    public Format getSourceFormat() {
        int[] fmt = new int[1];
        gst.gst_query_parse_convert(this, fmt, null, null, null);
        return Format.valueOf(fmt[0]);
    }
    public Format getDestinationFormat() {
        int[] fmt = new int[1];
        gst.gst_query_parse_convert(this, null, null, fmt, null);
        return Format.valueOf(fmt[0]);
    }
    public long getSourceValue() {
        long[] value = new long[1];
        gst.gst_query_parse_convert(this, null, value, null, null);
        return value[0];
    }
    public long getDestinationValue() {
        long[] value = new long[1];
        gst.gst_query_parse_convert(this, null, null, null, value);
        return value[0];
    }
}
