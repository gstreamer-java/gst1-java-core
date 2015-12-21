package org.freedesktop.gstreamer.lowlevel;

import java.util.Arrays;
import java.util.List;

import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.SeekType;
import org.freedesktop.gstreamer.Segment;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

public interface GstSegmentAPI extends com.sun.jna.Library {
	GstSegmentAPI GSTSEGMENT_API = GstNative.load(GstSegmentAPI.class);

	@CallerOwnsReturn Pointer ptr_gst_segment_new();

	GType gst_segment_get_type();

	void gst_segment_init(Segment segment, Format format);
    boolean gst_segment_do_seek(Segment segment, double rate, Format format, int flags, SeekType startType, long start, SeekType stopType, long stop, boolean []update);
    void gst_segment_clip (Segment segment, Format format, long start, long stop, long clip_start, long clip_stop);

    public static final class GstSegmentStruct extends com.sun.jna.Structure {
        /*< public >*/
        public int flags;
        public double rate;
        public double applied_rate;
        public Format format;
        public long base;
        public long offset;
        public long start;
        public long stop;
        public long time;

        public long position;
        public long duration;

        /*< private >*/
        public volatile byte[] _gst_reserved = new byte[Pointer.SIZE * GstAPI.GST_PADDING - Double.SIZE / 8];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "flags", "rate", "applied_rate", "format",
                "base", "offset", "start", "stop",
                "time", "position",
                "duration", "_gst_reserved"
            });
        }
    };


}
