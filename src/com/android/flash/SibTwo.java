package com.android.flash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;



import android.os.Parcel;
import android.os.Parcelable;

public class SibTwo extends Item implements Parcelable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<SibOne> myVerbs;

	/** constructor to build a SibTwo item */
	public SibTwo(String name) {
		this.name = name;
		this.date = new Date();

	}
	
	public void addVerb(String item1, String item2) {
		SibOne tmpSibOne = new SibOne(item1);
		SibTwo tmpSibTwo = new SibTwo(item2);
		tmpSibOne.updatePair(tmpSibTwo);
		
		if (myVerbs == null) {
			myVerbs = new ArrayList<SibOne>();
		}
		myVerbs.add(tmpSibOne);
	}
	
	public ArrayList<SibOne> getVerbs() {
		return myVerbs;
	}

	public int describeContents() {
		return 0;
	}
	
	
	/** PARCELABLE STUFF BELOW, PROBABLY NEEDS UPDATING */

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);

	}
	
	/** constructor to build a SibTwo item from parcel */
	public SibTwo(Parcel in) {
		this.name = in.readString();

	}

	public static final Parcelable.Creator<SibTwo> CREATOR = new Parcelable.Creator<SibTwo>() {
		public SibTwo createFromParcel(Parcel in) {
			return new SibTwo(in);
		}

		public SibTwo[] newArray(int size) {
			return new SibTwo[size];
		}
	};

	public void setName(String name) {
		this.name = name;
	}
}