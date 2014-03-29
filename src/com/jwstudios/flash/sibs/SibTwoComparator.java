package com.jwstudios.flash.sibs;

import java.util.Comparator;

import com.jwstudios.flash.SibOne;


public class SibTwoComparator implements Comparator<SibOne> {
	@Override
	public int compare(SibOne o1, SibOne o2) {
		return o1.getPair().getName().toLowerCase().compareTo(o2.getPair().getName().toLowerCase());
	}

}
