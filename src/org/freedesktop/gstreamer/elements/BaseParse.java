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
package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;

public class BaseParse<Child extends BaseParse<?>> extends Element {
	
    public static final String GTYPE_NAME = "GstBaseParse";
    
    public BaseParse(Initializer init) {
        super(init);
    }
    
    public Boolean isPassthroughDisabled() {
    	return get("disable-passthrough");
    }
    
    @SuppressWarnings("unchecked")
	public Child setPassthroughDisabled(boolean disabled) {
    	set("disable-passthrough", disabled);
    	return (Child) this;
    }

}
