package de.unisaarland.unsuparse;

import java.util.HashMap;

/**
 * A Label can either represent a word or a POS-tag, depending on the mode used.
 * 
 * @author Julia Dembowski
 */
public class Label {
	private String name;
	private double freq; // Frequency in the corpus
	private double startFreq; // Frequency of label at the beginning of sentences
	private double endFreq; // Frequency of label at the end of sentences
	private HashMap<String,Double> bigramFreqs; // Contains frequencies of this label preceding other labels
	private int f; // number of constituents with this label at the first position
	private int l; // number of constituents with this label at the last position
	
	public Label(String name) {
		this.name = name;
		this.freq = 0.1;
		this.startFreq = 0.1;
		this.endFreq = 0.1;
		this.bigramFreqs = new HashMap<String,Double>();
		this.f = 0;
		this.l = 0;
	}
	

	public String getText() {
		return this.name;
	}
	
	/**
	 * Returns frequency of this word/pos preceding the word/pos defined by labelName.
	 * 
	 * @param labelName
	 * @return bigram frequency 
	 */
	public double getBigramFreq(String labelName) {
		if (this.bigramFreqs.containsKey(labelName)) {
			return this.bigramFreqs.get(labelName);
		} else {
			return 0.1;
		}
	}
	
	public void incrementBigramFreq(String labelName) {
		double currentFreq = this.getBigramFreq(labelName);
		this.bigramFreqs.put(labelName, currentFreq + 1);
	}

	public double getStartFreq() {
		return this.startFreq;
	}

	public void incrementStartFreq() {
		this.startFreq += 1;
	}

	public double getEndFreq() {
		return this.endFreq;
	}

	public void incrementEndFreq() {
		this.endFreq += 1;
	}


	public double getFreq() {
		return this.freq;
	}


	public void incrementFreq() {
		this.freq += 1;
	}
	
	public void incrementF() {
		this.f += 1;
	}
	
	public void incrementL() {
		this.l += 1;
	}
	
	/**
	 * Returns preference of this label to be the first element in a constituent.
	 * 
	 * @return preference value 
	 */
	public double pref() {
		Double exp = (double) (this.f - this.l);
		if (exp > 50) { // this in necessary to avoid numbers going to infinity
			exp = 50.0;
		}
		if (exp < -50) { // this in necessary to avoid the preference value being zero
			exp = -50.0;
		}
		Double res = Math.pow(2, exp);
		return res;
	}

}
