package com.xwk.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CrossfitPerformance {

	private long id;
	private Date date;
	private long time;
	private SimpleDateFormat df;
	private String comment;
	
	public CrossfitPerformance() {
		df = new SimpleDateFormat(Ids.time, Locale.getDefault()); 
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public String getDateString() {
		return df.format(date);
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setDateString(String string) {
		try {
			date = df.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public long getTime() {
		return time;
	}
	public String getTimeString() {
		int secs = (int) (time / 1000);
		int mins = secs / 60;
		secs = secs % 60;
		return String.format("%02d", mins) + ":" + String.format("%02d", secs) + "";
	}
	public void setTime(long time) {
		this.time = time;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
