package org.freedesktop.gstreamer.util;

import org.freedesktop.gstreamer.Gst;
import org.junit.Assume;

/**
 * These Assumptions skip Tests if the environmental Properties do not meet the Assumption.
 */
public class TestAssumptions {
    /**
     * Assume a GStreamer-Installation of at least the specified Version, ignore the Test if not met.
     *
     * @param major Required Major Version
     * @param minor Required Minor Version
     */
    public static void requireGstVersion(int major, int minor) {
        Assume.assumeTrue(Gst.testVersion(major, minor));
    }
}
