/* 
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2007, 2008 Wayne Meissner
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

import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.Clock;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

/**
 * GstPipeline
 */
public interface GstPipelineAPI extends com.sun.jna.Library {
	GstPipelineAPI GSTPIPELINE_API = GstNative.load(GstPipelineAPI.class);

    @CallerOwnsReturn Pipeline gst_pipeline_new(String name);
    @CallerOwnsReturn Pointer ptr_gst_pipeline_new(String name);
    GType gst_pipeline_get_type();
    @CallerOwnsReturn Bus gst_pipeline_get_bus(Pipeline pipeline);
    void gst_pipeline_set_auto_flush_bus(Pipeline pipeline, boolean flush);
    boolean gst_pipeline_get_auto_flush_bus(Pipeline pipeline);
    void gst_pipeline_set_new_stream_time(Pipeline pipeline, long time);
    long gst_pipeline_get_last_stream_time(Pipeline pipeline);
    void gst_pipeline_use_clock(Pipeline pipeline, Clock clock);
    boolean gst_pipeline_set_clock(Pipeline pipeline, Clock clock);
    @CallerOwnsReturn Clock gst_pipeline_get_clock(Pipeline pipeline);
    void gst_pipeline_auto_clock(Pipeline pipeline);
    void gst_pipeline_set_delay(Pipeline pipeline, long delay);
    long gst_pipeline_get_delay(Pipeline pipeline);
}
