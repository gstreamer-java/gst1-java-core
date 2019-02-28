/*
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2016 Christophe Lafolet
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

import org.freedesktop.gstreamer.BufferPool;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GstQueryAPI;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.glib.Natives;

/**
 * An allocation query for querying allocation properties.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstQuery.html#gst-query-new-allocation"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstQuery.html#gst-query-new-allocation</a>
 * <p>
 *
 */
public class AllocationQuery extends Query {

    /**
     * This constructor is for internal use only.
     *
     * @param init initialization data.
     */
    AllocationQuery(Initializer init) {
        // special case : no ref shall be added
        // the allocationQuery is an in/out parameter during query notification
        // so, we shall keep query writable for add methods
        super(Natives.initializer(init.ptr.getPointer(), false, true));
    }

    /**
     * Create a new allocation query.
     *
     * @param caps the negotiated {@link Caps}
     * @param need_pool return a pool.
     */
    public AllocationQuery(Caps caps, boolean need_pool) {
        this(Natives.initializer(GstQueryAPI.GSTQUERY_API.ptr_gst_query_new_allocation(caps, need_pool)));
    }

    /**
     * Whether a {@link BufferPool} is needed.
     *
     * @return true if BufferPool needed
     */
    public boolean isPoolNeeded() {
        boolean[] need_pool = {false};
        GstQueryAPI.GSTQUERY_API.gst_query_parse_allocation(this, null, need_pool);
        return need_pool[0];
    }

    /**
     * Get the requested {@link Caps}
     *
     * @return requested Caps
     */
    public Caps getCaps() {
        Pointer[] ptr = new Pointer[1];
        GstQueryAPI.GSTQUERY_API.gst_query_parse_allocation(this, ptr, null);
//    	return new Caps(new Initializer(ptr[0], false, true));
        return Natives.objectFor(ptr[0], Caps.class, false, true);
    }

    // @TODO how best not to expose GType?
    void addAllocationMeta(GType api, Structure params) {
        GstQueryAPI.GSTQUERY_API.gst_query_add_allocation_meta(this, api, params);
    }

    /**
     * Set the pool parameters of the query.
     *
     * @param pool the {@link BufferPool}
     * @param size the buffer size
     * @param min_buffers the min buffers
     * @param max_buffers the max buffers
     */
    public void addAllocationPool(BufferPool pool, int size, int min_buffers, int max_buffers) {
        GstQueryAPI.GSTQUERY_API.gst_query_add_allocation_pool(this, pool, size, min_buffers, max_buffers);
    }
}
