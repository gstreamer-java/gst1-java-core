package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

public class V4l2Src extends PushSrc {
	
	public static final String GST_NAME = "v4l2Src";
    public static final String GTYPE_NAME = "GstV4l2Src";

    public V4l2Src() {
		this((String) null);
	}

    public V4l2Src(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public V4l2Src(Initializer init) {
		super(init);
	}

    public static interface PREPARE_FORMAT {
        /**
         * @param element The element which has the new Pad.
         * @param fd the file descriptor of the current device
         * @param caps the caps of the format being set
         */
        void pepareFormat(Element elem, int fd, Caps caps);
    }
    
    /**
     * Adds a listener for the <code>prepare-format</code> signal
     *
     * @param listener Listener to be called before calling the v4l2 VIDIOC_S_FMT ioctl (set format)
     * on the {@link V4l2Src}
     */
    public void connect(final PREPARE_FORMAT listener) {
        connect(PREPARE_FORMAT.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void pepareFormat(Element elem, int fd, Caps caps) {
                listener.pepareFormat(elem, fd, caps);
            }
        });
    }
    
    /**
     * Removes a listener for the <code>prepare-format</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(PREPARE_FORMAT listener) {
        disconnect(PREPARE_FORMAT.class, listener);
    }



}
