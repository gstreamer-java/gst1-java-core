/* 
 * Copyright (c) 2009 Levente Farkas
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

package org.freedesktop.gstreamer.glib;

import org.freedesktop.gstreamer.lowlevel.*;
import com.sun.jna.Pointer;
import static org.freedesktop.gstreamer.lowlevel.GlibAPI.GLIB_API;

/**
 *
 */
public class GMainContext extends RefCountedObject {
    
    public GMainContext() {
        this(Natives.initializer(GLIB_API.g_main_context_new()));
    }
    private GMainContext(Initializer init) {
        super(new Handle(init.ptr, init.ownsHandle), init.needRef);
    }
    
    public int attach(GSource source) {
        return GLIB_API.g_source_attach(source, this);
    }
    public static GMainContext getDefaultContext() {
        return new GMainContext(Natives.initializer(GLIB_API.g_main_context_default(), false, false));
    }
    
    private static final class Handle extends RefCountedObject.Handle {

        public Handle(GPointer ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            GLIB_API.g_main_context_unref(ptr);
        }

        @Override
        protected void ref() {
            GLIB_API.g_main_context_ref(getPointer());
        }

        @Override
        protected void unref() {
            GLIB_API.g_main_context_unref(getPointer());
        }
        
    }
}
