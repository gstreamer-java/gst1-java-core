package org.freedesktop.gstreamer.meta;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.timecode.GstVideoTimeCode;
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
public class GstVideoTimeCodeMeta extends MiniObject {

    public static final String GTYPE_NAME = "GstVideoTimeCodeMeta";
    private final GstVideoTimeCodeMetaStruct metaStruct;
    private final GstVideoTimeCode timeCode;

    public GstVideoTimeCodeMeta(Pointer pointer) {
        this(Natives.initializer(pointer, false, false));
    }

    GstVideoTimeCodeMeta(Initializer init) {
        super(init);
        metaStruct = new GstVideoTimeCodeMetaStruct(getRawPointer());
        timeCode = new GstVideoTimeCode(metaStruct.tc.getPointer());
    }

    /**
     * Time code attach to frame
     *
     * @return return time code
     */
    public GstVideoTimeCode getTimeCode() {
        return timeCode;
    }

    /**
     * Information about metadata
     *
     * @return return structure with information about metadata
     */
    public GstMetaInfo getMetaInfo() {
        return GST_META_API.gst_video_time_code_meta_get_info();
    }

    @Override
    public void disown() {
        timeCode.disown();
        super.disown();
    }

}
