/*
 * Copyright (c) 2020 Neil C Smith
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
 */
package org.freedesktop.gstreamer.video;

import java.util.EnumSet;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.glib.NativeFlags;
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.lowlevel.GPointer;
import org.freedesktop.gstreamer.lowlevel.GstVideoAPI.GstVideoTimeCodeConfigStruct;

/**
 * The configuration of the time code.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/documentation/video/gstvideotimecode.html#GstVideoTimeCodeConfig">
 * https://gstreamer.freedesktop.org/documentation/video/gstvideotimecode.html#GstVideoTimeCodeConfig</a>
 */
@Gst.Since(minor = 10)
public class VideoTimeCodeConfig extends NativeObject {

    private final GstVideoTimeCodeConfigStruct timeCodeConfig;

    VideoTimeCodeConfig(GstVideoTimeCodeConfigStruct struct) {
        this(struct, new Handle(new GPointer(struct.getPointer()), false));
    }

    private VideoTimeCodeConfig(GstVideoTimeCodeConfigStruct struct, Handle handle) {
        super(handle);
        timeCodeConfig = struct;
    }

    /**
     * The corresponding {@link VideoTimeCodeFlags}.
     *
     * @return return flags for current timecode
     */
    public EnumSet<VideoTimeCodeFlags> getFlags() {
        return NativeFlags.fromInt(VideoTimeCodeFlags.class, timeCodeConfig.flags);
    }

    /**
     * Numerator of the frame rate.
     *
     * @return numerator
     */
    public int getNumerator() {
        return timeCodeConfig.fps_n;
    }

    /**
     * Denominator of the frame rate.
     *
     * @return denominator
     */
    public int getDenominator() {
        return timeCodeConfig.fps_d;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GstVideoTimeCodeConfig{");
        sb.append("flags=").append(getFlags())
                .append(", numerator=").append(getNumerator())
                .append(", denominator=").append(getDenominator())
                .append('}');
        return sb.toString();
    }

    private static final class Handle extends NativeObject.Handle {

        /**
         * Construct a Handle for the supplied native reference.
         *
         * @param ptr           native reference
         * @param ownsReference whether the Handle owns the native reference and
         */
        public Handle(GPointer ptr, boolean ownsReference) {
            super(ptr, ownsReference);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            // usually video timecode config will be released together with video timecode
            //            GlibAPI.GLIB_API.g_free(ptr.getPointer());
        }

        @Override
        protected GPointer getPointer() {
            return super.getPointer();
        }
        
    }
}
