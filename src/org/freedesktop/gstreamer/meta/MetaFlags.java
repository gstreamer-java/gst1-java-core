package org.freedesktop.gstreamer.meta;

import org.freedesktop.gstreamer.glib.NativeEnum;

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
 * @see <a href="https://docs.gstreamer.com/documentation/gstreamer/gstmeta.html?gi-language=c#GstMetaFlags">GstMetaFlags</a>
 */
public enum MetaFlags implements NativeEnum<MetaFlags> {

    GST_META_FLAG_NONE(0), // no flags
    GST_META_FLAG_READONLY(1), // metadata should not be modified
    GST_META_FLAG_POOLED(2), // metadata is managed by a bufferpool
    GST_META_FLAG_LOCKED(4), // metadata should not be removed
    GST_META_FLAG_LAST(65536);

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
