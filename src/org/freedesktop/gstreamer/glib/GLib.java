/* 
 * 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2018 Ingo Randalf
 *
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

import java.util.stream.Stream;
import org.freedesktop.gstreamer.lowlevel.GlibAPI;
import static org.freedesktop.gstreamer.glib.Natives.registration;

/**
 * Miscellaneous Utility Functions — a selection of portable utility functions from GLib
 * 
 * Documentation derived from https://developer.gnome.org/glib/stable/glib-Miscellaneous-Utility-Functions.html#g-setenv
 */
public class GLib {

    /**
     * Sets an environment variable. On UNIX, both the variable's name and value
     * can be arbitrary byte strings, except that the variable's name cannot
     * contain '='. On Windows, they should be in UTF-8.
     *
     * Note that on some systems, when variables are overwritten, the memory
     * used for the previous variables and its value isn't reclaimed.
     *
     * You should be mindful of the fact that environment variable handling in
     * UNIX is not thread-safe, and your program may crash if one thread calls
     * g_setenv() while another thread is calling getenv(). (And note that many
     * functions, such as gettext(), call getenv() internally.) This function is
     * only safe to use at the very start of your program, before creating any
     * other threads (or creating objects that create worker threads of their
     * own).
     * 
     * @param variable the environment variable to set, must not contain '='. 
     * @param value the value to set the variable to.
     * @param overwrite whether to change the variable if it already exists.
     * @return FALSE if the environment variable couldn't be set.
     */
    public static boolean setEnv(String variable, final String value, boolean overwrite) {
        return GlibAPI.GLIB_API.g_setenv(variable, value, overwrite);
    }

    /**
     * Returns the value of an environment variable.
     *
     * On UNIX, the name and value are byte strings which might or might not be
     * in some consistent character set and encoding. On Windows, they are in
     * UTF-8. On Windows, in case the environment variable's value contains
     * references to other environment variables, they are expanded.
     * 
     * @param variable the environment variable to get. 
     * @return the value of the environment variable, or NULL if the environment
     * variable is not found.
     */
    public static String getEnv(String variable) {
        return GlibAPI.GLIB_API.g_getenv(variable);
    }

    /**
     * Removes an environment variable from the environment.
     *
     * Note that on some systems, when variables are overwritten, the memory
     * used for the previous variables and its value isn't reclaimed.
     *
     * You should be mindful of the fact that environment variable handling in
     * UNIX is not thread-safe, and your program may crash if one thread calls
     * g_unsetenv() while another thread is calling getenv(). (And note that
     * many functions, such as gettext(), call getenv() internally.) This
     * function is only safe to use at the very start of your program, before
     * creating any other threads (or creating objects that create worker
     * threads of their own).
     *
     * @param variable the environment variable to remove, must not contain '='.
     */
    public static void unsetEnv(String variable) {
        GlibAPI.GLIB_API.g_unsetenv(variable);
    }
    
    public static class Types implements NativeObject.TypeProvider {

        @Override
        public Stream<NativeObject.TypeRegistration<?>> types() {
            return Stream.of(
                    registration(GDate.class, GDate.GTYPE_NAME, GDate::new),
                    registration(GInetAddress.class, GInetAddress.GTYPE_NAME, GInetAddress::new),
                    registration(GSocket.class, GSocket.GTYPE_NAME, GSocket::new),
                    registration(GSocketAddress.class, GSocketAddress.GTYPE_NAME, GSocketAddress::new),
                    registration(GInetSocketAddress.class, GInetSocketAddress.GTYPE_NAME, GInetSocketAddress::new)
 
            );
        }
        
    }
}
