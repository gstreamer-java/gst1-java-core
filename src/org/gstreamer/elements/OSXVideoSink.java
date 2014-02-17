package org.gstreamer.elements;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.gstreamer.Bus;
import org.gstreamer.Message;
import org.gstreamer.Structure;
import org.gstreamer.swing.OSXVideoComponent;

import com.sun.jna.Pointer;

/**
 * 
 * @author dave
 *
 */
public class OSXVideoSink extends BaseSink {
    public static final String GST_NAME = "osxvideosink";
    public static final String GTYPE_NAME = "GstOSXVideoSink";

    public OSXVideoSink(String name) {
        this(makeRawElement(GST_NAME, name));
        set("embed", true);
        setQOSEnabled(false);
    }

    public OSXVideoSink(Initializer init) {
        super(init);
        setQOSEnabled(false);
    }
    
    public void setEmbedded(boolean embed) {
    	set("embed", true);
    }
    
    public boolean isEmbedded() {
    	return (Boolean) get("embed");
    }
    
    //TODO: where is the disconnect pair for this connect?
    public void listenForNewViews(Bus bus) {
    	bus.connect(new Bus.MESSAGE() {
			public void busMessage(Bus bus, Message message) {
				if (message.getSource().getNativeAddress().equals(getNativeAddress())) {
					final Structure structure = message.getStructure();
					if (structure != null && "have-ns-view".equals(structure.getName())) {
						final Pointer nsview = (Pointer) structure.getValue("nsview");
						fireNewVideoComponent(nsview);
					}
				}
			}
		});
    }
    
    public static interface Listener {
    	/** This will always be called on a Swing event dispatch thread. */
    	void newVideoComponent(Object source, OSXVideoComponent osxVideoComponent);
    }
    
    private final ArrayList<Listener> listeners = new ArrayList<Listener> ();
    
    public void addListener(Listener l) {
    	synchronized(listeners) {
    		listeners.add(l);
    	}
    }
    
    public void removeListener(Listener l) {
    	synchronized(listeners) {
    		listeners.remove(l);
    	}
    }
    
    private void fireNewVideoComponent(final Pointer nsview) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final OSXVideoComponent osxVideoComponent = new OSXVideoComponent(nsview);
		    	synchronized(listeners) {
			    	for (Listener l : listeners) {
			    		l.newVideoComponent(this, osxVideoComponent);
			    	}
		    	}
			}
		});
    }
}
