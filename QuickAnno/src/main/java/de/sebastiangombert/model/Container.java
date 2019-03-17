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
