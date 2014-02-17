/* 
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

package org.gstreamer.lowlevel;

import static org.gstreamer.lowlevel.GlibAPI.GLIB_API;

import java.util.concurrent.Callable;

import com.sun.jna.Pointer;
/**
 *
 */
public class GSource extends RefCountedObject {
    
    public GSource(Initializer init) {
        super(init);
    }
    public int attach(GMainContext context) {
        return GLIB_API.g_source_attach(this, context);
    }
    public void setCallback(final Callable<Boolean> call) {
        this.callback = new GlibAPI.GSourceFunc() {
            public boolean callback(Pointer data) {
                if (GLIB_API.g_source_is_destroyed(handle())) {
                    return false;
                }
                try {
                    return call.call().booleanValue();
                } catch (Exception ex) {
                    return false;
                }
            }
        };
        GLIB_API.g_source_set_callback(this, callback, null, null);
    }
    private GlibAPI.GSourceFunc callback;
    
    protected void ref() {
        GLIB_API.g_source_ref(handle());
    }
    protected void unref() {
        GLIB_API.g_source_unref(handle());
    }

    @Override
    protected void disposeNativeHandle(Pointer ptr) {
        GLIB_API.g_source_destroy(ptr);
        GLIB_API.g_source_unref(ptr);
    }
}
