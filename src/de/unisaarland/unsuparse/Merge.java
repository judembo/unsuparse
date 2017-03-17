package de.unisaarland.unsuparse;

/**
 * Defines a merge to be performed on a sentence.
 * 
 * @author Julia Dembowski
 *
 */
public class Merge {
	private ConstituentTree mergedCons; // the new constituent to be added to the sentence 
	private int index; // start in index of the merged constituent in the sentence
	private double sepVal; // separation value of this merge
	
	/**
	 * Creates a new Merge object and takes the merged constituent, its start index and separation value.
	 * 
	 * @param mergedCons
	 * @param index
	 * @param sepVal
	 */
	public Merge(ConstituentTree mergedCons, int index, double sepVal) {
		this.mergedCons = mergedCons;
		this.index = index;
		this.sepVal = sepVal;
	}
	
	public ConstituentTree getCons() {
		return this.mergedCons;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public double getSepVal() {
		return this.sepVal;
	}
	
	public String text() {
		return mergedCons.getLabelText();
	}
	
	public int length() {
		return this.mergedCons.length();
	}

}
