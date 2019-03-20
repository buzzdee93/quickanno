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

	public double getRawFeatureContent() {
		return this.feature;
	}
}
