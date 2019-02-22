/* 
 * Copyright (C) 2019 Neil C Smith
 * Copyright (C) 2009 Levente Farkas <lfarkas@lfarkas.org>
 * Copyright (C) 2009 Tamas Korodi <kotyo@zamba.fm>
 * Copyright (C) 2008 Wayne Meissner
 * Copyright (C) 2003 Ronald Bultje <rbultje@ronald.bitfreak.net>
 * Copyright (C) 2014 Claus Holst <claus@b-team.dk>
 * 
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.gstreamer.interfaces;

import static org.freedesktop.gstreamer.lowlevel.GstVideoOverlayAPI.GSTVIDEOOVERLAY_API;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.BusSyncReply;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.message.Message;


/**
 * Interface for setting/getting a window system resource on elements
 * supporting it to configure a window into which to render a video.
 */
public class VideoOverlay extends GstInterface {
    /**
     * Wraps the {@link Element} in a <tt>XOverlay</tt> interface
     * 
     * @param element the element to use as a <tt>XOverlay</tt>
     * @return a <tt>XOverlay</tt> for the element
     */
    public static VideoOverlay wrap(Element element) {
        return new VideoOverlay(element);
    }

    /**
     * Convenience function to check if the given message is a "prepare-window-handle".
     * This useful for setup native window handles with {@link BusSyncReply}.
     * 
     * @param message
     * @return
     */
    public static boolean isPrepareWindowHandleMessage(Message message) {
        return GSTVIDEOOVERLAY_API.gst_is_video_overlay_prepare_window_handle_message(message);
    }

    /**
     * Creates a new <tt>VideoOverlay</tt> instance
     * 
     * @param element the element that implements the overlay interface
     */
    private VideoOverlay(Element element) {
        super(element, GSTVIDEOOVERLAY_API.gst_video_overlay_get_type());
    }
    
    /**
     * Sets the native window for the {@link Element} to use to display video.
     *
     * @param handle A native handle to use to display video.
     */
    public void setWindowHandle(long handle) {
      GSTVIDEOOVERLAY_API.gst_video_overlay_set_window_handle(this, new Pointer(handle));
    }
       
    /**
     * Tell an overlay that it has been exposed. This will redraw the current frame
     * in the drawable even if the pipeline is PAUSED.
     */
    public void expose() {
      GSTVIDEOOVERLAY_API.gst_video_overlay_expose(this);
    }
    
    /**
     * Tell an overlay that it should handle events from the window system. 
     * These events are forwared upstream as navigation events. In some window 
     * system, events are not propagated in the window hierarchy if a client is 
     * listening for them. This method allows you to disable events handling 
     * completely from the XOverlay.
     */
    public void handleEvent(boolean handle_events) {
      GSTVIDEOOVERLAY_API.gst_video_overlay_handle_events(this, handle_events);
    }
    
    /**
     * Configure a subregion as a video target within the window set by 
     * {@link #setWindowHandle(long)}. If this is not used or not supported 
     * the video will fill the area of the window set as the overlay to 100%. 
     * By specifying the rectangle, the video can be overlaid to a specific 
     * region of that window only. After setting the new rectangle one should 
     * call {@link #expose()} to force a redraw. To unset the region pass -1 
     * for the width and height parameters.
     * 
     * This method is needed for non fullscreen video overlay in UI toolkits 
     * that do not support subwindows.
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public boolean setRenderRectangle(int x, int y, int width, int height) {
    	return GSTVIDEOOVERLAY_API.gst_video_overlay_set_render_rectangle(this, x, y, width, height);
    }

}
