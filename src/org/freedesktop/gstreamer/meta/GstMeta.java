package org.freedesktop.gstreamer.meta;

import java.util.stream.Stream;
import org.freedesktop.gstreamer.glib.NativeObject;
import static org.freedesktop.gstreamer.glib.Natives.registration;

/**
 * @author Jokertwo
 */
public class GstMeta implements NativeObject.TypeProvider {

    @Override
    public Stream<NativeObject.TypeRegistration<?>> types() {
        return Stream.of(
                registration(GstVideoTimeCodeMeta.class,
                        GstVideoTimeCodeMeta.GTYPE_NAME,
                        GstVideoTimeCodeMeta::new),
                registration(GstMetaInfo.class,
                        GstMetaInfo.GTYPE_NAME,
                        GstMetaInfo::new));
    }
}
