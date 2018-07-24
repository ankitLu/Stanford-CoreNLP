package stnfd.graph.extractor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;

public class GraphExtractor {

	private static final String space = CommonConstants.space;
	private static List<Sentence> sentenceList = new ArrayList<Sentence>();
	StanfordCoreNLP pipeline;
	CoreDocument document;
	Sentence st;

	public static String[] text = new String[] {
			"After collecting host information, CHOPSTICK creates a hidden file that may be named filename for temporary storage.",
			"encrypt and store output from other malware.",
			"It creates a thread that records user activity on the host, capturing desktop screenshots in JPEG format, tracks current window focus, collects keystrokes, and scrapes window contents (text, context menus, etc.).",
			"User activity is captured once every 500 milliseconds and logged in an HTML-like format.",
			"CHOPSTICK reads messages from the mailslot, encrypts them using RC4, and then stores the encrypted message in an edg6EF885E2.tmp temporary file.",
			"After approximately 60 seconds of execution time, CHOPSTICK begins communicating with one of its C2 servers over HTTP.",
			"After sending an initial HTTPGET request it uploads the file contents of filename to the C2 server using HTTP POST requests.",
			"Once the contents of filename are uploaded, CHOPSTICK deletes the file.",
			"The message body of the POST request is also base64 encoded.",
			"Credentials for the following applications are collected.",
			"Malware exploits local privilege escalation vulnerability to steal System token.",
			"A tool that exploits a privilege escalation vulnerability (CVE-2014-4076) to gain system privileges.",
			"Both email and HTTP can be used to send out the collected credentials." };

	/**
	 * 
	 * @param args
	 */
	

	public static void main(String[] args) {
		GraphExtractor sp = new GraphExtractor();
		sp.initializeStanford();
		for (int i = 0; i < text.length; i++) {
			String sentence = text[i];
			System.out.println();System.out.println();
			System.out.println(i + " Sentence: " + sentence);
			sp.readCSV();
		}
	}


	
	/**
	 * initializeStanford
	 */
	private void initializeStanford() {
		// set up pipeline properties
		Properties props = new Properties();
		// set the list of annotators to run
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse");
		props.setProperty("parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz");
		// build pipeline
		pipeline = new StanfordCoreNLP(props);
		
	}
	

/**
 * findImportantStuff
 * @param pipeline
 * @param st
 */

	private void findImportantStuff(Sentence st) {
		String line = st.getText();
		// create a document object
		document = new CoreDocument(line);
		pipeline.annotate(document);
		CoreSentence sentence = document.sentences().get(0);
		String src = "collecting";
		String tgt = "file";
 		SemanticGraph dependencyParse = sentence.dependencyParse();
 		
 		// now for every verb
 		String actionArr[] = st.action.split(space);
 		String restElements[] = st.objct.split(space);
 		for (String action: actionArr) {
 			for (String othr: restElements) {
 				extractRoute(action, othr, dependencyParse);
 			}
 		}
 		
		
	}



private void extractRoute(String src, String tgt, SemanticGraph dependencyParse) {
	IndexedWord source = dependencyParse.getNodeByWordPattern(src);
	IndexedWord target = dependencyParse.getNodeByWordPattern(tgt);
	List<SemanticGraphEdge> edgeList = dependencyParse.getShortestDirectedPathEdges(source, target);
	for(SemanticGraphEdge edge: edgeList) {
		System.out.print(edge.getRelation() + " -> ");
	}
	System.out.print("("+source+","+target+")");
	System.out.println();
}
	
	
	
	
	
	
	
	/**
	 * Look for relations
	 */
	private Sentence readCSV() {
		 String csvFile = "/Users/mkyong/csv/country.csv";
	        BufferedReader br = null;
	        String line = "";
	        String cvsSplitBy = ",";

	        try {

	            br = new BufferedReader(new FileReader(csvFile));
	            while ((line = br.readLine()) != null) {

	                // use comma as separator
	                String[] elements = line.split(cvsSplitBy);
	                Sentence sen = new Sentence();
	                sen.action = elements[0].trim();
	                sen.objct = elements[1].trim();
	                sen.setText(elements[5].trim());
	                GraphExtractor ge = new GraphExtractor();
	                ge.findImportantStuff(sen);

	            }

	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
			return null;

	}


}
