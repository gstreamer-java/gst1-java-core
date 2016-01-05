/* 
 * Copyright (c) 2016 Neil C Smith
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2007, 2008 Wayne Meissner
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

import java.util.HashMap;
import java.util.Map;

import com.sun.jna.Library;

/**
 * A convenience wrapper to aid in loading an API interface.
 */
@SuppressWarnings("serial")
public final class GstNative {

    private GstNative() {}
    
    private static final Map<String, Object> options = new HashMap<String, Object>() {{
        put(Library.OPTION_TYPE_MAPPER, new GTypeMapper());
        put(Library.OPTION_FUNCTION_MAPPER, new GFunctionMapper());
    }};

    // gstreamer runtime library name is gstreamer-1.0.0. The extra .0 will be appended in GNative.loadLibrary().
    // development versions may be gstreamer-1.0, which will be checked if gstreamer-1.0.0 is not found.
    public static <T extends Library> T load(Class<T> interfaceClass) {
        return load("gstreamer-1.0", interfaceClass);
    }

    public static <T extends Library> T load(String libraryName, Class<T> interfaceClass) {
    	try {
            return GNative.loadLibrary(libraryName, interfaceClass, options);
        } catch (UnsatisfiedLinkError ex) {
        	throw ex;
        }
    	
    }
}
