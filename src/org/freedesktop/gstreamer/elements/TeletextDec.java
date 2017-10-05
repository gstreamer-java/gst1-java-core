package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;

public class TeletextDec extends Element{
	
	public static final String GST_NAME = "teletextdec";
    public static final String GTYPE_NAME = "GstTeletextDec";

    public TeletextDec() {
		this((String) null);
	}

    public TeletextDec(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public TeletextDec(Initializer init) {
		super(init);
	}
    
    public Integer getPage() {
    	return get("page");
    }

    public TeletextDec setPage(int page) {
    	set("page", page);
    	return this;
    }

    public Integer getSubPage() {
    	return get("subpage");
    }

    public TeletextDec setSubPage(int subPage) {
    	set("subpage", subPage);
    	return this;
    }

    public Boolean isSubtitlesModeEnabled() {
    	return get("subtitles-mode");
    }
    
    public TeletextDec setSutitlesModeEnabled(boolean enabled) {
    	set("subtitles-mode", enabled);
    	return this;
    }
    
    public String getSubtitlesTemplate() {
    	return get("subtitles-template");
    }
    
    public TeletextDec setSubtitlesTemplate(String template) {
    	set("subtitles-template", template);
    	return this;
    }

    public String getFontDescription() {
    	return get("font-description");
    }
    
    public TeletextDec setFontDescription(String description) {
    	set("font-description", description);
    	return this;
    }
   
}
