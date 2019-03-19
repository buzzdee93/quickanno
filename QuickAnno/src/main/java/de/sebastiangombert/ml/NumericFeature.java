package de.sebastiangombert.ml;

public class NumericFeature extends Feature<Double>{

	private Double feature;
	
	@Override
	public String getEncodedFeatueContent() {
		return this.feature.toString();
	}

	@Override
	public void setFeatureContent(Double feature) {
		this.feature = feature;
	}

	@Override
	public boolean isSet() {
		return true;
	}

}
