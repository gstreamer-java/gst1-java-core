package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.EnumMapper;
import org.freedesktop.gstreamer.lowlevel.IntegerEnum;

public enum VideoResamplerMethod implements IntegerEnum {
	
	NEAREST(0), LINEAR(1), CUBIC(2), SINC(3), LANCZOS(4) ;
	
	private int value;
	
	private VideoResamplerMethod(int value){
		this.value = value;
	}
	
	@Override
	public int intValue() {
		return value;
	}	
	
    public final static  VideoResamplerMethod valueOf(int format) {
        return EnumMapper.getInstance().valueOf(format, VideoResamplerMethod.class);
    }


}
