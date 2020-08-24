package org.freedesktop.gstreamer.meta;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.glib.Natives;
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
