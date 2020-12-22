/* 
 * Copyright (c) 2020 Neil C Smith
 * Copyright (c) 2014 Tom Greenwood <tgreenwood@cafex.com>
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

import org.freedesktop.gstreamer.Clock;
import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.message.Message;
import org.freedesktop.gstreamer.message.MessageType;
import org.freedesktop.gstreamer.State;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.TagList;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GErrorStruct;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI.MiniObjectStruct;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.ConstReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.Invalidate;

import com.sun.jna.Pointer;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

/*
 * GstMessage functions
 */
public interface GstMessageAPI extends com.sun.jna.Library {
    GstMessageAPI GSTMESSAGE_API = GstNative.load(GstMessageAPI.class);

    @FieldOrder({"mini_object", "type", "timestamp", "src",
        "seqnum", "lock", "cond"})
    public static final class MessageStruct extends com.sun.jna.Structure {
    	public volatile MiniObjectStruct mini_object;
        public volatile int type;
        public volatile long timestamp;
        public volatile GstObjectPtr src;
        public volatile int seqnum;

        public volatile Pointer lock;
        public volatile Pointer cond;

        /**
         * Creates a new instance of MessageStruct
         */
        public MessageStruct() {
        }
        public MessageStruct(Pointer ptr) {
            useMemory(ptr);
        }
        
        int typeOffset() {
            return fieldOffset("type");
        }
        
        int srcOffset() {
            return fieldOffset("src");
        }
    }
    
    GType gst_message_get_type();
    String gst_message_type_get_name(MessageType type);
    void gst_message_parse_state_changed(Message msg, State[] old, State[] current, State[] pending);
    void gst_message_parse_state_changed(GstMessagePtr msg, IntByReference old, IntByReference current, IntByReference pending);
    void gst_message_parse_tag(Message msg, PointerByReference tagList);
    void gst_message_parse_tag(GstMessagePtr msg, PointerByReference tagList);
    void gst_message_parse_clock_provide(Message msg, PointerByReference clock, int[] reader);
    void gst_message_parse_new_clock(Message msg, PointerByReference clock);
    void gst_message_parse_error(Message msg, PointerByReference err, PointerByReference debug);
    void gst_message_parse_error(GstMessagePtr msg, PointerByReference err, PointerByReference debug);
    void gst_message_parse_error(Message msg, GErrorStruct[] err, Pointer[] debug);
    void gst_message_parse_warning(Message msg, PointerByReference err, PointerByReference debug);
    void gst_message_parse_warning(GstMessagePtr msg, PointerByReference err, PointerByReference debug);
    void gst_message_parse_warning(Message msg, GErrorStruct[] err, Pointer[] debug);
    void gst_message_parse_info(Message msg, PointerByReference err, PointerByReference debug);
    void gst_message_parse_info(GstMessagePtr msg, PointerByReference err, PointerByReference debug);
    void gst_message_parse_info(Message msg, GErrorStruct[] err, Pointer[] debug);
    void gst_message_parse_buffering(Message msg, int[] percent);
    void gst_message_parse_buffering(GstMessagePtr msg, IntByReference percent);
    void gst_message_parse_segment_start(Message message, Format[] format, long[] position); 
    void gst_message_parse_segment_start(GstMessagePtr msg, IntByReference format, LongByReference position); 
    void gst_message_parse_segment_done(Message message, Format[] format, long[] position);
    void gst_message_parse_segment_done(GstMessagePtr msg, IntByReference format, LongByReference position); 
    void gst_message_parse_duration(Message message, Format[] format, long[] position);
    void gst_message_parse_duration(GstMessagePtr msg, IntByReference format, LongByReference position);
    void gst_message_parse_async_start(Message message, boolean[] new_base_time);
    boolean gst_message_parse_context_type(Message message, String[] context_type);
    boolean gst_message_parse_context_type(GstMessagePtr msg, PointerByReference /*String*/ context_type);
    
    @CallerOwnsReturn Message gst_message_new_eos(GstObject src);
    Pointer ptr_gst_message_new_eos(GstObject src);
    @CallerOwnsReturn GstMessagePtr gst_message_new_eos(GstObjectPtr src);
    @CallerOwnsReturn Message gst_message_new_error(GstObject src, GErrorStruct error, String debug);
    @CallerOwnsReturn Message gst_message_new_warning(GstObject src, GErrorStruct error, String debug);
    @CallerOwnsReturn Message gst_message_new_info(GstObject src, GErrorStruct error, String debug);
    @CallerOwnsReturn Message gst_message_new_tag(GstObject src, @Invalidate TagList tag_list);
    Pointer ptr_gst_message_new_tag(GstObject src, @Invalidate TagList tag_list);
    @CallerOwnsReturn Message gst_message_new_buffering(GstObject src, int percent);
    Pointer ptr_gst_message_new_buffering(GstObject src, int percent);
    @CallerOwnsReturn Message gst_message_new_state_changed(GstObject src, State oldstate, State newstate, State pending);
    Pointer ptr_gst_message_new_state_changed(GstObject src, State oldstate, State newstate, State pending);
    @CallerOwnsReturn Message gst_message_new_state_dirty(GstObject src);
    @CallerOwnsReturn Message gst_message_new_clock_provide(GstObject src, Clock clock, boolean ready);
    @CallerOwnsReturn Message gst_message_new_clock_lost(GstObject src, Clock clock);
    @CallerOwnsReturn Message gst_message_new_new_clock(GstObject src, Clock clock);
    @CallerOwnsReturn Message gst_message_new_application(GstObject src, Structure structure);
    @CallerOwnsReturn Message gst_message_new_element(GstObject src, Structure structure);
    @CallerOwnsReturn Message gst_message_new_segment_start(GstObject src, Format format, long position);
    @CallerOwnsReturn Message gst_message_new_segment_done(GstObject src, Format format, long position);
    Pointer ptr_gst_message_new_segment_done(GstObject src, Format format, long position);
    @CallerOwnsReturn Message gst_message_new_duration_changed(GstObject src);
    Pointer ptr_gst_message_new_duration_changed(GstObject src);
    @CallerOwnsReturn Message gst_message_new_async_start(GstObject src, boolean new_base_time);
    @CallerOwnsReturn Message gst_message_new_async_done(GstObject src);
    @CallerOwnsReturn Message gst_message_new_latency(GstObject src);
    Pointer ptr_gst_message_new_latency(GstObject source);
    @CallerOwnsReturn Message gst_message_new_custom(MessageType type, GstObject src, @Invalidate Structure structure);
    @ConstReturn Structure gst_message_get_structure(Message message);
    Pointer ptr_gst_message_new_need_context(GstObject source, String context_type);
    @CallerOwnsReturn Message gst_message_new_need_context(GstObject source, String context_type);
}
