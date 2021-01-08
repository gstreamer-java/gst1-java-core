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

import com.sun.jna.Pointer;

/**
 * GstMessage pointer.
 */
public class GstMessagePtr extends GstMiniObjectPtr {

    private static final int TYPE_OFFSET;
    private static final int SRC_OFFSET;
    
    static {
        GstMessageAPI.MessageStruct struct = new GstMessageAPI.MessageStruct();
        TYPE_OFFSET = struct.typeOffset();
        SRC_OFFSET = struct.srcOffset();
    }
    
    
    public GstMessagePtr() {
    }

    public GstMessagePtr(Pointer ptr) {
        super(ptr);
    }
    
    public int getMessageType() {
        return getPointer().getInt(TYPE_OFFSET);
    }

    public GstObjectPtr getSource() {
        Pointer raw = getPointer().getPointer(SRC_OFFSET);
        return raw == null ? null : new GstObjectPtr(raw);
    }
    
    
}
