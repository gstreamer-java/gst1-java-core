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

package org.gstreamer.interfaces;

import org.gstreamer.GObject;
import org.gstreamer.lowlevel.GstTunerAPI;

/**
 *
 */
public class TunerNorm extends GObject {
    public static final String GTYPE_NAME = "GstTunerNorm";

    private final GstTunerAPI.TunerNormStruct struct;
    public TunerNorm(Initializer init) {
        super(init);
        struct = new GstTunerAPI.TunerNormStruct(getNativeAddress());
    }

    /**
     * Retrieves the label from the given norm
     *
     * @return the label
     */
    public String getLabel(){
        return struct.getLabel();
    }
}
