package com.unascribed.buzzyspeech.synth;

public class Oscillator {
	public static final int SAMPLE_RATE = 44100;
	public static final float TAU = (float)(Math.PI*2d);
	
	private float volumeAttack = 0.001f;
	private float volumeDecay = 0.0001f;
	
	/** This is the "target" volume that end-users set. */
	private float volume = 0.0f;
	/** This is the moving-current volume we actually use. */
	protected float volumeScramble = 0.0f;
	
	protected float frequency = 440.0f;
	protected float cyclesPerSample;
	protected float wavePos = 0f;
	
	private Waveform waveform;
	
	public float nextSample() {
		//volume scramble helps prevent clicking and creates something more like an ADSR curve
		if (volumeScramble<volume) {
			volumeScramble = Math.min(volumeScramble+volumeAttack, volume);
		} else if (volumeScramble>volume) {
			volumeScramble = Math.max(volumeScramble-volumeDecay, volume);
		}
		
		wavePos = (wavePos + cyclesPerSample) % 1f;
		
		return volumeScramble == 0 ? 0 : _nextSample(wavePos)*volumeScramble;
	}
	
	protected float _nextSample(float wavePos) {
		return waveform.getSample(wavePos);
	}

	public void genSamples(float[] buf, int ofs, int len, float volumeScale) {
		for (int i = ofs; i < ofs+len; i++) {
			buf[i] += nextSample()*volumeScale;
		}
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
		this.cyclesPerSample = this.frequency/SAMPLE_RATE;
	}
	
	public float getFrequency() {
		return this.frequency;
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
	}

	public float getVolume() {
		return volume;
	}

	public Waveform getWaveform() {
		return waveform;
	}
	
	public void setWaveform(Waveform waveform) {
		this.waveform = waveform;
	}
}
