package org.freedesktop.gstreamer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class PadBlockReliabilityTest {
    public static final int NUM_BLOCKS = 5_000;
    private static final Logger LOG = Logger.getLogger(ElementFactory.class.getName());

    @BeforeClass
    public static void setUpClass() {
        Gst.init("PadBlockReliabilityTest");
    }

    @AfterClass
    public static void tearDownClass() {
        Gst.deinit();
    }

    @Test
    public void testPadBlockReliability() throws InterruptedException, ExecutionException, TimeoutException {
        Pipeline pipeline = (Pipeline) Gst
            .parseLaunch("audiotestsrc name=src ! fakesink sync=false");

        Element src = pipeline.getElementByName("src");
        pipeline.play();

        for (int i = 0; i < NUM_BLOCKS; i++) {
            LOG.finest(String.format("> blocking %s", i));
            CompletableFuture<Void> future = new CompletableFuture<>();
            src.getStaticPad("src").block(() -> {
                LOG.finest("= blocked");
                future.complete(null);
                LOG.finest("= completed");
            });
            LOG.finest("< getting");
            future.get(1, TimeUnit.SECONDS);
            LOG.finest("< got");
        }

        LOG.info(String.format("successfully blocked and unblocked the Pad %s times", NUM_BLOCKS));
        pipeline.stop();
    }
}
