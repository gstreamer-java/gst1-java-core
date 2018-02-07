package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Message;

public class Tee extends Element{
	
	public static final String GST_NAME = "tee";
    public static final String GTYPE_NAME = "GstTee";

    public Tee() {
		this((String) null);
	}

    public Tee(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public Tee(Initializer init) {
		super(init);
	}
    
    public int getSourcePadCount() {
    	return get("num-src-pads");
    }
    
    public boolean hasChain(){
    	return get("has-chain");
    }
    
    public Tee setHasChain(boolean hasChain){
    	set("has-chain", hasChain);
    	return this;
    }
    
    public boolean isSilent() {
    	return get("silent");
    }
    
    public Tee setSilent(boolean silent) {
    	set("silent", silent);
    	return this;
    }
    
    public Message getLastMessage() {
    	return get("last-message");
    }

    public boolean allowNotLinked(){
    	return get("allow-not-linked");
    }
    
    public Tee setAllowNotLinked(boolean allowNotLinked){
    	set("allow-not-linked", allowNotLinked);
    	return this;
    }

    
}
