package de.unisaarland.unsuparse;

import java.util.HashMap;

/**
 * 
 * @author Julia Dembowski
 * 
 * A Label can either represent a word or a POS-tag, depending on the mode used.
 *
 */
public class Label {
	private String name;
	private int freq; // Frequency in the corpus
	private int startFreq; // Frequency of token at the beginning of sentences
	private int endFreq; // Frequency of token at the end of sentences
	private HashMap<String,Integer> bigramFreqs; // Contains frequencies of this token preceding other tokens
	
	public Label(String name) {
		this.name = name;
		this.freq = 0;
		this.startFreq = 0;
		this.endFreq = 0;
		this.bigramFreqs = new HashMap<String,Integer>();
	}
	

	public String getText() {
		return this.name;
	}
	
	/**
	 * @param tokenName	name of the following token
	 * @return frequency of this word/pos preceding the word/pos defined by tokenName
	 */
	public int getBigramFreq(String tokenName) {
		if (this.bigramFreqs.containsKey(tokenName)) {
			return this.bigramFreqs.get(tokenName);
		} else {
			return 0;
		}
	}
	
	public void incrementBigramFreq(String tokenName) {
		int currentFreq = this.getBigramFreq(tokenName);
		this.bigramFreqs.put(tokenName, currentFreq + 1);
	}

	public int getStartFreq() {
		return this.startFreq;
	}

	public void incrementStartFreq() {
		this.startFreq += 1;
	}

	public int getEndFreq() {
		return this.endFreq;
	}

	public void incrementEndFreq() {
		this.endFreq += 1;
	}


	public int getFreq() {
		return this.freq;
	}


	public void incrementFreq() {
		this.freq += 1;
	}

}
