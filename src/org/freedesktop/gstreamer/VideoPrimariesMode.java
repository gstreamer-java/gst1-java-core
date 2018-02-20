package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.EnumMapper;
import org.freedesktop.gstreamer.lowlevel.IntegerEnum;

public enum VideoPrimariesMode implements IntegerEnum {
	
	NONE(0), MERGE_ONLY(1), FAST(2);
	
	private int value;
	
	private VideoPrimariesMode(int value){
		this.value = value;
	}
	
	@Override
	public int intValue() {
		return value;
	}	
	
    public final static  VideoPrimariesMode valueOf(int format) {
        return EnumMapper.getInstance().valueOf(format, VideoPrimariesMode.class);
    }


}
