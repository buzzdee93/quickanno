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
