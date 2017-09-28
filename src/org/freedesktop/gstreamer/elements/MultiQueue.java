package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

public class MultiQueue extends Element{

	public static final String GST_NAME = "multiqueue";
	public static final String GTYPE_NAME = "GstMultiQueue";

	public MultiQueue() {
		this((String) null);
	}

	public MultiQueue(String name) {
		this(makeRawElement(GST_NAME, name));
	}

	public MultiQueue(Initializer init) {
		super(init);
	}

	public Integer getExtraSizeBytes() {
		return get("extra-size-bytes");
	}

	public MultiQueue setExtraSizeBytes(int size) {
		set("extra-size-bytes", size);
		return this;
	}

	public Integer getExtraSizeBuffers() {
		return get("extra-size-buffers");
	}

	public MultiQueue setExtraSizeBuffers(int size) {
		set("extra-size-buffers", size);
		return this;
	}

	public Long getExtraSizeTime() {
		return get("extra-size-time");
	}

	public MultiQueue setExtraSizeTime(long sizeInNanoseconds) {
		set("extra-size-time", sizeInNanoseconds);
		return this;
	}

	public Integer getMaxSizeBuffers() {
		return get("max-size-buffers");
	}

	public MultiQueue setMaxSizeBuffers(int maxSizeBuffers) {
		set("max-size-buffers", maxSizeBuffers);
		return this;
	}

	public Integer getMaxSizeBytes() {
		return get("max-size-bytes");
	}

	public MultiQueue setMaxSizeBytes(int maxSizeBytes) {
		set("max-size-bytes", maxSizeBytes);
		return this;
	}

	public Long getMaxSizeTime() {
		return get("max-size-time");
	}

	public MultiQueue setMaxSizeTime(long maxSizeTimeInNanos) {
		set("max-size-time", maxSizeTimeInNanos);
		return this;
	}	

	public Boolean isBufferingEnabled() {
		return get("use-buffering");
	}

	public MultiQueue setBufferingEnabled(boolean enabled) {
		set("use-buffering", enabled);
		return this;
	}

	@Deprecated
	public Integer getLowPercent() {
		return get("low-percent");
	}

	@Deprecated
	public MultiQueue setLowPercent(int lowPercent) {
		set("low-percent", lowPercent);
		return this;
	}

	@Deprecated
	public Integer getHighPercent() {
		return get("high-percent");
	}

	@Deprecated
	public MultiQueue setHighPercent(int highPercent) {
		set("high-percent", highPercent);
		return this;
	}
	
	public Double getLowWatermark() {
		return get("low-watermark");
	}

	public MultiQueue setLowWatermark(double normalizedLowWatermark) {
		set("low-watermark", normalizedLowWatermark);
		return this;
	}

	public Double getHighWatermark() {
		return get("high-watermark");
	}

	public MultiQueue setHighWatermark(double normalizedHighWatermark) {
		set("high-watermark", normalizedHighWatermark);
		return this;
	}

	public Boolean isSynchronizationByRunningTimeEnabled() {
		return get("sync-by-running-time");
	}

	public MultiQueue setSynchronizationByRunningTimeEnabled(boolean enabled) {
		set("sync-by-running-time", enabled);
		return this;
	}

	public Boolean isInterleavingEnabled() {
		return get("use-interleave");
	}

	public MultiQueue setInterleavingEnabled(boolean enabled) {
		set("use-interleave", enabled);
		return this;
	}

	public Long getUnlinkedCacheTime() {
		return get("unlinked-cache-time");
	}

	public MultiQueue setUnlinkedCacheTime(long timeInNanos) {
		set("unlinked-cache-time", timeInNanos);
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
		public void overrun(MultiQueue queue);
	}
	/**
	 * Add a listener for the <code>overrun</code> signal on this Queue
	 * 
	 * @param listener The listener to be called.
	 */
	public void connect(final OVERRUN listener) {
		connect(OVERRUN.class, listener, new GstCallback() {
			@SuppressWarnings("unused")
			public void callback(MultiQueue queue) {
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
		public void underrun(MultiQueue queue);
	}
	/**
	 * Add a listener for the <code>underrun</code> signal on this Queue
	 * 
	 * @param listener The listener to be called.
	 */
	public void connect(final UNDERRUN listener) {
		connect(UNDERRUN.class, listener, new GstCallback() {
			@SuppressWarnings("unused")
			public void callback(MultiQueue queue) {
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

	
}
