package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.IntegerEnum;

public enum WebRTCSDPType implements IntegerEnum {
    OFFER(1),
    PRANSWER(2),
    ANSWER(3),
    ROLLBACK(4);

    WebRTCSDPType(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
    private final int value;
}
