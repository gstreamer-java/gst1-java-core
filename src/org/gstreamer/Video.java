package org.gstreamer;

import static org.gstreamer.lowlevel.GstVideoAPI.GSTVIDEO_API;

import java.awt.Dimension;

import org.gstreamer.lowlevel.GValueAPI.GValue;

public class Video {
    private Video() {
    }
        
    /**
     * A convenience function to retrieve a GValue holding the framerate from the caps on a pad.
     *
     * The pad needs to have negotiated caps containing a framerate property.
     * 
     * @param pad
     * @return video framerate, otherwise null if the pad has no configured caps or the configured caps do not contain a framerate.
     */
    public static Fraction getVideoFrameRate(Pad pad) {
    	final GValue gvalue = GSTVIDEO_API.gst_video_frame_rate(pad);
    	return gvalue != null ? Fraction.objectFor(gvalue) : null;
    }
    
    /**
     * Inspect the caps of the provided pad and retrieve the width and height of the video frames it is configured for.
     *
     * The pad needs to have negotiated caps containing width and height properties.
     * @param pad
     * @return dimensions of the video, otherwise null if the video size could not be retrieved
     */
    public static Dimension getVideoSize(Pad pad) {
    	int [] width = { 0 };
    	int [] height = { 0 };
    	return GSTVIDEO_API.gst_video_get_size(pad, width, height) ? new Dimension(width [0], height [0]) : null;
    }
}
