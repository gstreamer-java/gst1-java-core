/*
 * Copyright (c) 2010 DHoyt <david.g.hoyt@gmail.com>
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

package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Bin;

public class DtlsSrtpDec extends Bin {
	public static final String GST_NAME = "dtlssrtpdec";
    public static final String GTYPE_NAME = "GstDtlsSrtpDec";
    
    public DtlsSrtpDec() {
    	this((String) null);
    }

    public DtlsSrtpDec(String name) {
        super(makeRawElement(GST_NAME, name));
    }

    public DtlsSrtpDec(Initializer init) {
        super(init);
    }

    public String getPEMCertificate() {
    	return get("pem");
    }
    
    public DtlsSrtpDec setPemCertificate(String pem) {
    	set("pem", pem);
    	return this;
    }
    
    public String getPeerPemCertificate() {
    	return get("peer-pem");
    }
 
    public String getConnectionId() {
    	return get("connection-id");
    }
    
    public DtlsSrtpDec setConnectionId(String connectionId) {
    	set("connection-id", connectionId);
    	return this;
    }
}
