/*
 * Copyright (c) 2014 Tom Greenwood <tgreenwood@cafex.com>
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
package org.gstreamer.event;

import org.gstreamer.ClockTime;
import org.gstreamer.Event;
import org.gstreamer.Format;
import org.gstreamer.lowlevel.GstNative;

import com.sun.jna.Pointer;

/**
 * Force key unit event that forces out key frames
 */
public class ForceKeyUnit extends Event
{
    private static interface API extends com.sun.jna.Library {
        Pointer ptr_gst_video_event_new_downstream_force_key_unit(
                ClockTime timestamp, ClockTime stream_time, ClockTime running_time,
                boolean all_headers, int count);
        Pointer ptr_gst_video_event_new_upstream_force_key_unit(
                ClockTime running_time, boolean all_headers, int count);
        Pointer ptr_gst_event_new_eos();
    }
    private static final API gst = GstNative.load("gstvideo", API.class);
    
    /**
     * Constructor - used internally!
     *
     * @param init
     */
    public ForceKeyUnit(Initializer init)
    {
        super(init);
    }
    
    /**
     * Constructor for a downstream force key unit.
     * See http://gstreamer.freedesktop.org/data/doc/gstreamer/head/gst-plugins-base-libs/html/gst-plugins-base-libs-gstvideo.html#gst-video-event-new-downstream-force-key-unit
     *
     * @param timestamp
     * @param stream_time
     * @param running_time
     * @param all_headers
     * @param count
     */
    public ForceKeyUnit(ClockTime timestamp, ClockTime stream_time, ClockTime running_time,
            boolean all_headers, int count) 
    {
            super(initializer(
                    gst.ptr_gst_video_event_new_downstream_force_key_unit(
                            timestamp, stream_time, running_time,
                            all_headers, count)));
    }
    /**
     * Constructor for upstream force key unit
     * See http://gstreamer.freedesktop.org/data/doc/gstreamer/head/gst-plugins-base-libs/html/gst-plugins-base-libs-gstvideo.html#gst-video-event-new-upstream-force-key-unit
     *
     * @param running_time Clock time to produce unit - use ClockTime.NONE for immediate
     * @param all_headers
     * @param count
     */
    public ForceKeyUnit(ClockTime running_time, boolean all_headers, int count) 
    {
        super(initializer(
            gst.ptr_gst_video_event_new_upstream_force_key_unit(running_time, all_headers, count)));
    }
}
