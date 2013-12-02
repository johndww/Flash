package com.android.flash;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class SibOne extends Item implements Serializable, Comparable<SibOne> {
	private static final long serialVersionUID = 1L;
    private String name;
	private final SibTwo sibTwo;
	private final Date date;
    private boolean daily;
    private int dailyDate;
    private boolean completed;
    public final static SibOne EMPTY = new SibOne(true);

	public SibOne(String name, final SibTwo sibTwo) {
		this.name = name;
        this.sibTwo = sibTwo;
		Calendar cal = Calendar.getInstance();
		this.date = cal.getTime();
	}

    private SibOne(final boolean empty) {
        this.name = "Empty";
        this.sibTwo = SibTwo.EMPTY;
        this.date = Calendar.getInstance().getTime();
    }

	public SibTwo getPair() {
		return this.sibTwo;
	}
	
	public Date getDate() {
		return date;
	}

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SibOne sibOne = (SibOne) o;

        if (!date.equals(sibOne.date)) return false;
        if (!name.equals(sibOne.name)) return false;
        if (!sibTwo.equals(sibOne.sibTwo)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + sibTwo.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }

    public int compareTo(SibOne o) {
		return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
	}
}