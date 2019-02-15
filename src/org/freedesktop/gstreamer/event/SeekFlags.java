/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer.event;

import org.freedesktop.gstreamer.glib.NativeFlags;

/**
 * Flags to be used with {@link Pipeline#seek seek} or
 * {@link org.freedesktop.gstreamer.event.SeekEvent#SeekEvent SeekEvent}
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#GstSeekFlags"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#GstSeekFlags</a>
 * <p>
 * All flags can be used together.
 * <p>
 * A non flushing seek might take some time to perform as the currently playing
 * data in the pipeline will not be cleared.
 * <p>
 * An accurate seek might be slower for formats that don't have any indexes or
 * timestamp markers in the stream. Specifying this flag might require a
 * complete scan of the file in those cases.
 * <p>
 * When performing a segment seek: after the playback of the segment completes,
 * no EOS will be emitted by the element that performed the seek, but a
 * {@link Bus.SEGMENT_DONE} message will be posted on the bus by the element.
 * When this message is posted, it is possible to send a new seek event to
 * continue playback. With this seek method it is possible to perform seamless
 * looping or simple linear editing.
 */
public enum SeekFlags implements NativeFlags<SeekFlags> {

//    /**
//     * No flag.
//     */
//    NONE(0),
    /**
     * Flush pipeline.
     */
    FLUSH(1 << 0),
    /**
     * Accurate position is requested, this might be considerably slower for
     * some formats.
     */
    ACCURATE(1 << 1),
    /**
     * Seek to the nearest keyframe. This might be faster but less accurate.
     */
    KEY_UNIT(1 << 2),
    /**
     * Perform a segment seek.
     */
    SEGMENT(1 << 3),
    /**
     * when doing fast forward or fast reverse playback, allow elements to skip
     * frames instead of generating all frames. (Since 1.6)
     */
    TRICKMODE(1 << 4),
    /**
     * go to a location before the requested position, if
     * %GST_SEEK_FLAG_KEY_UNIT this means the keyframe at or before the
     * requested position the one at or before the seek target.
     */
    SNAP_BEFORE(1 << 5),
    /**
     * go to a location after the requested position, if %GST_SEEK_FLAG_KEY_UNIT
     * this means the keyframe at of after the requested position.
     */
    SNAP_AFTER(1 << 6),
    //    /**
    //     * go to a position near the requested position, if %GST_SEEK_FLAG_KEY_UNIT
    //     * this means the keyframe closest to the requested position, if both
    //     * keyframes are at an equal distance, behaves like
    //     * %GST_SEEK_FLAG_SNAP_BEFORE.
    //     */
    //    public final static int SNAP_NEAREST = SNAP_BEFORE | SNAP_AFTER;
    /**
     * when doing fast forward or fast reverse playback, request that elements
     * only decode keyframes and skip all other content, for formats that have
     * keyframes.
     */
    TRICKMODE_KEY_UNITS(1 << 7),
    /**
     * when doing fast forward or fast reverse playback, request that audio
     * decoder elements skip decoding and output only gap events or silence.
     */
    TRICKMODE_NO_AUDIO(1 << 8);

    private final int value;

    private SeekFlags(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }
}
