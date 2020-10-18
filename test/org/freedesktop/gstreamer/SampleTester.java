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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.freedesktop.gstreamer.elements.AppSink;
import org.freedesktop.gstreamer.glib.GError;

/**
 * Utility class for unit testing API that operates on a Sample.
 *
 * Call {@link SampleTester#test(Consumer)} and pass a callback which will
 * perform the test on a Sample it is supplied. The callback runs on the
 * AppSink.NEW_SAMPLE thread. The sample is produced by a simple, ephemeral
 * pipeline that is fed by a video test source.
 */
public class SampleTester {
    public static void test(Consumer<Sample> callback) {
        new SampleTester(callback);
    }

    private static class NewSampleListener implements AppSink.NEW_SAMPLE {
        private Consumer<Sample> callback;
        Throwable exception;

        NewSampleListener(Consumer<Sample> callback) {
            this.callback = callback;
        }

        @Override
        public FlowReturn newSample(AppSink appSink) {
            if (callback != null) {
                Sample sample = appSink.pullSample();
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

    private SampleTester(Consumer<Sample> callback) {
        ArrayList<GError> errors = new ArrayList<GError>();
        String pipeline_descr = "videotestsrc is-live=true ! videoconvert ! appsink name=myappsink";
        Bin bin = Gst.parseBinFromDescription(pipeline_descr, false, errors);
        assertNotNull("Unable to create Bin from pipeline description: ", bin);

        AppSink appSink = (AppSink)bin.getElementByName("myappsink");
        appSink.set("emit-signals", true);

        NewSampleListener sampleListener = new NewSampleListener(callback);
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
