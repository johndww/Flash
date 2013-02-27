package com.android.flash.dailies;

import com.android.flash.SibOne;
import com.android.flash.util.Serializer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * User: johnwright
 * Date: 1/16/13
 * Time: 11:12 PM
 */
public class DailyCoordinator {
    final private static DailyCoordinator SINGLETON = new DailyCoordinator();
    public static final int DAILY_WORD_COUNT = 30;

    private ArrayList<SibOne> dailyItems;
    private ArrayList<SibOne> myItems;
    private int currentDailyDay;

    private DailyCoordinator() {
        //singleton
    }

    public static DailyCoordinator get() {
        return SINGLETON;
    }

    public boolean finished() {
        //todo removeme
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

    public void completed(final SibOne word, final boolean correct) {
        if (dailyItems.contains(word)) {
            ArrayList<SibOne> myItems = getMyItems();

            if (!myItems.contains(word)) {
                throw new RuntimeException("Trying to complete and persist a word, but can't find it");
            }

            final int idx = myItems.indexOf(word);
            final SibOne realWord = myItems.get(idx);

            realWord.incrPlayCount(correct);
            realWord.setDaily(false);
            realWord.setCorrectToday(correct);
            dailyItems.remove(word);

            //persist
            saveMyItems(myItems);
        }
    }

    private ArrayList<SibOne> getCompletedWords() {
        ArrayList<SibOne> allItems = getMyItems();
        final ArrayList<SibOne> completedItems = new ArrayList<SibOne>();

        for (SibOne tmpSibOne : allItems) {
            if (tmpSibOne.forToday() && !tmpSibOne.isDaily()) {
                // word is for currentDailyDay and is no longer a daily (its completed)
                completedItems.add(tmpSibOne);
            }

            if ((tmpSibOne.getPair().getVerbs() != null)) {
                for (SibOne tmpSibOne2 : tmpSibOne.getPair().getVerbs()) {
                    if (tmpSibOne2.forToday() && !tmpSibOne2.isDaily()) {
                        // verb word is for currentDailyDay and is no longer a daily (its completed)
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
        ArrayList<SibOne> myItems = getMyItems();
        this.dailyItems = new ArrayList<SibOne>();
        boolean playedToday = false;

        for (SibOne tmpSibOne : myItems) {
            if (tmpSibOne.forToday()) {
                playedToday = true;

                if (tmpSibOne.isDaily()) {
                    this.dailyItems.add(tmpSibOne);
                }
            } else if (tmpSibOne.isDaily()) {
                //it's a daily, but for the past
                tmpSibOne.setDaily(false);
            }

            if ((tmpSibOne.getPair().getVerbs() != null)) {
                for (SibOne tmpSibOne2 : tmpSibOne.getPair().getVerbs()) {
                    // add each verb sibone to words as well (for each eng
                    // word)
                    if (tmpSibOne2.forToday()) {
                        playedToday = true;

                        if (tmpSibOne2.isDaily()) {
                            this.dailyItems.add(tmpSibOne2);
                        }
                    } else if (tmpSibOne2.isDaily()) {
                        //it's a daily, but for the past
                        tmpSibOne2.setDaily(false);
                    }
                }
            }
        }

        if (this.dailyItems.isEmpty() && !playedToday) {
            //we haven't completed dailies today, need to get some
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
        saveMyItems(myItems);
    }

    private ArrayList<SibOne> getShuffledList(ArrayList<SibOne> myItems) {
        final ArrayList<SibOne> shuffledItems = new ArrayList<SibOne>();
        shuffledItems.addAll(myItems);
        Collections.shuffle(shuffledItems);
        return shuffledItems;
    }

    private ArrayList<SibOne> getSortedListByPlayCount(ArrayList<SibOne> myItems) {
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

    private ArrayList<SibOne> getMyItems() {
        if (this.myItems == null) {
            this.myItems = Serializer.deserialize();
        }
        return this.myItems;
    }

    private void saveMyItems(final ArrayList<SibOne> myItems) {
        this.myItems = myItems;
        Serializer.serialize(myItems);
    }

    private int getToday() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }
}
