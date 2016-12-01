package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.elements.AudioBaseSink.SlaveMethod;

public class AudioBaseSrc<Child extends AudioBaseSrc<?>> extends BaseSrc {
	
    public static final String GTYPE_NAME = "GstAudioBaseSrc";


	public AudioBaseSrc(Initializer init) {
		super(init);
	}

    public Long getActualBufferTime() {
		return get("actual-buffer-time");
	}

    public Long getActualLatencyTime() {
		return get("actual-latency-time");
	}

    public Long getLatencyTime() {
		return get("latency-time");
	}
	
	@SuppressWarnings("unchecked")
	public Child setLatencyTime(long latencyTimeInMicros){
		set("latency-time", latencyTimeInMicros);
		return (Child) this;
	}

	public Boolean isProvideClock() {
		return get("provide-clock");
	}
	
	@SuppressWarnings("unchecked")
	public Child setProvideClock(boolean provideClock) {
		set("provide-clock", provideClock);
		return (Child) this;
	}

	public SlaveMethod getSlaveMethod() {
		return get("slave-method");
	}
	
	@SuppressWarnings("unchecked")
	public Child setSlaveMethod(SlaveMethod method) {
		set("slave-method", method);
		return (Child) this;
	}
}
