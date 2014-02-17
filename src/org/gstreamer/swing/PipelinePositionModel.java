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

package org.gstreamer.swing;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;

import org.gstreamer.Bus;
import org.gstreamer.Format;
import org.gstreamer.Gst;
import org.gstreamer.GstObject;
import org.gstreamer.Pipeline;
import org.gstreamer.SeekFlags;
import org.gstreamer.SeekType;

/**
 *
 *
 */
public class PipelinePositionModel extends DefaultBoundedRangeModel {

    private static final long serialVersionUID = 6687380442713003174L;

    private static final int UPDATE_INTERVAL = 1000; // 1 second
    private static final TimeUnit scaleUnit = TimeUnit.SECONDS;
    private static final SwingExecutorService swingExec = new SwingExecutorService();
    private final Pipeline pipeline;
    private boolean updating = false;
    private final AtomicBoolean isSeeking = new AtomicBoolean(false);
    
    private long seekingPos = -1;
    
    private volatile ScheduledFuture<?> updateTask = null;
    
    /** Creates a new instance of MediaPositionModel */
    public PipelinePositionModel(Pipeline element) {
        this.pipeline = element;
        element.getBus().connect(swingExec.wrap(Bus.SEGMENT_DONE.class, new Bus.SEGMENT_DONE() {

            public void segmentDone(GstObject source, Format format, final long position) {
                PipelinePositionModel.this.segmentDone(position);
            }
        }));
    }
    private synchronized void startPoll() {
        Runnable task = new Runnable() {

            public void run() {
                final long position = pipeline.queryPosition(scaleUnit);
                final long duration = pipeline.queryDuration(scaleUnit);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        updatePosition(duration, position);
                    }
                });
            }
        };
        updateTask = Gst.getScheduledExecutorService().scheduleAtFixedRate(task, 
                UPDATE_INTERVAL, UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }
    private synchronized void stopPoll() {
        if (updateTask != null) {
            updateTask.cancel(true);
            updateTask = null;
        }
    }
    @Override
    public void addChangeListener(ChangeListener l) {
        if (listenerList.getListenerCount() == 0) {
            startPoll();
        }
        super.addChangeListener(l);
    }
    @Override
    public void removeChangeListener(ChangeListener l) {
        super.removeChangeListener(l);
        if (listenerList.getListenerCount() == 0) {
            stopPoll();
        }
    }
    private void updatePosition(long duration, long position) {
        // Don't update the slider when it is being dragged
        if (isSeeking.get() || getValueIsAdjusting()) {
            return;
        }
        final int min = 0;
        final int max = (int) duration;
        final int pos = (int) position;
        //System.out.printf("Setting range properties to %02d, %02d, %02d%n", min, max, pos);
        updating = true;
        super.setRangeProperties(pos, 1, min, max, false);
        updating = false;
    }
    
    public void setValue(int newValue) {
        super.setValue(newValue);
        //
        // Only seek when the slider is being dragged, and not when updating the
        // position from the poll
        //
        if (!updating && !isSeeking.get()) {
            long pos = TimeUnit.SECONDS.toNanos(getValue());
            if (pos != seekingPos) {
                seekTo(pos);
            }
        }
    }
    
    private void segmentSeek(final long position) {
        isSeeking.set(true);
        seekingPos = position;
        Gst.getExecutor().execute(new Runnable() {

            public void run() {
                // Play for 50ms after the seek
                long stop = position + TimeUnit.MILLISECONDS.toNanos(50);
                int flags = SeekFlags.FLUSH | SeekFlags.SEGMENT;
                pipeline.seek(1.0, Format.TIME, flags,
                        SeekType.SET, position, SeekType.SET, stop);
            }
        });
    }
    
    private void seekTo(final long position) {
        isSeeking.set(true);
        seekingPos = position;
        Gst.getExecutor().execute(new Runnable() {

            public void run() {
            	int flags = SeekFlags.FLUSH | SeekFlags.ACCURATE;
                pipeline.seek(1.0, Format.TIME, flags, SeekType.SET, position, SeekType.NONE, -1);
                isSeeking.set(false);
            }
        });
    }
    
    private void segmentDone(final long position) {
        long pos = scaleUnit.toNanos(getValue());
//        System.out.println("Segment done position=" + position 
//                + ", seekingPos=" + seekingPos + ", getValue()=" + pos);
        
        if (pos != seekingPos) {
            //
            // If the slider moved since this segment seek began, just start a new seek
            //
            segmentSeek(pos);
        } else {
            // Continue playing from this position
            Gst.getExecutor().execute(new Runnable() {

            public void run() {
                pipeline.seek(1.0, Format.TIME, 
                    SeekFlags.FLUSH | SeekFlags.KEY_UNIT,
                    SeekType.SET, position, SeekType.SET, -1);
                    pipeline.getState(50, TimeUnit.MILLISECONDS);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            seekFinished();
                        }
                    });
                }
            });
        }
    }
    private void seekFinished() {
        long pos = scaleUnit.toNanos(getValue());
        if (pos != seekingPos) {
            // Slider changed position - start the whole seek process over again.
            segmentSeek(pos);
        } else {
            isSeeking.set(false);
        }
    }
}

