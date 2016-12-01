package org.freedesktop.gstreamer.elements;

public class RtpBaseAudioPayload<Child extends RtpBaseAudioPayload<?>> extends RtpBasePayload<Child>{

	public RtpBaseAudioPayload(Initializer init) {
		super(init);
	}
	
    public Boolean isUseBufferListsEnabled() {
		return get("buffer-list");
	}
	
	@SuppressWarnings("unchecked")
	public Child setUseBufferListsEnabled(boolean enabled) {
		set("buffer-list", enabled);
		return (Child) this;
	}

	
	

}
