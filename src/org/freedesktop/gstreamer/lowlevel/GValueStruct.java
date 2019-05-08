/*
 * Copyright (c) 2007 Wayne Meissner
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

package org.freedesktop.gstreamer.lowlevel;

import com.sun.jna.Structure;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class GValueStruct extends Structure {
    public volatile int g_type;
    /** Creates a new instance of GValueStruct */
    public GValueStruct() {
    }

    @Override
    protected List<String> getFieldOrder() {
        return Collections.singletonList("g_type");
    }
}
