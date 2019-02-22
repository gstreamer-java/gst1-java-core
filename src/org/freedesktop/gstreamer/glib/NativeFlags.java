/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer.glib;

import java.util.EnumSet;

/**
 * Interface for enums that represent native bit flags.
 * @param <T> type of flag enum
 */
public interface NativeFlags<T extends Enum<T>> extends NativeEnum<T> {

    public static <FLAG extends Enum<FLAG> & NativeFlags<FLAG>> int toInt(EnumSet<FLAG> flags) {
        int ret = 0;
        for (FLAG flag : flags) {
            ret |= flag.intValue();
        }
        return ret;
    }

    public static <FLAG extends Enum<FLAG> & NativeFlags<FLAG>> EnumSet<FLAG>
            fromInt(Class<FLAG> type, int val) {
        EnumSet<FLAG> set = EnumSet.allOf(type);
        set.removeIf(f -> ((val & f.intValue()) == 0));
        return set;
    }

}
