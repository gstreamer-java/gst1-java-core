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

package org.freedesktop.gstreamer.query;

import static org.freedesktop.gstreamer.lowlevel.GstQueryAPI.GSTQUERY_API;

import org.freedesktop.gstreamer.glib.Natives;

public class ContextQuery extends Query {

	/**
	 * Creates a new context query.
	 *
	 * @param init internal initialization data.
	 */
	ContextQuery(Initializer init) {
		super(init);
	}

	/**
	 * Creates a new context query.
	 *
	 * @param context_type
	 */
	public ContextQuery(String context_type) {
		this(Natives.initializer(GSTQUERY_API.ptr_gst_query_new_context(context_type)));
	}

	/**
	 * Gets the context type contained in this query.
	 * 
	 * @return the context type.
	 */
	public String getContextType() {
		String[] context_type = new String[1];
		boolean isOk = GSTQUERY_API.gst_query_parse_context_type(this, context_type);
		return isOk ? context_type[0] : null;
	}
}
