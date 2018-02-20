package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.VideoAlphaMode;
import org.freedesktop.gstreamer.VideoChromaMode;
import org.freedesktop.gstreamer.VideoDitherMethod;
import org.freedesktop.gstreamer.VideoGammaMode;
import org.freedesktop.gstreamer.VideoMatrixMode;
import org.freedesktop.gstreamer.VideoPrimariesMode;
import org.freedesktop.gstreamer.VideoResamplerMethod;

public class VideoConvert extends VideoFilter {
	
	public static final String GST_NAME = "videoconvert";
    public static final String GTYPE_NAME = "GstVideoConvert";

    public VideoConvert() {
		this((String) null);
	}

    public VideoConvert(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public VideoConvert(Initializer init) {
		super(init);
	}
  
    public VideoDitherMethod getDitheringMethod() {
    	return VideoDitherMethod.valueOf( (Integer)get("dither"));
    }
    
    public VideoConvert setDitheringMethod(VideoDitherMethod ditheringMethod) {
    	set("dither", ditheringMethod.intValue());
    	return this;
    }
    
    public Long getDitherQuantization() {
    	return get("dither-quantization");
    }
    
    public VideoConvert setDitherQuantization(long quantizer) {
    	set("dither-quantization",quantizer);
    	return this;
    }
    
    public VideoResamplerMethod getResamplingMethod() {
    	return VideoResamplerMethod.valueOf( (Integer)get("chroma-resampler"));
    }
    
    public VideoConvert setResamplingMethod(VideoResamplerMethod resamplingMethod) {
    	set("chroma-resampler", resamplingMethod.intValue());
    	return this;
    }

    public VideoAlphaMode getAlphaMode() {
    	return VideoAlphaMode.valueOf( (Integer)get("alpha-mode"));
    }
    
    public VideoConvert setAlphaMode(VideoAlphaMode alphaMode) {
    	set("alpha-mode", alphaMode.intValue());
    	return this;
    }

    public VideoChromaMode getChromaMode() {
    	return VideoChromaMode.valueOf( (Integer)get("chroma-mode"));
    }
    
    public Double getAlphaValue() {
    	return get("alpha-value");
    }
    
    public VideoConvert setAlphaValue(double alpha) {
    	set("alpha-value",alpha);
    	return this;
    }

    
    public VideoConvert setChromaMode(VideoChromaMode chromaMode) {
    	set("chroma-mode", chromaMode.intValue());
    	return this;
    }
   
    public VideoMatrixMode getMatrixMode() {
    	return VideoMatrixMode.valueOf( (Integer)get("matrix-mode"));
    }
    
    public VideoConvert setMatrixMode(VideoMatrixMode matrixMode) {
    	set("matrix-mode", matrixMode.intValue());
    	return this;
    }

    public VideoGammaMode getGammaMode() {
    	return VideoGammaMode.valueOf( (Integer)get("gamma-mode"));
    }
    
    public VideoConvert setGammaMode(VideoGammaMode gammaMode) {
    	set("gamma-mode", gammaMode.intValue());
    	return this;
    }

    public VideoPrimariesMode getPrimariesMode() {
    	return VideoPrimariesMode.valueOf( (Integer)get("primaries-mode"));
    }
    
    public VideoConvert setPrimariesMode(VideoPrimariesMode primariesMode) {
    	set("primaries-mode", primariesMode.intValue());
    	return this;
    }

}
