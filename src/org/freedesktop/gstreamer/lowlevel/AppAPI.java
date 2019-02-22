/*
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2009 Andres Colubri
 * Copyright (c) 2008 Wayne Meissner
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

import org.freedesktop.gstreamer.Buffer;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.FlowReturn;
import org.freedesktop.gstreamer.Sample;
import org.freedesktop.gstreamer.elements.AppSink;
import org.freedesktop.gstreamer.elements.AppSrc;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.Invalidate;

import com.sun.jna.ptr.LongByReference;

/**
 *
 * @author wayne
 */
public interface AppAPI extends com.sun.jna.Library {
    
    AppAPI APP_API = GstNative.load("gstapp", AppAPI.class);

    // AppSrc functions
    GType gst_app_src_get_type();

    void gst_app_src_set_caps(AppSrc appsrc, Caps caps);
    @CallerOwnsReturn Caps gst_app_src_get_caps(AppSrc appsrc);

    void gst_app_src_set_size(AppSrc appsrc, long size);
    long gst_app_src_get_size(AppSrc appsrc);

    void gst_app_src_set_stream_type(AppSrc appsrc, AppSrc.StreamType type);
    AppSrc.StreamType gst_app_src_get_stream_type(AppSrc appsrc);

    void gst_app_src_set_max_bytes(AppSrc appsrc, long max);
    long gst_app_src_get_max_bytes(AppSrc appsrc);

    void gst_app_src_set_latency(AppSrc appsrc, long min, long max);
    void gst_app_src_get_latency(AppSrc appsrc, LongByReference min, LongByReference max);

    void gst_app_src_flush_queued(AppSrc appsrc);
    
    FlowReturn gst_app_src_push_buffer(AppSrc appsrc, @Invalidate Buffer buffer);
    FlowReturn gst_app_src_end_of_stream(AppSrc appsrc);

    // AppSink functions
    GType gst_app_sink_get_type();

    void gst_app_sink_set_caps(AppSink appsink, Caps caps);
    @CallerOwnsReturn Caps gst_app_sink_get_caps(AppSink appsink);

    boolean gst_app_sink_is_eos(AppSink appsink);

    @CallerOwnsReturn Sample gst_app_sink_pull_preroll(AppSink appsink);
    @CallerOwnsReturn Sample gst_app_sink_pull_sample(AppSink appsink);
}
