package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;

public class AudioEncoder<Child extends AudioEncoder<?>> extends Element {
	
    public static final String GTYPE_NAME = "GstAudioEncoder";

	public AudioEncoder(Initializer init) {
		super(init);
	}

	public Boolean isHardResyncEnabled() {
		return get("hard-resync");
	}
	
	@SuppressWarnings("unchecked")
	public Child setHardResyncEnabled(boolean enabled) {
		set("hard-resync", enabled);
		return (Child) this;
	}

	public Boolean isMarkGranuleEnabled() {
		return get("mark-granule");
	}
	
	@SuppressWarnings("unchecked")
	public Child setMarkGranuleEnabled(boolean enabled) {
		set("mark-granule", enabled);
		return (Child) this;
	}

	public Boolean isPerfectTimestampEnabled() {
		return get("perfect-timestamp");
	}
	
	@SuppressWarnings("unchecked")
	public Child setPerfectTimestampEnabled(boolean enabled) {
		set("perfect-timestamp", enabled);
		return (Child) this;
	}

}
