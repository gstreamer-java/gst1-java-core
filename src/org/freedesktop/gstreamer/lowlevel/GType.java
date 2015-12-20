/*
 * Copyright (c) 2015 Christophe Lafolet
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

    private static final GObjectAPI gst = GObjectAPI.GOBJECT_API;

    private static final GType[] cache;
    static {
        cache = new GType[22];
        for (int i = 0; i < GType.cache.length; ++i) {
            GType.cache[i] = new GType(i << 2);
        }
    };

    public static final GType INVALID = GType.init(0);
    public static final GType NONE = GType.init(1);
    public static final GType INTERFACE = GType.init(2);
    public static final GType CHAR = GType.init(3);
    public static final GType UCHAR = GType.init(4);
    public static final GType BOOLEAN = GType.init(5);
    public static final GType INT = GType.init(6);
    public static final GType UINT = GType.init(7);
    public static final GType LONG = GType.init(8);
    public static final GType ULONG = GType.init(9);
    public static final GType INT64 = GType.init(10);
    public static final GType UINT64 = GType.init(11);
    public static final GType ENUM = GType.init(12);
    public static final GType FLAGS = GType.init(13);
    public static final GType FLOAT = GType.init(14);
    public static final GType DOUBLE = GType.init(15);
    public static final GType STRING = GType.init(16);
    public static final GType POINTER = GType.init(17);
    public static final GType BOXED = GType.init(18);
    public static final GType PARAM = GType.init(19);
    public static final GType OBJECT = GType.init(20);
    public static final GType VARIANT = GType.init(21);

    private static GType init(int v) {
        return GType.valueOf(v << 2);
    }

    public GType(long t) {
    	super(GType.SIZE, t);
    }


    public GType() {
        this(0L);
    }

    public static GType valueOf(long value) {
        if (value >= 0 && value >> 2 < GType.cache.length) {
            return GType.cache[(int)value >> 2];
        }
        return new GType(value);
    }

    public static GType valueOf(Class<?> javaType) {
        if (Integer.class == javaType || int.class == javaType) {
            return GType.INT;
        } else if (Long.class == javaType || long.class == javaType) {
            return GType.INT64;
        } else if (Float.class == javaType || float.class == javaType) {
            return GType.FLOAT;
        } else if (Double.class == javaType || double.class == javaType) {
            return GType.DOUBLE;
        } else if (String.class == javaType) {
            return GType.STRING;
        } else {
            throw new IllegalArgumentException("No GType for " + javaType);
        }
    }

    @Override
    public Object fromNative(Object nativeValue, FromNativeContext context) {
        return GType.valueOf(((Number) nativeValue).longValue());
    }

    @Override
	public String toString() {
    	return "[" + GType.gst.g_type_name(this) + ":" + this.longValue() + "]";
    }
}
