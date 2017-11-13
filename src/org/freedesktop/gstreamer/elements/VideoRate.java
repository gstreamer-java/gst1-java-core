package org.freedesktop.gstreamer.elements;

public class VideoRate extends BaseTransform {
	
	public static final String GST_NAME = "videorate";
    public static final String GTYPE_NAME = "GstVideoRate";

    public VideoRate() {
		this((String) null);
	}

    public VideoRate(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public VideoRate(Initializer init) {
		super(init);
	}

    public long getDroppedFramesCount() {
    	return get("drop");
    }

    public long getDuplicatedFramesCount() {
    	return get("duplicate");
    }

    public long getInputFramesCount() {
    	return get("in");
    }

    public long getOutputFramesCount() {
    	return get("out");
    }

    public boolean isDropOnlyEnabled() {
    	return get("drop-only");
    }
    
    public VideoRate setDropOnlyEnabled(boolean enabled) {
    	set("drop-only",enabled);
    	return this;
    }
    
    public long getAveragePeriod() {
    	return get("average-period");
    }

    public VideoRate setAveragePeriod(long averagePeriod) {
    	set("average-period", averagePeriod);
    	return this;
    }

    public int getMaximumAllowedFramesPerSecond() {
    	return get("max-rate");
    }
    
    public VideoRate setMaximumAllowedFramesPerSecond(int maximumFramesPerSecond) {
    	set("max-rate", maximumFramesPerSecond);
    	return this;
    }
    
    public double getNewFramesPreferenceWeight() {
    	return get("new-pref");
    }

    public VideoRate setNewFramesPreferenceWeight(double normalizedNewFramePreferenceWeight) {
    	set("new-pref", normalizedNewFramePreferenceWeight);
    	return this;
    }

    public boolean isSilentEnabled() {
    	return get("silent");
    }
    
    public VideoRate setSilentEnabled(boolean enabled) {
    	set("silent",enabled);
    	return this;
    }

    public boolean isSkipToFirstEnabled() {
    	return get("skip-to-first");
    }
    
    public VideoRate setSkipToFirstEnabled(boolean enabled) {
    	set("skip-to-first",enabled);
    	return this;
    }

    public double getRate() {
    	return get("rate");
    }

    public VideoRate setRate(double rate) {
    	set("rate", rate);
    	return this;
    }
}
