package org.freedesktop.gstreamer.timecode;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstMetaApi.GstVideoTimeCodeConfigStruct;
import org.freedesktop.gstreamer.timecode.GstVideoTimeCodeFlags;

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
 * The configuration of the time code.
 *
 * @see <a href="https://docs.gstreamer.com/documentation/video/gstvideotimecode.html?gi-language=c#GstVideoTimeCodeConfig">GstVideoTimeCodeConfig</a>
 */
public class GstVideoTimeCodeConfig extends MiniObject {

    public static final String GTYPE_NAME = "GstVideoTimeCodeConfig";
    private final GstVideoTimeCodeConfigStruct timeCodeConfig;

    public GstVideoTimeCodeConfig(Pointer pointer) {
        this(Natives.initializer(pointer,false,false));
    }

    GstVideoTimeCodeConfig(Initializer init) {
        super(init);
        timeCodeConfig = new GstVideoTimeCodeConfigStruct(getRawPointer());
    }

    /**
     * The corresponding {@link GstVideoTimeCodeFlags}
     *
     * @return return flag for current timecode
     */
    public GstVideoTimeCodeFlags getTimeCodeFlags() {
        return timeCodeConfig.flags;
    }

    /**
     * Numerator of the frame rate
     *
     * @return return positive number
     */
    public int getFramerateNumerator() {
        return timeCodeConfig.fps_n;
    }

    /**
     * Denominator of the frame rate
     *
     * @return return positive number
     */
    public int getFramerateDenominator() {
        return timeCodeConfig.fps_d;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GstVideoTimeCodeConfig{");
        sb.append("flags=").append(getTimeCodeFlags())
                .append(", numerator=").append(getFramerateNumerator())
                .append(", denominator=").append(getFramerateDenominator())
                .append('}');
        return sb.toString();
    }
}
