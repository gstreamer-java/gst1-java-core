package org.freedesktop.gstreamer.meta;

import com.sun.jna.Pointer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.lowlevel.GType;
import static org.freedesktop.gstreamer.glib.Natives.registration;
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
 * Factory for creating metadata
 */
public class MetaDataFactory {
    private static final Map<Class<? extends Meta>, MetaDataCrate> metaMapping;

    static {
        metaMapping = new HashMap<>();
        metaMapping.put(VideoTimeCodeMeta.class, new MetaDataCrate(GST_META_API::gst_video_time_code_meta_api_get_type, VideoTimeCodeMeta::new));
        //metaMapping.put(video_interest_meta_class,new MetaDataCrate(GST_META_API::gst_video_region_of_interest_meta_api_get_type,ideo_interest_meta_class::new);
        //metaMapping.put(video_meta_class,GST_META_API::gst_video_meta_api_get_type,video_meta_class::new);
        //metaMapping.put(video_texture_meta_class,GST_META_API::gst_video_gl_texture_upload_meta_api_get_type,video_texture_meta_class::new);
        //metaMapping.put(video_crop_meta_class,GST_META_API::gst_video_crop_meta_api_get_type,video_crop_meta_class::new);
    }

    /**
     * Return GType for selected Metadata class
     *
     * @param clazz metadata class
     * @return Return GStreamer GType related to Java metadata class
     * @throws IllegalArgumentException throws exception in case that selected type is not registered in factory
     */
    public GType getGType(Class<? extends Meta> clazz) {
        MetaDataCrate crate = metaMapping.get(clazz);
        if (crate != null) {
            return crate.getGType().get();
        }
        throw new IllegalArgumentException("Unknown meta class: " + clazz);
    }

    /**
     * Create Java class instance of metadata based on type of class
     *
     * @param clazz   selected type of metadata
     * @param pointer pointer to memory with gstreamer metadata
     * @return return create metadata instance filled with data from gstreamer
     * @throws IllegalArgumentException throws exception in case that selected type is not registered in factory
     */
    public <M extends Meta> M getInstance(Class<M> clazz, Pointer pointer) {
        MetaDataCrate crate = metaMapping.get(clazz);
        if (crate != null) {
            return (M) crate.getCreator().apply(pointer);
        }
        throw new IllegalArgumentException("Unknown meta class: " + clazz);
    }

    /**
     * Helper class holding function for obtain GType and new instance some implementation of {@link Meta}
     */
    public static final class MetaDataCrate {
        private final Supplier<GType> gTypeSup;
        private final Function<Pointer, ? extends Meta> creator;

        public MetaDataCrate(Supplier<GType> gTypeSup, Function<Pointer, ? extends Meta> creator) {
            this.gTypeSup = gTypeSup;
            this.creator = creator;
        }

        public Supplier<GType> getGType() {
            return gTypeSup;
        }

        public Function<Pointer, ? extends Meta> getCreator() {
            return creator;
        }
    }

    /**
     * Register new metadata. This method enable register new type metadata outside of this library
     *
     * @param clazz metadata class
     * @param crate create with GType and function for creating new metadata instance
     */
    public static void registerMetadata(Class<? extends Meta> clazz, MetaDataCrate crate) {
        metaMapping.put(clazz, crate);
    }

    public static final class Types implements NativeObject.TypeProvider{

        @Override
        public Stream<NativeObject.TypeRegistration<?>> types() {
            return Stream.of(
                    registration(VideoTimeCodeMeta.class,
                            VideoTimeCodeMeta.GTYPE_NAME,
                            VideoTimeCodeMeta::new),
                    registration(MetaInfo.class,
                            MetaInfo.GTYPE_NAME,
                            MetaInfo::new));
        }
    }

}
