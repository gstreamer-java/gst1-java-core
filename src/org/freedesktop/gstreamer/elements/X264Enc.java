package org.freedesktop.gstreamer.elements;

public class X264Enc extends VideoEncoder {
	
	public static final String GST_NAME = "x264enc";
    public static final String GTYPE_NAME = "GstX264Enc";
    
	public static enum TunePreset {
		NO_TUNNING(0x0),STILLIMAGE(0x1),FASTDECODE(0x2),ZEROLATENCY(0x4);
		
		private long flags;
		
		private TunePreset(long flags) {
			this.flags = flags;
		}
		
		public long toGstX264Enc() {
			return flags;
		}
		
		public static TunePreset fromGstTunePreset(long gstX264Enc) {
			for(TunePreset candidate : values()) {
				if (candidate.toGstX264Enc() == gstX264Enc) {
					return candidate;
				}
			}
			return null;
		}
	}

	public static enum SpeedPreset {
		NONE(0x0),ULTRAFAST(0x1),SUPERFAST(0x2),VERYFAST(0x3),FASTER(0x4),FAST(0x5),MEDIUM(0x6),SLOW(0x7),SLOWER(0x8),VERYSLOW(0x9),PLACEBO(0x10);
		
		private long flags;
		
		private SpeedPreset(long flags) {
			this.flags = flags;
		}
		
		public long toGstX264EncPreset() {
			return flags;
		}
		
		public static SpeedPreset fromGstTunePreset(long gstX264EncPreset) {
			for(SpeedPreset candidate : values()) {
				if (candidate.toGstX264EncPreset() == gstX264EncPreset) {
					return candidate;
				}
			}
			return null;
		}
	}

	


    public X264Enc() {
		this((String) null);
	}

    public X264Enc(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public X264Enc(Initializer init) {
		super(init);
	}

	public int getBitrate() {
		return get("bitrate");
	}
	
	public X264Enc setBitrate(int videoBitrateInKbps) {
		set("bitrate",videoBitrateInKbps);	
		return this;
	}
	
	public Boolean isIntraRefreshEnabled() {
		return get("intra-refresh");
	}
	
	public X264Enc setIntraRefreshEnabled(boolean enabled) {
		set("intra-refresh", enabled);
		return this;
	}
	
	public int getMaxKeyFrameInterval() {
		return get("key-int-max");
	}
	
	public X264Enc setMaxKeyFrameInterval(int maximumFramesBetweenKeyFrames) {
		set("key-int-max", maximumFramesBetweenKeyFrames);
		return this;
	}
	
	public TunePreset getTunePreset() {
		return TunePreset.fromGstTunePreset((Long) get("tune"));
	}
	
	public X264Enc setTunePreset(TunePreset tunePreset) {
		set("tune", tunePreset.toGstX264Enc());
		return this;
	}
	
	public SpeedPreset getSpeedPreset() {
		return SpeedPreset.fromGstTunePreset((Long) get("speed-preset"));
	}
	
	public X264Enc setSpeedPreset(SpeedPreset speedPreset) {
		set("speed-preset", speedPreset.toGstX264EncPreset());
		return this;
	}
	
	public Boolean isGenerateByteStreamFormatOfNALUEnabled() {
		return get("byte-stream");
	}
	
	public X264Enc setGenerateByteStreamFormatOfNALUEnabled(boolean enabled) {
		set("byte-stream", enabled);
		return this;
	}


}
