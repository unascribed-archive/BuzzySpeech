package com.unascribed.buzzyspeech;

public class Formant {
	
	public final int frequencyA;
	public final float volumeA;
	public final int frequencyB;
	public final float volumeB;
	public final int frequencyC;
	public final float volumeC;
	public final int frequencyD;
	public final float volumeD;
	
	public final float volumeNoise;

	private Formant(int frequencyA, float volumeA, int frequencyB, float volumeB,
			int frequencyC, float volumeC, int frequencyD, float volumeD,
			float volumeNoise) {
		this.frequencyA = frequencyA;
		this.volumeA = volumeA;
		this.frequencyB = frequencyB;
		this.volumeB = volumeB;
		this.frequencyC = frequencyC;
		this.volumeC = volumeC;
		this.frequencyD = frequencyD;
		this.volumeD = volumeD;
		this.volumeNoise = volumeNoise;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private int frequencyA;
		private float volumeA;
		private int frequencyB;
		private float volumeB;
		private int frequencyC;
		private float volumeC;
		private int frequencyD;
		private float volumeD;
		
		private float volumeNoise;
		
		private Builder() {}
		
		public Builder a(int freq, int db) {
			this.frequencyA = freq;
			this.volumeA = (float) Math.pow(10, db/10f);
			return this;
		}
		
		public Builder b(int freq, int db) {
			this.frequencyB = freq;
			this.volumeB = (float) Math.pow(10, db/10f);
			return this;
		}
		
		public Builder c(int freq, int db) {
			this.frequencyC = freq;
			this.volumeC = (float) Math.pow(10, db/10f);
			return this;
		}
		
		public Builder d(int freq, int db) {
			this.frequencyD = freq;
			this.volumeD = (float) Math.pow(10, db/10f);
			return this;
		}
		
		public Builder noise(int db) {
			this.volumeNoise = (float) Math.pow(10, db/10f);
			return this;
		}
		
		
		public Formant build() {
			float mod = volumeNoise > 0 ? 1 : (1/volumeA);
			return new Formant(frequencyA, volumeA*mod, frequencyB, volumeB*mod, frequencyC, volumeC*mod, frequencyD, volumeD*mod, volumeNoise);
		}
		
	}
	
	
}
