package de.sebastiangombert.ml;

public abstract class Feature <A extends Object> {
	
	protected String name;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public abstract String getEncodedFeatueContent();
	
	public abstract boolean isSet();
	
	public abstract void setFeatureContent(A feature);
}
