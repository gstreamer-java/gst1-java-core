package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.IntegerEnum;

public enum SDPResult {
    OK(0),
    EINVAL(-1);

    SDPResult(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    private int value;
}
