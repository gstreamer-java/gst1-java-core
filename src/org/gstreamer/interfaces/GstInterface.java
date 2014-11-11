/* 
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2008 Wayne Meissner
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

package org.gstreamer.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.gstreamer.Element;
import org.gstreamer.GObject;
import org.gstreamer.lowlevel.GType;
import org.gstreamer.lowlevel.NativeValue;
import org.gstreamer.lowlevel.GlibAPI.GList;

import com.sun.jna.Pointer;

import static org.gstreamer.lowlevel.GstInterfacesAPI.GSTINTERFACES_API;

/**
 * Base type for all gstreamer interface proxies
 */
public class GstInterface extends NativeValue {
    protected final Pointer handle;
    protected final Element element;
    protected GstInterface(Element element, GType type) {
//        if (!GSTINTERFACES_API.gst_element_implements_interface(element, type)) {
//            throw new IllegalArgumentException("Element does not implement interface");
//        }
        this.element = element;
//        handle = GSTINTERFACES_API.gst_implements_interface_cast(element, type);
        handle = element.getNativeAddress();
    }
    protected Object nativeValue() {
        return handle;
    }
    public Element getElement() {
    	return element;
    }
    
    protected interface ListElementCreator<E> {
        E create(Pointer pointer);
    }
    
    /**
     * Build a {@link java.util.List} of {@link Object} from the native GList.
     * @param glist The native list to get the objects from.
     * @param creator The proxy class to wrap the list elements in.
     * @return The converted list.
     */
    protected <T extends GObject> List<T> objectList(GList glist, ListElementCreator<T> creator) {
        List<T> list = new ArrayList<T>();
        GList next = glist;
        while (next != null) {
            if (next.data != null) {
                list.add(creator.create(next.data));
            }
            next = next.next();   
        }
        return list;
    }
}
