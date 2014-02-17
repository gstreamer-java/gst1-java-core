/* 
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

package org.gstreamer.media.event;

import org.gstreamer.State;
import org.gstreamer.media.MediaPlayer;

/**
 * Based on code from FMJ by Ken Larson
 */
public class TransitionEvent extends MediaEvent {

    private static final long serialVersionUID = 708470584903128800L;

    public final State previousState;
    public final State currentState;
    public final State pendingState;

    public TransitionEvent(MediaPlayer player, State previousState, State currentState, 
            State pendingState) {
        super(player);
        this.previousState = previousState;
        this.currentState = currentState;
        this.pendingState = pendingState;
    }

    public State getPreviousState() {
        return previousState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public State getPendingState() {
        return pendingState;
    }
    public State getTargetState() {
        return pendingState;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[source=" + getSource() + ",previousState=" + previousState +
                ",currentState=" + currentState + ",targetState=" + pendingState + "]";
    }
}
