package org.freedesktop.gstreamer;

/**
 * The exception you can throw when a pad link operation returns a non-OK result
 */
public class PadLinkException
    extends GstException
{
    public final PadLinkReturn linkResult;

    public PadLinkException(PadLinkReturn result)
    {
        this("failed to link pads ("+result+")", result);
    }

    public PadLinkException(String message, PadLinkReturn result)
    {
        super(message);
        linkResult = result;
    }
}
