/*
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
package org.freedesktop.gstreamer.lowlevel;

import org.freedesktop.gstreamer.BufferPool;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

/**
 * GstBufferPool methods and structures
 * @see https://cgit.freedesktop.org/gstreamer/gstreamer/tree/gst/gstbufferpool.h?h=1.8
 */
public interface GstBufferPoolAPI extends com.sun.jna.Library {
    GstBufferPoolAPI GSTBUFFERPOOL_API = GstNative.load(GstBufferPoolAPI.class);

    GType gst_buffer_pool_get_type();

    /* allocation */
    @CallerOwnsReturn BufferPool gst_buffer_pool_new();    
    Pointer ptr_gst_buffer_pool_new();
    
    /* state management */
    Structure gst_buffer_pool_get_config(BufferPool pool);
    
    /* helpers for configuring the config structure */
    boolean gst_buffer_pool_config_get_params(Structure config, /* Caps ** */ Pointer[] caps, /* guint * */ int[] size, /* guint * */ int[] min_buffers, /* guint * */ int[] max_buffers);
    void gst_buffer_pool_config_set_params(Structure config, Caps caps, int size, int min_buffers, int max_buffers);
    
}
