/* 
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

package org.gstreamer.message;

import org.gstreamer.GstObject;
import org.gstreamer.Message;
import org.gstreamer.State;
import org.gstreamer.lowlevel.GstMessageAPI;
import org.gstreamer.lowlevel.GstNative;

import com.sun.jna.Pointer;

/**
 * A state change message. 
 * <p>
 * This message is posted whenever an element changes its state.
 */
public class StateChangedMessage extends Message {
    private static interface API extends GstMessageAPI {
        Pointer ptr_gst_message_new_state_changed(GstObject src, State oldstate, State newstate, State pending);
    }
    private static final API gst = GstNative.load(API.class);
    
    /**
     * Creates a new Buffering message.
     * @param init internal initialization data.
     */
    public StateChangedMessage(Initializer init) {
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
        super(initializer(gst.ptr_gst_message_new_state_changed(src, old, current, pending)));
    }
    
    /**
     * Gets the previous state.
     * 
     * @return the previous state.
     */
    public State getOldState() {
        State[] state = new State[1];
        gst.gst_message_parse_state_changed(this, state, null, null);
        return state[0];
    }
    
    /**
     * Gets the new (current) state.
     * 
     * @return the new state.
     */
    public State getNewState() {
        State[] state = new State[1];
        gst.gst_message_parse_state_changed(this, null, state, null);
        return state[0];
    }
    
    /**
     * Gets the pending (target) state.
     * 
     * @return the pending state.
     */
    public State getPendingState() {
        State[] state = new State[1];
        gst.gst_message_parse_state_changed(this, null, null, state);
        return state[0];
    }
}
