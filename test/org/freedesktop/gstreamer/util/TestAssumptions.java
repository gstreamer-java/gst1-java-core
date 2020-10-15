package org.freedesktop.gstreamer.util;

import org.freedesktop.gstreamer.ElementFactory;
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
    
    /**
     * Assume a GStreamer installation has the required element.
     * 
     * @param elementType element type
     */
    public static void requireElement(String elementType) {
        ElementFactory factory = null;
        try {
            factory = ElementFactory.find(elementType);
        } catch (Exception ex) {}
        Assume.assumeNotNull(factory);
    }
}
