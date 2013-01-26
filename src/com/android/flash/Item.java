package com.android.flash;

import java.io.Serializable;
import java.util.Date;


/** abstract class that the siblings use */
public class Item implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private int correct;
    private int incorrect;
    private boolean correctToday;

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


}