/* 
 * This file is part of gstreamer-java.
 *
 * gstreamer-java is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * gstreamer-java is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with gstreamer-java.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.glib.GlibUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GlibUtilsTest {
    
    @Test
    public void getEnv() {       
        String user = GlibUtils.getEnv("USER");
        String pwd = GlibUtils.getEnv("PWD");
        System.out.println("user: " + user);
        System.out.println("path: " + pwd);        
    }
    
    @Test
    public void setUnsetEnv() {
        
        // set environment
        GlibUtils.setEnv("TESTVAR", "foo", true);
        
        // get environment
        assertEquals("could not set TESTVAR!", GlibUtils.getEnv("TESTVAR"), "foo");
        
        // unset
        GlibUtils.unsetEnv("TESTVAR");
        assertEquals("could not unset TESTVAR!", GlibUtils.getEnv("TESTVAR"), null);
    }
}
