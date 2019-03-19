package de.sebastiangombert.ml;

public class NominalToNumericFeature extends Feature<String>{
	
	private String feature;

	@Override
	public String getEncodedFeatueContent() {
		return this.feature;
	}

	@Override
	public void setFeatureContent(String feature) {
		this.feature = feature;
	}

	@Override
	public boolean isSet() {
		return this.feature != null;
	}


}