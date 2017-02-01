package org.freedesktop.gstreamer.nice;

public class NiceStreamCredentials {
	
	private String ufrag;
	private String pwd;
	
	public NiceStreamCredentials(String ufrag, String pwd) {
		super();
		this.ufrag = ufrag;
		this.pwd = pwd;
	}

	public String getUfrag() {
		return ufrag;
	}

	public String getPwd() {
		return pwd;
	}

}
