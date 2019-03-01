/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2008 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wim.taymans@chello.be>
 *                    2005 Wim Taymans <wim@fluendo.com>
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
package org.freedesktop.gstreamer.event;

import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GType;
import static org.freedesktop.gstreamer.lowlevel.GstEventAPI.GSTEVENT_API;

/**
 * Navigation events are usually used for communicating user requests, such as
 * mouse or keyboard movements, to upstream elements.
 */
// @TODO API seems OK but need to check and update against GstNavigation
public class NavigationEvent extends Event {

    /**
     * This constructor is for internal use only.
     *
     * @param init initialization data.
     */
    NavigationEvent(Initializer init) {
        super(init);
    }

    /**
     * Creates a new navigation event from the given description.
     * <p>
     * Unless you really need a custom navigation event, use one of the static
     * convenience methods for creating navigation events.
     *
     * @param structure the description of the navigation event.
     */
    public NavigationEvent(Structure structure) {
        this(Natives.initializer(GSTEVENT_API.ptr_gst_event_new_navigation(structure)));
    }

    /**
     * Gets a human-readable string representation of this navigation event.
     *
     * @return a string
     */
    @Override
    public String toString() {
        Structure s = getStructure();
        String event = s.getString("event");
        if (event.startsWith("key-")) {
            return String.format("%s: [key=%s]",
                    event, s.getString("key"));
        } else if (event.startsWith("mouse-")) {
            return String.format("%s: [x=%f, y=%f button=%x]",
                    event,
                    s.getDouble("pointer_x"), s.getDouble("pointer_y"),
                    s.getInteger("button"));
        } else {
            return String.format("%s",
                    s.getString("event"));
        }
    }

    /**
     * Creates a mouse navigation event.
     *
     * @param event the type of mouse event.
     * @param x the X location of the mouse cursor
     * @param y the Y location of the mouse cursor
     * @param button the button(s) currently pressed
     * @return a new navigation event
     */
    public static NavigationEvent createMouseEvent(String event, double x, double y, int button) {
        return new MouseEvent(event, x, y, button);
    }

    /**
     * Creates a mouse move navigation event.
     *
     * @param x the X location of the mouse cursor
     * @param y the Y location of the mouse cursor
     * @param button the button(s) currently pressed
     * @return a new navigation event
     */
    public static NavigationEvent createMouseMoveEvent(double x, double y, int button) {
        return createMouseEvent("mouse-move", x, y, button);
    }

    /**
     * Creates a mouse button press navigation event.
     *
     * @param x the X location of the mouse cursor
     * @param y the Y location of the mouse cursor
     * @param button the button(s) currently pressed
     * @return a new navigation event
     */
    public static NavigationEvent createMouseButtonPressEvent(double x, double y, int button) {
        return createMouseEvent("mouse-button-press", x, y, button);
    }

    /**
     * Creates a mouse button release navigation event.
     *
     * @param x the X location of the mouse cursor
     * @param y the Y location of the mouse cursor
     * @param button the button(s) currently pressed
     * @return a new navigation event
     */
    public static NavigationEvent createMouseButtonReleaseEvent(double x, double y, int button) {
        return createMouseEvent("mouse-button-release", x, y, button);
    }

    /**
     * Creates a new key navigation event.
     *
     * @param event the type of key event.
     * @param key the ascii key code for the key.
     * @return a new navigation event
     */
    public static NavigationEvent createKeyEvent(String event, String key) {
        return new KeyEvent(event, key);
    }

    /**
     * Creates a new key press navigation event.
     *
     * @param key the ascii key code for the key.
     * @return a new navigation event
     */
    public static NavigationEvent createKeyPressEvent(String key) {
        return createKeyEvent("key-press", key);
    }

    /**
     * Creates a new key release navigation event.
     *
     * @param key the ascii key code for the key.
     * @return a new navigation event
     */
    public static NavigationEvent createKeyReleaseEvent(String key) {
        return createKeyEvent("key-release", key);
    }

    private static final class MouseEvent extends NavigationEvent {

        public MouseEvent(String event, double x, double y, int button) {
            super(new Structure("application/x-gst-navigation",
                    "event", GType.STRING, event,
                    "button", GType.INT, button,
                    "pointer_x", GType.DOUBLE, x,
                    "pointer_y", GType.DOUBLE, y));
        }

        /**
         * Gets a human-readable string representation of this navigation event.
         *
         * @return a string
         */
        @Override
        public String toString() {
            Structure s = getStructure();
            return String.format("%s: [x=%f, y=%f button=%x]",
                    s.getString("event"),
                    s.getDouble("pointer_x"), s.getDouble("pointer_y"),
                    s.getInteger("button"));
        }
    }

    private static final class KeyEvent extends NavigationEvent {

        public KeyEvent(String event, String key) {
            super(new Structure("application/x-gst-navigation",
                    "event", GType.STRING, event,
                    "key", GType.STRING, key));
        }

        /**
         * Gets a human-readable string representation of this navigation event.
         *
         * @return a string
         */
        @Override
        public String toString() {
            Structure s = getStructure();
            return String.format("%s: [key=%s]",
                    s.getString("event"), s.getString("key"));
        }
    }
}
