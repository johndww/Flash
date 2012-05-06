package com.android.flash;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;



import android.os.Parcel;
import android.os.Parcelable;

public class SibOne extends Item implements Serializable, Comparable<SibOne> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String name;
	private SibTwo sibTwo;
	private Date date;

	/** constructor to build a SibOne item */
	public SibOne(String name) {
		this.name = name;
		Calendar cal = Calendar.getInstance();
		this.date = cal.getTime();
	}

	public void updatePair(SibTwo sibTwo) {
		this.sibTwo = sibTwo;
	}
	
	public SibTwo getPair() {
		return this.sibTwo;
	}
	
	public Date getDate() {
		return date;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getName() {
        return name;
    }
	
	public int compareTo(SibOne o) {
		return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
	}
}