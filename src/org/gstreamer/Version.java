/* 
 * Copyright (c) 2007, 2008 Wayne Meissner
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

/**
 * Describes the version of gstreamer currently in use.
 */
public class Version {
    public Version(long major, long minor, long micro, long nano) {
        this.major = major;
        this.minor = minor;
        this.micro = micro;
        this.nano = nano;
    }
    
    /**
     * Gets a string representation of the version.
     * 
     * @return a string representing the version.
     */
    @Override
    public String toString() {
        return String.format("%d.%d.%d%s", 
                major, minor, micro,
                nano == 1 ? " (CVS)" : nano >= 2 ? " (Pre-release)" : "");
    }
    
    /**
     * Gets the major version of GStreamer at compile time.
     * @return the major version.
     */
    public long getMajor() {
        return major;
    }
    
    /**
     * Gets the minor version of GStreamer at compile time.
     * @return the minor version.
     */
    public long getMinor() {
        return minor;
    }
    
    /**
     * Gets the micro version of GStreamer at compile time.
     * @return the micro version.
     */
    public long getMicro() {
        return micro;
    }
    
    /**
     * Gets the nano version of GStreamer at compile time.
     * @return the nano version.
     */
    public long getNano() {
        return nano;
    }
    private final long major, minor, micro, nano;
}
