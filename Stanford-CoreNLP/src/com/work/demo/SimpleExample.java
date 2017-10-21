package com.work.demo;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.DeterministicCorefAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;


public class SimpleExample {

	static ArrayList<String> location = new ArrayList<String>();
    static ArrayList person = new ArrayList();
    static ArrayList time = new ArrayList();
    static ArrayList duration = new ArrayList();
    static ArrayList money = new ArrayList();

	
	public static void main(String[] args) throws IOException {
    // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
    Properties props = new Properties();
    props.put("customAnnotatorClass.stopwords", "lemma.StopwordAnnotation");
    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, stopwords");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
   // pipeline.addAnnotator(new CustomLemmaAnnotator("custom.lemma.lemmaFile"));
    // read some text from the file..
   // File inputFile = new File("src/test/resources/sample-content.txt");
    //String text = Files.toString(inputFile, Charset.forName("UTF-8"));
    String text ="Prime minister Modi met President Trump in Washigton.Therafter they went to  Europen countries including Germany, Italy  and France ...";  
    // create an empty Annotation just with the given text
    Annotation document = new Annotation(text);

    // run all Annotators on this text
    pipeline.annotate(document);

    // these are all the sentences in this document
    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

    for(CoreMap sentence: sentences) {
      // traversing the words in the current sentence
      // a CoreLabel is a CoreMap with additional token-specific methods
      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
        // this is the text of the token
        String word = token.get(TextAnnotation.class);
        // this is the POS tag of the token
        String pos = token.get(PartOfSpeechAnnotation.class);
        // this is the NER label of the token
        String ne = token.get(NamedEntityTagAnnotation.class);
        
        Boolean b  = token.get(StopwordAnnotation.class);
        
        if(b){
        }else{
        System.out.println("word: " + word + " pos: " + pos + " ne:" + ne + " b: " + b);
        
        if(ne.equals("LOCATION")){
        	location.add(word);
        }
        
        if(ne.equals("PERSON")){
        	person.add(word);
        }
        
        if(ne.equals("DATE")){
        	time.add(word);
        }
/*        if(ne.equals("MONEY")){
        	money.add(word);
        }
        if(ne.equals("DURATION")){
        	duration.add(word);
        }*/
        
      }
      }
      System.out.println("locations included in text are ::::::::::"+location);
      System.out.println("Person included in text are ::::::::::"+person);
      System.out.println("Date/Time included in text are ::::::::::"+time);
      // System.out.println("Money included in text are ::::::::::"+money);
      //System.out.println("Duration included in text are ::::::::::"+duration);
      // this is the parse tree of the current sentence
  
    }


    
  }

}