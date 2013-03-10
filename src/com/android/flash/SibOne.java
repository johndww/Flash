package com.android.flash;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class SibOne extends Item implements Serializable, Comparable<SibOne> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String name;
	private SibTwo sibTwo;
	private Date date;
    private boolean daily;
    private int dailyDate;
    private boolean completed;
    public final static SibOne EMPTY = new SibOne(true);

	/** constructor to build a SibOne item */
	public SibOne(String name) {
		this.name = name;
		Calendar cal = Calendar.getInstance();
		this.date = cal.getTime();
	}

    private SibOne(final boolean empty) {
        this.name = "Empty";
        this.date = Calendar.getInstance().getTime();
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

    public boolean isDailyForToday() {
        return daily && forToday();
    }

    public boolean isDaily() {
        return this.daily;
    }

    public void setDaily(final boolean enable) {
        this.daily = enable;

        if (enable) {
            this.dailyDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        setCompleted(false);
    }

    public void setCompleted(final boolean status) {
        this.completed = status;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public boolean forToday() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == this.dailyDate;
    }
	
	public int compareTo(SibOne o) {
		return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
	}
}