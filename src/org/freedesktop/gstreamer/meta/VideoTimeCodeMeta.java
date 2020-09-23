package org.freedesktop.gstreamer.meta;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GPointer;
import org.freedesktop.gstreamer.timecode.VideoTimeCode;
import static org.freedesktop.gstreamer.lowlevel.GstMetaApi.GST_META_API;
import static org.freedesktop.gstreamer.lowlevel.GstMetaApi.GstVideoTimeCodeMetaStruct;

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
 * Extra buffer metadata describing the GstVideoTimeCode of the frame.
 *
 *
 * @see <a href="https://docs.gstreamer.com/documentation/video/gstvideometa.html?gi-language=c#GstVideoTimeCodeMeta">GstVideoTimeCodeMeta</a>
 */
@Gst.Since(minor = 10)
public class VideoTimeCodeMeta extends NativeObject implements Meta {

    public static final String GTYPE_NAME = "GstVideoTimeCodeMeta";
    private final GstVideoTimeCodeMetaStruct metaStruct;
    private final VideoTimeCode timeCode;

    public VideoTimeCodeMeta(Pointer pointer) {
        this(Natives.initializer(pointer, false, false));
    }

    VideoTimeCodeMeta(Initializer init) {
        super(new Handle(init.ptr, init.ownsHandle));
        metaStruct = new GstVideoTimeCodeMetaStruct(getRawPointer());
        timeCode = new VideoTimeCode(metaStruct.tc.getPointer());
    }

    /**
     * Time code attach to frame
     *
     * @return return time code
     */
    public VideoTimeCode getTimeCode() {
        return timeCode;
    }

    /**
     * Information about metadata
     *
     * @return return structure with information about metadata
     */
    public MetaInfo getMetaInfo() {
        return GST_META_API.gst_video_time_code_meta_get_info();
    }

    @Override
    public void disown() {
        timeCode.disown();
        super.disown();
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
            // structure will be released automatically by GStreamer
            /** <a href="https://gitlab.freedesktop.org/gstreamer/gst-plugins-base/-/blob/master/gst-libs/gst/video/gstvideometa.c#L1115"> by nested function</a>*/
        }
    }

}
