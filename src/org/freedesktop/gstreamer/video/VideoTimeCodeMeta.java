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

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Meta;

import static org.freedesktop.gstreamer.lowlevel.GstVideoAPI.GstVideoTimeCodeMetaStruct;

 /**
 * Extra buffer metadata describing the GstVideoTimeCode of the frame.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/documentation/video/gstvideometa.html#GstVideoTimeCodeMeta"
 * >https://gstreamer.freedesktop.org/documentation/video/gstvideometa.html#GstVideoTimeCodeMeta</a>
 */
@Gst.Since(minor = 10)
public class VideoTimeCodeMeta extends Meta {
    
    /**
     * Meta.API for VideoTimeCodeMeta.
     */
    public static final API<VideoTimeCodeMeta> API =
            new API(VideoTimeCodeMeta.class, "GstVideoTimeCodeMetaAPI");

    /**
     * Underlying GType name.
     */
    public static final String GTYPE_NAME = "GstVideoTimeCodeMeta";
    
    private final VideoTimeCode timeCode;

    VideoTimeCodeMeta(Initializer init) {
        super(init);
        GstVideoTimeCodeMetaStruct metaStruct =
                new GstVideoTimeCodeMetaStruct(init.ptr.getPointer());
        timeCode = new VideoTimeCode(metaStruct.tc);
    }

    /**
     * Retrieve the time code attached to frame.
     *
     * @return time code
     */
    public VideoTimeCode getTimeCode() {
        return timeCode;
    }

    @Override
    public void disown() {
        timeCode.disown();
        super.disown();
    }

}
