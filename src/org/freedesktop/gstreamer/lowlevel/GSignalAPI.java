/* 
 * Copyright (c) 2009 Levente Farkas
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

package org.freedesktop.gstreamer.lowlevel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.freedesktop.gstreamer.glib.GObject;
import org.freedesktop.gstreamer.glib.GQuark;
import org.freedesktop.gstreamer.lowlevel.GObjectAPI.GClosureNotify;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

/**
 *
 * @author wayne
 */
@SuppressWarnings("serial")
public interface GSignalAPI extends Library {
    GSignalAPI GSIGNAL_API = GNative.loadLibrary("gobject-2.0", GSignalAPI.class,
            new HashMap<String, Object>() {{
                put(Library.OPTION_TYPE_MAPPER, new GTypeMapper());
            }});

    public static int G_CONNECT_AFTER = 1 << 0;
    public static int G_CONNECT_SWAPPED = 1 << 1;
    
    public static final class GSignalQuery extends com.sun.jna.Structure {
    	public int signal_id;
    	public String signal_name;
    	public GType itype;
    	public int /* GSignalFlags */ signal_flags;
    	public GType return_type; /* mangled with G_SIGNAL_TYPE_STATIC_SCOPE flag */
    	public int n_params;
    	public Pointer param_types; /* mangled with G_SIGNAL_TYPE_STATIC_SCOPE flag */

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "signal_id", "signal_name", "itype",
                "signal_flags", "return_type", "n_params",
                "param_types"
            });
        }
    }
    
    NativeLong g_signal_connect_data(GObject obj, String signal, Callback callback, Pointer data,
            GClosureNotify destroy_data, int connect_flags);
    void g_signal_handler_disconnect(GObject obj, NativeLong id);
    
    int g_signal_lookup(String name, GType itype);
    String g_signal_name(int signal_id);
    void g_signal_query(int signal_id, GSignalQuery query);
    int g_signal_list_ids(GType itype, int[] n_ids);

    void g_signal_emit(GObject obj, int signal_id, GQuark detail, Object... arguments);
    void g_signal_emit_by_name(GObject obj, String signal, Object... arguments);
    
    // Do nothing, but provide a base Callback class that gets automatic type conversion
    public static interface GSignalCallbackProxy extends com.sun.jna.CallbackProxy {}
}
