/*
 * Copyright (c) 2020 Christophe Lafolet
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.jna.FromNativeContext;
import com.sun.jna.IntegerType;
import com.sun.jna.Native;

import static org.freedesktop.gstreamer.lowlevel.GObjectAPI.GOBJECT_API;

/**
 *
 */
@SuppressWarnings("serial")
public class GType extends IntegerType {
    /** Size of a native <code>GType</code>, in bytes. */
    public static final int SIZE = Native.SIZE_T_SIZE;
    private static final int G_TYPE_FUNDAMENTAL_SHIFT = 2;

    private static final Map<Long, GType> gTypeByValues = new ConcurrentHashMap<Long, GType>();
    private static final Map<String, GType> gTypeByNames = new ConcurrentHashMap<String, GType>();
    
    public static final GType INVALID = init(0);
    public static final GType NONE = init(1);
    public static final GType INTERFACE = init(2);
    public static final GType CHAR = init(3);
    public static final GType UCHAR = init(4);
    public static final GType BOOLEAN = init(5);
    public static final GType INT = init(6);
    public static final GType UINT = init(7);
    public static final GType LONG = init(8);
    public static final GType ULONG = init(9);
    public static final GType INT64 = init(10);
    public static final GType UINT64 = init(11);
    public static final GType ENUM = init(12);
    public static final GType FLAGS = init(13);
    public static final GType FLOAT = init(14);
    public static final GType DOUBLE = init(15);
    public static final GType STRING = init(16);
    public static final GType POINTER = init(17);
    public static final GType BOXED = init(18);
    public static final GType PARAM = init(19);
    public static final GType OBJECT = init(20);
    public static final GType VARIANT = init(21);

    // descriptions set in lazy
    private GType parent;
    private String name;
    
    /**
     * @param value the fundamental type number.
     * @return the GType
     */
    private static GType init(int value) {
        return valueOf(value << G_TYPE_FUNDAMENTAL_SHIFT);
    }
    
    protected GType(long t) {
    	super(SIZE, t);
    }

    /**
     * Default constructor needed by NativeMappedConverter
     */
    public GType() {
        this(0L);
    }

    public static GType valueOf(long value) {
    	return gTypeByValues.computeIfAbsent(value, GType::new);
    }

    public static GType valueOf(String typeName) {
    	GType result = gTypeByNames.get(typeName);
    	if (result == null) {
    		result = GOBJECT_API.g_type_from_name(typeName);
    		if (result.equals(INVALID)) {
    			// no type has been registered yet
    		} else {
        		gTypeByNames.put(typeName, result);
        		result.name = typeName;
    		}
    	}
    	return result;
    }
    
    // FIXME : to move in GstTypes
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
        return GType.valueOf(((Number) nativeValue).longValue());
    }

    /**
     * Return the direct parent type of the current type.
     * @return the direct parent type or INVALID if the type has no parent
     */
    public GType getParentType() {
    	if (this.parent == null) 
    		this.parent = GOBJECT_API.g_type_parent(this);
    	return this.parent;
    }
    
    public String getTypeName() {
    	if (this.name == null) {
    		this.name = GOBJECT_API.g_type_name(this);
    		gTypeByNames.put(this.name, this);
    	}
    	return this.name;    	
    }
    
    @Override
    public String toString() {
        String gtypeName = this.equals(INVALID) ? "invalid" : this.getTypeName();
    	return "[" + gtypeName + ":" + super.longValue() + "]";
    }
}
