/*
 * Copyright (c) 2019 Neil C Smith
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
import com.sun.jna.PointerType;
import java.util.function.Function;

/**
 * Base GLib pointer
 */
public class GPointer extends PointerType {
    
    public GPointer() {
    }
    
    public GPointer(Pointer ptr) {
        super(ptr);
    }
    
    public <T extends GPointer> T as(Class<T> cls, Function<Pointer, T> converter) {
        if (cls.isInstance(this)) {
            return cls.cast(this);
        } else {
            return converter.apply(this.getPointer());
        }
    }
    
}
