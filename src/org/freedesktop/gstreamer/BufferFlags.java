/* 
 * Copyright (c) 2019 Neil C Smith
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

import org.freedesktop.gstreamer.glib.NativeFlags;

/**
 * A set of buffer flags used to describe properties of a {@link Buffer}.
 */
public enum BufferFlags implements NativeFlags<BufferFlags> {

    /**  
     * the {@link Buffer} is live data and should be discarded in the PAUSED state.
     */
    LIVE(MiniObjectFlags.LAST.intValue() << 0),

    /**
     * the {@link Buffer} contains data that should be dropped
     * because it will be clipped against the segment
     * boundaries or because it does not contain data
     * that should be shown to the user.
     */
    DECODE_ONLY(MiniObjectFlags.LAST.intValue() << 1),

    /**
     * The {@link Buffer} marks a discontinuity in the stream.
     * This typically occurs after a seek or a dropped buffer from a live or
     * network source.
     */
    DISCONT(MiniObjectFlags.LAST.intValue() << 2),

    /**
     * The {@link Buffer} timestamps might have a discontinuity and this buffer is a good point to resynchronize.
     */
    RESYNC(MiniObjectFlags.LAST.intValue() << 3),

    /**
     * the {@link Buffer} data is corrupted.
     */
    CORRUPTED(MiniObjectFlags.LAST.intValue() << 4),

    /**
     * the buffer contains a media specific marker. for video this is typically the end of a frame boundary, for audio this is usually the start of a talkspurt.
     */
    MARKER(MiniObjectFlags.LAST.intValue() << 5),

    /**
     * he buffer contains header information that is needed to decode the following data.
     */
    HEADER(MiniObjectFlags.LAST.intValue() << 6),

    /**
     * The {@link Buffer} has been created to fill a gap in the
     * stream and contains media neutral data (elements can switch to optimized code
     * path that ignores the buffer content).
     */
    GAP(MiniObjectFlags.LAST.intValue() << 7),

    /**
     * the {@link Buffer} can be dropped without breaking the stream, for example to reduce bandwidth.
     */
    DROPPABLE(MiniObjectFlags.LAST.intValue() << 8),
    
    /** This unit cannot be decoded independently. */
    DELTA_UNIT(MiniObjectFlags.LAST.intValue() << 9),

    /**
     * this flag is set when memory of the {@link Buffer} is added/removed
     */
    TAG_MEMORY(MiniObjectFlags.LAST.intValue() << 10),

    /**
     * Elements which write to disk or permanent storage should ensure the data is synced after writing the contents of this {@link Buffer}. (Since 1.6)
     */
    SYNC_AFTER(MiniObjectFlags.LAST.intValue() << 11),
    
    /**
     * This buffer is important and should not be dropped. This can be used to
     * mark important buffers, e.g. to flag RTP packets carrying keyframes or
     * codec setup data for RTP Forward Error Correction purposes, or to prevent
     * still video frames from being dropped by elements due to QoS. (Since
     * 1.14)

     */
    @Gst.Since(minor = 14)
    NON_DROPPABLE(MiniObjectFlags.LAST.intValue() << 12),
    
    /* padding */
    LAST(MiniObjectFlags.LAST.intValue() << 16);
    
    private final int value;
    
    private BufferFlags(int value) {
        this.value = value;
    }
    /**
     * Get the integer value of the enum.
     * @return The integer value for this enum.
     */
    public final int intValue() {
        return value;
    }
    
}
