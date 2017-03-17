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
	
	/**
	 * Creates a new Merge object and takes the merged constituent, its start index and separation value.
	 * 
	 * @param mergedCons
	 * @param index
	 */
	public Merge(ConstituentTree mergedCons, int index) {
		this.mergedCons = mergedCons;
		this.index = index;
	}
	
	public ConstituentTree getCons() {
		return this.mergedCons;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public String text() {
		return mergedCons.getLabelText();
	}
	
	public int length() {
		return this.mergedCons.length();
	}

}
