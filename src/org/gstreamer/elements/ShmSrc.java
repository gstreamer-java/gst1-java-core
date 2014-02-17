/* 
 * Copyright (c) 2009 Levente Farkas
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


/**
 * Send data over shared memory to the matching source
 *
 */
public class ShmSrc extends BaseSink {
    public static final String GST_NAME = "shmsrc";
    public static final String GTYPE_NAME = "GstShmSrc";

    public ShmSrc(String name) {
        this(makeRawElement(GST_NAME, name));
    }

    public ShmSrc(Initializer init) {
        super(init);
    }    
}
