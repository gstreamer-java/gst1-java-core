/* 
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

import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValue;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface GstVideoAPI extends Library {
	public final static GstVideoAPI GSTVIDEO_API = GstNative.load("gstvideo", GstVideoAPI.class);

    GValue gst_video_frame_rate(Pad pad);
    boolean gst_video_get_size(Pad pad, int [] width, int [] height);
    
    /* */
    Pointer ptr_gst_video_event_new_downstream_force_key_unit(
            long timestamp, long stream_time, long running_time,
            boolean all_headers, int count);

    Pointer ptr_gst_video_event_new_upstream_force_key_unit(
            long running_time, boolean all_headers, int count);
}
