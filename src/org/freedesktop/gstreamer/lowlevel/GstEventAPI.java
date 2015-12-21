/*
 * Copyright (c) 2015 Christophe Lafolet
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

import java.util.Arrays;
import java.util.List;

import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.ClockTime;
import org.freedesktop.gstreamer.Event;
import org.freedesktop.gstreamer.EventType;
import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.Message;
import org.freedesktop.gstreamer.QosType;
import org.freedesktop.gstreamer.SeekType;
import org.freedesktop.gstreamer.Segment;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.TagList;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.Invalidate;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * GstEvent functions
 */
public interface GstEventAPI extends com.sun.jna.Library {
    GstEventAPI GSTEVENT_API = GstNative.load(GstEventAPI.class);

    String gst_event_type_get_name(EventType type);
    int gst_event_type_get_flags(EventType type);

    GType gst_event_get_type();

    /* custom event */
    @CallerOwnsReturn Event gst_event_new_custom(EventType type, @Invalidate Structure structure);
    Structure gst_event_get_structure(Event event);
    Structure gst_event_writable_structure(Event event);

    /* Stream start event */
    @CallerOwnsReturn Event gst_event_new_stream_start(String stream_id);
    void gst_event_parse_stream_start(Event event, String[] stream_id);

    /* flush events */
    @CallerOwnsReturn Event gst_event_new_flush_start();
    @CallerOwnsReturn Event gst_event_new_flush_stop(boolean reset_time);

    /* EOS event */
    @CallerOwnsReturn Event gst_event_new_eos();

    /* GAP event */
    @CallerOwnsReturn Event gst_event_new_gap(ClockTime timestamp, ClockTime duration);
    void gst_event_parse_gap(Event event, ClockTime[] timestamp, ClockTime[] duration);

    /* Caps events */
    @CallerOwnsReturn Event gst_event_new_caps(Caps caps);
    void gst_event_parse_caps (Event event, Caps[] caps);

    /* QOS events */
    @CallerOwnsReturn Event gst_event_new_qos(QosType type, double proportion, long diff, ClockTime timestamp);
    void gst_event_parse_qos(Event event, QosType[] qos, double[] proportion, long[] diff, ClockTime[] timestamp);

    /* segment events */
    @CallerOwnsReturn Event gst_event_new_segment(Segment segment);
	@CallerOwnsReturn Pointer ptr_gst_event_new_segment(Segment segment);

    void gst_event_parse_segment(Event event, Segment[] segment);
    void gst_event_copy_segment(Event event, Segment segment);

    /* tag event */
    @CallerOwnsReturn Event gst_event_new_tag(@Invalidate TagList taglist);
    void gst_event_parse_tag(Event event, PointerByReference taglist);

    /* TOC event */
//    @CallerOwnsReturn Event gst_event_new_toc(Toc toc, boolean updated);
//    void gst_event_parse_toc(Event event, Toc[] toc, boolean updated);

    /* buffer */
    @CallerOwnsReturn Event gst_event_new_buffer_size(Format format, long minsize, long maxsize, boolean async);
    void gst_event_parse_buffer_size(Event event, Format format, long[] minsize,
						 long[] maxsize, int[] async);

    /* sink message */
    @CallerOwnsReturn Event gst_event_new_sink_message(String name, Message msg);
    void gst_event_parse_sink_message(Event event, Message[] msg);

    /* seek event */
    @CallerOwnsReturn Event gst_event_new_seek(double rate, Format format, int flags,
						 SeekType start_type, long start,
						 SeekType stop_type, long stop);
    void gst_event_parse_seek(Event event, double rate, Format[] format,
		                                 int[] flags, int[] start_type, long[] start,
                                                 int[] stop_type, long[] stop);
    /* navigation event */
    @CallerOwnsReturn Event gst_event_new_navigation(@Invalidate Structure structure);

    /* latency event */
    @CallerOwnsReturn Event gst_event_new_latency(ClockTime latency);
    void gst_event_parse_latency(Event event, ClockTime[] latency);

    /* step event */
    @CallerOwnsReturn Event gst_event_new_step(Format format, long amount, double rate, boolean flush, boolean intermediate);

    /* renegotiate event */
    @CallerOwnsReturn Event gst_event_new_reconfigure();

    /* TOC select event */
    @CallerOwnsReturn Event gst_event_new_toc_select(String uid);
    void gst_event_parse_toc_select(Event event, String[] uid);

    /* segment-done event */
    @CallerOwnsReturn Event gst_event_new_segment_done(Format format, long position);
    void gst_event_parse_segment_done(Event event, Format format, long position);

    public static final class EventStruct extends com.sun.jna.Structure {
        public volatile GstMiniObjectAPI.MiniObjectStruct mini_object;
        public volatile int type;
        public EventStruct(Pointer ptr) {
            this.useMemory(ptr);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "mini_object", "type"
            });
        }
    }
}
