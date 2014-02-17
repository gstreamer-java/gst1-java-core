/* 
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

package org.gstreamer.lowlevel;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.NativeLibrary;

/**
 *
 * @author wayne
 */
public class GFunctionMapper implements com.sun.jna.FunctionMapper {
    private final static List<String> stripPrefixes = Arrays.asList("ptr_");

    public String getFunctionName(NativeLibrary library, Method method) {

        String name = method.getName();
        for (String prefix : stripPrefixes) {
            if (name.startsWith(prefix)) {
                return name.substring(prefix.length());
            }
        }
        // Default to just returning the name
        return name;
    }
}
