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

package de.sebastiangombert.ml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.LMT;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class MLSet {
	
	public static String outPath = System.getProperty("user.home") + "/QuickAnno/MLTemps/";
	
	private static HashMap<Integer, MLSet> mlSetContainer = new HashMap<>();
	
	private static int maxId = 0;
	
	private HashMap<String, Integer> instancesTotal = new HashMap<>();
	private HashMap<String, Integer> instancesDeclarative = new HashMap<>();
	private HashMap<String, Double> relativeDeclarativeFrequencies = new HashMap<>();
	
	public MLSet() {
		mlSetContainer.put(maxId, this);
		this.setId(maxId);
		maxId++;
	}
	
	public static MLSet byId(int id) {
		return mlSetContainer.get(id);
	}
	
	private int id;
	
	private static String LF = System.lineSeparator();

	private List<Instance> trainingInstances = new ArrayList<>();
	
	private List<Instance> classificationInstances = new ArrayList<>();
	
	private String fileName;
	
	private LMT classifier;
	
	private Evaluation eval;
	
	public List<Instance> getTrainingInstances() {
		return this.trainingInstances;
	}
	
	public List<Instance> getClassificationInstances() {
		return this.classificationInstances;
	}
	
	public String toArffString(boolean training) {
		
		List<Instance> instances = training ? this.trainingInstances : this.classificationInstances;
		
		List<String> labels = new ArrayList<>();
		for (Instance inst : instances) {
			if (!labels.contains(inst.getLabel()))
				labels.add(inst.getLabel());
		}
		
		StringBuilder strBuild = new StringBuilder();
		
		strBuild.append("@relation 'Declarative Speech Acts'");
		strBuild.append(LF);
		strBuild.append(LF);
		
		Instance first = instances.get(0);
		
		for (Feature<? extends Object> feature : first.getFeatures()) {
			strBuild.append("@attribute ");
			strBuild.append(feature.getName());
			strBuild.append(" numeric");
			strBuild.append(LF);
		}
		strBuild.append("@attribute outcome {None,Declarative}");
		
		strBuild.append(LF);
		strBuild.append(LF);
		strBuild.append("@data");
		strBuild.append(LF);
		
		for (Instance inst : instances) {
			for (Feature<? extends Object> feature : inst.getFeatures()) {
				strBuild.append(feature.getEncodedFeatueContent());
				strBuild.append(',');
			}
			strBuild.append(inst.getLabel());
			strBuild.append(LF);
		}
		
		return strBuild.toString();
	}
	
	public void saveArffTemp(boolean training) {
		try {
			String finalName = this.fileName + (training ? "_training" : "_classification");
			
			String outputPath = outPath + finalName + ".arff";
			
			new File( outPath).mkdirs();
			new File(outputPath).createNewFile();
			
			BufferedWriter write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), "utf8"));
			
			write.write(this.toArffString(training));
			
			write.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HashMap<String, Integer> getInstancesTotal() {
		return instancesTotal;
	}

	public void setInstancesTotal(HashMap<String, Integer> instancesTotal) {
		this.instancesTotal = instancesTotal;
	}

	public HashMap<String, Integer> getInstancesDeclarative() {
		return instancesDeclarative;
	}

	public void setInstancesDeclarative(HashMap<String, Integer> instancesDeclarative) {
		this.instancesDeclarative = instancesDeclarative;
	}

	public HashMap<String, Double> getRelativeDeclarativeFrequencies() {
		return relativeDeclarativeFrequencies;
	}

	public void setRelativeDeclarativeFrequencies(HashMap<String, Double> relativeDeclarativeFrequencies) {
		this.relativeDeclarativeFrequencies = relativeDeclarativeFrequencies;
	}
	
	public List<String> getNMostSignificantTerms(int n) {
		List<Entry<String, Double>> orderedTerms = new ArrayList<>(this.relativeDeclarativeFrequencies.entrySet());
		Collections.sort(orderedTerms, (a, b) -> { return (int)(b.getValue() * 100 - a.getValue() * 100); });
		
		List<String> terms = new ArrayList<>(n);
		for (int i = 0; i < n && i < orderedTerms.size(); i++)
			terms.add(orderedTerms.get(i).getKey());
		
		return terms;
	}
	
	public void calculateRelativeDistributions(double procentualThresholdTotal, double procentualThresholdDeclarative) {
		int totalTerms = 0;
		int declTerms = 0;
		
		for (Entry<String, Integer> ent : this.getInstancesTotal().entrySet())
			totalTerms += ent.getValue();

		for (Entry<String, Integer> ent : this.getInstancesDeclarative().entrySet())
			declTerms += ent.getValue();
		
		for (Entry<String, Integer> ent : this.getInstancesTotal().entrySet()) {
			if (ent.getValue() < (totalTerms / 100) * procentualThresholdTotal )
				continue;
			
			if (this.getInstancesDeclarative().get(ent.getKey()) == null || this.getInstancesDeclarative().get(ent.getKey()) < (declTerms / 100) * procentualThresholdDeclarative)
				continue;
			
			double declFrequency = this.getInstancesDeclarative().containsKey(ent.getKey()) ? this.getInstancesDeclarative().get(ent.getKey()) : 0;
			double logedTermDeclFrequency = 1 + Math.log10(declFrequency) - Math.log10(ent.getValue());
			
			this.getRelativeDeclarativeFrequencies().put(ent.getKey(), logedTermDeclFrequency);
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	private Instances toWekaDataSet(boolean training) {
		List<Instance> instances = training ? this.trainingInstances : this.classificationInstances;

		ArrayList<Attribute> atts = new ArrayList<>(instances.get(0).getFeatures().length +1);
		for (int i = 0; i < instances.get(0).getFeatures().length; i++)
			atts.add(new Attribute(instances.get(0).getFeatures()[i].getName()));
		
		ArrayList<String> outcomeLabels = new ArrayList<>();
		outcomeLabels.add("None");
		outcomeLabels.add("Declarative");
		atts.add(new Attribute("outcome", outcomeLabels));
		
		Instances insts = new Instances("Declarative Speech Acts", atts, instances.size());
		
		for (Instance inst : instances) {
			double[] featureValues = new double[inst.getFeatures().length + 1];
			for (int i = 0; i < inst.getFeatures().length; i++)
				featureValues[i] = ((NumericFeature)inst.getFeatures()[i]).getRawFeatureContent();
			
			featureValues[featureValues.length -1] = inst.getLabel().equals("None") ? 0 : 1; 
			
			insts.add(new DenseInstance(1.0, featureValues));
		}
		
		return insts;
	}
	
	public void buildClassificationModel() throws Exception {
		this.classifier = new LMT();
		
		String[] classifierOptions = {
			"-I", "-1", "-M", "15", "-W", "0.0"	
		};
		
		this.classifier.setOptions(classifierOptions);
		
		Instances data = this.toWekaDataSet(true);
		data.setClassIndex(data.numAttributes() -1);
		this.classifier.buildClassifier(data);
		this.eval = new Evaluation(data);
		eval.crossValidateModel(classifier, data, 10, new Random(1));
	}
	
	public void classify() throws Exception {
		Instances data = this.toWekaDataSet(false);
		data.setClassIndex(data.numAttributes() -1);
		
		for (int i = 0; i < data.size(); i++) {
			double classLabel = this.classifier.classifyInstance(data.get(i));
			
			this.classificationInstances.get(i).setPredictedLabel(classLabel == 0 ? "None" : "Declarative");
		}
	}

	public Evaluation getEval() {
		return eval;
	}

	public void setEval(Evaluation eval) {
		this.eval = eval;
	}
	
	
	
}
