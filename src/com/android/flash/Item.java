package com.android.flash;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


/** abstract class that the siblings use */
public class Item implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private UUID uniqueId;
    private int correct;
    private int incorrect;
    private boolean correctToday;

    public UUID getUniqueId() {
        if (this.uniqueId == null) {
            this.uniqueId = UUID.randomUUID();
        }
        return this.uniqueId;
    }

    public int getPlayedCount() {
        return correct + incorrect;
    }

    public void incrPlayCount(final boolean correct) {
        if (correct) {
            this.correct++;
        }
        else {
            this.incorrect++;
        }
    }

    public int getCorrectCount() {
        return correct;
    }

    public int getIncorrectCount() {
        return incorrect;
    }

    public void setCorrectToday(final boolean correct) {
        this.correctToday = correct;
    }

    public boolean getCorrectToday() {
        return this.correctToday;
    }

    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass() && (this == obj || getUniqueId() == ((Item) obj).getUniqueId());

    }
}