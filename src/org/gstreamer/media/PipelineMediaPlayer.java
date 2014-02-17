/* 
 * Copyright (c) 2007,2008 Wayne Meissner
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

package org.gstreamer.media;

import java.net.URI;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.gstreamer.Bus;
import org.gstreamer.ClockTime;
import org.gstreamer.Format;
import org.gstreamer.Gst;
import org.gstreamer.GstObject;
import org.gstreamer.Pipeline;
import org.gstreamer.State;
import org.gstreamer.media.event.DurationChangedEvent;
import org.gstreamer.media.event.EndOfMediaEvent;
import org.gstreamer.media.event.MediaListener;
import org.gstreamer.media.event.PauseEvent;
import org.gstreamer.media.event.PositionChangedEvent;
import org.gstreamer.media.event.StartEvent;
import org.gstreamer.media.event.StopEvent;

/**
 * A MediaPlayer that uses a {@link Pipeline}
 */
public abstract class PipelineMediaPlayer extends AbstractMediaPlayer {
    protected final Pipeline pipeline;    
    private volatile ScheduledFuture<?> positionTimer = null;
    
    protected PipelineMediaPlayer(Pipeline pipeline, Executor executor) {
        super(executor);
        this.pipeline = pipeline;
        getPipeline().getBus().connect(eosSignal);
        getPipeline().getBus().connect(stateChanged);
    }
    @Override
    public synchronized void addMediaListener(MediaListener listener) {
        // Only run the timer when needed
        if (getMediaListeners().isEmpty()) {
            positionTimer = Gst.getScheduledExecutorService().scheduleAtFixedRate(positionUpdater, 1, 1, TimeUnit.SECONDS);
        }
        super.addMediaListener(listener);
    }
    
    @Override
    public synchronized void removeMediaListener(MediaListener listener) {
        super.removeMediaListener(listener);
        // Only run the timer when needed
        
        if (getMediaListeners().isEmpty() && positionTimer != null) {
            positionTimer.cancel(true);
            positionTimer = null;
        }
    }
    /**
     * Gets the {@link Pipeline} that the MediaPlayer uses to play media.
     * 
     * @return A Pipeline
     */
    public Pipeline getPipeline() {
        return pipeline;
    }
    
    private Runnable positionUpdater = new Runnable() {
        private long lastPosition = 0;
        private ClockTime lastDuration = ClockTime.ZERO;
        public void run() {
            final long position = pipeline.queryPosition(Format.TIME);
            final long percent = pipeline.queryPosition(Format.PERCENT);
            final ClockTime duration = pipeline.queryDuration();
            final boolean durationChanged = !duration.equals(lastDuration) 
                    && !duration.equals(ClockTime.ZERO)
                    && !duration.equals(ClockTime.NONE);
            lastDuration = duration;
            final boolean positionChanged = position != lastPosition && position >= 0;
            lastPosition = position;
            final PositionChangedEvent pue = new PositionChangedEvent(PipelineMediaPlayer.this,
                    ClockTime.valueOf(position, TimeUnit.NANOSECONDS), (int) percent);
            final DurationChangedEvent due = new DurationChangedEvent(PipelineMediaPlayer.this,
                    duration);
            for (MediaListener l : getMediaListeners()) {
                if (durationChanged) {
                    l.durationChanged(due);
                }
                if (positionChanged) {
                    l.positionChanged(pue);
                }
            }
        }
    };
    /*
     * Handle EOS signals.
     */
    private Bus.EOS eosSignal = new Bus.EOS() {
        public void endOfStream(GstObject source) {
            URI next = playList.poll();
            if (next != null) {
                setURI(next);
            } else {
                final EndOfMediaEvent evt = new EndOfMediaEvent(PipelineMediaPlayer.this, 
                            State.PLAYING, State.NULL, State.VOID_PENDING);
                // Notify any listeners that the last media file is finished
                fireEndOfMediaEvent(evt);
            }
            
        }
    };

    private final Bus.STATE_CHANGED stateChanged = new Bus.STATE_CHANGED() {
        public void stateChanged(GstObject source, State old, State newState, State pending) {
            if (!source.equals(getPipeline()))
            	return;
            final ClockTime position = getPipeline().queryPosition();
            if (newState.equals(State.PLAYING) && old.equals(State.PAUSED)) {
                fireStartEvent(new StartEvent(PipelineMediaPlayer.this, old, newState, State.VOID_PENDING, position));
            } else if (newState.equals(State.PAUSED) && pending.equals(State.VOID_PENDING)) {
                firePauseEvent(new PauseEvent(PipelineMediaPlayer.this, old, newState, State.VOID_PENDING, position));
            } else if (newState.equals(State.READY) && pending.equals(State.NULL)) {
                fireStopEvent(new StopEvent(PipelineMediaPlayer.this, old, newState, pending, position));
            }

            // Anything else means we are transitioning!
            if (!pending.equals(State.VOID_PENDING) && !pending.equals(State.NULL)) {
                // TODO: Maybe a new callback method on MediaListener with TransitionEvent?
            }
        }
    };
}
