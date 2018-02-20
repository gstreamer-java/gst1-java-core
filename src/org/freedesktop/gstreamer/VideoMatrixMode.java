package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.EnumMapper;
import org.freedesktop.gstreamer.lowlevel.IntegerEnum;

public enum VideoMatrixMode implements IntegerEnum {
	
	FULL(0), INPUT_ONLY(1), OUTPUT_ONLY(2), NONE(3);
	
	private int value;
	
	private VideoMatrixMode(int value){
		this.value = value;
	}
	
	@Override
	public int intValue() {
		return value;
	}	
	
    public final static  VideoMatrixMode valueOf(int format) {
        return EnumMapper.getInstance().valueOf(format, VideoMatrixMode.class);
    }


}
