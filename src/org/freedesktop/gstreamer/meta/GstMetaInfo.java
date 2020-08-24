package org.freedesktop.gstreamer.meta;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GType;
import static org.freedesktop.gstreamer.lowlevel.GstMetaApi.GstMetaInfoStruct;

/**
 * The GstMetaInfo provides information about a specific metadata structure.
 *
 * @author Jokertwo
 * @see <a href="https://gstreamer.freedesktop.org/documentation/gstreamer/gstmeta.html?gi-language=c#GstMetaInfo">GstMetaInfo</a>
 */
public class GstMetaInfo extends MiniObject {

    public static final String GTYPE_NAME = "GstMetaInfo";
    private GstMetaInfoStruct metaStruct;

    public GstMetaInfo(Pointer pointer) {
        this(Natives.initializer(pointer, false, false));
    }


    GstMetaInfo(Initializer init) {
        super(init);
        metaStruct = new GstMetaInfoStruct(getRawPointer());
    }

    /**
     * Type identifying the implementor of the api
     *
     * @return return dynamic GType
     */
    public GType getGType() {
        return this.metaStruct.type;
    }

    /**
     * Tag identifying the metadata structure and api
     *
     * @return return dynamic api type
     */
    public GType getApiType() {
        return this.metaStruct.api;
    }

}
