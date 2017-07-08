/*
 * Copyright (c) 2016 Christophe Lafolet
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

import org.freedesktop.gstreamer.lowlevel.GstBufferAPI;

import com.sun.jna.ptr.PointerByReference;

class GstMetaIterator implements java.lang.Iterable<Meta> {
	private final Buffer buffer;
	
	GstMetaIterator(final Buffer aBuffer) {
		this.buffer = aBuffer;
	}
	
    @Override
	public Iterator<Meta> iterator() {
        return new MetaIteratorImpl(this.buffer);
    }
    
    public List<Meta> asList() {
        List<Meta> list = new LinkedList<Meta>();
        for (Meta t : this) {
            list.add(t);
        }
        return Collections.unmodifiableList(list);
    }
        
    private static class MetaIteratorImpl implements java.util.Iterator<Meta> {
    	private final PointerByReference state;
    	private final Buffer buffer;
        private Meta next;
        
        MetaIteratorImpl(final Buffer aBuffer) {
    		state = new PointerByReference();
    		buffer = aBuffer;
            next = getNext();
        }
        
    	@Override
    	public boolean hasNext() {
            return next != null;
    	}

        private Meta getNext() {
        	return GstBufferAPI.GSTBUFFER_API.gst_buffer_iterate_meta(this.buffer, this.state);
        }        

        @Override
        public Meta next() {
            Meta result = next;
            next = getNext();
            return result;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Items cannot be removed.");
        }
    }

}