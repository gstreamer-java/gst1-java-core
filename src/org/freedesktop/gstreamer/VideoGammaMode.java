package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.EnumMapper;
import org.freedesktop.gstreamer.lowlevel.IntegerEnum;

public enum VideoGammaMode implements IntegerEnum {
	
	NONE(0), REMAP(1);
	
	private int value;
	
	private VideoGammaMode(int value){
		this.value = value;
	}
	
	@Override
	public int intValue() {
		return value;
	}	
	
    public final static  VideoGammaMode valueOf(int format) {
        return EnumMapper.getInstance().valueOf(format, VideoGammaMode.class);
    }


}
