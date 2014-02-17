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

package org.gstreamer.elements;

import org.gstreamer.Element;

/**
 * A gstreamer element for data queue.
 */
public class Queue2 extends Element {
    public static final String GST_NAME = "queue2";
    public static final String GTYPE_NAME = "GstQueue2";

    public Queue2(String name) {
        this(makeRawElement(GST_NAME, name));
    }  

    public Queue2(Initializer init) {
        super(init);
    }
}
