/* 
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

package org.gstreamer.elements;


/**
 * A gstreamer element that reads from a file.
 */
public class FileSrc extends BaseSrc {
	public static final String GST_NAME = "filesrc";
    public static final String GTYPE_NAME = "GstFileSrc";
    
    public FileSrc(String name) {
        this(makeRawElement(GST_NAME, name));
    }  
  
    public FileSrc(Initializer init) {
        super(init);
    }
    /**
     * Sets the path of the file this source is to read.
     * 
     * @param location the path to the file to read.
     */
    public void setLocation(java.io.File location) {
        setLocation(location.getAbsolutePath());
    }
    
    /**
     * Sets the path of the file this source is to read.
     * 
     * @param location the path to the file to read.
     */
    public void setLocation(String location) {
        set("location", location);
    }
}
