/*
 * Copyright (c) 2020 Neil C Smith
 * 
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License version 3 only, as published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License version 3 along with
 * this work. If not, see <http://www.gnu.org/licenses/>.
 */
package org.freedesktop.gstreamer.lowlevel;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * Base GstMeta pointer
 */
public class GstMetaPtr extends GTypedPtr {
    
    private static final int INFO_OFFSET;
    private static final int IMPL_TYPE_OFFSET;
    
    static {
        INFO_OFFSET = new GstMetaAPI.GstMetaStruct().infoOffset();
        IMPL_TYPE_OFFSET = new GstMetaAPI.GstMetaInfoStruct().typeOffset();
    }

    public GstMetaPtr() {
    }

    public GstMetaPtr(Pointer ptr) {
        super(ptr);
    }

    @Override
    public GType getGType() {
        // Quick getter for GType without allocation
        Pointer metaInfo = getPointer().getPointer(INFO_OFFSET);
        if (Native.SIZE_T_SIZE == 8) {
            return GType.valueOf(metaInfo.getLong(IMPL_TYPE_OFFSET));
        } else if (Native.SIZE_T_SIZE == 4) {
            return GType.valueOf(((long) metaInfo.getInt(IMPL_TYPE_OFFSET)) & 0xffffffffL);
        } else {
            throw new IllegalStateException("SIZE_T size not supported: " + Native.SIZE_T_SIZE);
        }
    }
    
    public GType getAPIGType() {
        // Quick getter for GType without allocation
        Pointer metaInfo = getPointer().getPointer(INFO_OFFSET);
        if (Native.SIZE_T_SIZE == 8) {
            return GType.valueOf(metaInfo.getLong(0));
        } else if (Native.SIZE_T_SIZE == 4) {
            return GType.valueOf(((long) metaInfo.getInt(0)) & 0xffffffffL);
        } else {
            throw new IllegalStateException("SIZE_T size not supported: " + Native.SIZE_T_SIZE);
        }
    }

}
