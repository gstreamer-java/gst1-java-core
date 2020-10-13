/*
 * Copyright (c) 2020 Neil C Smith
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
 */
package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.glib.NativeFlags;

 /**
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/documentation/gstreamer/gstmeta.html#GstMetaFlags"
 * >https://gstreamer.freedesktop.org/documentation/gstreamer/gstmeta.html#GstMetaFlags</a>
 */
/*public*/ enum MetaFlags implements NativeFlags<MetaFlags> {

//    /**
//     * no flags
//     */
//    GST_META_FLAG_NONE(0),
    /**
     * metadata should not be modified
     */
    GST_META_FLAG_READONLY(1 << 0),
    /**
     * metadata is managed by a bufferpool
     */
    GST_META_FLAG_POOLED(1 << 1),
    /**
     * metadata should not be removed
     */
    GST_META_FLAG_LOCKED(1 << 2),
//    /**
//     * additional flags can be added starting from this flag.
//     */
//    GST_META_FLAG_LAST(1 << 16)
//    
    ;

    private final int value;

    MetaFlags(int value) {
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
