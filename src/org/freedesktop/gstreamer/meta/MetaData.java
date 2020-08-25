package org.freedesktop.gstreamer.meta;

import java.util.function.Supplier;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.lowlevel.GType;
import static org.freedesktop.gstreamer.lowlevel.GstMetaApi.GST_META_API;

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
 * Gst meta data GTypes
 *
 */
public enum MetaData {

    VIDEO_CROP_META(GST_META_API::gst_video_crop_meta_api_get_type),
    VIDEO_GL_TEXTURE_META(GST_META_API::gst_video_gl_texture_upload_meta_api_get_type),
    VIDEO_META(GST_META_API::gst_video_meta_api_get_type),
    VIDEO_REGION_OF_INTEREST(GST_META_API::gst_video_region_of_interest_meta_api_get_type),
    @Gst.Since(minor = 10)
    VIDEO_TIME_CODE_META(GST_META_API::gst_video_time_code_meta_api_get_type);

    private final Supplier<GType> gTypeMetaSup;

    MetaData(Supplier<GType> gTypeMetaSup) {
        this.gTypeMetaSup = gTypeMetaSup;
    }

    public GType getType() {
        if(name().equals(VIDEO_TIME_CODE_META.name())){
            Gst.checkVersion(1,10);
        }
        return gTypeMetaSup.get();
    }
}
