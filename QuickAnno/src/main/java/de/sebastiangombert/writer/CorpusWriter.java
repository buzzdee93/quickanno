/* 
 * This file is part of the QuickAnno application (https://github.com/buzzdee93/quickanno).
 * Copyright (c) 2019 Sebastian Gombert.
 * 
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package de.sebastiangombert.writer;

import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

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

public class CorpusWriter extends JCasConsumer_ImplBase {
	public static final String PARAM_CORPUS = "Corpus";
	@ConfigurationParameter(name = PARAM_CORPUS,
			description = "Corpus object.",
			mandatory = true)
	private String corpusId;
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		
		Corpus corpus = Corpus.corpusBank.get(corpusId);
		
		System.out.println(corpus);
		
		corpus.setCorpusText(aJCas.getDocumentText());
		
		for (Text text : JCasUtil.select(aJCas, Text.class)) {
			TextContainer textContainer = new TextContainer();
			textContainer.setBegin(text.getBegin());
			textContainer.setEnd(text.getEnd());
			textContainer.setTextDate(text.getDate());
			textContainer.setTextId(text.getId());
			textContainer.setTextVz(text.getVz());
			textContainer.setTextLegislature(text.getLegislature());
			textContainer.setTextParty(text.getParty());
			textContainer.setTextSpeaker(text.getSpeaker());
			textContainer.setTextYear(text.getYear());
			textContainer.setTextYearMonth(text.getYearMonth());
			corpus.getTextList().add(textContainer);
		}
		
		corpus.getTokenList().clear();
		corpus.getPosList().clear();
		corpus.getLemmaList().clear();
		corpus.getSentenceIndices().clear();
		corpus.getDeclIndices().clear();
		corpus.getMaybeIndices().clear();
		for (Sentence sent : JCasUtil.select(aJCas, Sentence.class)) {
			int[] sentenceIndices = new int[2];
			sentenceIndices[0] = sent.getBegin();
			sentenceIndices[1] = sent.getEnd();
			corpus.getSentenceIndices().add(sentenceIndices);
			
			List<Declarative> de = JCasUtil.selectCovered(aJCas, Declarative.class, sent);
			if (!de.isEmpty()) {
				int[] declIndices = new int[2];
				declIndices[0] = de.get(0).getBegin();
				declIndices[1] = de.get(0).getEnd();
				corpus.getDeclIndices().add(declIndices);
			}
			
			List<Maybe> may = JCasUtil.selectCovered(aJCas, Maybe.class, sent);
			if (!may.isEmpty()) {
				int[] mayIndices = new int[2];
				mayIndices[0] = may.get(0).getBegin();
				mayIndices[1] = may.get(0).getEnd();
				corpus.getMaybeIndices().add(mayIndices);
			}
			
			List<BreakAnchor> breakAnchor = JCasUtil.selectCovered(aJCas, BreakAnchor.class, sent);
			if (!breakAnchor.isEmpty()) {
				int[] anchorIndices = new int[2];
				anchorIndices[0] = sent.getBegin();
				anchorIndices[1] = sent.getEnd();
				corpus.setBreakIndices(anchorIndices);
			}
			
			for (Token tk : JCasUtil.selectCovered(aJCas, Token.class, sent)) {
				corpus.getTokenList().add(new Container<Token>(tk, "getCoveredText"));
				corpus.getPosList().add(new Container<POS>(JCasUtil.selectCovered(aJCas, POS.class, tk).get(0), "getPosValue"));
				corpus.getLemmaList().add(new Container<Lemma>(JCasUtil.selectCovered(aJCas, Lemma.class, tk).get(0), "getValue"));
			}
		}
	}

}
