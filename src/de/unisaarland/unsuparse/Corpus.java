package de.unisaarland.unsuparse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class Corpus {
	private int size; // number of tokens
	private int sentenceCount; // number of sentences
	private boolean usePos;
	private HashMap<String,Label> labelMap;
	private LinkedList<Sentence> sentences;
	
	public Corpus(String filename, boolean usePos) throws IOException {
		this.usePos = usePos;
		this.size = 0;
		this.buildCorpus(filename, usePos);
	}
	
	private void buildCorpus(String filename, boolean usePos) throws IOException {
		BufferedReader br = null;
		
		try{
			br = new BufferedReader(new FileReader(filename));
			String line;
			String[] tokens;
			while ((line = br.readLine()) != null){
				tokens = line.split("\\s");
				Sentence s = new Sentence();
				for (int i = 0; i < tokens.length; i++) {
					this.size += 1;
					String token = tokens[i];
					Label label;
					if (usePos) {
						String[] fields = token.split("_");
						String word;
						String pos;
						if (fields.length == 1) {
							// Original token was "_"
							word = "_";
							pos = fields[0];
						} else {
							word = fields[0];
							pos = fields[1];
						}
						
						
						s.addTag(pos);
						s.addWord(word);
						
						if (this.labelMap.containsKey(pos)) {
							label = this.labelMap.get(pos);
						} else {
							label = new Label(pos);
							this.labelMap.put(pos, label);
						}
					} else {
						s.addWord(token);
						
						if (this.labelMap.containsKey(token)) {
							label = this.labelMap.get(token);
						} else {
							label = new Label(token);
							this.labelMap.put(token, label);
						}
						
					}
					label.incrementFreq();
					if (i == 0) {
						label.incrementStartFreq();
					}
					if (i == tokens.length-1) {
						label.incrementEndFreq();
					} else {
						String nextToken = tokens[i+1];
						if (usePos) {
							String[] fields = nextToken.split("_");
							if (fields.length == 1) {
								nextToken = "_";
							} else {
								nextToken = fields[0];
							}
						}
						label.incrementBigramFreq(nextToken);
					}
					
				}
				if (s.length() > 0) {
					this.sentences.add(s);
					this.sentenceCount += 1;
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
	
	public int getSize() {
		return this.size;
	}
	
	public int getSentenceCount() {
		return this.sentenceCount;
	}
	
	public LinkedList<Sentence> getSentences() {
		return this.sentences;
	}
	
}
