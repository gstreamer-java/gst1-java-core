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

import org.freedesktop.gstreamer.glib.GLib;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GLibTest {
    
    @Test
    public void getEnv() {       
        String user = GLib.getEnv("USER");
        String pwd = GLib.getEnv("PWD");
        System.out.println("user: " + user);
        System.out.println("path: " + pwd);        
    }
    
    @Test
    public void setUnsetEnv() {
        
        // set environment
        GLib.setEnv("TESTVAR", "foo", true);
        
        // get environment
        assertEquals("could not set TESTVAR!", GLib.getEnv("TESTVAR"), "foo");
        
        // unset
        GLib.unsetEnv("TESTVAR");
        assertEquals("could not unset TESTVAR!", GLib.getEnv("TESTVAR"), null);
    }
}
