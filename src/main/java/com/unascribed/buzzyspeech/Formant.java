package com.unascribed.buzzyspeech;

import java.util.ArrayList;

import com.unascribed.buzzyspeech.synth.Waveform;

public class Formant {
	
	protected ArrayList<Harmonic> harmonics = new ArrayList<>();
	protected final float volumeNoise;

	
	private Formant(ArrayList<Harmonic> harmonics, float volumeNoise) {
		this.harmonics = harmonics;
		this.volumeNoise = volumeNoise;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		private ArrayList<Harmonic> harmonics = new ArrayList<>();
		private float volumeNoise;
		
		private Builder() {}
		
		public Builder harmonic(float freq, float db) {
			harmonics.add(new Harmonic(freq, (float) Math.pow(10, db/10f)));
			return this;
		}
		
		public Builder harmonic(Waveform wave, float freq, float db) {
			harmonics.add(new Harmonic(wave, freq, (float) Math.pow(10, db/10f)));
			return this;
		}
		
		public Builder noise(int db) {
			this.volumeNoise = (float) Math.pow(10, db/10f);
			return this;
		}
		
		public Formant build() {
			
			if (volumeNoise<=0 && !harmonics.isEmpty()) {
				//There are harmonics and no noise channel. Boost the harmonics so at least one has full volume
				float maxVolume = 0f;
				for(Harmonic harmonic : harmonics) {
					maxVolume = Math.max(maxVolume, harmonic.volume);
				}
				
				float volumeScale = 1f/maxVolume;
				
				for(Harmonic harmonic : harmonics) {
					harmonic.volume *= volumeScale;
				}
			}
			
			
			return new Formant(harmonics, volumeNoise);
		}
		
	}
	
	public static class Harmonic {
		public float frequency;
		public float volume;
		public Waveform waveform;
		
		public Harmonic(float frequency, float volume) {
			this.frequency = frequency;
			this.volume = volume;
			this.waveform = Waveform.SINE;
		}
		
		public Harmonic(Waveform wave, float frequency, float volume) {
			this.waveform = wave;
			this.frequency = frequency;
			this.volume = volume;
		}
	}
}
