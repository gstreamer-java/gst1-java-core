/*
 * Copyright (c) 2020 Christophe Lafolet
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstQueryAPI;
import org.freedesktop.gstreamer.query.Query;
import org.freedesktop.gstreamer.query.QueryType;

public class QueryProbeTester {
    public static void test(Consumer<Query> callback, QueryType queryType) {
        test(callback, queryType, "videotestsrc ! fakesink name=sink");
    }

    public static void test(Consumer<Query> callback, QueryType queryType, String pipelineDescription) {
        assertNotNull("Pipeline description can not be null", pipelineDescription);
        assertFalse("Pipeline description can not be empty", pipelineDescription.isEmpty());
        Pipeline pipe = (Pipeline) Gst.parseLaunch(pipelineDescription);
        assertNotNull("Unable to create Pipeline from pipeline description: ", pipe);

        Element sink = pipe.getElementByName("sink");
        Pad pad = sink.getStaticPad("sink");
        QueryProbe probe = new QueryProbe(callback, queryType);
        pad.addQueryProbe(probe);

        pipe.play();

        // Wait for the sample to arrive and for the client supplied test function to
        // complete
        try {
            probe.await(5000);
        } catch (Throwable t) {
            fail("Unexpected exception waiting for query\n" + t);
        } finally {
            pipe.stop();
            pad.removeQueryProbe(probe);
        }

        // If the test threw an exception on the sample listener thread, throw it here
        // (on the main thread)
        if (probe.exception != null) {
            throw new AssertionError(probe.exception);
        }
    }

    private static class QueryProbe implements Pad.QUERY_PROBE {

    	private final Consumer<Query> callback;
        private final QueryType expectedQueryType;
        private final CountDownLatch latch;

        private Throwable exception;

        QueryProbe(Consumer<Query> callback, QueryType aQueryType) {
        	this.callback = callback;
            this.expectedQueryType = aQueryType;
            latch = new CountDownLatch(1);
        }

        @Override
        public PadProbeReturn queryReceived(Pad pad, Query query) {
            GstQueryAPI.QueryStruct struct = new GstQueryAPI.QueryStruct(Natives.getRawPointer(query));
            QueryType type = (QueryType) struct.readField("type");
        	
            if (type == expectedQueryType) {
                try {
                    // Run the client's test logic on the query (only once)
                    try {
                    	if (callback != null)
                    		callback.accept(query);
                    } catch (Throwable exc) {
                        exception = exc;
                    }
                } finally {
                    latch.countDown();
                }
            }
            return PadProbeReturn.OK;
        }

        void await(long millis) throws Throwable {
            if (!latch.await(millis, TimeUnit.MILLISECONDS)) {
                throw new TimeoutException();
            }
            if (this.exception != null) {
            	throw this.exception;
            }
            	
        }
    }
    
}
