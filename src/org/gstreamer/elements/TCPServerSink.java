/*
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2009 Wayne Meissner
 * Copyright (c) 2008 Wayne Meissner
 * Copyright (C) 2007 David Schleef <ds@schleef.org>
 *           (C) 2008 Wim Taymans <wim.taymans@gmail.com>
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

package org.gstreamer.elements;


/**
 * A sink {@link org.gstreamer.Element} that send data as a server over the network via TCP.
 */
public class TCPServerSink extends MultiFdSink {
    public static final String GST_NAME = "tcpserversink";
    public static final String GTYPE_NAME = "GstTCPServerSink";

    public TCPServerSink(String name) {
        this(makeRawElement(GST_NAME, name));
    }

    public TCPServerSink(Initializer init) {
        super(init);
    }
}
