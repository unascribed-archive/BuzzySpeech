package com.unascribed.buzzyspeech.synth;

public class NoiseSynth extends AbstractSynth {

	@Override
	protected float _nextSample() {
		return (float)((Math.random()*2)-1);
	}

}
