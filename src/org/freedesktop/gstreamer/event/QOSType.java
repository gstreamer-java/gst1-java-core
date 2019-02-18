/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer.event;

import org.freedesktop.gstreamer.glib.NativeEnum;

/**
 * The different types of QoS events that can be given to the
 * gst_event_new_qos() method.
 */
public enum QOSType implements NativeEnum<QOSType> {
    /**
     * The QoS event type that is produced when downstream elements are
     * producing data too quickly and the element can't keep up processing the
     * data. Upstream should reduce their processing rate. This type is also
     * used when buffers arrive early or in time.
     */
    OVERFLOW(0),
    /**
     * The QoS event type that is produced when downstream elements are
     * producing data too slowly and need to speed up their processing rate.
     */
    UNDERFLOW(1),
    /**
     * The QoS event type that is produced when the application enabled
     * throttling to limit the datarate.
     */
    THROTTLE(2);
    
    private final int value;
    
    private QOSType(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }
    
    
}
