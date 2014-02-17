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

package org.gstreamer.lowlevel;

import org.gstreamer.ClockTime;
import org.gstreamer.Event;
import org.gstreamer.EventType;
import org.gstreamer.Format;
import org.gstreamer.SeekType;
import org.gstreamer.Structure;
import org.gstreamer.TagList;
import org.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.gstreamer.lowlevel.annotations.Invalidate;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import java.util.Arrays;
import java.util.List;

/**
 * GstEvent functions
 */
public interface GstEventAPI extends com.sun.jna.Library {
    GstEventAPI GSTEVENT_API = GstNative.load(GstEventAPI.class);

    String gst_event_type_get_name(EventType type);
    int gst_event_type_get_flags(EventType type);

    GType gst_event_get_type();
    
    /* custom event */
    Event gst_event_new_custom(EventType type, @Invalidate Structure structure);

    Structure gst_event_get_structure(Event event);

    /* flush events */
    @CallerOwnsReturn Event gst_event_new_flush_start();
    @CallerOwnsReturn Event gst_event_new_flush_stop();
    /* EOS event */
    @CallerOwnsReturn Event gst_event_new_eos();
    

    /* newsegment events */
    @CallerOwnsReturn Event gst_event_new_new_segment( boolean update,  double rate,
             Format format, long start,  long stop, long position);
    @CallerOwnsReturn Event gst_event_new_new_segment_full(boolean update, double rate,
             double applied_rate, Format format, long start, long stop, long position);
    void gst_event_parse_new_segment(Event event, boolean[] update, double[] rate,
            Format[] format, long[] start, long[] stop, long[] position);
    void gst_event_parse_new_segment_full(Event event, boolean[] update, double[] rate,
            double[] applied_rate, Format[] format, long[] start, long[] stop, long[] position);

    /* tag event */
    @CallerOwnsReturn Event gst_event_new_tag(@Invalidate TagList taglist);
    void gst_event_parse_tag(Event event, PointerByReference taglist);

    /* buffer */
    @CallerOwnsReturn Event gst_event_new_buffer_size(Format format, long minsize, long maxsize, boolean async);
    void gst_event_parse_buffer_size(Event event, Format format, long[] minsize,
						 long[] maxsize, int[] async);

    /* QOS events */
    @CallerOwnsReturn Event gst_event_new_qos(double proportion, long diff, ClockTime timestamp);
    @CallerOwnsReturn Event gst_event_new_qos(double proportion, long diff, long timestamp);
    void gst_event_parse_qos(Event event, double[] proportion, long[] diff, long[] timestamp);
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
    
    public static final class EventStruct extends com.sun.jna.Structure {
        public volatile GstMiniObjectAPI.MiniObjectStruct mini_object;
        public volatile int type;
        public EventStruct(Pointer ptr) {
            useMemory(ptr);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "mini_object", "type"
            });
        }
    }
}
