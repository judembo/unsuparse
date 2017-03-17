package de.unisaarland.unsuparse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
/**
 * Contains the main method to run the parser.
 * 
 * @author Julia Dembowski
 *
 */
public class Run {

	/**
	 * Parses the specified corpus and writes the resulting parse trees to a file.
	 * First two arguments should be corpus file to be parsed and output file.
	 * Optional arguments are:
	 * "-p" for parsing on POS tags instead of words (default is on words)
	 * "-s" for using only the safe learning phase (results in shallow parse trees)
	 * "-t <threshold>" for specifying a threshold value (default value is 0.05)
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.out.println("Please specify the file you want to parse and the output file");
		} else {
			String corpusFileName = args[0];
			String outputFileName = args[1];
			boolean usePos = false;
			boolean onlySafe = false;
			Double threshold = 0.05;
			
			for (int i = 2; i < args.length; i++) {
				switch (args[i]) {
				case "-p":
					usePos = true;
					System.out.println("- parse on POS tags");
					break;
				case "-s":
					System.out.println("- only safe learning");
					onlySafe = true;
					break;
				case "-t":
					if (i+1 == args.length) {
						System.out.println("WARNING: no threshold value specified, default will be used");
						break;
					}
					i += 1;
					try {
						threshold = Double.parseDouble(args[i]);
						System.out.println("- threshold value " + threshold + " used during safe learning");
					} catch (NumberFormatException e) {
						System.out.println("WARNING: invalid threshold value specified, default will be used");
					}
					break;
				}
			}
			System.out.println();
			System.out.println("-----START PARSER-----");
			
			Parser parser = new Parser(corpusFileName, usePos);
			ArrayList<ConstituentTree> parsed = parser.parseCorpus(threshold, onlySafe);		
			
			try{
			    PrintWriter writer = new PrintWriter(outputFileName, "UTF-8");
			    for (int i = 0; i<parsed.size(); i++) {
					writer.println(parsed.get(i));
				}
			    writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
}
