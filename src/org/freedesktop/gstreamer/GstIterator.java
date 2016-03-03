/* 
 * Copyright (c) 2016 Neil C Smith
 * Copyright (c) 2009 Levente Farkas
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

package org.freedesktop.gstreamer;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.NativeObject;
import org.freedesktop.gstreamer.lowlevel.GstIteratorAPI;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GValueAPI;
import org.freedesktop.gstreamer.lowlevel.GstTypes;

/**
 *
 */
class GstIterator<T extends NativeObject> extends NativeObject implements java.lang.Iterable<T> {
    private static final GstIteratorAPI gst = GstNative.load(GstIteratorAPI.class);

    private final GType gtype;
    
    GstIterator(Pointer ptr, Class<T> cls) {
        super(initializer(ptr));
        gtype = GstTypes.typeFor(cls);
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl();
    }
    
    @Override
    protected void disposeNativeHandle(Pointer ptr) {
        gst.gst_iterator_free(ptr);
    }
    
    public List<T> asList() {
        List<T> list = new LinkedList<T>();
        for (T t : this) {
            list.add(t);
        }
        return Collections.unmodifiableList(list);
    }
    
    class IteratorImpl implements java.util.Iterator<T> {
      
        final GValueAPI.GValue gValue;
        
        T next;
        
        IteratorImpl() {
            gValue = new GValueAPI.GValue(gtype);
            next = getNext();
        }
        
        private T getNext() {
            if (gst.gst_iterator_next(handle(), gValue) == 1) {
                next = (T) gValue.getValue();
                // reset cached structure or we get a memory leak
                GValueAPI.GVALUE_API.g_value_reset(gValue);
                return next;
            } else {
                return null;
            }
        }
        
        @Override
        public boolean hasNext() {
            return next != null;
        }
        
        @Override
        public T next() {
            T result = next;
            next = getNext();
            return result;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Items cannot be removed.");
        }
    }
}
