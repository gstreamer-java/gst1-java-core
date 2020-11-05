package org.freedesktop.gstreamer.video;

import org.freedesktop.gstreamer.Meta;

public class VideoMeta extends Meta {

    /**
     * Meta.API for VideoMeta.
     */
    public static final API<VideoMeta> API = new API<>(VideoMeta.class, "GstVideoMetaAPI");

    /**
     * Underlying GType name.
     */
    public static final String GTYPE_NAME = "GstVideoMeta";

    VideoMeta(Initializer init) {
        super(init);
    }

}
