package com.android.flash;

import java.io.Serializable;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class SibOne extends Item implements Parcelable, Serializable, Comparable<SibOne> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SibTwo sibTwo;
	private Date date;

	/** constructor to build a SibOne item */
	public SibOne(String name) {
		this.name = name;
		this.date = new Date();
	}

	public void updatePair(SibTwo sibTwo) {
		this.sibTwo = sibTwo;
	}
	
	public SibTwo getPair() {
		return this.sibTwo;
	}

	public int describeContents() {
		return 0;
	}
	
	public Date getDate() {
		if (this.date == null) {
			//hasn't been initalized yet, return and set todays date
			this.date = new Date();
		}
		return this.date;
	}
	
	/** PARCELABLE STUFF BELOW HERE, PROBABLY NEEDS UPDATING */

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeParcelable(sibTwo, flags);
	}

	public static final Parcelable.Creator<SibOne> CREATOR = new Parcelable.Creator<SibOne>() {
		public SibOne createFromParcel(Parcel in) {
			return new SibOne(in);
		}

		public SibOne[] newArray(int size) {
			return new SibOne[size];
		}
	};
	
	/** constructor to build our SibOne with parcel data */
	public SibOne(Parcel in) {
		this.name = in.readString();
		this.sibTwo = (SibTwo) in.readParcelable(SibTwo.class.getClassLoader());
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int compareTo(SibOne o) {
		return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
	}
}