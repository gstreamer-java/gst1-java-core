/*
 * Copyright (c) 2015 Neil C Smith
 * Copyright (C) 2014 Tom Greenwood <tgreenwood@cafex.com>
 * Copyright (C) 2007 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wtay@chello.be>
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

package org.freedesktop.gstreamer;

import static org.freedesktop.gstreamer.lowlevel.GstBufferAPI.GSTBUFFER_API;

import java.nio.ByteBuffer;

import org.freedesktop.gstreamer.lowlevel.GstBufferAPI;
import org.freedesktop.gstreamer.lowlevel.GstBufferAPI.BufferStruct;
import org.freedesktop.gstreamer.lowlevel.GstBufferAPI.MapInfoStruct;

import com.sun.jna.Pointer;

/**
 * Data-passing buffer type, supporting sub-buffers.
 * Ssee {@link Pad}, {@link MiniObject}
 * <p>
 * Buffers are the basic unit of data transfer in GStreamer.  The Buffer
 * type provides all the state necessary to define a region of memory as part
 * of a stream.  Sub-buffers are also supported, allowing a smaller region of a
 * buffer to become its own buffer, with mechanisms in place to ensure that
 * neither memory space goes away prematurely.
 * <p>
 * Non-plugins will usually not need to allocate buffers, but they can be allocated
 * using new {@link #Buffer(int)} to create a buffer with preallocated data of a given size.
 * <p>
 * The data pointed to by the buffer can be accessed with the {@link #getByteBuffer}
 * method.  For buffers of size 0, the data pointer is undefined (usually NULL) 
 * and should never be used.
 * <p>
 * If an element knows what pad you will push the buffer out on, it should use
 * gst_pad_alloc_buffer() instead to create a buffer.  This allows downstream
 * elements to provide special buffers to write in, like hardware buffers.
 * <p>
 * A buffer has a pointer to a {@link Caps} describing the media type of the data
 * in the buffer. Attach caps to the buffer with {@link #setCaps}; this
 * is typically done before pushing out a buffer using gst_pad_push() so that
 * the downstream element knows the type of the buffer.
 * <p>
 * A buffer will usually have a timestamp, and a duration, but neither of these
 * are guaranteed (they may be set to -1). Whenever a
 * meaningful value can be given for these, they should be set. The timestamp
 * and duration are measured in nanoseconds (they are long values).
 * <p>
 * A buffer can also have one or both of a start and an end offset. These are
 * media-type specific. For video buffers, the start offset will generally be
 * the frame number. For audio buffers, it will be the number of samples
 * produced so far. For compressed data, it could be the byte offset in a
 * source or destination file. Likewise, the end offset will be the offset of
 * the end of the buffer. These can only be meaningfully interpreted if you
 * know the media type of the buffer (the #GstCaps set on it). Either or both
 * can be set to -1.
 * <p>
 * To efficiently create a smaller buffer out of an existing one, you can
 * use {@link #createSubBuffer}.
 * <p>
 * If a plug-in wants to modify the buffer data in-place, it should first obtain
 * a buffer that is safe to modify by using {@link #makeWritable}.  This
 * function is optimized so that a copy will only be made when it is necessary.
 * <p>
 * A plugin that only wishes to modify the metadata of a buffer, such as the
 * offset, timestamp or caps, should use gst_buffer_make_metadata_writable(),
 * which will create a subbuffer of the original buffer to ensure the caller
 * has sole ownership, and not copy the buffer data.
 * <p>
 * Buffers can be efficiently merged into a larger buffer with
 * gst_buffer_merge() and gst_buffer_span() if the gst_buffer_is_span_fast()
 * function returns TRUE.
 * <p>
 */
public class Buffer extends MiniObject {
    public static final String GTYPE_NAME = "GstBuffer";

    private final MapInfoStruct mapInfo;
    private final BufferStruct struct;

    
    public Buffer(Initializer init) {
        super(init);
        mapInfo = new MapInfoStruct();
        struct = new BufferStruct(handle());
    }
    
    /**
     * Creates a newly allocated buffer without any data.
     */
    public Buffer() {
        this(initializer(GSTBUFFER_API.ptr_gst_buffer_new()));
    }
    
    /**
     * Creates a newly allocated buffer with data of the given size.
     * The buffer memory is not cleared. If the requested amount of
     * memory cannot be allocated, an exception will be thrown.
     *
     * Note that when size == 0, the buffer data pointer will be NULL.
     *
     * @param size
     */
    public Buffer(int size) {
        this(initializer(allocBuffer(size)));
    }
    
    private static Pointer allocBuffer(int size) {
        Pointer ptr = GSTBUFFER_API.ptr_gst_buffer_new_allocate(null, size, null);
        if (ptr == null) {
            throw new OutOfMemoryError("Could not allocate Buffer of size "+ size);
        }
        return ptr;
    }
    
//    /**
//     * Gets the size of the buffer data
//     * 
//     * @return the size of the buffer data in bytes.
//     */
//    public int getSize() {
//    	return GstBufferAPI.GSTBUFFER_API.gst_buffer_get_size(this).intValue();
//    }
    
    /**
     * Gets a {@link java.nio.ByteBuffer} that can access the native memory
     * associated with this Buffer.
     * 
     * @return A {@link java.nio.ByteBuffer} that can access this Buffer's data.
     */
    public ByteBuffer map(boolean writeable) {
        final boolean ok = GSTBUFFER_API.gst_buffer_map(this, mapInfo,
                writeable ? GstBufferAPI.GST_MAP_WRITE : GstBufferAPI.GST_MAP_READ);
        if (ok && mapInfo.data != null) {
            return mapInfo.data.getByteBuffer(0, mapInfo.size.intValue());
        }
        return null;
    }
    
    public void unmap() {
        GSTBUFFER_API.gst_buffer_unmap(this, mapInfo);
    }
    
    /**
     * Gets the timestamps of this buffer.
     * The buffer DTS refers to the timestamp when the buffer should be decoded and is usually monotonically increasing.
     *
     * @return a ClockTime representing the timestamp or {@link ClockTime#NONE} when the timestamp is not known or relevant.
     */
    public ClockTime getDecodeTimestamp() {
		return (ClockTime)this.struct.readField("dts");
    }

    /**
     * Gets the timestamps of this buffer.
     * The buffer PTS refers to the timestamp when the buffer content should be presented to the user and is not always monotonically increasing.
     *
     * @return a ClockTime representing the timestamp or {@link ClockTime#NONE} when the timestamp is not known or relevant.
     */
    public ClockTime getPresentationTimestamp() {
		return (ClockTime)this.struct.readField("pts");
    }

}
