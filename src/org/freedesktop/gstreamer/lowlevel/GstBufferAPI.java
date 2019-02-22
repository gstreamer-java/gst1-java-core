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
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI.MiniObjectStruct;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import static org.freedesktop.gstreamer.lowlevel.GstAPI.GST_PADDING;

/**
 * GstBuffer methods and structures
 * @see https://cgit.freedesktop.org/gstreamer/gstreamer/tree/gst/gstbuffer.h?h=1.8
 */
public interface GstBufferAPI extends com.sun.jna.Library {
    
    GstBufferAPI GSTBUFFER_API = GstNative.load(GstBufferAPI.class);

    public static final int GST_LOCK_FLAG_READ = (1 << 0);
    public static final int GST_LOCK_FLAG_WRITE = (1 << 1);
    public static final int GST_MAP_READ = GST_LOCK_FLAG_READ;
    public static final int GST_MAP_WRITE = GST_LOCK_FLAG_WRITE;
    
    /**
    * @see https://cgit.freedesktop.org/gstreamer/gstreamer/tree/gst/gstmemory.h?h=1.8
    * 
    * GstMapInfo:
    * @memory: a pointer to the mapped memory
    * @flags: flags used when mapping the memory
    * @data: (array length=size): a pointer to the mapped data
    * @size: the valid size in @data
    * @maxsize: the maximum bytes in @data
    * @user_data: extra private user_data that the implementation of the memory
    *             can use to store extra info.
    *
    * A structure containing the result of a map operation such as
    * gst_memory_map(). It contains the data and size.
    */
    public static final class MapInfoStruct extends com.sun.jna.Structure {
    	public volatile Pointer memory; // Pointer to GstMemory
    	public volatile int flags; // GstMapFlags
        public volatile Pointer /* gunit8 */ data;
        public volatile NativeLong size;
        public volatile NativeLong maxSize;
        
        /*< protected >*/
        public volatile Pointer[] user_data = new Pointer[4];
        
        /*< private >*/
        public volatile Pointer[] _gst_reserved = new Pointer[GST_PADDING];
        
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
                "memory", "flags", "data", "size", "maxSize",
                "user_data", "_gst_reserved"
            });
        }
    }
    
    GType gst_buffer_get_type();
    
    /* allocation */
    @CallerOwnsReturn Buffer gst_buffer_new();
    @CallerOwnsReturn Buffer gst_buffer_new_allocate(Pointer allocator, int size, Pointer params);
    @CallerOwnsReturn Pointer ptr_gst_buffer_new();
    @CallerOwnsReturn Pointer ptr_gst_buffer_new_allocate(Pointer allocator, int size, Pointer params);
    
    /* memory blocks */
    NativeLong gst_buffer_get_size(Buffer buffer);
    boolean gst_buffer_map(Buffer buffer, MapInfoStruct info, int flags);
    void gst_buffer_unmap(Buffer buffer, MapInfoStruct info);
    int gst_buffer_n_memory(Buffer buffer);
    boolean gst_buffer_map_range(Buffer buffer, int idx, int length, MapInfoStruct info, int flags);
    
    // re-introduces in gstreamer 1.9
    int gst_buffer_get_flags(Buffer buffer);
    boolean gst_buffer_set_flags(Buffer buffer, int flags);
    boolean gst_buffer_unset_flags(Buffer buffer, int flags);
    
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
    
    /**
    * GstBuffer:
    * @mini_object: the parent structure
    * @pool: pointer to the pool owner of the buffer
    * @pts: presentation timestamp of the buffer, can be #GST_CLOCK_TIME_NONE when the
    *     pts is not known or relevant. The pts contains the timestamp when the
    *     media should be presented to the user.
    * @dts: decoding timestamp of the buffer, can be #GST_CLOCK_TIME_NONE when the
    *     dts is not known or relevant. The dts contains the timestamp when the
    *     media should be processed.
    * @duration: duration in time of the buffer data, can be #GST_CLOCK_TIME_NONE
    *     when the duration is not known or relevant.
    * @offset: a media specific offset for the buffer data.
    *     For video frames, this is the frame number of this buffer.
    *     For audio samples, this is the offset of the first sample in this buffer.
    *     For file data or compressed data this is the byte offset of the first
    *       byte in this buffer.
    * @offset_end: the last offset contained in this buffer. It has the same
    *     format as @offset.
    *
    * The structure of a #GstBuffer. Use the associated macros to access the public
    * variables.
    */
    public static final class BufferStruct extends com.sun.jna.Structure {
        volatile public MiniObjectStruct mini_object;
        
        /*< public >*/ /* with COW */
        public Pointer /* BufferPool */ pool;
        
        /* timestamp */
        public long pts;
        public long dts;
        public long duration;
        
        /* media specific offset */
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
