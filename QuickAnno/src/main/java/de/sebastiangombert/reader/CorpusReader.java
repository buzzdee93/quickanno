package de.sebastiangombert.reader;

import java.io.IOException;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.JCasBuilder;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import de.sebastiangombert.model.Container;
import de.sebastiangombert.model.Corpus;
import de.sebastiangombert.model.TextContainer;
import de.sebastiangombert.types.BreakAnchor;
import de.sebastiangombert.types.Declarative;
import de.sebastiangombert.types.Maybe;
import de.sebastiangombert.types.Text;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class CorpusReader extends JCasCollectionReader_ImplBase {
	
	public static final String PARAM_CORPUS = "Corpus";
	@ConfigurationParameter(name = PARAM_CORPUS,
			description = "Corpus object.",
			mandatory = true)
	private Corpus corpus;
	
	private boolean read = false;

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		return !read;
	}

	@Override
	public Progress[] getProgress() {
		return new Progress[] { new ProgressImpl(read ? 1 : 0, 1,
				Progress.ENTITIES) };
	}

	@Override
	public void getNext(JCas jCas) throws IOException, CollectionException {
		
		jCas.setDocumentText(corpus.getCorpusText());
		
		for (TextContainer textContainer : corpus.getTextList()) {
			Text text = new Text(jCas, textContainer.getBegin(), textContainer.getEnd());
			
			text.setDate(textContainer.getTextDate());
			text.setId(textContainer.getTextId());
			text.setVz(textContainer.getTextVz());
			text.setLegislature(textContainer.getTextLegislature());
			text.setParty(textContainer.getTextParty());
			text.setSpeaker(textContainer.getTextSpeaker());
			text.setYear(textContainer.getTextYear());
			text.setYearMonth(textContainer.getTextYearMonth());
			
			text.addToIndexes();
		}
		
		if (corpus.getBreakIndices() != null) {
			BreakAnchor anch = new BreakAnchor(jCas, corpus.getBreakIndices()[0], 
					corpus.getBreakIndices()[1]);
			
			anch.addToIndexes();
		}
		
		for (int[] indices : corpus.getSentenceIndices()) {
			Sentence sent = new Sentence(jCas, indices[0], indices[1]);
			sent.addToIndexes();
		}
		
		for (int[] indices : corpus.getDeclIndices()) {
			Declarative decl = new Declarative(jCas, indices[0], indices[1]);
			decl.addToIndexes();
		}
		
		for (int[] indices : corpus.getMaybeIndices()) {
			Maybe maybe = new Maybe(jCas, indices[0], indices[1]);
			maybe.addToIndexes();
		}
		
		for (Container<Token> tk : corpus.getTokenList()) {
			Token tok = new Token(jCas, tk.getFrom(), tk.getTo());
			tok.addToIndexes();
		}
		
		for (Container<POS> pos : corpus.getPosList()) {
			POS ps = new POS(jCas, pos.getFrom(), pos.getTo());
			ps.setPosValue(pos.getContent());
			ps.addToIndexes();
		}
		
		for (Container<Lemma> lemma : corpus.getLemmaList()) {
			Lemma lem = new Lemma(jCas, lemma.getFrom(), lemma.getTo());
			lem.setValue(lemma.getContent());
			lem.addToIndexes();
		}
		
		read = true;
		
	}

}
