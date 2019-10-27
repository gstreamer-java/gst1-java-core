/*
 * Copyright (c) 2019 Christophe Lafolet
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

import static org.freedesktop.gstreamer.lowlevel.GstContextAPI.GSTCONTEXT_API;

import org.freedesktop.gstreamer.glib.GObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GstContextAPI;

public class Context extends MiniObject {

	public static final String GTYPE_NAME = "GstContext";

	private static final GstContextAPI gst = GstContextAPI.GSTCONTEXT_API;

	protected Context(Initializer init) {
		super(init);
	}

	public Context(String context_type) {
		this(Natives.initializer(gst.ptr_gst_context_new(context_type, true)));
	}

	public void set(final String field, final String contextTypeName, final GObject aContext) {
		final GType gtype = GType.valueOf(contextTypeName);
		GSTCONTEXT_API.gst_context_writable_structure(this).setValue(field, gtype, aContext);
	}

	public String getContextType() {
		return GSTCONTEXT_API.gst_context_get_context_type(this);
	}

	public boolean isPersistent() {
		return GSTCONTEXT_API.gst_context_is_persistent(this);
	}
}
