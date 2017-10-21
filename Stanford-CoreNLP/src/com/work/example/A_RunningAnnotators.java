package com.work.example;

/**
 * Simple Program to show how Annotators are run.
 * 
 * 
 */


import edu.stanford.nlp.coref.data.Document;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.PropertiesUtils;
import java.util.*;

public class A_RunningAnnotators {
	
	    public static void main(String[] args) {

	        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
	        Properties props = new Properties();
	        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	        
	        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	        // read some text in the text variable
	        String text = "It isn't raining today!";

	        // create an empty Annotation just with the given text
	        Annotation document = new Annotation(text);

	        // run all Annotators on this text
	        pipeline.annotate(document);
	        System.out.println("Document annotating completed.");

	    }

	}