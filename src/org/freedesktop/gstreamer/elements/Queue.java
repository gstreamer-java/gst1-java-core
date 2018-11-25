/* 
 * Copyright (c) 2008 Levente Farkas
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

package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;

/**
 * A gstreamer element for data queue.
 */
public class Queue extends Element {
    public static final String GST_NAME = "queue";
    public static final String GTYPE_NAME = "GstQueue";

    public Queue(String name) {
        this(makeRawElement(GST_NAME, name));
    }  

    public Queue(Initializer init) {
        super(init);
    }
}
