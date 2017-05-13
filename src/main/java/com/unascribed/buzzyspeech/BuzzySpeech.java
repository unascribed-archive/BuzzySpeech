package com.unascribed.buzzyspeech;

import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import com.google.common.collect.ImmutableList;
import com.unascribed.buzzyspeech.synth.Note;
import com.unascribed.buzzyspeech.synth.Oscillator;
import com.unascribed.buzzyspeech.synth.Waveform;

public class BuzzySpeech {
	private static final int MAX_SYNTHS = 100;
	private static Formant curFormant = null;
	private static final ArrayList<Oscillator> synths = new ArrayList<>();
	
	private static int counter = 0;
	
	public static void main(String[] args) throws Exception {
		AudioFormat af = new AudioFormat(44100, 8, 1, true, false);
		SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
		sdl.open(af);
		sdl.start();
		
		float volume = 0.25f; //0.35f;
		
		float[] samples = new float[512];
		byte[] buf = new byte[512];
		
		while (true) {
			Arrays.fill(samples, 0f);
			run(volume, samples, buf, sdl);
		}
	}
	
	private static void run(float volume, float[] samples, byte[] buf, SourceDataLine sdl) {
		configure(CYCLE.get((counter/5000)%CYCLE.size()));
		
		int writable = sdl.available();
		if (writable > samples.length) writable = samples.length;
		for(Oscillator synth : synths) {
			synth.genSamples(samples, 0, writable, volume);
		}
		
		for (int i = 0; i < writable; i++) {
			buf[i] = (byte)(samples[i]*127);
		}
		sdl.write(buf, 0, writable);
		
		counter+= writable;
	}

	private static void configure(Formant formant) {
		if (curFormant!=null && formant==curFormant) return; //Don't reconfigure to the same formant. That's silly.
		curFormant = formant;
		
		int numHarmonics = Math.min(formant.harmonics.size(), MAX_SYNTHS);
		
		while (synths.size()<numHarmonics) {
			synths.add(new Oscillator());
		}
		
		for(int i=0; i<synths.size(); i++) {
			if (i>=formant.harmonics.size()) {
				synths.get(i).setVolume(0);
				continue;
			} else {
				Formant.Harmonic source = formant.harmonics.get(i);
				Oscillator target = synths.get(i);
				target.setFrequency((int)source.frequency);
				target.setVolume(source.volume);
				target.setWaveform(source.waveform);
			}
		}
		
		//n.setVolume(formant.volumeNoise);
	}
	
	public static final Formant SILENCE = Formant.builder().build();
	
	public static final Formant NOTE_A4 = Formant.builder()
			.harmonic(Waveform.SAW, Note.getFrequency( 9, 4), 0)
			.harmonic(Waveform.SQUARE, Note.getFrequency( 9, 1), 0)
			.build();
	
	public static final Formant NOTE_B4 = Formant.builder()
			.harmonic(Waveform.SAW, Note.getFrequency(11, 4) , 0)
			.harmonic(Waveform.SQUARE, Note.getFrequency( 9, 2), 0)
			.build();
	
	public static final Formant NOTE_C5 = Formant.builder()
			.harmonic(Waveform.SAW, Note.getFrequency( 0, 5) , 0)
			.build();
	
	public static final Formant NOTE_D5 = Formant.builder()
			.harmonic(Waveform.SAW, Note.getFrequency( 2, 5) , 0)
			.harmonic(Waveform.SQUARE, Note.getFrequency( 9, 2), 0)
			.build();
	
	public static final Formant NOTE_E5 = Formant.builder()
			.harmonic(Waveform.SAW, Note.getFrequency( 4, 5) , 0)
			.build();
	
	public static final Formant A = Formant.builder()
			.harmonic(650, 0)
			.harmonic(1080, -6)
			.harmonic(2650, -7)
			.harmonic(2900, -8)
			.build();
	
	public static final Formant E = Formant.builder()
			.harmonic(400, 0)
			.harmonic(1700, -14)
			.harmonic(2600, -12)
			.harmonic(3200, -14)
			.build();
	
	public static final Formant I = Formant.builder()
			.harmonic(290, 0)
			.harmonic(1870, -15)
			.harmonic(2800, -18)
			.harmonic(3250, -20)
			.build();
	
	public static final Formant O = Formant.builder()
			.harmonic(400, 0)
			.harmonic(800, -10)
			.harmonic(2600, -12)
			.harmonic(2800, -12)
			.build();
	
	public static final Formant U = Formant.builder()
			.harmonic(350, 0)
			.harmonic(600, -20)
			.harmonic(2700, -17)
			.harmonic(2900, -14)
			.build();
	
	
	
	public static final Formant H = Formant.builder()
			.harmonic(1610, -26)
			.harmonic(2373, -26)
			.harmonic(3365, -23)
			.harmonic(4523, -29)
			.noise(-4)
			.build();
	
	public static final Formant L = Formant.builder()
			.harmonic(404, -21)
			.harmonic(1121, -30)
			.harmonic(2609, -31)
			.harmonic(5917, -34)
			.build();
	
	public static final Formant W = Formant.builder()
			.harmonic(  355, -46.0f) //Lesser peak
			.harmonic(  820, -38.3f)
			.harmonic( 1097, -57.0f) //Not a peak (sampled)
			.harmonic( 1327, -61.0f) //Not a peak (sampled)
			.harmonic( 1872, -63.9f)
			.harmonic( 2246, -58.9f)
			.harmonic( 3039, -61.9f)
			.harmonic( 3604, -60.4f)
			.harmonic( 6436, -64.7f)
			.harmonic(18759, -61.1f)
			
			//.harmonic(450, -18)
			//.harmonic(844, -13)
			//.harmonic(2545, -27)
			//.harmonic(29977, -34)
			.build();
	
	
	private static final ImmutableList<Formant> CYCLE = ImmutableList.of(NOTE_A4, SILENCE, NOTE_B4, SILENCE, NOTE_C5, SILENCE, NOTE_D5, SILENCE, NOTE_E5, SILENCE, NOTE_B4, SILENCE, NOTE_C5, SILENCE, NOTE_D5, SILENCE );

}
