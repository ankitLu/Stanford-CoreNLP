package com.work.example;

/**
 * 
 * A different style to pass anotators to properties
 * 
 */

import java.util.Properties;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.PropertiesUtils;

public class B_PropertiesStyle {

    public static void main(String[] args) {

    	
    	/**
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        **/	
    	
    	
    	Properties props = PropertiesUtils.asProperties(
    			"annotators", "tokenize,ssplit,pos,lemma,parse,natlog",
    			"ssplit.isOneSentence", "true",
    			"parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz",
    			"tokenize.language", "en");
        
        
        
        
        
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        String text = "It isn't raining today!";

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        System.out.println(document);
        System.out.println("Am done!");

    }

}