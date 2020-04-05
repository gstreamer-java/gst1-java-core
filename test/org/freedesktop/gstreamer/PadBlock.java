package org.freedesktop.gstreamer;

import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PadBlock {
    private static final int RELIABILITY_TEST_NUM_BLOCKS = 5_000;
    private static final Object MAGIC_COOKIE = new Object();
    private static final Logger LOG = Logger.getLogger(ElementFactory.class.getName());

    private Pipeline pipeline;
    private Element src;

    @BeforeClass
    public static void setUpClass() {
        Gst.init("PadBlockReliabilityTest");
    }

    @AfterClass
    public static void tearDownClass() {
        Gst.deinit();
    }

    @Before
    public void before() {
        pipeline = (Pipeline) Gst
            .parseLaunch("audiotestsrc name=src ! fakesink sync=false");

        pipeline.play();
        src = pipeline.getElementByName("src");
    }

    @After
    public void after() {
        pipeline.stop();
    }

    @Test
    public void block() throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Pad pad = this.src.getStaticPad("src");
        pad.block(() -> {
            assertTrue(pad.isBlocked());
            future.complete(null);
        });
        future.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void blockAndWait() {
        List<String> orderOfEvents = new ArrayList<>();

        Pad pad = this.src.getStaticPad("src");
        orderOfEvents.add("blocking");

        pad.blockAndWait(() -> {
            orderOfEvents.add("blocked");
            assertTrue(pad.isBlocked());
        });

        orderOfEvents.add("after block");
        assertEquals(orderOfEvents, Arrays.asList("blocking", "blocked", "after block"));
    }

    @Test
    public void blockAndWaitWithReturn() {
        List<String> orderOfEvents = new ArrayList<>();

        Pad pad = this.src.getStaticPad("src");
        orderOfEvents.add("blocking");

        Object ret = pad.blockAndWait(() -> {
            orderOfEvents.add("blocked");
            assertTrue(pad.isBlocked());
            return MAGIC_COOKIE;
        });

        orderOfEvents.add("after block");
        assertEquals(ret, MAGIC_COOKIE);

        assertEquals(orderOfEvents, Arrays.asList("blocking", "blocked", "after block"));
    }

    @Test
    public void reliabilityTest() throws InterruptedException, ExecutionException, TimeoutException {
        for (int i = 0; i < RELIABILITY_TEST_NUM_BLOCKS; i++) {
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

        LOG.info(String.format("successfully blocked and unblocked the Pad %s times", RELIABILITY_TEST_NUM_BLOCKS));
    }
}
