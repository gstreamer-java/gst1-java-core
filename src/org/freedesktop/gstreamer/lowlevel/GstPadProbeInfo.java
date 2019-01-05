/*
 * Copyright (c) 2014 Tom Greenwood <tgreenwood@cafex.com>
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
package org.freedesktop.gstreamer.lowlevel;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import static org.freedesktop.gstreamer.lowlevel.GstAPI.GST_PADDING;

/**
 * GstPadProbeInfo structure
 * @see https://cgit.freedesktop.org/gstreamer/gstreamer/tree/gst/gstpad.h?h=1.8
 */
public class GstPadProbeInfo extends Structure {
    
    public volatile int padProbeType;   // GstPadProbeInfo enum constants
    public volatile NativeLong id;      // id of the probe
    public volatile Pointer data;       // (allow-none): type specific data, check the @type field to know the datatype. This field can be %NULL.
    public volatile long offset;        // offset of pull probe, this field is valid when type contains GST_PAD_PROBE_TYPE_PULL
    public volatile int size;           // size of pull probe, this field is valid when type contains GST_PAD_PROBE_TYPE_PULL

    /*< private >*/
    public volatile Pointer[] _gst_reserved = new Pointer[GST_PADDING];
    
    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(new String[] {
            "padProbeType", "id", "data", "offset", "size",
            "_gst_reserved"
        });
    }

}
