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
import org.gstreamer.media.MediaPlayer;

/**
 *
 * @author wayne
 */
public class PositionChangedEvent extends MediaEvent {

    private static final long serialVersionUID = 269889318281659313L;

    public final ClockTime position;
    public final int percent;
    public PositionChangedEvent(MediaPlayer player, ClockTime position, int percent) {
        super(player);
        this.position = position;
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }

    public ClockTime getPosition() {
        return position;
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "[source=" + getSource() + ",position=" + position + "]";

    }
}
