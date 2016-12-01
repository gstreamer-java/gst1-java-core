package org.freedesktop.gstreamer.elements;

public class AudioBaseSink<Child extends AudioBaseSink<?>> extends BaseSink {
	
	public static final String GTYPE_NAME = "GstAudioBaseSink";
	
	public enum SlaveMethod {
		RESAMPLE,RETIMESTAMP,SKEW,NONE;
	}

    public AudioBaseSink(Initializer init) {
		super(init);
	}
    
    public Long getAlignmentThreshold() {
		return get("alignment-threshold");
	}
	
	@SuppressWarnings("unchecked")
	public Child setAlignmentThreshold(long alignmentThresholdInNanos){
		set("alignment-threshold", alignmentThresholdInNanos);
		return (Child) this;
	}
    
    public Long getBufferTime() {
		return get("buffer-time");
	}
	
	@SuppressWarnings("unchecked")
	public Child setBufferTime(long bufferTimeInMicros){
		set("buffer-time", bufferTimeInMicros);
		return (Child) this;
	}
    
    public Boolean isPullActivationAllowed() {
		return get("can-activate-pull");
	}
	
	@SuppressWarnings("unchecked")
	public Child setPullActivationAllowed(boolean allowed) {
		set("can-activate-pull", allowed);
		return (Child) this;
	}

    public Long getDiscontinuityWait() {
		return get("discont-wait");
	}
	
	@SuppressWarnings("unchecked")
	public Child setDiscontinuityWait(long discontinuityWaitInNanos){
		set("discont-wait", discontinuityWaitInNanos);
		return (Child) this;
	}

    
    public Long getDriftTolerance() {
		return get("drift-tolerance");
	}
	
	@SuppressWarnings("unchecked")
	public Child setDriftTolerance(long driftToleranceInMicros){
		set("drift-tolerance", driftToleranceInMicros);
		return (Child) this;
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
