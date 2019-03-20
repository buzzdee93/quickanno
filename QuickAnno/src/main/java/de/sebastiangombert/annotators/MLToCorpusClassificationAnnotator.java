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

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.sebastiangombert.ml.MLSet;
import de.sebastiangombert.types.Declarative;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;

public class MLToCorpusClassificationAnnotator extends JCasAnnotator_ImplBase {
	public static final String PARAM_ML_SET = "MLSet";
	@ConfigurationParameter(name = PARAM_ML_SET,
			description = "MLSet",
			mandatory = true)
	private int mlSetId;
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		MLSet mlSet = MLSet.byId(mlSetId);
		
		int i = 0;
		for (Sentence sent : JCasUtil.select(aJCas, Sentence.class)) {
			if (mlSet.getClassificationInstances().get(i).getPredictedLabel().equals("Declarative")) {
				Declarative decl = new Declarative(aJCas, sent.getBegin(), sent.getEnd());
				decl.addToIndexes();
			}
			i++;
		}
		
	}

}
