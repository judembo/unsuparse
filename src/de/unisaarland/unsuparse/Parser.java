package de.unisaarland.unsuparse;

import java.io.IOException;

public class Parser {
	
	private Corpus corpus;
	
	public Parser(String filename, boolean usePos) throws IOException {
		this.corpus = new Corpus(filename, usePos);
	}
	
	/**
	 * @param label
	 * @return significance of seeing a word or POS at the beginning of a sentence 
	 */
	private double sigBegin(Label label) {
		int a = label.getStartFreq(); // frequency of word/pos at start of sentence
		int b = this.corpus.getSentenceCount() - a;
		int c = label.getFreq() - a;
		int d = corpus.getSize() - label.getFreq(); // no need to subtract number of sentences, as they are not counted in corpus size
		return sig(a,b,c,d);
	}
	
	/**
	 * @param label
	 * @return significance of seeing a word or POS at the end of a sentence 
	 */
	private double sigEnd(Label label) {
		int a = label.getEndFreq(); // frequency of word/pos at end of sentence
		int b = this.corpus.getSentenceCount() - a;
		int c = label.getFreq() - a;
		int d = corpus.getSize() - label.getFreq(); // no need to subtract number of sentences, as they are not counted in corpus size
		return sig(a,b,c,d);
	}
	
	/**
	 * @param label1
	 * @param label2
	 * @return significance of seeing the bigram
	 */
	private double sigBigram(Label label1, Label label2) {
		int a = label1.getBigramFreq(label2.getText());
		int b = label1.getFreq() - a;
		int c = label2.getFreq() - a;
		int d = corpus.getSize() - a - b - c;
		return sig(a,b,c,d);
	}
	
	/**
	 * 
	 * @param a	bigram frequency
	 * @param b	frequency of w1 occurring without w2
	 * @param c	frequency of w2 occurring without w1
	 * @param d	corpus size without w1 and w2
	 * @return log-likelihood significance value given the four parameters 
	 */
	private double sig(int a, int b, int c, int d) {
		double ll_score = 2*(a*Math.log(a) + b*Math.log(b) + c*Math.log(c) + d*Math.log(d) - (a+b)*Math.log(a+b) - (a+c)*Math.log(a+c) - (b+d)*Math.log(b+d) - (c+d)*Math.log(c+d) + (a+b+c+d)*Math.log(a+b+c+d));
		return ll_score;
	}
	
	/**
	 * @param label1
	 * @param label2
	 * @return separation value of the bigram
	 */
	private double separationValue(Label label1, Label label2) {
		double a = sigEnd(label1);
		double b = sigBegin(label2);
		double c = sigBigram(label1,label2);
		return (a*b)/Math.pow(c, 2);
	}

}
