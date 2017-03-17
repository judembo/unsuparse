package de.unisaarland.unsuparse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

/**
 * Defines a corpus.
 * 
 * @author Julia Dembowski
 *
 */
public class Corpus {
	private int size; // number of tokens
	private int sentenceCount; // number of sentences
	private HashMap<String,Label> labelMap; // maps string representations to their corresponding labels
	private ArrayList<Sentence> sentences;
	
	/**
	 * A Corpus needs to be initialised with the corpus file and 
	 * a boolean specifying whether parsing should be performed on POS tags or words.
	 * 
	 * @param filename
	 * @param usePos
	 * @throws IOException
	 */
	public Corpus(String filename, boolean usePos) throws IOException {
		this.size = 0;
		this.sentenceCount = 0;
		this.labelMap = new HashMap<String,Label>(); 
		this.sentences = new ArrayList<Sentence>();
		this.buildCorpus(filename, usePos);
	}
	
	/**
	 * Reads the corpus file and builds the corpus.
	 * 
	 * @param filename
	 * @param usePos
	 * @throws IOException
	 */
	private void buildCorpus(String filename, boolean usePos) throws IOException {
		Pattern punct = Pattern.compile("\\p{Punct}");
		
		BufferedReader br = null;
		int column;
		if (usePos) {
			column = 1;
		} else {
			column = 0;
		}
		
		try{
			br = new BufferedReader(new FileReader(filename));
			String line;
			Sentence s = new Sentence();
			while ((line = br.readLine()) != null){
				if (line.equals("")) { // new sentence
					ConstituentTree prev = s.getLast();
					if (prev != null && isPunct(prev.getRawText(),punct)) {
						s.removeLast();
					}
					if (s.length() > 0) {
						s.getLast().getLastLabel().incrementEndFreq();
						this.sentences.add(s);
						this.sentenceCount += 1;
						this.size += s.length();
						s = new Sentence();
					}	
				} else {
					String[] fields = line.trim().split("\\s+");
					String word = fields[0];
					String wordLabel = fields[column]; // can be word itself or POS, depending on mode
					Label label;
					if (this.labelMap.containsKey(wordLabel)) {
						label = this.labelMap.get(wordLabel);
					} else {
						label = new Label(wordLabel);
						this.labelMap.put(wordLabel, label);
					}
					
					label.incrementFreq();
					ConstituentTree prev = s.getLast();
					if (prev == null) {
						label.incrementStartFreq();
					} else {
						prev.getLastLabel().incrementBigramFreq(wordLabel);
					}
					
					s.addWord(label, word);	
				}
			}		
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    br.close();
		}
	}
	
	private boolean isPunct(String text, Pattern p) {
		Matcher m = p.matcher(text);
		return m.matches();
	}
	
	public int getSize() {
		return this.size;
	}
	
	public int getSentenceCount() {
		return this.sentenceCount;
	}
	
	public ArrayList<Sentence> getSentences() {
		return this.sentences;
	}
	
}
