package de.sebastiangombert.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class Corpus {
	
	public static HashMap<String, Corpus> corpusBank = new HashMap<>();
	
	private static int corpusCount;
	
	private String id;

	private String corpusText;
	private List<Container<Token>> tokenList = new ArrayList<>();
	private List<Container<POS>> posList = new ArrayList<>();
	private List<Container<Lemma>> lemmaList = new ArrayList<>();
	
	private int[] breakIndices = null;

	private ArrayList<int[]> sentenceIndices = new ArrayList<>();
	private ArrayList<int[]> declIndices = new ArrayList<>();
	private ArrayList<int[]> maybeIndices = new ArrayList<>();
	
	private ArrayList<TextContainer> textList = new ArrayList<>();
	
	public Corpus() {
		this.id = ""+corpusCount++;
		corpusBank.put(this.id, this);
	}
	
	public Corpus(String id) {
		if (corpusBank.containsKey(id)) {
			Corpus toCopy = corpusBank.get(id);
			this.corpusText = toCopy.corpusText;
			this.tokenList = toCopy.tokenList;
			this.posList = toCopy.posList;
			this.lemmaList = toCopy.lemmaList;
			this.sentenceIndices = toCopy.sentenceIndices;
			this.declIndices = toCopy.declIndices;
			this.maybeIndices = toCopy.maybeIndices;
			this.breakIndices = toCopy.breakIndices;
			this.textList = toCopy.textList;
		}
		this.setId(id);
		corpusBank.put(id, this);
	}

	public String getCorpusText() {
		return corpusText;
	}

	public void setCorpusText(String corpusText) {
		this.corpusText = corpusText;
	}

	public ArrayList<int[]> getSentenceIndices() {
		return sentenceIndices;
	}

	public void setSentenceIndices(ArrayList<int[]> sentenceIndices) {
		this.sentenceIndices = sentenceIndices;
	}

	public ArrayList<int[]> getDeclIndices() {
		return declIndices;
	}

	public void setDeclIndices(ArrayList<int[]> declIndices) {
		this.declIndices = declIndices;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Container<Token>> getTokenList() {
		return tokenList;
	}

	public void setTokenList(List<Container<Token>> tokenList) {
		this.tokenList = tokenList;
	}

	public List<Container<POS>> getPosList() {
		return posList;
	}

	public void setPosList(List<Container<POS>> posList) {
		this.posList = posList;
	}

	public List<Container<Lemma>> getLemmaList() {
		return lemmaList;
	}

	public void setLemmaList(List<Container<Lemma>> lemmaList) {
		this.lemmaList = lemmaList;
	}

	public ArrayList<int[]> getMaybeIndices() {
		return maybeIndices;
	}

	public void setMaybeIndices(ArrayList<int[]> maybeIndices) {
		this.maybeIndices = maybeIndices;
	}

	public ArrayList<TextContainer> getTextList() {
		return textList;
	}

	public void setTextList(ArrayList<TextContainer> textList) {
		this.textList = textList;
	}

	public int[] getBreakIndices() {
		return breakIndices;
	}

	public void setBreakIndices(int... breakIndices) {
		this.breakIndices = breakIndices;
	}
	
	
}
