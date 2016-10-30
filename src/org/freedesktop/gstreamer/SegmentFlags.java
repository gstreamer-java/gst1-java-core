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
package org.freedesktop.gstreamer;

/**
 * GstSegmentFlags
 */
public final class SegmentFlags {

    /**
     * no flags
     */
    public static final int NONE = SeekFlags.NONE;
    /**
     * reset the pipeline running_time to the segment running_time
     */
    public static final int RESET = SeekFlags.FLUSH;
    /**
     * perform skip playback (Since 1.6)
     */
    public static final int TRICKMODE = SeekFlags.TRICKMODE;
    @Deprecated
    public static final int SKIP = SeekFlags.TRICKMODE;
    /**
     * send SEGMENT_DONE instead of EOS
     */
    public static final int SEGMENT = SeekFlags.SEGMENT;
    /**
     * Decode only keyframes, where possible (Since 1.6)
     */
    public static final int TRICKMODE_KEY_UNITS = SeekFlags.TRICKMODE_KEY_UNITS;
    /**
     * Do not decode any audio, where possible (Since 1.6)
     */
    public static final int TRICKMODE_NO_AUDIO = SeekFlags.TRICKMODE_NO_AUDIO;
}
