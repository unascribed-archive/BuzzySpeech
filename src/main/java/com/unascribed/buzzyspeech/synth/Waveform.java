package com.unascribed.buzzyspeech.synth;


public enum Waveform {
	SINE  ( it-> (float)Math.sin(it*Oscillator.TAU) ),
	SAW   ( it-> -1 + (it * 2)                 ),
	SQUARE( it-> (it<0.5f)?-1:1                ),
	NOISE ( it-> (float)((Math.random()*2)-1)  );

	private final PeriodicFunction function;
	
	Waveform(PeriodicFunction function) {
		this.function = function;
	}
	
	public float getSample(float wavePos) {
		return function.apply(wavePos);
	}
	
	@FunctionalInterface
	private static interface PeriodicFunction {
		public float apply(float period);
	}
}