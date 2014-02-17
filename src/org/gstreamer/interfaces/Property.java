/*
 * Copyright (c) 2008 Andres Colubri
 * Copyright (c) 2008 Wayne Meissner
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

package org.gstreamer.interfaces;

import org.gstreamer.GObject;
import org.gstreamer.lowlevel.GObjectAPI;

import com.sun.jna.Pointer;

/**
 *
 */
public class Property extends GObject {
    private final GObjectAPI.GParamSpec spec;
    
    /**
     * For internal gstreamer-java use only
     * 
     * @param init initialization data
     */
    public Property(Initializer init) {
        super(init);
        throw new IllegalArgumentException("Cannot instantiate this class");
    }
    
    Property(Pointer ptr, boolean needRef, boolean ownsHandle) {
        super(initializer(ptr, needRef, ownsHandle));
        spec = new GObjectAPI.GParamSpec(ptr);
    }

    public String getName() {
        return spec.g_name;
    }
    
    GObjectAPI.GParamSpec getSpec() {
        return spec;
    }
}
