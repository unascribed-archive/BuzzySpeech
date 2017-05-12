package com.unascribed.buzzyspeech.synth;

public interface Synth {
	// 44.1kHz
	public static final int SAMPLE_RATE = 44100;
	
	public static final double TAU = Math.PI*2;
	
	float nextSample();
	void genSamples(float[] buf, int ofs, int len);
	
	void setVolume(float volume);
	float getVolume();
}
