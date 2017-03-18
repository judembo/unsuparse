# UnsuParse Parser

This is a reimplementation of the UnsuParse algorithm, which was originally developed by Hänig et al. (2008). 
UnsuParse is an algorithm for learning syntactic parses of sentences in an entirely unsupervised manner. 
The parses can be learned either directly on words or on their corresponding Part-of-Speech tags.

## Input format

The input file should have one token per line and sentences should be separated by a blank line. 
If the parser is to be used on POS tags, the file should contain a second column, separated by any sequence of 
whitespace characters, with the corresponding POS tags. Any additional columns will be ignored. 
The parser can be used for any language and on any tagset.  

EXAMPLE INPUT:  
```
Jeder     PIDAT  
Mensch    NN  
trägt     VVFIN  
eine      ART  
Maske     NN  
.         $. 
```

## Output format

The output file will contain the sentences with the bracketing resulting from the parsing, one sentence per line.  

EXAMPLE OUTPUT:  
```
[ [ Jeder Mensch ] [ trägt [ eine Maske ] ] ]
```

## Running the parser

To execute the parser from the command line run
```
java -jar UnsuParse.jar <inputFile> <outputFile> [options]
```

The specification of the input and output files is mandatory while options are
optional.  
Possible options are
* -p for specifying that the parsing should be performed on POS tags instead of words directly,
* -s for using safe learning mode and omitting the unsafe learning step (results in shallow parse trees)
* -t <threshold> for specifying a threshold value (should be a double) to be used during the safe learning phase

An example call of the program would be
```
java -jar UnsuParse.jar input.txt parsedFile.txt -p -t 0.01
```
which would parse input.txt using POS tags and a threshold of 0.01 and save the parsed sentences to parsedFile.txt.
By default and if not specified otherwise the parsing is performed on words, the unsafe learning step is included and a threshold of 0.05 is used.

## References

Hänig, et al. (2008): [Unsuparse: unsupervised parsing with unsupervised part of speech tagging](http://asv.informatik.uni-leipzig.de/publication/file/132/lrec_unsuparse.pdf)


