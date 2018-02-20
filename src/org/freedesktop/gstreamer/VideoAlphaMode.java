package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.EnumMapper;
import org.freedesktop.gstreamer.lowlevel.IntegerEnum;

public enum VideoAlphaMode implements IntegerEnum {
	
	COPY(0), SET(1), MULT(2);
	
	private int value;
	
	private VideoAlphaMode(int value){
		this.value = value;
	}
	
	@Override
	public int intValue() {
		return value;
	}	
	
    public final static  VideoAlphaMode valueOf(int format) {
        return EnumMapper.getInstance().valueOf(format, VideoAlphaMode.class);
    }


}
