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
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.ReferenceManager;
import org.freedesktop.gstreamer.lowlevel.annotations.HasSubtype;

/**
 * Base query type
 */
@HasSubtype
public class Query extends MiniObject {
    public static final String GTYPE_NAME = "GstQuery";

    private static interface API extends com.sun.jna.Library, GstMiniObjectAPI {
        Structure gst_query_get_structure(Query query);
    }
    private static final API gst = GstNative.load(API.class);
    
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
        return ReferenceManager.addKeepAliveReference(gst.gst_query_get_structure(this), this);
    }
    
    /**
     * Makes a writable query from this query.
     * <p>
     * <b>Note:</b> After calling this method, this Query instance is invalidated
     * and should no longer be used.
     * <p>
     * This should be used like this:
     * <p>
     * <code>
     *  query = query.makeWritable();
     * </code>
     * 
     * @return A new Query that is writable.
     */
    public Query makeWritable() {
        return makeWritable(getClass());
    }
}
