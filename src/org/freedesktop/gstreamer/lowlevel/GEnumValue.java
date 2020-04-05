package org.freedesktop.gstreamer.lowlevel;

import com.sun.jna.Pointer;

import java.util.Arrays;
import java.util.List;

public class GEnumValue extends com.sun.jna.Structure {
    public int value;
    public String value_name;
    public String value_nick;

    public GEnumValue() {
        super();
    }

    public GEnumValue(Pointer pointer) {
        super(pointer);
        read();
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("value", "value_name", "value_nick");
    }
}
