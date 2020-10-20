/*
 * Copyright (c) 2020 Neil C Smith
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
 */
package org.freedesktop.gstreamer.video;

import java.util.stream.Stream;
import org.freedesktop.gstreamer.glib.NativeObject;

import static org.freedesktop.gstreamer.glib.Natives.registration;

/**
 * Utility class for GStreamer Video library.
 */
public final class Video {

    private Video() {
    }

    /**
     * TypeProvider implementation for GStreamer Video library.
     */
    public static class Types implements NativeObject.TypeProvider {

        @Override
        public Stream<NativeObject.TypeRegistration<?>> types() {
            return Stream.of(
                    registration(VideoTimeCodeMeta.class, VideoTimeCodeMeta.GTYPE_NAME,
                            VideoTimeCodeMeta::new)
            );
        }

    }

}
