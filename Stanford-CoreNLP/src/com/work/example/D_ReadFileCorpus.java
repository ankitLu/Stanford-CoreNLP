package com.work.example;

import java.util.List;

/**
 * 
 * A different style to pass anotators to properties
 * 
 */

import java.util.Properties;

import com.work.utility.CreateCorpus;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

public class D_ReadFileCorpus {

    public static void main(String[] args) {

    	
    	/**
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, natlog");
        **/	
    	
    	
    	Properties props = PropertiesUtils.asProperties(
    			"annotators", "tokenize,ssplit,pos,lemma, ner,parse,depparse,relation",
    			"ssplit.isOneSentence", "true",
    			"parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz",
    			"tokenize.language", "en");
        
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        CreateCorpus obj =  new CreateCorpus("/corpus/HealthNews");
        String text = obj.getCorpus();
        
        text="hello, how are you mr. Peter England";

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        System.out.println(document);
        
        // these are all the sentences in this document
	     // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	     List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	
	     for(CoreMap sentence: sentences) {
	       // traversing the words in the current sentence
	       // a CoreLabel is a CoreMap with additional token-specific methods
	       for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	         // this is the text of the token
	    	   //String tokenn = token.get(TokenizerAnnotator.class);
	         String word = token.get(TextAnnotation.class);
	         // this is the POS tag of the token
	         String pos = token.get(PartOfSpeechAnnotation.class);
	         // this is the NER label of the token
	         String ne = token.get(NamedEntityTagAnnotation.class);
	      // this is the Relation Extractor label of the token
	         @SuppressWarnings("deprecation")
			SemanticGraph tree = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
	         
	         System.out.println(String.format("Print: word: [%s] pos: [%s] ne: [%s] tree: [%tree]", word, pos, ne, tree));
	         
	       }
	
	       // this is the parse tree of the current sentence
	       //Tree tree = sentence.get(TreeAnnotation.class);
	
	       // this is the Stanford dependency graph of the current sentence
	       //SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
	     }
	
	     // This is the coreference link graph
	     // Each chain stores a set of mentions that link to each other,
	     // along with a method for getting the most representative mention
	     // Both sentence and token offsets start at 1!
//	     Map<Integer, CorefChain> graph = 
//	       document.get(CorefChainAnnotation.class);
	
	    }
	
	}