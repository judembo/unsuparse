package de.unisaarland.unsuparse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Parser class that reads and parses a specified corpus file.
 * 
 * @author Julia Dembowski
 *
 */
public class Parser {
	
	private Corpus corpus;
	private boolean usePos;
	
	/**
	 * The Parser has to be initialised with a corpus file and a boolean  specifying
	 * wether parsing should be performed on words or POS tags.
	 * 
	 * @param filename
	 * @param usePos
	 * @throws IOException
	 */
	public Parser(String filename, boolean usePos) throws IOException {
		this.corpus = new Corpus(filename, usePos);
		this.usePos = usePos;
	}
	
	/**
	 * Returns the significance of seeing a word or POS at the beginning of a sentence.
	 * 
	 * @param label
	 * @return significance value
	 */
	private double sigBegin(Label label) {
		double a = label.getStartFreq(); // frequency of word/pos at start of sentence
		double b = this.corpus.getSentenceCount();
		double c = label.getFreq();
		double d = corpus.getSize();
		return sig(a,b,c,d);
	}
	
	/**
	 * Returns the significance of seeing a word or POS at the end of a sentence: 
	 * 
	 * @param label
	 * @return significance value
	 */
	private double sigEnd(Label label) {
		double a = label.getEndFreq(); // frequency of word/pos at end of sentence
		double b = this.corpus.getSentenceCount();
		double c = label.getFreq();
		double d = corpus.getSize();
		return sig(a,b,c,d);
	}
	
	/**
	 * Returns the significance of seeing a bigram.
	 * 
	 * @param label1
	 * @param label2
	 * @return significance value
	 */
	private double sigBigram(Label label1, Label label2) {
		double a = label1.getBigramFreq(label2.getText());
		double b = label1.getFreq();
		double c = label2.getFreq();
		double d = corpus.getSize();
		return sig(a,b,c,d);
	}
	
	/**
	 * Returns the log-likelihood significance value given the four parameters.
	 * 
	 * @param a	bigram frequency
	 * @param b	frequency of w1 occurring without w2
	 * @param c	frequency of w2 occurring without w1
	 * @param d	corpus size without w1 and w2
	 * @return significance value
	 */
	private double sig(double a, double b, double c, double d) {
		double ll_score = 2*(a*Math.log(a) + b*Math.log(b) + c*Math.log(c) + d*Math.log(d) - (a+b)*Math.log(a+b) - (a+c)*Math.log(a+c) - (b+d)*Math.log(b+d) - (c+d)*Math.log(c+d) + (a+b+c+d)*Math.log(a+b+c+d));
		return ll_score;
	}
	
	/**
	 * Returns the separation value of a bigram without preference value (used on words).
	 * 
	 * @param label1
	 * @param label2
	 * @return separation value
	 */
	private double sepWords(Label label1, Label label2) {
		double a = sigEnd(label1);
		double b = sigBegin(label2);
		double c = sigBigram(label1,label2);
		Double sep = (a*b)/Math.pow(c, 2);
		return sep;
	}
	
	/**
	 * Returns separation value of a bigram considering preference values (used on POS tags).
	 * 
	 * @param label1
	 * @param label2
	 * @return separation value
	 */
	private double sepPref(Label label1, Label label2) {
		double a = sigEnd(label1);
		double b = sigBegin(label2);
		double c = sigBigram(label1,label2);
		Double sep = (label2.pref()*a*b)/(label1.pref()*Math.pow(c, 2));
		return sep;
	}
	
	/**
	 * Returns separation value a the bigram.
	 * 
	 * @param label1
	 * @param label2
	 * @return separation value
	 */
	private double sep(Label label1, Label label2) {
		if (this.usePos) {
			return sepPref(label1, label2);
		} else {
			return sepWords(label1, label2);
		}
	}
	
	/**
	 * Returns the separation value for constituents i and i+1 within a sentence s, 
	 * considering long distance.
	 * 
	 * @param s
	 * @param i 
	 * @return separation value
	 */
	private double sepDist(Sentence s, int i) {
		ArrayList<ConstituentTree> cons = s.getConstituents();
		double min = sep(cons.get(i).getLastLabel(),cons.get(i+1).getFirstLabel());
		double sepVal;
		
		for (int j = Math.max(0, i-4); j <= i; j++) {
			Label k = cons.get(i+1).getFirstLabel();
			sepVal = sep(cons.get(j).getLastLabel(),k);
			if (sepVal < min) {
				min = sepVal;
			} 
		}
		return min;
	}
	
