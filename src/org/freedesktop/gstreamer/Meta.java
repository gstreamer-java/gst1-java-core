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
package org.freedesktop.gstreamer;

import java.util.Objects;
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.lowlevel.GPointer;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GstMetaPtr;

/**
 * Base for all metadata types added to a Buffer.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/documentation/gstreamer/gstmeta.html"
 * >https://gstreamer.freedesktop.org/documentation/gstreamer/gstmeta.html</a>
 * <p>
 */
public class Meta extends NativeObject {

    /**
     * Create Meta from Initializer.
     *
     * @param init initializer.
     */
    protected Meta(Initializer init) {
        this(new Handle(init.ptr.as(GstMetaPtr.class, GstMetaPtr::new),
                init.ownsHandle));
    }

    /**
     * Create Meta from Handle.
     *
     * @param handle native object handle.
     */
    protected Meta(Handle handle) {
        super(handle);
    }

    @Override
    public String toString() {
        GstMetaPtr pointer = (GstMetaPtr)this.getPointer();
        return "[meta : gType=" + pointer.getGType() + "]";
    }

    /**
     * NativeObject.Handle implementation.
     */
    protected static class Handle extends NativeObject.Handle {

        /**
         * Create Handle.
         *
         * @param ptr pointer to underlying native GstMeta.
         * @param ownsHandle
         */
        public Handle(GstMetaPtr ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
        }

    }

    /**
     * API for Meta subclass. Used for querying from Buffer.
     * <p>
     * The relevant API will usually be available as a public static final field
     * on the implementation class.
     * <p>
     * The API type reflects two distinct types (api and implementation) used in
     * the underlying GStreamer GstMetaInfo.
     * <p>
     * See upstream documentation at
     * <a href="https://gstreamer.freedesktop.org/documentation/gstreamer/gstmeta.html#GstMetaInfo"
     * >https://gstreamer.freedesktop.org/documentation/gstreamer/gstmeta.html#GstMetaInfo</a>
     * <p>
     *
     * @param <T> implementation type
     */
    public static final class API<T extends Meta> {

        private final Class<T> implClass;
        private final String apiTypeName;

        private GType apiType;

        /**
         * Create an API for the given implementation type.
         *
         * @param impl class implementing the API, must be registered to the
         * underlying implementation GType
         * @param api name of the underlying API GType
         */
        public API(Class<T> impl, String api) {
            this.implClass = Objects.requireNonNull(impl);
            this.apiTypeName = Objects.requireNonNull(api);
            apiType = GType.INVALID;
        }

        Class<T> getImplClass() {
            return implClass;
        }

        GType getAPIGType() {
            GType type = apiType;
            if (type == GType.INVALID) {
                type = GType.valueOf(apiTypeName);
                apiType = type;
            }
            return type;
        }

    }
}
