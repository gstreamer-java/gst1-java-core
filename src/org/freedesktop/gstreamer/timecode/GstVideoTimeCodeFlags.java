package org.freedesktop.gstreamer.timecode;

import org.freedesktop.gstreamer.glib.NativeFlags;

/**
 * Flags related to the time code information. For drop frame, only 30000/1001 and 60000/1001 frame rates are supported.
 *
 * @author Jokertwo
 */
public enum GstVideoTimeCodeFlags implements NativeFlags<GstVideoTimeCodeFlags> {
    /**
     * No flags
     */
    GST_VIDEO_TIME_CODE_FLAGS_NONE(0), // No flags
    /**
     * Whether we have drop frame rate
     */
    GST_VIDEO_TIME_CODE_FLAGS_DROP_FRAME(1),
    /**
     * Whether we have interlaced video
     */
    GST_VIDEO_TIME_CODE_FLAGS_INTERLACED(2);


    private final int value;

    GstVideoTimeCodeFlags(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }
}
