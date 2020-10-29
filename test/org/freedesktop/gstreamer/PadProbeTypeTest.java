/* 
 * Copyright (c) 2020 Neil C Smith
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

import java.lang.reflect.Field;
import java.util.Set;
import org.freedesktop.gstreamer.glib.NativeFlags;
import org.freedesktop.gstreamer.lowlevel.GstPadAPI;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class PadProbeTypeTest {
    

    @Test
    public void testCombinations() throws Exception {
        for (Field field : PadProbeType.class.getFields()) {
            if (Set.class.isAssignableFrom(field.getType())) {
                Set<PadProbeType> flags = (Set<PadProbeType>) field.get(null);
//                System.out.println(field.getName() + " : " + flags);
                Field nativeField = GstPadAPI.class.getField("GST_PAD_PROBE_TYPE_" + field.getName());
                assertEquals(NativeFlags.toInt(flags), nativeField.get(null));
            }
        }
    }
    
    
}
