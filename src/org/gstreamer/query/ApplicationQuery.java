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

import org.gstreamer.Query;
import org.gstreamer.QueryType;
import org.gstreamer.Structure;
import org.gstreamer.lowlevel.GstNative;
import org.gstreamer.lowlevel.annotations.Invalidate;

import com.sun.jna.Pointer;

/**
 * A Custom application query.
 */
public class ApplicationQuery extends Query {
    private static interface API extends com.sun.jna.Library {
        Pointer ptr_gst_query_new_application(QueryType type, @Invalidate Structure structure);
    }
    private static final API gst = GstNative.load(API.class);
    public ApplicationQuery(Initializer init) {
        super(init);
    }
    /**
     * Constructs a new custom application query object.
     * 
     * @param type the query type
     * @param structure a structure for the query
     */
    public ApplicationQuery(QueryType type, Structure structure) {
        this(initializer(gst.ptr_gst_query_new_application(type, structure)));
    }
}
