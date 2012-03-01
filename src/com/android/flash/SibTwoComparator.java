package com.android.flash;

import java.util.Comparator;

public class SibTwoComparator implements Comparator<SibOne> {

	public int compare(SibOne o1, SibOne o2) {
		return o1.getPair().getName().toLowerCase().compareTo(o2.getPair().getName().toLowerCase());
	}

}
