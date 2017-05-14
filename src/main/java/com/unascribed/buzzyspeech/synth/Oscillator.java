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
	
	protected float frequency = 440.0f; //TODO: for *speech* it may actually be desirable to scramble-to-frequency (portamento)
	protected float cyclesPerSample;
	protected float wavePos = 0f;
	
	protected Waveform modWave = Waveform.SINE;
	protected float modFreq = 438.0f;
	protected float modAmp = 0f;
	protected float modCycles = 0f;
	protected float modPos = 0f;
	
	private Waveform waveform;
	private Mode mode = Mode.SINGLE;
	
	public float nextSample() {
		//volume scramble helps prevent clicking and creates something more like an ADSR curve
		if (volumeScramble<volume) {
			volumeScramble = Math.min(volumeScramble+volumeAttack, volume);
		} else if (volumeScramble>volume) {
			volumeScramble = Math.max(volumeScramble-volumeDecay, volume);
		}
		
		if (mode==Mode.FM) {
			modPos = (modPos + modCycles) % 1f;
			float modulatorSample = modWave.getSample(modPos) * modAmp;
			cyclesPerSample = (frequency/SAMPLE_RATE) + modulatorSample;
			wavePos = (wavePos + cyclesPerSample) % 1f;
		} else {
			wavePos = wavePos + cyclesPerSample;
			modPos = (modPos + modCycles) % 1f;
			if (mode==Mode.SYNC && wavePos>=1f) modPos = 0f;
			wavePos = wavePos % 1f;
		}
		
		return volumeScramble == 0 ? 0 : _nextSample(wavePos)*volumeScramble;
	}
	
	protected float _nextSample(float wavePos) {
		switch(mode) {
		case SINGLE:
		case FM:
			return waveform.getSample(wavePos);
		default:
			return waveform.getSample(wavePos) + waveform.getSample(modPos);
		//case SYNC:
		//	return waveform.getSample(modPos) + (waveform.getSample(wavePos)*0.25f);
		//case DETUNE:
		//	return waveform.getSample(wavePos) + waveform.getSample(mod)
		
		}
		
		//if (mode==Mode.SINGLE)return waveform.getSample(wavePos);
		
		//return (waveform.getSample(wavePos) + waveform.getSample(modPos)) * 0.6f;
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
	
	public Mode getMode() { return mode; }
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public void setModulator(Mode mode, Waveform wave, float amp, float frequency) {
		this.mode = mode;
		this.modWave = wave;
		this.modFreq = frequency;
		this.modAmp = amp;
		this.modCycles = this.modFreq/SAMPLE_RATE;
	}
	
	public static enum Mode {
		/** A single pure tone */
		SINGLE,
		/** A carrier wave, plus a second wave at a *very* slightly different frequency, often used/percieved as a chorus effect */
		DETUNE,
		/** A carrier wave retriggered by a fundamental frequency, causing asymmetries and more complex harmonics */
		SYNC,
		/** A carrier wave whose frequency is constantly adjusted by a modulator wave, creating extremely complex, often metallic or anharmonic, tones */
		FM;
	}
}
