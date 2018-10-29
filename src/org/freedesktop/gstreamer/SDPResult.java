package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.IntegerEnum;
import org.freedesktop.gstreamer.lowlevel.annotations.DefaultEnumValue;

public enum SDPResult {
    OK(0),
    EINVAL(-1),

    @DefaultEnumValue
    __UNKNWOND_NATIVE_VALUE(~0);

    SDPResult(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    private int value;
}
