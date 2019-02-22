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

import java.lang.reflect.Field;
import java.util.EnumSet;

import org.freedesktop.gstreamer.lowlevel.annotations.DefaultEnumValue;
import org.freedesktop.gstreamer.glib.NativeEnum;

/**
 * Maps to and from native int and an Enum value.
 * @author wayne
 */
public class EnumMapper {
    private static final EnumMapper mapper = new EnumMapper();
    public static EnumMapper getInstance() {
        return mapper;
    }
    
    public int intValue(Enum<?> value) {
        return value instanceof NativeEnum ? ((NativeEnum) value).intValue() : value.ordinal();
    }
    public <E extends Enum<E>> E valueOf(int value, Class<E> enumClass) {
        //
        // Just loop around all the enum values and find one that matches.
        // Storing the values in a Map might be faster, but by the time you deal
        // with locking overhead, its hardly worth it for small enums.
        // 
        if (NativeEnum.class.isAssignableFrom(enumClass)) {
            for (E e : EnumSet.allOf(enumClass)) {
                if (((NativeEnum) e).intValue() == value) {
                    return e;
                }
            }
        } else {
            for (E e : EnumSet.allOf(enumClass)) {
                if (e.ordinal() == value) {
                    return e;
                }
            }
        }
        //
        // No value found - try to find the default value for unknown values.
        // This is useful for enums that aren't fixed in stone and/or where you
        // don't want to throw an Exception for an unknown value.
        //
        try {
            for (Field f : enumClass.getDeclaredFields()) {
                if (f.getAnnotation(DefaultEnumValue.class) != null) {
                    return Enum.valueOf(enumClass, f.getName());
                }
            }
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException ex) {      
            //
            // No default, so just give up and throw an exception
            //
            throw new IllegalArgumentException("No known Enum mapping for "
                    + enumClass.getName() + " value=" + value);
        }
    }
}
