package com.unascribed.buzzyspeech.synth;


public class SineSynth extends AbstractSynth {

	private int frequency;
	private int sampleCount;
	
	public SineSynth(int frequency) {
		this.frequency = frequency;
	}
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	@Override
	protected float _nextSample() {
		float sample = (float) Math.sin((sampleCount * (frequency/(float)SAMPLE_RATE)) * TAU);
		sampleCount++;
		return sample;
	}
	
}
