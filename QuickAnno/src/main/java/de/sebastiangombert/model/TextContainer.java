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

public class TextContainer {

	private int begin;
	
	private int end;
	
	private String textDate = "";
	private String textId = "";
	private String textVz = "";
	private String textLegislature = "";
	private String textParty = "";
	private String textSpeaker = "";
	private String textYear = "";
	private String textYearMonth = "";
	
	public int getBegin() {
		return begin;
	}
	public void setBegin(int begin) {
		this.begin = begin;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getTextDate() {
		return textDate;
	}
	public void setTextDate(String textDate) {
		this.textDate = textDate;
	}
	public String getTextId() {
		return textId;
	}
	public void setTextId(String textId) {
		this.textId = textId;
	}
	public String getTextVz() {
		return textVz;
	}
	public void setTextVz(String textVz) {
		this.textVz = textVz;
	}
	public String getTextLegislature() {
		return textLegislature;
	}
	public void setTextLegislature(String textLegislature) {
		this.textLegislature = textLegislature;
	}
	public String getTextParty() {
		return textParty;
	}
	public void setTextParty(String textParty) {
		this.textParty = textParty;
	}
	public String getTextSpeaker() {
		return textSpeaker;
	}
	public void setTextSpeaker(String textSpeaker) {
		this.textSpeaker = textSpeaker;
	}
	public String getTextYear() {
		return textYear;
	}
	public void setTextYear(String textYear) {
		this.textYear = textYear;
	}
	public String getTextYearMonth() {
		return textYearMonth;
	}
	public void setTextYearMonth(String textYearMonth) {
		this.textYearMonth = textYearMonth;
	}
}
