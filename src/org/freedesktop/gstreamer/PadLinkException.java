package org.freedesktop.gstreamer;

/**
 * The exception thrown when a pad link operation returns a non-OK result
 */
public class PadLinkException extends GstException {

    private final PadLinkReturn linkResult;

    PadLinkException(PadLinkReturn result) {
        this("failed to link pads (" + result + ")", result);
    }

    PadLinkException(String message, PadLinkReturn result) {
        super(message);
        linkResult = result;
    }

    /**
     * Get the PadLinkReturn result that caused this exception.
     * @return PadLinkReturn
     */
    public PadLinkReturn getLinkResult() {
        return linkResult;
    }
    
}
