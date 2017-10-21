//Importing the required classes
package com.work.demo;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class BasicPipelineExample {

	public static void main(String[] args) throws IOException {

		String positive = ReadPositive("G://DEMO_KDD//positivefile.txt");
		String negative = ReadPositive("G://DEMO_KDD//negativefile.txt");
		//System.out.println("positivefile read:::::::::" + positive);
		//System.out.println("negativefile read:::::::::" + negative);
		List positive_words = Preprocessing(positive);
		List negative_words = Preprocessing(negative);

		System.out.println("positive_wordslist::::::::::::::::: " + positive_words);
		System.out.println("negative_wordslist::::::::::::::::: " + negative_words);
		SentimentAnalyis(positive_words, negative_words);

	}
	
	

	private static void SentimentAnalyis(List pos, List neg) throws IOException {

		TreeSet t1 = new TreeSet();
		TreeSet t2 = new TreeSet();
		t1.addAll(pos);
		t2.addAll(neg);
		System.out.println(t1.size());

		for (int n = 0; n < 10; n++) {
			System.out.println("Please provide you review");
			Scanner sc = new Scanner(System.in);
			String s = sc.next();
			s += sc.nextLine();
			List review_words = Preprocessing(s);
			double prob_pos = 1;
			double prob_neg = 1;
			for (int i = 0; i < review_words.size(); i++) {

				double pos_count = 0;
				double neg_count = 0;
				String word = (String) review_words.get(i);
				for (int j = 0; j < pos.size(); j++) {
					String pos_word = (String) pos.get(j);
					if (word.equals(pos_word)) {
						pos_count++;
					}
				}

				for (int j = 0; j < neg.size(); j++) {
					String neg_word = (String) neg.get(j);
					if (word.equals(neg_word)) {
						neg_count++;
					}
				}

				prob_pos = (prob_pos * ((pos_count + 1) / (pos.size() + t1.size())));
				prob_neg = (prob_neg * ((neg_count + 1) / (neg.size() + t2.size())));
				System.out.println("word ::" + word +" prob_pos:::::::::: " + prob_pos);
				System.out.println("word ::" + word +" prob_neg:::::::::: " + prob_neg);
			}

			if (prob_pos > prob_neg) {
				System.out.println("Your review is positive");
			} else if (prob_pos < prob_neg) {
				System.out.println("Your review is negative");
			} else {
				System.out.println("Your review is neutral");
			}

		}
	}

	private static List Preprocessing(String positive) throws IOException {
		Properties props = new Properties();
		PrintWriter xmlOut = new PrintWriter("xmlOutput.xml");
		props.setProperty("annotators", " tokenize, ssplit,parse, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		ArrayList l = new ArrayList<>();
		// read some text in the text variable
		// String text = "operate operating operates operation operative
		// operatives operational";
		// great performaces and thrilling experience -pos
		// poorly written movie and pathetic acting
		//dismal performances
		Annotation document_positive = new Annotation(positive);

		// run all Annotators on this text
		pipeline.annotate(document_positive);

		pipeline.xmlPrint(document_positive, xmlOut);
		List<CoreMap> sentences_positive = document_positive.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences_positive) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				int charBegin = token.get(CharacterOffsetBeginAnnotation.class);
				int charEnd = token.get(CharacterOffsetEndAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				// this is the NER label of the token
				String ne = token.get(NamedEntityTagAnnotation.class);
				String lemma = token.getString(LemmaAnnotation.class);
				System.out
						.println(String.format("word :[%s] pos :[%s] ne: [%s] Lemma :[%s] charStart [%d] char End[%d]",
								word, pos, ne, lemma, charBegin, charEnd));
				// System.out.println(String.format("word :[%s] Lemma :[%s]",
				// word, lemma));
				l.add(lemma);
			}
		}

		return l;
	}

	private static String ReadPositive(String file) {
		String text = null;
		String x = null;
		BufferedReader br = null;
		FileReader fr = null;
		StringBuffer bf = new StringBuffer();
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String sCurrentLine = null;

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				bf.append(sCurrentLine);
			}
			text = bf.toString();
			return text;
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		return text;

	}
}