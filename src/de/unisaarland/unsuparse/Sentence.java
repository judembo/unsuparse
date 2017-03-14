package de.unisaarland.unsuparse;

import java.util.ArrayList;

public class Sentence {
	private ArrayList<String> words;
	private ArrayList<String> tags;
	
	public Sentence() {
		this.words = new ArrayList<String>();
		this.tags = new ArrayList<String>();
	}
	
	public ArrayList<String> getWords() {
		return this.words;
	}
	
	public void addWord(String word) {
		this.words.add(word);
	}
	
	public ArrayList<String> getTags() {
		return this.tags;
	}
	
	public void addTag(String tag) {
		this.tags.add(tag);
	}
	
	public int length() {
		return this.words.size();
	}
	
	

}
