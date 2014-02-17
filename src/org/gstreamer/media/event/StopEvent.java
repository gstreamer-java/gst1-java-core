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

import org.gstreamer.ClockTime;
import org.gstreamer.State;
import org.gstreamer.media.MediaPlayer;

/**
 * Based on code from FMJ by Ken Larson
 */
public class StopEvent extends TransitionEvent {

    private static final long serialVersionUID = -1646275975260781455L;

    private ClockTime mediaTime;

    public StopEvent(MediaPlayer from, State previous, State current, State target, ClockTime mediaTime) {
        super(from, previous, current, target);
        this.mediaTime = mediaTime;
    }

    public ClockTime getMediaTime() {
        return mediaTime;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[source=" + getSource() + ",previousState=" + getPreviousState() +
                ",currentState=" + getCurrentState() + ",targetState=" + getPendingState() +
                ",mediaTime=" + mediaTime + "]";
    }
}
