/* 
 * Copyright (c) 2019 Neil C Smith
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

package org.freedesktop.gstreamer;

/**
 * Describes a GStreamer version.
 */
public class Version {
    
    private final int major, minor, micro, nano;
    
    public Version(int major, int minor) {
        this(major, minor, 0, 0);
    }
    
    public Version(int major, int minor, int micro, int nano) {
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
     * Gets the major version of GStreamer.
     * @return the major version.
     */
    public int getMajor() {
        return major;
    }
    
    /**
     * Gets the minor version of GStreamer.
     * @return the minor version.
     */
    public int getMinor() {
        return minor;
    }
    
    /**
     * Gets the micro version of GStreamer.
     * @return the micro version.
     */
    public int getMicro() {
        return micro;
    }
    
    /**
     * Gets the nano version of GStreamer.
     * @return the nano version.
     */
    public int getNano() {
        return nano;
    }
    
    /**
     * Check whether this version is equal to or greater than the passed in 
     * version. Roughly comparable to GST_CHECK_VERSION
     * @param required version to check against
     * @return true if this version satisfies the required version
     */
    public boolean checkSatisfies(Version required) {
        return (major == required.major && minor > required.minor) ||
                (major == required.major && minor == required.minor && micro >= required.micro);
    }
    
}
