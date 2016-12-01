package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

public class TypeFind extends Element {
	
	public static final String GST_NAME = "typefind";
    public static final String GTYPE_NAME = "GstTypeFindElement";
    
    public TypeFind() {
    	this((String)null);
    }
    

    public TypeFind(String name) {
        super(makeRawElement(GST_NAME, name));
    }

    public TypeFind(Initializer init) {
		super(init);
	}

    /**
     * Signal is emitted when a pad for which there is no further possible decoding is added to the {@link DecodeBin}.
     */
    public static interface HAVE_TYPE {
        /**
         * @param element The element which has the new Pad.
         * @param pad the new Pad.
         * @param caps the caps of the pad that cannot be resolved.
         */
        void typeFound(Element elem, int probability, Caps caps);
    }
    
    /**
     * Adds a listener for the <code>unknown-type</code> signal
     *
     * @param listener Listener to be called when a new {@link Pad} is encountered
     * on the {@link Element}
     */
    public void connect(final HAVE_TYPE listener) {
        connect(HAVE_TYPE.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Element elem, int probability, Caps caps) {
                listener.typeFound(elem, probability, caps);
            }
        });
    }
    /**
     * Removes a listener for the <code>unknown-type</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(HAVE_TYPE listener) {
        disconnect(HAVE_TYPE.class, listener);
    }

    
	

}
