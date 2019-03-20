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
