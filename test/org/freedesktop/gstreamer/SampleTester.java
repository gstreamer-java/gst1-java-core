/*
 * Copyright (c) 2020 John Cortell
 *
 * This file is part of gstreamer-java.
 *
 * gstreamer-java is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * gstreamer-java is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with gstreamer-java.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.gstreamer;

import java.util.ArrayList;
import java.util.function.Consumer;
import org.freedesktop.gstreamer.elements.AppSink;
import org.freedesktop.gstreamer.glib.GError;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Utility class for unit testing API that operates on a Sample.
 * <p>
 * Call {@link SampleTester#test(Consumer)} and pass a callback which will
 * perform the test on a Sample it is supplied. The callback runs on the
 * AppSink.NEW_SAMPLE thread. The sample is produced by a simple, ephemeral
 * pipeline that is fed by a video test source.
 */
public class SampleTester {
    public static void test(Consumer<Sample> callback) {
        test(callback, "videotestsrc ! videoconvert ! appsink name=myappsink");
    }

    public static void test(Consumer<Sample> callback, String pipelineDescription) {
        new SampleTester(callback, pipelineDescription, 0);
    }

    public static void test(Consumer<Sample> callback, String pipelineDescription, int skipFrames) {
        new SampleTester(callback, pipelineDescription, skipFrames);
    }

    private static class NewSampleListener implements AppSink.NEW_SAMPLE {
        private Consumer<Sample> callback;
        private final int skipFrames;
        private Throwable exception;
        private int counter = 0;


        NewSampleListener(Consumer<Sample> callback) {
            this(callback, 0);
        }

        NewSampleListener(Consumer<Sample> callback, int skip) {
            this.callback = callback;
            skipFrames = skip;
        }

        @Override
        public FlowReturn newSample(AppSink appSink) {
            if (callback != null) {
                Sample sample = appSink.pullSample();
                if (counter < skipFrames) {
                    counter++;
                    sample.dispose();
                    return FlowReturn.OK;
                }
                try {
                    // Run the client's test logic on the sample (only once)
                    try {
                        callback.accept(sample);
                    }
                    catch (Throwable exc) {
                        exception = exc;
                    }
                    callback = null;
                }
                finally {
                    synchronized (this) {
                        notify();
                    }
                    sample.dispose();
                }
            }
            return FlowReturn.OK;
        }
    }

    private SampleTester(Consumer<Sample> callback, String pipelineDescription, int skipFrames) {
        assertNotNull("Pipeline description can not be null", pipelineDescription);
        assertFalse("Pipeline description can not be empty", pipelineDescription.isEmpty());
        ArrayList<GError> errors = new ArrayList<GError>();
        Bin bin = Gst.parseBinFromDescription(pipelineDescription, false, errors);
        assertNotNull("Unable to create Bin from pipeline description: ", bin);

        AppSink appSink = (AppSink)bin.getElementByName("myappsink");
        appSink.set("emit-signals", true);

        NewSampleListener sampleListener = new NewSampleListener(callback, skipFrames);
        appSink.connect(sampleListener);

        bin.play();

        // Wait for the sample to arrive and for the client supplied test function to
        // complete
        try {
            synchronized (sampleListener) {
                sampleListener.wait();
            }
        } catch (InterruptedException e) {
            fail("Unexpected interruption waiting for sample");
        }

        bin.stop();
        appSink.disconnect(sampleListener);

        // If the test threw an exception on the sample listener thread, throw it here
        // (on the main thread)
        if (sampleListener.exception != null) {
            throw new AssertionError(sampleListener.exception);
        }
    }
}
