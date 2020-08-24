package org.freedesktop.gstreamer.meta;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Version;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertNotNull;

/**
 * @author Jokertwo
 */
@RunWith(Parameterized.class)
public class GstMetaDataTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Stream.of(GstMetaData.values()).filter(gstMeta -> Version.BASELINE.getMinor() == gstMeta.getMinorVersion()).map(gstMeta -> new Object[]{gstMeta}).collect(Collectors.toList());

    }

    private final GstMetaData gstMetaData;

    @BeforeClass
    public static void beforeClass() {
        Gst.init(new Version(1, 8));
    }

    @AfterClass
    public static void afterClass() {
        Gst.deinit();
    }

    public GstMetaDataTest(GstMetaData gstMetaData) {
        this.gstMetaData = gstMetaData;
    }


    @Test
    public void testGetType() {
        assertNotNull(gstMetaData.getType());
    }
}