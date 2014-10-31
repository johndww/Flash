package com.jwstudios.flash;

import java.io.Serializable;
import java.util.ArrayList;

public class SibTwo
        extends Item
        implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<SibOne> myVerbs;
    public static final SibTwo EMPTY = new SibTwo("empty2");

    /**
     * constructor to build a SibTwo item
     */
    public SibTwo(String name) {
        this.name = name;

    }

    public void addVerb(String item1, String item2) {
        SibOne tmpSibOne = new SibOne(item1, new SibTwo(item2), 0, 0);

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SibTwo sibTwo = (SibTwo) o;

        if (!name.equalsIgnoreCase(sibTwo.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return this.name + " / verbCount: " + (this.myVerbs == null ? "0" : this.myVerbs.size());
    }
}