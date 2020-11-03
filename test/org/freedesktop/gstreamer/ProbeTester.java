/*
 * Copyright (c) 2020 Neil C Smith
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

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

/**
 * Utility class for unit testing API that operates on a Probe.
 * <p>
 * Call {@link ProbeTester#test(Consumer)} and pass a callback which will
 * perform the test on a PadProbeInfo it is supplied. The callback runs in a Pad
 * probe. The test uses a simple, ephemeral pipeline that is fed by a video test
 * source (or custom pipeline).
 * <p>
 * The callback is a {@link Predicate} and should return false if the input info
 * doesn't match that required by the test. Test exceptions should be thrown as
 * normal. This is to allow the probe to be called repeatedly until the input
 * info matches that expected. If the probe never matches the test will time
 * out.
 */
public class ProbeTester {

    /**
     * Run a probe test on a simple test pipeline. The callback will be called
     * by the probe until it returns true, allowing for probe callbacks to be
     * ignored. If the callback never returns true the test will timeout and
     * fail.
     * <p>
     * The pipeline is <code>videotestsrc ! fakesink name=sink</code>. The probe
     * will be attached to the sink pad of the sink element.
     *
     * @param mask PadProbeType mask to use when attaching probe to sink pad
     * @param callback probe callback
     */
    public static void test(Set<PadProbeType> mask, Predicate<PadProbeInfo> callback) {
        test("videotestsrc ! fakesink name=sink", mask, callback);
    }

    /**
     * Run a probe test on a simple test pipeline. The callback will be called
     * by the probe until it returns true, allowing for probe callbacks to be
     * ignored. If the callback never returns true the test will timeout and
     * fail.
     * <p>
     * The pipeline must have a sink element named sink. The probe will be
     * attached to the sink pad of the sink element.
     *
     * @param pipeline custom pipeline with named sink element
     * @param mask PadProbeType mask to use when attaching probe to sink pad
     * @param callback probe callback
     */
    public static void test(String pipeline, Set<PadProbeType> mask, Predicate<PadProbeInfo> callback) {
        assertNotNull("Pipeline description can not be null", pipeline);
        assertFalse("Pipeline description can not be empty", pipeline.isEmpty());
        Pipeline pipe = (Pipeline) Gst.parseLaunch(pipeline);
        assertNotNull("Unable to create Pipeline from pipeline description: ", pipe);

        Element sink = pipe.getElementByName("sink");
        Pad pad = sink.getStaticPad("sink");
        PadProbe probe = new PadProbe(callback);
        pad.addProbe(mask, probe);

        pipe.play();

        // Wait for the probe to complete
        try {
            probe.await(5000);
        } catch (TimeoutException ex) {
            fail("Timed out waiting for probe condition\n" + ex);
        } catch (Exception ex) {
            fail("Unexpected exception waiting for probe\n" + ex);
        } finally {
            pipe.stop();
        }

        // If the test threw an exception on the sample listener thread, throw it here
        // (on the main thread)
        if (probe.exception != null) {
            throw new AssertionError(probe.exception);
        }
    }

    private static class PadProbe implements Pad.PROBE {

        private final CountDownLatch latch;
        private final Predicate<PadProbeInfo> callback;

        private Throwable exception;

        PadProbe(Predicate<PadProbeInfo> callback) {
            this.callback = callback;
            latch = new CountDownLatch(1);
        }

        @Override
        public PadProbeReturn probeCallback(Pad pad, PadProbeInfo info) {
            if (latch.getCount() > 0) {
                try {
                    if (callback.test(info)) {
                        latch.countDown();
                    }
                } catch (Throwable exc) {
                    exception = exc;
                    latch.countDown();
                }
            }
            return PadProbeReturn.OK;
        }

        void await(long millis) throws InterruptedException, TimeoutException {
            if (!latch.await(millis, TimeUnit.MILLISECONDS)) {
                throw new TimeoutException();
            }
        }

    }

}