	/**
	 * Finds the best Merge with the lowest separation value for a sentence.
	 * 
	 * @param s
	 * @return Merge object
	 */
	private Merge bestMerge(Sentence s, double threshold, boolean safe) {
		if (s.length() <= 1) {
			return null;
		}
		
		ArrayList<ConstituentTree> cons = s.getConstituents();
		Double minSep = sepDist(s,0);
		int startIndex = 0;
		for (int i = 1; i < cons.size()-1; i++) {
			double sepVal = sepDist(s,i);
			if (sepVal < minSep) {
				minSep = sepVal;
				startIndex = i;
			}
		}
		if (safe && minSep > threshold) {
			return null;
		} else {
			ConstituentTree mergedCons = new ConstituentTree(new ArrayList<ConstituentTree>(cons.subList(startIndex, startIndex + 2)));
			Merge m = new Merge(mergedCons, startIndex, minSep);
			return m;
		}
	}
	
	/**
	 * Parses the corpus and returns the ConstituentTree for each sentence.
	 * 
	 * @param threshold to be used during safe learning phase
	 * @param onlySafe
	 * @return
	 */
	public ArrayList<ConstituentTree> parseCorpus(double threshold, boolean onlySafe) {
		ArrayList<Sentence> sents = this.corpus.getSentences();
		
		// safe learning
		System.out.println("start safe learning phase...");
		int counter = 0;
		int minFreq;
		if (usePos) {
			minFreq = this.corpus.getSentenceCount()/1000;
		} else {
			minFreq = 0;
		}
		HashMap<Integer,Merge> candidateMerges = new HashMap<Integer,Merge>();
		HashMap<String,Integer> candidateFreqs = new HashMap<String,Integer>();
		while (true) {
			boolean updated = false;
			for (int i = 0; i < sents.size(); i++) {
				Merge m = bestMerge(sents.get(i), threshold, true);
				if (m != null) {
					candidateMerges.put(i, m);
					String cons = m.text();
					if (candidateFreqs.containsKey(cons)) {
						candidateFreqs.put(cons, candidateFreqs.get(cons)+1);
					} else {
						candidateFreqs.put(cons, 1);
					}
				}
			}
			for (int i : candidateMerges.keySet()) {
				Merge m = candidateMerges.get(i);
				if (candidateFreqs.get(m.text()) >= minFreq) {
					sents.get(i).merge(m, true);
					updated = true;
				}
			}
			
			counter += 1;
			System.out.println("\t iteration "+counter);
			if (!updated) {
				break;
			} else {
				candidateMerges.clear();
				candidateFreqs.clear();
			}
		}
		
		ArrayList<ConstituentTree> parsed = new ArrayList<ConstituentTree>();
		
		// unsafe learning
		System.out.println();
		System.out.println("finishing...");
		if (!onlySafe) {
			for (Sentence s : sents) {
				Merge m = bestMerge(s, threshold, false);
				while (m != null) {
					s.merge(m, false);
					m = bestMerge(s, threshold, false);
				}
				parsed.add(s.getLast());
			}
		} else {
			for (Sentence s : sents) {
				ArrayList<ConstituentTree> cons = s.getConstituents();
				if (cons.size() == 1) {
					ConstituentTree c = cons.get(0);
					if (c.getSubConstituents() == null) {
						ArrayList<ConstituentTree> newCons = new ArrayList<ConstituentTree>();
						newCons.add(new ConstituentTree(cons));
						s.setConstituents(newCons);
					}
				} else {
					ArrayList<ConstituentTree> newCons = new ArrayList<ConstituentTree>();
					newCons.add(new ConstituentTree(cons));
					s.setConstituents(newCons);
				}
				parsed.add(s.getLast());
			}
		}
		System.out.println("-----DONE PARSING-----");
		return parsed;
	}
	

}
