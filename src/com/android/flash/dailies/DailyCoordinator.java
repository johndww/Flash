package com.android.flash.dailies;

import com.android.flash.SibOne;
import com.android.flash.util.PersistanceUtils;

import java.util.*;

/**
 * User: johnwright
 * Date: 1/16/13
 * Time: 11:12 PM
 */
public class DailyCoordinator {
    final private static DailyCoordinator SINGLETON = new DailyCoordinator();
    public static final int DAILY_WORD_COUNT = 10;

    private ArrayList<SibOne> dailyItems;
    private int currentDailyDay;

    private DailyCoordinator() {
        //singleton
    }

    public static DailyCoordinator get() {
        return SINGLETON;
    }

    public boolean isFinished() {
        return getDailyWords(false).size() == 0;
    }

    public ArrayList<SibOne> getDailyWords(final boolean completed) {
        if (completed) {
            return getCompletedWords();
        }
        if (this.dailyItems == null || currentDailyDay != getToday()) {
            initDailies();
        }
        return this.dailyItems;
    }

    public void completeWord(final SibOne word, final boolean correct) {
        if (dailyItems.remove(word)) {

            word.incrPlayCount(correct);
            word.setCompleted(true);
            word.setCorrectToday(correct);

            PersistanceUtils.updateSibs();
        } else {
            throw new RuntimeException("Could not complete the word - didn't exist in dailies");
        }
    }

    private ArrayList<SibOne> getCompletedWords() {
        Set<SibOne> allItems = PersistanceUtils.getSibOnesSet();
        final ArrayList<SibOne> completedItems = new ArrayList<SibOne>();

        for (SibOne tmpSibOne : allItems) {
            if (tmpSibOne.forToday() && tmpSibOne.isCompleted()) {
                // word is for currentDailyDay and is no longer a daily (its completeWord)
                completedItems.add(tmpSibOne);
            }

            if ((tmpSibOne.getPair().getVerbs() != null)) {
                for (SibOne tmpSibOne2 : tmpSibOne.getPair().getVerbs()) {
                    if (tmpSibOne2.forToday() && tmpSibOne2.isCompleted()) {
                        // verb word is for currentDailyDay and is no longer a daily (its completeWord)
                        completedItems.add(tmpSibOne2);
                    }

                }
            }
        }
        return completedItems;
    }

    /**
     * init if its a new day or if we haven't set dailyItems yet for this load
     */
    private void initDailies() {
        Set<SibOne> myItems = PersistanceUtils.getSibOnesSet();
        this.dailyItems = new ArrayList<SibOne>();
        boolean playedToday = false;

        for (SibOne tmpSibOne : myItems) {
            if (tmpSibOne.isDaily()) {
                if (tmpSibOne.forToday()) {
                    playedToday = true;
                    if (!tmpSibOne.isCompleted()) {
                        this.dailyItems.add(tmpSibOne);
                    }
                } else {
                    tmpSibOne.setDaily(false);
                }
            }
        }

        if (this.dailyItems.isEmpty() && !playedToday) {
            //we haven't completeWord dailies today, need to get some
            //TODO need to add verbs to allitems
            //sort so the least played have lower indicies

            // itemPool will be what we draw from for currentDailyDay
            ArrayList<SibOne> itemPool;

            if (getToday() % 3 == 0) {
                // every 3 days, should pump up the least played
                itemPool = getSortedListByPlayCount(myItems);
            } else {
                // random shuffle
                itemPool = getShuffledList(myItems);
            }

            //for now just take the first 10
            int i = 0;
            for (final SibOne tmpSib : itemPool) {
                tmpSib.setDaily(true);
                this.dailyItems.add(tmpSib);
                if (i == DAILY_WORD_COUNT) {
                    break;
                }
                ++i;
            }
            this.currentDailyDay = getToday();
        }

        // state of myItems may have changed, need to persist
        PersistanceUtils.updateSibs();
    }

    private ArrayList<SibOne> getShuffledList(Collection<SibOne> myItems) {
        final ArrayList<SibOne> shuffledItems = new ArrayList<SibOne>();
        shuffledItems.addAll(myItems);
        Collections.shuffle(shuffledItems);
        return shuffledItems;
    }

    private ArrayList<SibOne> getSortedListByPlayCount(Collection<SibOne> myItems) {
        final ArrayList<SibOne> sortedItems = new ArrayList<SibOne>();
        sortedItems.addAll(myItems);

        Collections.sort(sortedItems, new Comparator<SibOne>() {
            @Override
            public int compare(SibOne o1, SibOne o2) {
                final int played1 = o1.getPlayedCount();
                final int played2 = o2.getPlayedCount();
                if (played1 > played2) {
                    return 1;
                }
                if (played1 < played2) {
                    return -1;
                }
                return 0;
            }
        });
        return sortedItems;
    }

    private int getToday() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }
}
