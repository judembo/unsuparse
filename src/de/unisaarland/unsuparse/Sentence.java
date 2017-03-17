package de.unisaarland.unsuparse;

import java.util.ArrayList;

/**
 * Defines a sentence in a corpus.
 * 
 * @author Julia Dembowski
 *
 */
public class Sentence {
	private ArrayList<ConstituentTree> constituents;

	public Sentence() {
		this.constituents = new ArrayList<ConstituentTree>();
	}
	
	public void addWord(Label label, String word) {
		this.constituents.add(new ConstituentTree(label, word));
	}
	
	public int length() {
		return this.constituents.size();
	}
	
	public ArrayList<ConstituentTree> getConstituents() {
		return this.constituents;
	}
	
	public void setConstituents(ArrayList<ConstituentTree> constituents) {
		this.constituents = constituents;
	}
	
	/**
	 * Performs a merge with the specified Merge object on the sentence.
	 * If safe learning is active, the merged constituent is flattened before it is added.
	 * 
	 * @param m
	 * @param safe
	 */
	public void merge(Merge m, boolean safe) {
		int index = m.getIndex();
		for (int i = 0; i < m.length(); i++) {
			this.constituents.remove(index);
		}
		ConstituentTree newCons = m.getCons();
		if (safe) {
			newCons.flatten();
			newCons.getFirstLabel().incrementF();
			newCons.getLastLabel().incrementL();
		}
		this.constituents.add(index, newCons);
	}
	
	/**
	 * @return last constituent of the sentence
	 */
	public ConstituentTree getLast() {
		if (this.constituents.size() == 0) {
			return null;
		} else {
			return this.constituents.get(this.constituents.size()-1);
		}
	}
	
	public void removeLast() {
		this.constituents.remove(this.constituents.size()-1);
	}
}
