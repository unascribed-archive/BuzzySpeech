package com.unascribed.buzzyspeech;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import com.google.common.collect.ImmutableList;
import com.unascribed.buzzyspeech.synth.NoiseSynth;
import com.unascribed.buzzyspeech.synth.SineSynth;

public class BuzzySpeech {

	private static final SineSynth a = new SineSynth(650);
	private static final SineSynth b = new SineSynth(1080);
	private static final SineSynth c = new SineSynth(2650);
	private static final SineSynth d = new SineSynth(2900);
	private static final NoiseSynth n = new NoiseSynth();
	
	private static int counter = 0;
	
	public static void main(String[] args) throws Exception {
		AudioFormat af = new AudioFormat(44100, 8, 1, true, false);
		SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
		sdl.open(af);
		sdl.start();
		
		float volume = 0.35f;
		
		float[] aout = new float[512];
		float[] bout = new float[512];
		float[] cout = new float[512];
		float[] dout = new float[512];
		float[] nout = new float[512];
		
		float[] samples = new float[512];
		byte[] buf = new byte[512];
		
		while (true) {
			run(volume, aout, bout, cout, dout, nout, samples, buf, sdl);
		}
	}
	
	private static void run(float volume, float[] aout, float[] bout, float[] cout, float[] dout, float[] nout, float[] samples, byte[] buf, SourceDataLine sdl) {
		configure(CYCLE.get((counter/10)%CYCLE.size()));
		
		a.genSamples(aout, 0, aout.length);
		b.genSamples(bout, 0, bout.length);
		c.genSamples(cout, 0, cout.length);
		d.genSamples(dout, 0, dout.length);
		n.genSamples(nout, 0, nout.length);
		
		for (int i = 0; i < samples.length; i++) {
			float sample = 0;
			sample += aout[i]*volume;
			sample += bout[i]*volume;
			sample += cout[i]*volume;
			sample += dout[i]*volume;
			sample += nout[i]*volume;
			samples[i] = sample;
		}
		
		for (int i = 0; i < buf.length; i++) {
			buf[i] = (byte)(samples[i]*127);
		}
		sdl.write(buf, 0, buf.length);
		
		counter++;
	}

	private static void configure(Formant formant) {
		a.setFrequency(formant.frequencyA);
		a.setVolume(formant.volumeA);
		
		b.setFrequency(formant.frequencyB);
		b.setVolume(formant.volumeB);
		
		c.setFrequency(formant.frequencyC);
		c.setVolume(formant.volumeC);
		
		d.setFrequency(formant.frequencyD);
		d.setVolume(formant.volumeD);
		
		n.setVolume(formant.volumeNoise);
	}
	
	public static final Formant SILENCE = Formant.builder().build();
	
	public static final Formant A = Formant.builder()
			.a(650, 0)
			.b(1080, -6)
			.c(2650, -7)
			.d(2900, -8)
			.build();
	
	public static final Formant E = Formant.builder()
			.a(400, 0)
			.b(1700, -14)
			.c(2600, -12)
			.d(3200, -14)
			.build();
	
	public static final Formant I = Formant.builder()
			.a(290, 0)
			.b(1870, -15)
			.c(2800, -18)
			.d(3250, -20)
			.build();
	
	public static final Formant O = Formant.builder()
			.a(400, 0)
			.b(800, -10)
			.c(2600, -12)
			.d(2800, -12)
			.build();
	
	public static final Formant U = Formant.builder()
			.a(350, 0)
			.b(600, -20)
			.c(2700, -17)
			.d(2900, -14)
			.build();
	
	
	
	public static final Formant H = Formant.builder()
			.a(1610, -26)
			.b(2373, -26)
			.c(3365, -23)
			.d(4523, -29)
			.noise(-4)
			.build();
	
	public static final Formant L = Formant.builder()
			.a(404, -21)
			.b(1121, -30)
			.c(2609, -31)
			.d(5917, -34)
			.build();
	
	public static final Formant W = Formant.builder()
			.a(450, -18)
			.b(844, -13)
			.c(2545, -27)
			.d(29977, -34)
			.build();
	
	
	private static final ImmutableList<Formant> CYCLE = ImmutableList.of(H, E, L, L, O, SILENCE, SILENCE, SILENCE, SILENCE);

}
