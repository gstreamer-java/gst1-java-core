/* 
 * Copyright (C) 2019 Neil C Smith
 * Copyright (C) 2008 Wayne Meissner
 * Copyright (C) 2004 Wim Taymans <wim@fluendo.com>
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

package org.freedesktop.gstreamer.message;

import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.State;
import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstMessageAPI.GSTMESSAGE_API;

/**
 * A state change message. 
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-state-changed"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-state-changed</a>
 * <p>
 * This message is posted whenever an element changes its state.
 */
public class StateChangedMessage extends Message {

    /**
     * Creates a new Buffering message.
     * @param init internal initialization data.
     */
    StateChangedMessage(Initializer init) {
        super(init);
    }
    
    /**
     * Creates a new state-changed message.
     *
     * @param src the source object emitting this message.
     * @param old the previous state.
     * @param current the new (current) state.
     * @param pending the pending (target) state.
     */
    public StateChangedMessage(GstObject src, State old, State current, State pending) {
        super(Natives.initializer(GSTMESSAGE_API.ptr_gst_message_new_state_changed(src, old, current, pending)));
    }
    
    /**
     * Gets the previous state.
     * 
     * @return the previous state.
     */
    public State getOldState() {
        State[] state = new State[1];
        GSTMESSAGE_API.gst_message_parse_state_changed(this, state, null, null);
        return state[0];
    }
    
    /**
     * Gets the new (current) state.
     * 
     * @return the new state.
     */
    public State getNewState() {
        State[] state = new State[1];
        GSTMESSAGE_API.gst_message_parse_state_changed(this, null, state, null);
        return state[0];
    }
    
    /**
     * Gets the pending (target) state.
     * 
     * @return the pending state.
     */
    public State getPendingState() {
        State[] state = new State[1];
        GSTMESSAGE_API.gst_message_parse_state_changed(this, null, null, state);
        return state[0];
    }
}
