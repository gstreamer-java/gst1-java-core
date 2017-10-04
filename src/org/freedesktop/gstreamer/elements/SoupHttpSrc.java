package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;

public class SoupHttpSrc extends Element {

	public static final String GST_NAME = "souphttpsrc";
	public static final String GTYPE_NAME = "GstSoupHTTPSrc";

	public SoupHttpSrc() {
		this((String) null);
	}

	public SoupHttpSrc(String name) {
		this(makeRawElement(GST_NAME, name));
	}

	public SoupHttpSrc(Initializer init) {
		super(init);
	}

	public String getLocation() {
		return get("location");
	}
	
	public SoupHttpSrc setLocation(String location) {
		set("location", location);
		return this;
	}
	
	public Integer getTimeout() {
		return get("timeout");
	}
	
	public SoupHttpSrc setTimeout(int timeoutInSeconds) {
		set("timeout", timeoutInSeconds);
		return this;
	}

	public Boolean isLive() {
		return get("is-live");
	}
	
	public SoupHttpSrc setLive(boolean live) {
		set("is-live", live);
		return this;
	}
	
	public String getUsername() {
		return get("user-id");
	}
	
	public SoupHttpSrc setUsername(String username) {
		set("user-id", username);
		return this;
	}
	
	public String getPassword() {
		return get("user-pw");
	}
	
	public SoupHttpSrc setPassword(String password) {
		set("user-pw", password);
		return this;
	}

}
