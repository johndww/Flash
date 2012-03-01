package com.android.flash;

import java.io.Serializable;
import java.util.Date;


/** abstract class that the siblings use */
public class Item implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String name;
	protected Date date;

	public String getName() {
		return this.name;
	}


}