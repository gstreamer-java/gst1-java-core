package org.freedesktop.gstreamer.meta;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.timecode.GstVideoTimeCode;
import static org.freedesktop.gstreamer.lowlevel.GstMetaApi.GstMetaInfo;
import static org.freedesktop.gstreamer.lowlevel.GstMetaApi.GstVideoTimeCodeMetaStruct;

/**
 * Extra buffer metadata describing the GstVideoTimeCode of the frame.
 * @author Jokertwo
 */
public class GstVideoTimeCodeMeta extends MiniObject {

    public static final String GTYPE_NAME = "GstVideoTimeCodeMeta";
    private final GstVideoTimeCodeMetaStruct metaStruct;
    private final GstVideoTimeCode timeCode;

    public GstVideoTimeCodeMeta(Pointer pointer) {
        this(Natives.initializer(pointer,false,false));
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

    public GstMetaInfo getMetaInfo() {
        return metaStruct.meta.info;
    }

    @Override
    public void disown() {
        timeCode.disown();
        super.disown();
    }

}
