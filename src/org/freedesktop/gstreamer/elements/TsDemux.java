package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;

public class TsDemux extends Element {

	public static final String GST_NAME = "tsdemux";
	public static final String GTYPE_NAME = "GstTSDemux";

	public TsDemux() {
		this((String) null);
	}

	public TsDemux(String name) {
		this(makeRawElement(GST_NAME, name));
	}

	public TsDemux(Initializer init) {
		super(init);
	}

	public Boolean isParsePrivateSectionsEnabled() {
		return get("parse-private-sections");
	}
	
	public TsDemux setParsePrivateSections(boolean enabled) {
		set("parse-private-sections", enabled);
		return this;
	}
	
	public Integer getProgramNumber() {
		return get("program-number");
	}
	
	public TsDemux setProgramNumber(int programNumber) {
		set("program-number", programNumber);
		return this;
	}
	
	public Boolean isEmitStatisticsEnabled() {
		return get("emit-stats");
	}
	
	public TsDemux setEmitStatisticsEnabled(boolean enabled) {
		set("emit.stats", enabled);
		return this;
	}

}
