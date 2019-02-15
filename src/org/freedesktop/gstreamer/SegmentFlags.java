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
package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.event.SeekFlags;
import org.freedesktop.gstreamer.glib.NativeFlags;

/**
 * GstSegmentFlags
 */
enum SegmentFlags implements NativeFlags<SegmentFlags> {

//    /**
//     * no flags
//     */
//    public static final int NONE = SeekFlags.NONE;
    /**
     * reset the pipeline running_time to the segment running_time
     */
    RESET(SeekFlags.FLUSH),
    
    /**
     * perform skip playback
     */
    TRICKMODE(SeekFlags.TRICKMODE),
    /**
     * send SEGMENT_DONE instead of EOS
     */
    SEGMENT(SeekFlags.SEGMENT),
    /**
     * Decode only keyframes, where possible
     */
    TRICKMODE_KEY_UNITS(SeekFlags.TRICKMODE_KEY_UNITS),
    /**
     * Do not decode any audio, where possible (Since 1.6)
     */
    TRICKMODE_NO_AUDIO(SeekFlags.TRICKMODE_NO_AUDIO);

    private final SeekFlags seekFlags;
    
    private SegmentFlags(SeekFlags seekFlags) {
        this.seekFlags = seekFlags;
    }

    @Override
    public int intValue() {
        return seekFlags.intValue();
    }

}
