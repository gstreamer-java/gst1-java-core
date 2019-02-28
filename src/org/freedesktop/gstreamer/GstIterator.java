/* 
 * Copyright (c) 2019 Neil C Smith
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

import com.sun.jna.Pointer;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GValueAPI;
import org.freedesktop.gstreamer.lowlevel.GstTypes;
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.lowlevel.GPointer;

import static org.freedesktop.gstreamer.lowlevel.GstIteratorAPI.GSTITERATOR_API;

/**
 *
 */
class GstIterator<T extends NativeObject> extends NativeObject implements java.lang.Iterable<T> {

    private final GType gtype;

    GstIterator(Pointer ptr, Class<T> cls) {
        super(new Handle(new GPointer(ptr), true));
        gtype = GstTypes.typeFor(cls);
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl();
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
            if (GSTITERATOR_API.gst_iterator_next(getRawPointer(), gValue) == 1) {
                T result = (T) gValue.getValue();
                // reset cached structure or we get a memory leak
                gValue.reset();
                return result;
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
    
    private static final class Handle extends NativeObject.Handle {

        public Handle(GPointer ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            GSTITERATOR_API.gst_iterator_free(ptr.getPointer());
        }
        
    }
}
