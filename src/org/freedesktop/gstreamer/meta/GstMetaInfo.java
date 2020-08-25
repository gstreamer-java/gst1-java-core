package org.freedesktop.gstreamer.meta;

import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.lowlevel.GPointer;
import org.freedesktop.gstreamer.lowlevel.GType;
import static org.freedesktop.gstreamer.lowlevel.GstMetaApi.GstMetaInfoStruct;


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
 *
 * The GstMetaInfo provides information about a specific metadata structure.
 *
 * @see <a href="https://gstreamer.freedesktop.org/documentation/gstreamer/gstmeta.html?gi-language=c#GstMetaInfo">GstMetaInfo</a>
 */
public class GstMetaInfo extends NativeObject {

    public static final String GTYPE_NAME = "GstMetaInfo";
    private final GstMetaInfoStruct metaStruct;


    GstMetaInfo(Initializer init) {
        super(new Handle(init.ptr, init.ownsHandle));
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


    private static final class Handle extends NativeObject.Handle {

        /**
         * Construct a Handle for the supplied native reference.
         *
         * @param ptr           native reference
         * @param ownsReference whether the Handle owns the native reference and
         */
        public Handle(GPointer ptr, boolean ownsReference) {
            super(ptr, ownsReference);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            // Meta info is destructed by nested function
        }
    }

}
