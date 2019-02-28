/* 
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2009 Tamas Korodi <kotyo@zamba.fm>
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
package org.freedesktop.gstreamer.interfaces;

import java.util.List;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

import com.sun.jna.Pointer;
import java.util.ArrayList;
import org.freedesktop.gstreamer.lowlevel.GlibAPI;

import static org.freedesktop.gstreamer.lowlevel.GstColorBalanceAPI.GSTCOLORBALANCE_API;

public class ColorBalance extends GstInterface {

    /**
     * Wraps the {@link Element} in a <tt>ColorBalance</tt> interface
     *
     * @param element the element to use as a <tt>ColorBalance</tt>
     * @return a <tt>ColorBalance</tt> for the element
     */
    public static final ColorBalance wrap(Element element) {
        return new ColorBalance(element);
    }

    /**
     * Creates a new ColorBalance instance
     *
     * @param element the element that implements the ColorBalance interface
     */
    private ColorBalance(Element element) {
        super(element, GSTCOLORBALANCE_API.gst_color_balance_get_type());
    }

    /**
     * Retrieves a list of ColorBalanceChannels from the ColorBalance
     *
     * @return a list of color balance channels available on this device
     */
    public List<ColorBalanceChannel> getChannelList() {
        
        GlibAPI.GList glist = GSTCOLORBALANCE_API.gst_color_balance_list_channels(this);
        List<ColorBalanceChannel> list = new ArrayList<>();
        GlibAPI.GList next = glist;
        while (next != null) {
            if (next.data != null) {
                list.add(channelFor(next.data, true));
            }
            next = next.next();
        }
        return list;
    }
        
    /**
     * Retrieves a ColorBalanceChannel for the given Pointer
     *
     * @param pointer
     * @param needRef
     * @return a ColorBalanceChannel instance
     */
    private final ColorBalanceChannel channelFor(Pointer pointer,
            boolean needRef) {
        return new ColorBalanceChannel(this, pointer, needRef, true);
    }

    /**
     * Signal emitted when color balance value changed
     *
     * @see #connect(VALUE_CHANGED)
     * @see #disconnect(VALUE_CHANGED)
     */
    public static interface VALUE_CHANGED {

        /**
         * Called when the color balance channel value changes
         */
        public void colorBalanceValueChanged(ColorBalance colorBalance,
                ColorBalanceChannel channel, int value);
    }

    /**
     * Add a listener for norm-changed messages.
     *
     * @param listener the listener to be called when the norm changes
     */
    public void connect(final VALUE_CHANGED listener) {
        element.connect(VALUE_CHANGED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(Pointer colorBalance, ColorBalanceChannel channel, int value) {
                listener.colorBalanceValueChanged(ColorBalance.this, channel, value);
                return true;
            }
        });
    }

    /**
     * Disconnect the listener for norm-changed messages.
     *
     * @param listener the listener that was registered to receive the message.
     */
    public void disconnect(VALUE_CHANGED listener) {
        element.disconnect(VALUE_CHANGED.class, listener);
    }

}
