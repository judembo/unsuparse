package de.unisaarland.unsuparse;

import java.util.ArrayList;

/**
 * This is a recursive data structure defining a constituent of a sentence.
 * It can be regarded as a tree, the children are stored in the constitutents attribute. 
 * 
 * @author Julia Dembowski
 *
 */
public class ConstituentTree {
	private Label firstLabel;
	private Label lastLabel;
	private String text; // raw text spanned by the ConstituentTree
	private String labelText; // a string with the label names of the ConstituentTree
	private ArrayList<ConstituentTree> subConstituents; // the sub-constituents (or children) of this constituent
	
	/**
	 * Constructor for single word ConstituentTrees (leaves of the parse tree)
	 * 
	 * @param label
	 * @param text
	 */
	public ConstituentTree(Label label, String text) {
		this.firstLabel = label;
		this.lastLabel = label;
		this.text = text;
		this.labelText = label.getText();
		this.subConstituents = null;
	}
	
	/**
	 * Constructor for higher order ConstituentTrees
	 * 
	 * @param subConstituents
	 */
	public ConstituentTree(ArrayList<ConstituentTree> subConstituents) {
		this.subConstituents = subConstituents;
		this.firstLabel = subConstituents.get(0).firstLabel;
		this.lastLabel = subConstituents.get(subConstituents.size()-1).lastLabel;
		this.text = subConstituents.get(0).text;
		for (int i = 1; i < subConstituents.size(); i++) {
			this.text += " " + subConstituents.get(i).text;
		}
		this.labelText = subConstituents.get(0).labelText;
		for (int i = 1; i < subConstituents.size(); i++) {
			this.labelText += " " + subConstituents.get(i).labelText;
		}
	}
	
	public ArrayList<ConstituentTree> getSubConstituents() {
		return this.subConstituents;
	}
	
	/**
	 * Flattens the top level of the ConstituentTree.
	 */
	public void flatten() {
		if (this.subConstituents == null) {
			return;
		}
		ArrayList<ConstituentTree> flattened = new ArrayList<ConstituentTree>();
		for (ConstituentTree c : this.subConstituents) {
			if (c.subConstituents == null) {
				flattened.add(c);
			} else {
				for (ConstituentTree subTree : c.subConstituents) {
					flattened.add(subTree);
				}
			}
		}
		this.subConstituents = flattened;
	}
	
	/**
	 * @return sentence without brackets
	 */
	public String getRawText() {
		return this.text;
	}
	
	/**
	 * @return the labels of the sentence (can be words or POS tags)
	 */
	public String getLabelText() {
		return this.labelText;
	}
	
	/**
	 * @return String representation of the parse tree
	 */
	public String toString() {
		if (this.subConstituents == null) {
			return this.text;
		} else {
			String parsedText = "[";
			for (ConstituentTree c : this.subConstituents) {
				parsedText += " " + c.toString();
			}
			parsedText += " ]";
			return parsedText;
		}
	}
	
	public Label getFirstLabel() {
		return this.firstLabel;
	}
	
	public Label getLastLabel() {
		return this.lastLabel;
	}
	
	public int length() {
		return this.subConstituents.size();
	}

}
