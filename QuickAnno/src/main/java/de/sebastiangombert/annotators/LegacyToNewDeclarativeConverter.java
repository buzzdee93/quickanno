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

package de.sebastiangombert.annotators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.sebastiangombert.types.Declarative;
import de.sebastiangombert.types.LegacyDeclarative;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;

public class LegacyToNewDeclarativeConverter extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		
		Collection<Sentence> sents = JCasUtil.select(aJCas, Sentence.class);
		Collection<LegacyDeclarative> decls = JCasUtil.select(aJCas, LegacyDeclarative.class);
		
		for (LegacyDeclarative decl : decls) {
			for (Sentence sent : sents) {
				if ((decl.getBegin() >= sent.getBegin() && decl.getEnd() <= sent.getEnd()) 
						|| (decl.getEnd() <= sent.getEnd() && decl.getEnd() >= sent.getBegin())
						|| (decl.getBegin() >= sent.getBegin() && decl.getBegin() <= sent.getEnd())) {
						
					Declarative newDecl = new Declarative(aJCas, sent.getBegin(), sent.getEnd());
					newDecl.addToIndexes();
				}
			}
		}
		
	}

}
