/* 
 * Copyright (C) 2009 Levente Farkas <lfarkas@lfarkas.org>
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

package org.gstreamer.swt.overlay;

import java.lang.reflect.Field;

import org.eclipse.swt.widgets.Composite;
import org.gstreamer.Element;
import org.gstreamer.GstException;
import org.gstreamer.interfaces.XOverlay;

import com.sun.jna.Platform;

/**
 * Interface for elements providing tuner operations
 */
public class SWTOverlay extends XOverlay {
    /**
     * Wraps the {@link Element} in a <tt>SWTOverlay</tt> interface
     * 
     * @param element the element to use as a <tt>SWTOverlay</tt>
     * @return a <tt>SWTOverlay</tt> for the element
     */
    public static SWTOverlay wrap(Element element) {
        return new SWTOverlay(element);
    }
    
    /**
     * Creates a new <tt>SWTOverlay</tt> instance
     * 
     * @param element the element that implements the tuner interface
     */
    private SWTOverlay(Element element) {
        super(element);
    }
    
    /**
     * Helper function to get the proper handle for a given SWT Composite.
     *
     * @param composite the SWT Composite for what i like to get the handle. 
     * The type can't be Control since only Composite has embeddedHandle and
     * the Composite's style must be embedded.  
     * @return the handle of the Composite or 0 if the handle is not available.
     */
    public static long getNativeHandle(Composite composite) {
        if (composite != null /*&& ((composite.getStyle() | SWT.EMBEDDED) != 0)*/)
	        try {
	            Class<? extends Composite> compositeClass = composite.getClass();
	            Field handleField = Platform.isLinux() ? compositeClass.getField("embeddedHandle") : compositeClass.getField("handle");
	            Class<?> t = handleField.getType();
	            if (t.equals(long.class))
	                return handleField.getLong(composite);
	            else if (t.equals(int.class))
	                return handleField.getInt(composite);
	        } catch (Exception e) {
	            throw new GstException("Cannot set window ID, in XOverlay interface, composite is null or not SWT.EMBEDDED");
	        }
        return 0L;
    }

    /**
     * Sets the native window for the {@link Element} to use to display video.
     *
     * @param window A native window to use to display video, or <tt>null</tt> to
     * stop using the previously set window.
     */
    public void setWindowHandle(Composite composite) {
        setWindowHandle(getNativeHandle(composite));
    }
    /**
     * Sets the native window for the {@link Element} to use to display video.
     *
     * @param window A native window to use to display video, or <tt>null</tt> to
     * stop using the previously set window.
     * @deprecated use {@link #setWindow(Composite)} instead
     */
    @Deprecated
    public void setWindowID(Composite composite) {
    	setWindowHandle(composite);
    }
}
