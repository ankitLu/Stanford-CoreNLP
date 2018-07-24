package stnfd.graph.builder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;

public class RelationExtractor {

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
		RelationExtractor sp = new RelationExtractor();
		sp.initializeStanford();
		for (int i = 0; i < text.length; i++) {
			String sentence = text[i];
			System.out.println();System.out.println();
			System.out.println(i + " Sentence: " + sentence);
			sp.findImportantStuff(sentence);
			sp.lookForRelations();
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

	private void findImportantStuff(String text) {
		st = new Sentence();
		st.setText(text);
		// create a document object
		document = new CoreDocument(text);
		pipeline.annotate(document);
		CoreSentence sentence = document.sentences().get(0);

		SemanticGraph dependencyParse = sentence.dependencyParse();
		for (SemanticGraphEdge edge : dependencyParse.edgeIterable()) {
			GrammaticalRelation relation = edge.getRelation();
			String relationString = relation.toString();
			IndexedWord source = edge.getSource();
			IndexedWord target = edge.getTarget();

			// Rule 1
			// Get all the Dobjs

			if (relationString.equals(CommonConstants.dobj)) {
				SemanticGraphEdge onlyDobjEdge = new SemanticGraphEdge(source, target, relation, 0.0, false);
				st.getDobjEdgeList().add(onlyDobjEdge);
			}

			// Rule 2
			// Find the compound
			if (relationString.equals(CommonConstants.compound)) {
				SemanticGraphEdge onlyCmpoundEdge = new SemanticGraphEdge(source, target, relation, 0.0, false);
				st.getCompEdgeList().add(onlyCmpoundEdge);
			}
			
			// Rule 3
			// Find all the amods
			if (relationString.equals(CommonConstants.amod)) {
				SemanticGraphEdge onlyAmodEdge = new SemanticGraphEdge(source, target, relation, 0.0, false);
				st.getAmodEdgeList().add(onlyAmodEdge);
			}

			sentenceList.add(st);
		}
	}
	
	
	
	/**
	 * printer
	 * @param strings
	 */
	private static void printer(String...strings) {
		for (String string: strings) {
			if (string!=null && !string.equals(space.trim())) {
				System.out.print(string);
				System.out.print(space);
			}
		}
		System.out.println();
			
	}
	
	
	
	/**
	 * Look for relations
	 */
	private void lookForRelations() {
		for (SemanticGraphEdge dobjEdge : st.getDobjEdgeList()) {
			st.action = dobjEdge.getSource().toString().split("/")[0];
			st.objct = dobjEdge.getTarget().toString().split("/")[0];
			
			// Look for compound relationship
			
			for (SemanticGraphEdge compoundEdge : st.getCompEdgeList()) {
				IndexedWord cmpndSource = compoundEdge.getSource();
				IndexedWord cmpndTarget = compoundEdge.getTarget();
				if (cmpndSource.equals(dobjEdge.getTarget())) {
					st.compound = cmpndTarget.toString().split("/")[0];
				}
			}
			
			// Look for amod relationship
			
			for (SemanticGraphEdge amodEdge : st.getAmodEdgeList()) {
				IndexedWord amodSource = amodEdge.getSource();
				IndexedWord amodTarget = amodEdge.getTarget();
				if (amodSource.equals(dobjEdge.getTarget())) {
					st.amod = amodTarget.toString().split("/")[0];
				}
			}
			
			// finally print relationship
			
			if (st.compound != null || st.amod!=null || (st.action !=null && st.objct != null)) {
				// finally print sentence
				printer(st.action,st.amod,st.compound,st.objct);
				st.compound = null;
				st.amod = null;
			}
			
			
		}
	}


}
