package org.freedesktop.gstreamer.meta;

import org.freedesktop.gstreamer.glib.NativeEnum;
import org.freedesktop.gstreamer.glib.NativeFlags;

/**
 * @author Jokertwo
 * @see <a href="https://docs.gstreamer.com/documentation/gstreamer/gstmeta.html?gi-language=c#GstMetaFlags">GstMetaFlags</a>
 */
public enum GstMetaFlags implements NativeEnum<GstMetaFlags> {

    GST_META_FLAG_NONE(0), // no flags
    GST_META_FLAG_READONLY(1), // metadata should not be modified
    GST_META_FLAG_POOLED(2), // metadata is managed by a bufferpool
    GST_META_FLAG_LOCKED(4), // metadata should not be removed
    GST_META_FLAG_LAST(65536);

    private final int value;

    GstMetaFlags(int value) {
        this.value = value;
    }

    /**
     * Gets the integer value of the enum.
     *
     * @return The integer value for this enum.
     */
    @Override
    public int intValue() {
        return value;
    }


}
