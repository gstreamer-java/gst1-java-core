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
import com.sun.jna.Structure;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.video.VideoTimeCodeFlags;

public interface GstVideoAPI extends Library {
	public final static GstVideoAPI GSTVIDEO_API = GstNative.load("gstvideo", GstVideoAPI.class);

    @CallerOwnsReturn
    Pointer gst_video_time_code_new_empty();
    void gst_video_time_code_free(Pointer gstVideoTimeCode);
    GValue gst_video_frame_rate(Pad pad);
    boolean gst_video_get_size(Pad pad, int [] width, int [] height);

    /* */
    Pointer ptr_gst_video_event_new_downstream_force_key_unit(
            long timestamp, long stream_time, long running_time,
            boolean all_headers, int count);

    Pointer ptr_gst_video_event_new_upstream_force_key_unit(
            long running_time, boolean all_headers, int count);
    
    GType gst_video_time_code_meta_api_get_type();

    GType gst_video_crop_meta_api_get_type();

    GType gst_video_gl_texture_upload_meta_api_get_type();

    GType gst_video_meta_api_get_type();

    GType gst_video_region_of_interest_meta_api_get_type();

//    MetaInfo gst_video_time_code_meta_get_info();


    @Structure.FieldOrder({"meta", "tc"})
    class GstVideoTimeCodeMetaStruct extends Structure {
        public GstMetaAPI.GstMetaStruct.ByValue meta;
        public GstVideoTimeCodeStruct.ByValue tc;

        public GstVideoTimeCodeMetaStruct(Pointer p) {
            super(p);
            read();
        }
    }

    @Structure.FieldOrder({"config", "hours", "minutes", "seconds", "frames", "field_count"})
    @Gst.Since(minor = 10)
    class GstVideoTimeCodeStruct extends Structure {
        public static class ByValue extends GstVideoTimeCodeStruct implements Structure.ByValue {
        }

        public GstVideoTimeCodeConfigStruct.ByValue config;
        public int hours;
        public int minutes;
        public int seconds;
        public int frames;
        public int field_count;

        public GstVideoTimeCodeStruct() {
        }

        public GstVideoTimeCodeStruct(Pointer p) {
            super(p);
            read();
        }
    }
    
    @Structure.FieldOrder({"fps_n", "fps_d", "flags", "latest_daily_jam"})
    @Gst.Since(minor = 10)
    class GstVideoTimeCodeConfigStruct extends Structure {

        public static class ByValue extends GstVideoTimeCodeConfigStruct implements Structure.ByValue {
        }

        public int fps_n;
        public int fps_d;
        public int flags;
        public Pointer latest_daily_jam;

        public GstVideoTimeCodeConfigStruct() {
        }

        public GstVideoTimeCodeConfigStruct(Pointer p) {
            super(p);
            read();
        }
    }
}
