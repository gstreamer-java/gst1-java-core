/*
 * Copyright (c) 2015 Christophe Lafolet
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

import org.freedesktop.gstreamer.lowlevel.IntegerEnum;

public enum StreamStatus implements IntegerEnum {
	/** a new thread need to be created */
	CREATE(0),
	/** a thread entered its loop function */
	ENTER(1),
	/** a thread left its loop function */
	LEAVE(2),
	/** a thread is destroyed */
	DESTROY(3),
	/** a thread is started */
	START(8),
	/** a thread is paused */
	PAUSE(9),
	/** a thread is stopped */
	STOP(10);

	private StreamStatus(int value)	{
        this.value = value;
	}

    /**
     * Gets the integer value of the enum.
     * @return The integer value for this enum.
     */
    @Override
	public int intValue() {
        return this.value;
    }
    private final int value;

}
