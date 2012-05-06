package com.android.flash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;



import android.os.Parcel;
import android.os.Parcelable;

public class SibTwo extends Item implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String name;
	private ArrayList<SibOne> myVerbs;

    /** constructor to build a SibTwo item */
	public SibTwo(String name) {
		this.name = name;

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

	public void setName(String name) {
		this.name = name;
	}

    public String getName() {
        return name;
    }
}