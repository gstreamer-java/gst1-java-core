/* 
 * Copyright (c) 2019 Neil C Smith
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

package org.freedesktop.gstreamer.interfaces;

import org.freedesktop.gstreamer.lowlevel.GType;

import org.freedesktop.gstreamer.Element;

import org.freedesktop.gstreamer.glib.GObject;

/**
 * Base type for all gstreamer interface proxies
 */
class GstInterface implements GObject.GInterface {
    protected final Element element;
    protected GstInterface(Element element, GType type) {
        this.element = element;
    }

    @Override
    public Element getGObject() {
        return element;
    }
    
}
