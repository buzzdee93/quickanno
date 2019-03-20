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

package de.sebastiangombert.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.uima.jcas.tcas.Annotation;

public class Container<T extends Annotation> {
	
	private int from;
	private int to;
	
	private String content;

	public Container(T containedItem, String getContentMethodName) {
		this.from = containedItem.getBegin();
		this.to = containedItem.getEnd();
		try {
			this.content = (String)containedItem.getClass().getMethod(getContentMethodName).invoke(containedItem);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public String getContent() {
		return content;
	}

}
