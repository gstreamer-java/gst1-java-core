package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.EnumMapper;
import org.freedesktop.gstreamer.lowlevel.IntegerEnum;

public enum VideoScalingMethod implements IntegerEnum {
	
	NEAREST(0), 
	BILINEAR(1), 
	FOUR_TAP(2), 
	LANCZOS(3), 
	BILINEAR2(4),
	SINC(5), 
	HERMITE(6), 
	SPLINE(7), 
	CATMULL_ROM(8), 
	MITCHELL(9);
	
	private int value;
	
	private VideoScalingMethod(int value){
		this.value = value;
	}
	
	@Override
	public int intValue() {
		return value;
	}	
	
    public final static  VideoScalingMethod valueOf(int format) {
        return EnumMapper.getInstance().valueOf(format, VideoScalingMethod.class);
    }


}
