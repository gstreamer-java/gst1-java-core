/* 
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

package org.gstreamer.lowlevel;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * GstIterator functions
 */
public interface GstIteratorAPI extends com.sun.jna.Library {
    GstIteratorAPI GSTITERATOR_API = GstNative.load(GstIteratorAPI.class);

    void gst_iterator_free(Pointer iter);
    int gst_iterator_next(Pointer iter, PointerByReference next);
    void gst_iterator_resync(Pointer iter);
}
