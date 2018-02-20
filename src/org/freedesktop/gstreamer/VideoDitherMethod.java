package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.EnumMapper;
import org.freedesktop.gstreamer.lowlevel.IntegerEnum;

public enum VideoDitherMethod implements IntegerEnum {
	
	NONE(0), VERT_ERR(1), FLOYID_STEINBERG(2), SIERRA_LITE(3), BAYER(4) ;
	
	private int value;
	
	private VideoDitherMethod(int value){
		this.value = value;
	}
	
	@Override
	public int intValue() {
		return value;
	}	
	
    public final static  VideoDitherMethod valueOf(int format) {
        return EnumMapper.getInstance().valueOf(format, VideoDitherMethod.class);
    }


}
