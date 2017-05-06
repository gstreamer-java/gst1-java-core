/* 
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

import java.util.Arrays;
import java.util.List;

import org.freedesktop.gstreamer.Buffer;
import org.freedesktop.gstreamer.ClockTime;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI.MiniObjectStruct;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

/**
 * GstBuffer functions
 */
public interface GstBufferAPI extends com.sun.jna.Library {
    GstBufferAPI GSTBUFFER_API = GstNative.load(GstBufferAPI.class);

    public static final int GST_LOCK_FLAG_READ = (1 << 0);
    public static final int GST_LOCK_FLAG_WRITE = (1 << 1);
    public static final int GST_MAP_READ = GST_LOCK_FLAG_READ;
    public static final int GST_MAP_WRITE = GST_LOCK_FLAG_WRITE;
    public static final class MapInfoStruct extends com.sun.jna.Structure {
    	public volatile Pointer memory; // Pointer to GstMemory
    	public volatile int flags; // GstMapFlags
        public volatile Pointer data;
        public volatile NativeLong size;
        public volatile NativeLong maxSize;
        
        /**
         * Creates a new instance of MessageStruct
         */
        public MapInfoStruct() {
        }
        public MapInfoStruct(Pointer ptr) {
            super(ptr);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "memory", "flags", "data", "size", "maxSize"
            });
        }
    }
    
    GType gst_buffer_get_type();
    @CallerOwnsReturn Buffer gst_buffer_new();
    @CallerOwnsReturn Buffer gst_buffer_new_allocate(Pointer allocator, int size, Pointer params);
    @CallerOwnsReturn Pointer ptr_gst_buffer_new();
    @CallerOwnsReturn Pointer ptr_gst_buffer_new_allocate(Pointer allocator, int size, Pointer params);
    NativeLong gst_buffer_get_size(Buffer buffer);
    boolean gst_buffer_map(Buffer buffer, MapInfoStruct info, int flags);
    void gst_buffer_unmap(Buffer buffer, MapInfoStruct info);
    int gst_buffer_n_memory(Buffer buffer);
    boolean gst_buffer_map_range(Buffer buffer, int idx, int length, MapInfoStruct info, int flags);
//    boolean gst_buffer_is_metadata_writable(Buffer buf);
//    Buffer gst_buffer_make_metadata_writable(@Invalidate Buffer buf);
//    /* creating a subbuffer */
//    @CallerOwnsReturn Buffer gst_buffer_create_sub(Buffer parent, int offset, int size);
//    
//    @CallerOwnsReturn Caps gst_buffer_get_caps(Buffer buffer);
//    void gst_buffer_set_caps(Buffer buffer, Caps caps);
//    /* span two buffers intelligently */
//    boolean gst_buffer_is_span_fast(Buffer buf1, Buffer buf2);
//    @CallerOwnsReturn Buffer gst_buffer_span(Buffer buf1, int offset, Buffer buf2, int len);
//    /* buffer functions from gstutils.h */
//    @CallerOwnsReturn Buffer gst_buffer_merge(Buffer buf1, Buffer buf2);
//    @CallerOwnsReturn Buffer gst_buffer_join(@Invalidate Buffer buf1, @Invalidate Buffer buf2);
    
    public static final class BufferStruct extends com.sun.jna.Structure {
        volatile public MiniObjectStruct mini_object;
        public Pointer pool;
        public ClockTime pts;
        public ClockTime dts;
        public ClockTime duration;
        public long offset;
        public long offset_end;
        
        public BufferStruct(Pointer ptr) {
            super(ptr);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "mini_object", "pool", "pts",
                "dts", "duration",
                "offset", "offset_end"
            });
        }
        
        @Override
        public String toString() {
        	return super.toString() + " " + pts + " " + dts + " " + duration;
        }
    }
}
