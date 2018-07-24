package stnfd.graph.extractor;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.semgraph.SemanticGraphEdge;

public class Sentence{
	
		String action;
		String objct;
		private String objct2;
		String compound;
		String amod;
		private String goal1;
		private String goal2;
	
		public String getObjct() {
			return objct;
		}
		public void setObjct(String objct) {
			this.objct = objct;
		}
		public String getObjct2() {
			return objct2;
		}
		public void setObjct2(String objct2) {
			this.objct2 = objct2;
		}
		public String getGoal1() {
			return goal1;
		}
		public void setGoal1(String goal1) {
			this.goal1 = goal1;
		}
		public String getGoal2() {
			return goal2;
		}
		public void setGoal2(String goal2) {
			this.goal2 = goal2;
		}
		private String text;
		private List<SemanticGraphEdge> dobjEdgeList = new ArrayList<SemanticGraphEdge>();
		private List<SemanticGraphEdge> compEdgeList = new ArrayList<SemanticGraphEdge>();
		private List<SemanticGraphEdge> amodEdgeList = new ArrayList<SemanticGraphEdge>();
		public List<SemanticGraphEdge> getDobjEdgeList() {
			return dobjEdgeList;
		}
		public void setDobjEdgeList(List<SemanticGraphEdge> dobjEdgeList) {
			this.dobjEdgeList = dobjEdgeList;
		}
		public List<SemanticGraphEdge> getCompEdgeList() {
			return compEdgeList;
		}
		public void setCompEdgeList(List<SemanticGraphEdge> compEdgeList) {
			this.compEdgeList = compEdgeList;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public List<SemanticGraphEdge> getAmodEdgeList() {
			return amodEdgeList;
		}
		public void setAmodEdgeList(List<SemanticGraphEdge> amodEdgeList) {
			this.amodEdgeList = amodEdgeList;
		}
		
		
		
		
	}