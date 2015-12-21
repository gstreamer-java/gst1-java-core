/*
 * Copyright (c) 2015 Christophe Lafolet
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

import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValue;
import org.freedesktop.gstreamer.lowlevel.GstIteratorAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.GstTypes;
import org.freedesktop.gstreamer.lowlevel.NativeObject;

import com.sun.jna.Pointer;

/**
 *
 */
class GstIterator<T extends NativeObject> extends NativeObject implements java.lang.Iterable<T> {
    private static final GstIteratorAPI gst = GstNative.load(GstIteratorAPI.class);

    private final Class<T> objectType;
    GstIterator(Pointer ptr, Class<T> cls) {
        super(NativeObject.initializer(ptr));
        this.objectType = cls;
    }

    @Override
	public Iterator<T> iterator() {
        return new IteratorImpl();
    }

    @Override
	protected void disposeNativeHandle(Pointer ptr) {
        GstIterator.gst.gst_iterator_free(ptr);
    }

    public List<T> asList() {
        List<T> list = new LinkedList<T>();
        for (T t : this) {
            list.add(t);
        }
        return Collections.unmodifiableList(list);
    }

    class IteratorImpl implements java.util.Iterator<T> {
        T next;
        IteratorImpl() {
            this.next = this.getNext();
        }
        private T getNext() {
            GValue value = new GValue(GstTypes.typeFor(GstIterator.this.objectType));
            if (GstIterator.gst.gst_iterator_next(GstIterator.this.handle(), value) == 1) {
            	Object obj = value.getValue();
            	return (T)obj;
            }
            return null;
        }
        @Override
		public boolean hasNext() {
            return this.next != null;
        }

        @Override
		public T next() {
            T result = this.next;
            this.next = this.getNext();
            return result;
        }

        @Override
		public void remove() {
            throw new UnsupportedOperationException("Items cannot be removed.");
        }
    }
}
