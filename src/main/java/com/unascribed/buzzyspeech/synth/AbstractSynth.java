package com.unascribed.buzzyspeech.synth;

public abstract class AbstractSynth implements Synth {

	private float volume = 1;
	
	@Override
	public float nextSample() {
		return volume == 0 ? 0 : _nextSample()*volume;
	}
	
	protected abstract float _nextSample();

	@Override
	public void genSamples(float[] buf, int ofs, int len) {
		for (int i = ofs; i < ofs+len; i++) {
			buf[i] = nextSample();
		}
	}

	@Override
	public void setVolume(float volume) {
		this.volume = volume;
	}

	@Override
	public float getVolume() {
		return volume;
	}

}
