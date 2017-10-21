package com.work.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.Annotator;
import edu.stanford.nlp.util.ArraySet;

public class StopwordAnnotation implements Annotator, CoreAnnotation<Boolean> {

	public static final String ANNOTATOR_CLASS = "stopword";

	public static final String STOPWORDS_LIST = "stopword-list";

	private static Class<Boolean> bool;

	private Properties props;
	private ArrayList<String> stopwords;

	public StopwordAnnotation() {
		this(new Properties());
	}

	public StopwordAnnotation(String notUsed, Properties props) {
		this(props);
	}

	public StopwordAnnotation(Properties props) {
		this.props = props;
        String s= "a,an,the,this,that,of,in,on,for,under,or,and,upon,The";
		
			this.stopwords = getStopWordList(s);

		
	}

	@Override
	public void annotate(Annotation annotation) {
		if (stopwords != null && stopwords.size() > 0
				&& annotation.containsKey(CoreAnnotations.TokensAnnotation.class)) {
			List<CoreLabel> tokens = annotation.get(CoreAnnotations.TokensAnnotation.class);
			for (CoreLabel token : tokens) {
				boolean isWordStopword = stopwords.contains(token.word().toLowerCase());
				token.set(StopwordAnnotation.class, isWordStopword);
			}
		}
	}

	@Override
	public Set<Class<? extends CoreAnnotation>> requirementsSatisfied() {
		return Collections.singleton(com.work.demo.StopwordAnnotation.class);
	}

	@Override
	public Set<Class<? extends CoreAnnotation>> requires() {
		return Collections.unmodifiableSet(new ArraySet<>(
				Arrays.asList(CoreAnnotations.TextAnnotation.class, CoreAnnotations.TokensAnnotation.class,
						// CoreAnnotations.LemmaAnnotation.class,
						CoreAnnotations.PartOfSpeechAnnotation.class)));
	}

	public static ArrayList<String> getStopWordList(String stopwordList) {
		String[] terms = stopwordList.split(",");
		ArrayList<String> stopwords = new ArrayList<String>();
		for (String term : terms) {
			stopwords.add(term);
		}
		return stopwords;
	}

	@Override
	public Class<Boolean> getType() {
		// TODO Auto-generated method stub
		return bool;
	}
}