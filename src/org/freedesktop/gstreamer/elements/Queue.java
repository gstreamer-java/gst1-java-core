package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

public class Queue extends Element{
	
	public static final String GST_NAME = "queue";
    public static final String GTYPE_NAME = "GstQueue";

    public Queue() {
		this((String) null);
	}

    public Queue(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public Queue(Initializer init) {
		super(init);
	}
    
    public Integer getCurrentLevelBuffers() {
    	return get("current-level-buffers");
    }

    public Integer getCurrentLevelBytes() {
    	return get("current-level-bytes");
    }

    public Integer getCurrentLevelTime() {
    	return get("current-level-time");
    }
    
    public Boolean isFlushOnEOSEnabled() {
    	return get("flush-on-eos");
    }
    
    public Queue setFlushOnEosEnabled(boolean enabled) {
    	set("flush-on-eos", enabled);
    	return this;
    }
    
    public Boolean isLeaky() {
    	return get("leaky");
    }
    
    public Queue setLeaky(boolean leaky) {
    	set("leaky", leaky);
    	return this;
    }
    
	public Integer getMaxSizeBuffers() {
		return get("max-size-buffers");
	}
	
	public Queue setMaxSizeBuffers(int maxSizeBuffers) {
		set("max-size-buffers", maxSizeBuffers);
		return this;
	}

	public Integer getMaxSizeBytes() {
		return get("max-size-bytes");
	}
	
	public Queue setMaxSizeBytes(int maxSizeBytes) {
		set("max-size-bytes", maxSizeBytes);
		return this;
	}
	
	public Long getMaxSizeTimeIn() {
		return get("max-size-time");
	}
	
	public Queue setMaxSizeTime(long maxSizeTimeInNs) {
		set("max-size-time", maxSizeTimeInNs);
		return this;
	}
	
	public Integer getMinThresholdBuffers() {
		return get("min-threshold-buffers");
	}
	
	public Queue setMinThresholdBuffers(int minThresholdBuffers) {
		set("min-threshold-buffers", minThresholdBuffers);
		return this;
	}

	public Integer getMinThresholdBytes() {
		return get("min-threshold-bytes");
	}
	
	public Queue setMinThresholdBytes(int minThresholdBytes) {
		set("min-threshold-bytes", minThresholdBytes);
		return this;
	}

	public Integer getMinThresholdTime() {
		return get("min-threshold-time");
	}
	
	public Queue setMinThresholdTime(long minThresholdTimeInNs) {
		set("min-threshold-time", minThresholdTimeInNs);
		return this;
	}

	public Boolean isSilent() {
		return get("silent");
	}
	
	public Queue setSilent(boolean silent) {
		set("silent", silent);
		return this;
	}
	
	/**
	 * Reports that the buffer became full (overrun). A buffer is full if the total 
	 * amount of data inside it (num-buffers, time, size) is higher than the boundary 
	 * values which can be set through the GObject properties.
     * 
     * @see #connect(OVERRUN)
     * @see #disconnect(OVERRUN)
     */
    public static interface OVERRUN {
        /**
         * @param queue the object which received the signal
         */
        public void overrun(Queue queue);
    }
    /**
     * Add a listener for the <code>overrun</code> signal on this Queue
     * 
     * @param listener The listener to be called.
     */
    public void connect(final OVERRUN listener) {
        connect(OVERRUN.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Queue queue) {
                listener.overrun(queue);
            }
        });
    }    
    /**
     * Disconnect the listener for the <code>overrun</code> signal on this Queue
     * 
     * @param listener The listener that was registered to receive the signal.
     */
    public void disconnect(OVERRUN listener) {
        disconnect(OVERRUN.class, listener);
    }

	/**
	 * Reports that enough (min-threshold) data is in the queue. Use this signal 
	 * together with the underrun signal to pause the pipeline on underrun and 
	 * wait for the queue to fill-up before resume playback.
     * 
     * @see #connect(RUNNING)
     * @see #disconnect(RUNNING)
     */
    public static interface RUNNING {
        /**
         * @param queue the object which received the signal
         */
        public void running(Queue queue);
    }
    /**
     * Add a listener for the <code>running</code> signal on this Queue
     * 
     * @param listener The listener to be called.
     */
    public void connect(final RUNNING listener) {
        connect(RUNNING.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Queue queue) {
                listener.running(queue);
            }
        });
    }    
    /**
     * Disconnect the listener for the <code>running</code> signal on this Queue
     * 
     * @param listener The listener that was registered to receive the signal.
     */
    public void disconnect(RUNNING listener) {
        disconnect(RUNNING.class, listener);
    }

	/**
	 * Reports that the buffer became empty (underrun). A buffer is empty 
	 * if the total amount of data inside it (num-buffers, time, size) is 
	 * lower than the boundary values which can be set through the GObject properties.
     * 
     * @see #connect(UNDERRUN)
     * @see #disconnect(UNDERRUN)
     */
    public static interface UNDERRUN {
        /**
         * @param queue the object which received the signal
         */
        public void underrun(Queue queue);
    }
    /**
     * Add a listener for the <code>underrun</code> signal on this Queue
     * 
     * @param listener The listener to be called.
     */
    public void connect(final UNDERRUN listener) {
        connect(UNDERRUN.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Queue queue) {
                listener.underrun(queue);
            }
        });
    }    
    /**
     * Disconnect the listener for the <code>underrun</code> signal on this Queue
     * 
     * @param listener The listener that was registered to receive the signal.
     */
    public void disconnect(UNDERRUN listener) {
        disconnect(UNDERRUN.class, listener);
    }

    /**
     *  Reports when the queue has enough data to start pushing data again on the source pad.
     *  
     * @see #connect(PUSHING)
     * @see #disconnect(PUSHING)
     */
    public static interface PUSHING {
        /**
         * @param queue the object which received the signal
         */
        public void pushing(Queue queue);
    }
    /**
     * Add a listener for the <code>pushing</code> signal on this Queue
     * 
     * @param listener The listener to be called.
     */
    public void connect(final PUSHING listener) {
        connect(PUSHING.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Queue queue) {
                listener.pushing(queue);
            }
        });
    }    
    /**
     * Disconnect the listener for the <code>pushing</code> signal on this Queue
     * 
     * @param listener The listener that was registered to receive the signal.
     */
    public void disconnect(PUSHING listener) {
        disconnect(PUSHING.class, listener);
    }
}
