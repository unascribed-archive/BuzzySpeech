package com.unascribed.buzzyspeech.synth;

public class Note {
	public static final float[] BASE_FREQUENCIES = {
			16.351f, 17.324f, 18.354f, 19.445f,
			20.601f, 21.827f, 23.124f, 24.499f,
			25.956f, 27.500f, 29.135f, 30.868f
	};
	
	public static final String[] NAMES = {
			"C-",	 "C#",    "D-",    "D#",
			"E-",    "F-",    "F#",    "G-",
			"G#",    "A-",    "A#",    "B-",
	};
	
	public static float getFrequency(int noteIndex) {
		int octave = noteIndex / 12;
		int note = noteIndex % 12;
		
		if (noteIndex<0 || octave>9) return 0f;
		
		return BASE_FREQUENCIES[note] * (float)Math.pow(2, octave);
	}
	
	public static float getFrequency(int note, int octave) {
		if (note>=12) return 0f;
		return BASE_FREQUENCIES[note] * (float)Math.pow(2, octave);
	}
	
	public static String getName(int noteIndex) {
		int octave = noteIndex / 12;
		int note = noteIndex % 12;
		
		if (noteIndex<0) return "---";
		if (octave>9) return "...";
		
		return NAMES[note]+octave;
	}
}
