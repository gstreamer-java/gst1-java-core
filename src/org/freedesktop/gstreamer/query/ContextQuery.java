/*
 * Copyright (c) 2015 Christophe Lafolet
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

import org.freedesktop.gstreamer.Query;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.GstQueryAPI;
import org.freedesktop.gstreamer.lowlevel.NativeObject;

import com.sun.jna.Pointer;

public class ContextQuery extends Query {
    private static interface API extends GstQueryAPI {
        Pointer ptr_gst_query_new_context(String context_type);
    }
    private static final API gst = GstNative.load(API.class);

    public ContextQuery(Initializer init) {
        super(init);
    }

    public ContextQuery(String context_type) {
        this(NativeObject.initializer(ContextQuery.gst.ptr_gst_query_new_context(context_type)));
    }

    public String getContextType() {
    	String[] context_type = new String[1];
    	boolean isOk = ContextQuery.gst.gst_query_parse_context_type(this, context_type);
    	return isOk ? context_type[0] : null;
    }

}
