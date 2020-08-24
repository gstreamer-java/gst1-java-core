package org.freedesktop.gstreamer.meta;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.timecode.GstVideoTimeCode;
import static org.freedesktop.gstreamer.lowlevel.GstMetaApi.GST_META_API;
import static org.freedesktop.gstreamer.lowlevel.GstMetaApi.GstVideoTimeCodeMetaStruct;

/**
 * Extra buffer metadata describing the GstVideoTimeCode of the frame.
 * @author Jokertwo
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
