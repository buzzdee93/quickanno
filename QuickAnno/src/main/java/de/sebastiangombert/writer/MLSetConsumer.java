package de.sebastiangombert.writer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.sebastiangombert.ml.Feature;
import de.sebastiangombert.ml.Instance;
import de.sebastiangombert.ml.MLSet;
import de.sebastiangombert.ml.NominalToNumericFeature;
import de.sebastiangombert.ml.NumericFeature;
import de.sebastiangombert.types.Declarative;
import de.sebastiangombert.types.Maybe;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;

public class MLSetConsumer extends JCasConsumer_ImplBase {
	public static final String PARAM_ML_SET = "MLSet";
	@ConfigurationParameter(name = PARAM_ML_SET,
			description = "MLSet",
			mandatory = true)
	private int mlSetId;
	
	public static final String PARAM_CREATE_TRAINING_SET = "Create training set.";
	@ConfigurationParameter(name = PARAM_CREATE_TRAINING_SET,
			description = "Create training set",
			mandatory = true)
	private boolean createTrainingSet;
	
	public static final String PARAM_NUM_WORD_FEATURES = "Num word features";
	@ConfigurationParameter(name = PARAM_NUM_WORD_FEATURES,
			description = "Create training set",
			mandatory = true)
	private int numWordFeatures;
	
	public static final String PARAM_PERCENT_TOTAL = "Percent total";
	@ConfigurationParameter(name = PARAM_PERCENT_TOTAL,
			description = "Param percent total",
			mandatory = false)
	private float percentTotal;
	
	public static final String PARAM_PERCENT_DECLARATIVE = "Percent declarative";
	@ConfigurationParameter(name = PARAM_PERCENT_DECLARATIVE,
			description = "Param percent declarative",
			mandatory = false)
	private float percentDeclarative;
	
	private void incrementInMap(Map<String, Integer> map, String term) {
		if (map.containsKey(term))
			map.put(term, map.get(term) +1);
		else
			map.put(term, 1);
	}
	
	private String[] stopLemmata = {
			")", "!", ",", ".", "-", "'"
	};
	
	private String[] predefinedKeywords = {
		"iermit",
		"m Namen von",
		"n meiner Funktion als",
		"kündig",
		"erteil",
		"verbiet",
		"m Namen des",
		"tauf",
		"das Wort",
		"Das Wort"
	};
	
	private String[] predefinedLemmata = {
			"eröffnen",
			"bitten", 
			"auffordern",
			"ablehnen",
			"eintreten",
			"hinweisen",
			"erlauben", 
			"genehmigen",
			"erteilen",
			"fortfahren",
			"feststellen",
			"beschließen", 
			"schließen"		
	};

	@SuppressWarnings("unchecked")
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		MLSet mlSet = MLSet.byId(mlSetId);
		
		int longestSentence = 0;
		
		if (createTrainingSet) {
			for (Sentence sent : JCasUtil.select(aJCas, Sentence.class)) {
				boolean isDeclarative = !JCasUtil.selectCovered(aJCas, Declarative.class, sent).isEmpty();
				
				int length = JCasUtil.selectCovered(aJCas, Token.class, sent).size();
				if (length > longestSentence)
					longestSentence = length;
				
				for (Lemma lemma : JCasUtil.selectCovered(aJCas, Lemma.class, sent)) {
					
					boolean toFilter = false;
					for (String toFilt : stopLemmata) {
						if (toFilt.equals(lemma.getValue())) {
							toFilter = true;
							break;
						}
					}
					if (!toFilter) toFilter = !JCasUtil.selectCovered(aJCas, NamedEntity.class, lemma).isEmpty();
					
					if (!toFilter) {
						incrementInMap(mlSet.getInstancesTotal(), lemma.getValue());
						if (isDeclarative)
							incrementInMap(mlSet.getInstancesDeclarative(), lemma.getValue());
						
					}
				}
			}
		}
		
		mlSet.calculateRelativeDistributions(percentTotal, percentDeclarative);
		
		List<String> terms = mlSet.getNMostSignificantTerms(this.numWordFeatures);
		
		for (Sentence sent : JCasUtil.select(aJCas, Sentence.class)) {
			Feature[] features = new Feature[this.numWordFeatures + 4];
			
			double significantTermCount = 1.0;
			
			int i = 0;
			for (String term : terms) {
				boolean contained = false;
				for (Lemma lemma : JCasUtil.selectCovered(aJCas, Lemma.class, sent)) {
					if (lemma.getValue().equals(term)) {
						contained = true;
						break;
					}
				}
				NumericFeature f = new NumericFeature();
				f.setName(term);
				if (contained) {
					f.setFeatureContent(1.0);
					significantTermCount *= (1 + mlSet.getRelativeDeclarativeFrequencies().get(term));
				} else
					f.setFeatureContent(0.0);
				
				features[i] = f;
				i++;
			}
			
			NumericFeature significantTerms = new NumericFeature();
			significantTerms.setName("significantTerms");
			significantTerms.setFeatureContent(significantTermCount);
			features[i] = significantTerms;
			i++;
			
			NumericFeature sentenceLength = new NumericFeature();
			sentenceLength.setName("sentenceLength");
			sentenceLength.setFeatureContent(((double)JCasUtil.selectCovered(aJCas, Token.class, sent).size()) / ((double)longestSentence));
			features[i] = sentenceLength;
			i++;
			
			double pred = 0.0;
			String coveredText = sent.getCoveredText();
			for (String key : predefinedKeywords) {
				if (coveredText.contains(key))
					pred++;
			}
			
			NumericFeature containsPredefinedKeyword = new NumericFeature();
			containsPredefinedKeyword.setName("containsPredefinedKeyword");
			containsPredefinedKeyword.setFeatureContent(pred / predefinedKeywords.length);
			features[i] = containsPredefinedKeyword;
			i++;
			
			double containsLemma = 0.0;
			for (Lemma lemma : JCasUtil.selectCovered(aJCas, Lemma.class, sent)) {
				List<String> seen = new ArrayList<>();
				for (String pr : predefinedLemmata) {
					if (lemma.equals(pr) && !seen.contains(pr)) {
						containsLemma++;
					}
				}
			}
			
			NumericFeature containsPredefinedLemma = new NumericFeature();
			containsPredefinedLemma.setName("containsPredefinedLemma");
			containsPredefinedLemma.setFeatureContent(containsLemma / predefinedLemmata.length);
			features[i] = containsPredefinedLemma;
			i++;
			
			Instance inst = new Instance();
			inst.setFeatures(features);
			
			if (JCasUtil.selectCovered(aJCas, Declarative.class, sent).isEmpty())
				inst.setLabel("None");
			else
				inst.setLabel("Declarative");
			
			if (this.createTrainingSet)
				mlSet.getTrainingInstances().add(inst);
			else
				mlSet.getClassificationInstances().add(inst);
			

		}
		
	}

}
