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
package org.freedesktop.gstreamer.glib;

import org.freedesktop.gstreamer.lowlevel.GlibAPI;

public class GLib {
    
    public static boolean setEnv(String variable, final String value, boolean overwrite) {        
        return GlibAPI.GLIB_API.g_setenv(variable, value, overwrite);       
    }
    
    public static String getEnv(String variable) {       
        return GlibAPI.GLIB_API.g_getenv(variable);       
    }   
    
    public static void unsetEnv(String variable) {
        GlibAPI.GLIB_API.g_unsetenv(variable);       
    }
}
