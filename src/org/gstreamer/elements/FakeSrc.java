/* 
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

package org.gstreamer.elements;


/**
 *
 * @author wayne
 */
public class FakeSrc extends BaseSrc {
    public static final String GST_NAME = "fakesrc";
    public static final String GTYPE_NAME = "GstFakeSrc";
    
    public FakeSrc(String name) {
        this(makeRawElement(GST_NAME, name));
    }  
  
    public FakeSrc(Initializer init) {
        super(init);
    }
}
