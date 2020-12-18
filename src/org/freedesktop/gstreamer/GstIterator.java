/* 
 * Copyright (c) 2021 Neil C Smith
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
package org.freedesktop.gstreamer;

import java.util.ArrayList;
import java.util.List;

import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.lowlevel.GstIteratorPtr;
import org.freedesktop.gstreamer.lowlevel.GstTypes;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GValueAPI;

import static org.freedesktop.gstreamer.lowlevel.GstIteratorAPI.GSTITERATOR_API;

/**
 * Utility class for working with gstiterator.
 */
class GstIterator {
    
    static <T extends NativeObject> List<T> asList(GstIteratorPtr iter, Class<T> type) {
        final GType gtype = GstTypes.typeFor(type);
        final GValueAPI.GValue gValue = new GValueAPI.GValue(gtype);
        List<T> list = new ArrayList<>();
        while (GSTITERATOR_API.gst_iterator_next(iter, gValue) == 1) {
            list.add((T) gValue.getValue());
        }
        gValue.reset();
        GSTITERATOR_API.gst_iterator_free(iter);
        return list;
    }
    
}
