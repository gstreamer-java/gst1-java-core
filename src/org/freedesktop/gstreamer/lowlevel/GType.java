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

import com.sun.jna.FromNativeContext;
import com.sun.jna.IntegerType;
import com.sun.jna.Native;

/**
 *
 */
@SuppressWarnings("serial")
public class GType extends IntegerType {
    /** Size of a native <code>GType</code>, in bytes. */
    public static final int SIZE = Native.SIZE_T_SIZE;

    private static final GType[] cache;
    static {
        cache = new GType[22];
        for (int i = 0; i < cache.length; ++i) {
            cache[i] = new GType(i << 2);
        }        
    };
    
    public static final GType INVALID = init(0, "INVALID");
    public static final GType NONE = init(1, "NONE");
    public static final GType INTERFACE = init(2, "INTERFACE");
    public static final GType CHAR = init(3, "CHAR");
    public static final GType UCHAR = init(4, "UCHAR");
    public static final GType BOOLEAN = init(5, "BOOLEAN");
    public static final GType INT = init(6, "INT");
    public static final GType UINT = init(7, "UINT");
    public static final GType LONG = init(8, "LONG");
    public static final GType ULONG = init(9, "ULONG");
    public static final GType INT64 = init(10, "INT64");
    public static final GType UINT64 = init(11, "UINT64");
    public static final GType ENUM = init(12, "ENUM");
    public static final GType FLAGS = init(13, "FLAGS");
    public static final GType FLOAT = init(14, "FLOAT");
    public static final GType DOUBLE = init(15, "DOUBLE");
    public static final GType STRING = init(16, "STRING");
    public static final GType POINTER = init(17, "POINTER");
    public static final GType BOXED = init(18, "BOXED");
    public static final GType PARAM = init(19, "PARAM");
    public static final GType OBJECT = init(20, "OBJECT");
    public static final GType VARIANT = init(21, "VARIANT");

    private final String description;

    private static GType init(int v, String description) {
        return valueOf(v << 2, description);
    }
    
    public GType(long t, String description) {
    	super(SIZE, t);
    	this.description = description;
    }
    
    public GType(long t) {
        this(t, "?");
    }
    
    public GType() {
        this(0L);
    }
    
    public static GType valueOf(long value) {
    	return valueOf(value, "?");
    }
    
    public static GType valueOf(long value, String description) {
        if (value >= 0 && (value >> 2) < cache.length) {
            return cache[(int)value >> 2];
        }
        return new GType(value, description);
    }
    
    public static GType valueOf(Class<?> javaType) {
        if (Integer.class == javaType || int.class == javaType) {
            return INT;
        } else if (Long.class == javaType || long.class == javaType) {
            return INT64;
        } else if (Float.class == javaType || float.class == javaType) {
            return FLOAT;
        } else if (Double.class == javaType || double.class == javaType) {
            return DOUBLE;
        } else if (String.class == javaType) {
            return STRING;
        } else {
            throw new IllegalArgumentException("No GType for " + javaType);
        }
    }
    
    @Override
    public Object fromNative(Object nativeValue, FromNativeContext context) {
        return valueOf(((Number) nativeValue).longValue(), "");
    }    
    
    public String toString() {
    	return ("[" + description + ":" + longValue() + "]");
    }
}
