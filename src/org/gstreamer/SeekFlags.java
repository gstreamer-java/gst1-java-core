/* 
 * Copyright (c) 2007 Wayne Meissner
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

package org.gstreamer;

/**
 * Flags to be used with {@link Pipeline#seek seek} or 
 * {@link org.gstreamer.event.SeekEvent#SeekEvent SeekEvent}
 * <p>
 * All flags can be used together.
 *<p>
 * A non flushing seek might take some time to perform as the currently
 * playing data in the pipeline will not be cleared.
 *<p>
 * An accurate seek might be slower for formats that don't have any indexes
 * or timestamp markers in the stream. Specifying this flag might require a
 * complete scan of the file in those cases.
 *<p>
 * When performing a segment seek: after the playback of the segment completes,
 * no EOS will be emitted by the element that performed the seek, but a
 * {@link Bus.SEGMENT_DONE} message will be posted on the bus by the element.
 * When this message is posted, it is possible to send a new seek event to
 * continue playback. With this seek method it is possible to perform seamless
 * looping or simple linear editing.
 */
public final class SeekFlags {
    /** No flag. */
    public final static int NONE = 0;
    /** Flush pipeline. */
    public final static int FLUSH = 1 << 0;
    /** 
     * Accurate position is requested, this might be considerably slower for some formats. 
     */
    public final static int ACCURATE = 1 << 1;
    
    /**
     * Seek to the nearest keyframe. This might be faster but less accurate.
     */
    public final static int KEY_UNIT = 1 << 2;
    
    /** Perform a segment seek. */
    public final static int SEGMENT = 1 << 3;
    
    /** when doing fast forward or fast reverse playback, allow elements to skip frames instead of generating all frames. Since 0.10.22. */
    public final static int SKIP = 1 << 4;
}
