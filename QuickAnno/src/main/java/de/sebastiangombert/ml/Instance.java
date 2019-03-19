package de.sebastiangombert.ml;

public class Instance {

	private Feature<? extends Object>[] features;
	
	private String label;
	
	private String predictedLabel;

	public Feature<? extends Object>[] getFeatures() {
		return features;
	}

	public void setFeatures(Feature<? extends Object>[] features) {
		this.features = features;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPredictedLabel() {
		return predictedLabel;
	}

	public void setPredictedLabel(String predictedLabel) {
		this.predictedLabel = predictedLabel;
	}
}
