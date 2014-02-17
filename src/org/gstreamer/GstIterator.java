/* 
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

package org.gstreamer;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.gstreamer.lowlevel.GstNative;
import org.gstreamer.lowlevel.NativeObject;
import org.gstreamer.lowlevel.GstIteratorAPI;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 *
 */
class GstIterator<T extends NativeObject> extends NativeObject implements java.lang.Iterable<T> {
    private static final GstIteratorAPI gst = GstNative.load(GstIteratorAPI.class);

    private Class<T> objectType;
    GstIterator(Pointer ptr, Class<T> cls) {
        super(initializer(ptr));
        objectType = cls;
    }

    public Iterator<T> iterator() {
        return new IteratorImpl();
    }
    
    protected void disposeNativeHandle(Pointer ptr) {
        gst.gst_iterator_free(ptr);
    }
    public List<T> asList() {
        List<T> list = new LinkedList<T>();
        for (java.util.Iterator<T> it = iterator(); it.hasNext(); ) {
            list.add(it.next());
        }
        return Collections.unmodifiableList(list);
    }
    
    class IteratorImpl implements java.util.Iterator<T> {
        T next;        
        IteratorImpl() {
            next = getNext();
        }
        private T getNext() {
            PointerByReference nextRef = new PointerByReference();
            if (gst.gst_iterator_next(handle(), nextRef) == 1) {                
            	return NativeObject.objectFor(nextRef.getValue(), objectType, -1, true);
            }
            return null;
        }
        public boolean hasNext() {
            return next != null;
        }
        
        public T next() {
            T result = next;
            next = getNext();
            return result;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Items cannot be removed.");
        }
    }
}
