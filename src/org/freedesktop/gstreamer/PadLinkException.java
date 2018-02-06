package org.freedesktop.gstreamer;

public class PadLinkException
    extends Exception
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
