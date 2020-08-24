package org.freedesktop.gstreamer.timecode;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstMetaApi.GstVideoTimeCodeConfigStruct;
import org.freedesktop.gstreamer.timecode.GstVideoTimeCodeFlags;

/**
 * The configuration of the time code.
 * @author Jokertwo
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
