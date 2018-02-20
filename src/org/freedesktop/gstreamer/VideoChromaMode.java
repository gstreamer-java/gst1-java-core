package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.EnumMapper;
import org.freedesktop.gstreamer.lowlevel.IntegerEnum;

public enum VideoChromaMode implements IntegerEnum {
	
	FULL(0), UPSAMPLE_ONLY(1), DOWNSAMPLE_ONLY(2), NONE(3);
	
	private int value;
	
	private VideoChromaMode(int value){
		this.value = value;
	}
	
	@Override
	public int intValue() {
		return value;
	}	
	
    public final static  VideoChromaMode valueOf(int format) {
        return EnumMapper.getInstance().valueOf(format, VideoChromaMode.class);
    }


}
