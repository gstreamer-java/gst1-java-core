package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

import com.sun.jna.ptr.LongByReference;

public class InputSelector extends Element {
    public static final String GST_NAME = "input-selector";
    public static final String GTYPE_NAME = "GstInputSelector";

    public InputSelector(String name) {
        this(makeRawElement(GST_NAME, name));
    }  

    public InputSelector(Initializer init) {
        super(init);
    }
}
