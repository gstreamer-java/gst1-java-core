package org.freedesktop.gstreamer.timecode;

import org.freedesktop.gstreamer.glib.NativeFlags;

/*
 * Copyright (c) 2020 Petr Lastovka
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
 *
 * Flags related to the time code information. For drop frame, only 30000/1001 and 60000/1001 frame rates are supported.
 *
 * @see <a href="https://docs.gstreamer.com/documentation/video/gstvideotimecode.html?gi-language=c#GstVideoTimeCodeFlags">GstVideoTimeCodeFlags</a>
 */
public enum VideoTimeCodeFlags implements NativeFlags<VideoTimeCodeFlags> {
    /**
     * No flags
     */
    GST_VIDEO_TIME_CODE_FLAGS_NONE(0), // No flags
    /**
     * Whether we have drop frame rate
     */
    GST_VIDEO_TIME_CODE_FLAGS_DROP_FRAME(1),
    /**
     * Whether we have interlaced video
     */
    GST_VIDEO_TIME_CODE_FLAGS_INTERLACED(2);


    private final int value;

    VideoTimeCodeFlags(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }
}
