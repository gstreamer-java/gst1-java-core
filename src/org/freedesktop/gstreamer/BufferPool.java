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
package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.GstBufferPoolAPI;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.glib.Natives;

/**
 * A BufferPool is an object that can be used to pre-allocate and recycle
 * buffers of the same size and with the same properties.
 * <p>
 *  See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstBufferPool.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstBufferPool.html</a>
 */
public class BufferPool extends GstObject {

    public static final String GTYPE_NAME = "GstBufferPool";

    /**
     * Creates a new instance of BufferPool
     */
    public BufferPool() {
        this(Natives.initializer(GstBufferPoolAPI.GSTBUFFERPOOL_API.ptr_gst_buffer_pool_new()));
    }
    
    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    BufferPool(final Initializer init) {
        super(init);
    }

    /**
     * Configure the BufferPool with the given parameters.
     * 
     * @param caps the {@link Caps} for the buffers
     * @param size the size of each buffer, not including prefix and padding
     * @param min_buffers the minimum amount of buffers to allocate
     * @param max_buffers the maximum amount of buffers to allocate or 0 for unlimited
     */
    public void setParams(Caps caps, int size, int min_buffers, int max_buffers) {
    	Structure config = GstBufferPoolAPI.GSTBUFFERPOOL_API.gst_buffer_pool_get_config(this);
    	GstBufferPoolAPI.GSTBUFFERPOOL_API.gst_buffer_pool_config_set_params(config, caps, size, min_buffers, max_buffers);
    }

    /**
     * Query the {@link Caps} configured on the BufferPool.
     * 
     * @return Caps configured on the BufferPool
     */
    public Caps getCaps() {
    	Structure config = GstBufferPoolAPI.GSTBUFFERPOOL_API.gst_buffer_pool_get_config(this);
    	Pointer[] ptr = new Pointer[1];
    	GstBufferPoolAPI.GSTBUFFERPOOL_API.gst_buffer_pool_config_get_params(config, ptr, null, null, null);
    	return new Caps(Natives.initializer(ptr[0], false, true));
    }

}
