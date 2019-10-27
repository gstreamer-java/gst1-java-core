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

import java.util.function.Function;

/**
 * Default memory allocator
 */
public class SysmemAllocator extends Allocator {

	public static final String GTYPE_NAME = "GstAllocatorSysmem";

	/**
	 * This constructor is for internal use only.
	 * 
	 * @param init initialization data.
	 */
	protected SysmemAllocator(Initializer init) {
		super(init);
	}

	@Override
	public Function<Initializer, Memory> getFactory() {
		return Memory::new;
	}

}
