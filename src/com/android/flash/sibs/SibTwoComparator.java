package com.android.flash.sibs;

import java.util.Comparator;

import com.android.flash.SibOne;


public class SibTwoComparator implements Comparator<SibOne> {

	public int compare(SibOne o1, SibOne o2) {
		return o1.getPair().getName().toLowerCase().compareTo(o2.getPair().getName().toLowerCase());
	}

}