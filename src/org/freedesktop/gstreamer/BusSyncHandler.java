/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.message.Message;

/**
 * A BusSyncHandler will be invoked synchronously, when a new message has been
 * injected into a {@link Bus}. This function is mostly used internally. Only
 * one sync handler can be attached to a given bus.
 *
 * @see Bus#setSyncHandler(org.freedesktop.gstreamer.BusSyncHandler)
 */
public interface BusSyncHandler {

    public BusSyncReply syncMessage(Message message);
}
