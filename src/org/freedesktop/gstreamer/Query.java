/* 
 * Copyright (C) 2008 Wayne Meissner
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

package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.ReferenceManager;
import org.freedesktop.gstreamer.lowlevel.annotations.HasSubtype;

import static org.freedesktop.gstreamer.lowlevel.GstQueryAPI.GSTQUERY_API;

/**
 * Base query type
 */
@HasSubtype
public class Query extends MiniObject {
    public static final String GTYPE_NAME = "GstQuery";

    /**
     * Internally used constructor.  Do not use.
     * 
     * @param init internal initialization data.
     */
    public Query(Initializer init) {
        super(init);
    }
    
    /**
     * Get the structure of this query.
     *
     * @return The structure of this Query.
     */
    public Structure getStructure() {
        return ReferenceManager.addKeepAliveReference(GSTQUERY_API.gst_query_get_structure(this), this);
    }
    
}
