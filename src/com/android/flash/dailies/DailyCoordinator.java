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
            //initDailiesOriginalOld();
        }
        return this.dailyItems;
    }

    public void completeWord(final SibOne word, final boolean correct) {
        if (dailyItems.remove(word)) {

            word.incrPlayCount(correct);
            word.setCompletedDaily(true);
            word.setCorrectToday(correct);
            word.incrPriority();
            word.updateStreak(correct);

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

    private void initDailies() {
        final Set<SibOne> myItems = PersistanceUtils.getSibOnesSet();
        this.dailyItems = new ArrayList<SibOne>(DAILY_WORD_COUNT);
        boolean playedToday = false;

        // add current dailies that haven't broken the streak. reset dailies that finished their streak
        for (SibOne tmpSibOne : myItems) {
            if (tmpSibOne.isDaily()) {
                // completed this word already today, skip it
                if (tmpSibOne.isCompleted() && tmpSibOne.forToday()) {
                    playedToday = true;
                    continue;
                }

                // completed this word yesterday, add it for a new day (verify streak)
                if (tmpSibOne.isCompleted() && !tmpSibOne.forToday()) {
                    verifyStreakAndAddDaily(tmpSibOne);
                    continue;
                }

                // daily not completed yet just add it for today
                tmpSibOne.setForToday();
                this.dailyItems.add(tmpSibOne);

            }
        }

        final int wordsRemaining = DAILY_WORD_COUNT - this.dailyItems.size();

        // only add more words if we havent played today and need more to get our daily word count
        if (!playedToday && wordsRemaining > 0) {

            ArrayList<SibOne> itemPool = getLowestPriorityWords(myItems, wordsRemaining);

            if (getToday() % 3 == 0) {
                // every 3 days, should pump up the least played
                itemPool = getSortedListByPlayCount(itemPool);
            } else {
                // random shuffle
                itemPool = getShuffledList(itemPool);
            }

            int i = 0;
            while (this.dailyItems.size() < DAILY_WORD_COUNT && itemPool.size() > 0) {
                final SibOne sibOne = itemPool.remove(i);
                sibOne.setDaily(true);
                sibOne.resetDailyStreak();
                this.dailyItems.add(sibOne);
                i++;
            }
        }

        this.currentDailyDay = getToday();

        PersistanceUtils.updateSibs();
    }

    private void verifyStreakAndAddDaily(SibOne tmpSibOne) {
        if (tmpSibOne.getDailyStreak() < 3) {
            tmpSibOne.setForToday();
            this.dailyItems.add(tmpSibOne);
        } else {
            tmpSibOne.setDaily(false);
            tmpSibOne.resetDailyStreak();
        }
        tmpSibOne.setCompletedDaily(false);
    }

    private ArrayList<SibOne> getLowestPriorityWords(final Set<SibOne> myItems, final int wordsRemaining) {
        int lowestPriority = 0;
        int maxPriority = 0;
        final ArrayList<SibOne> result = new ArrayList<SibOne>(myItems.size());

        for (SibOne tmpSibOne : myItems) {
            final int dailyPriority = tmpSibOne.getDailyPriority();
            if (dailyPriority < lowestPriority) {
                lowestPriority = dailyPriority;
            } else if (dailyPriority > maxPriority) {
                maxPriority = dailyPriority;
            }
        }

        int currentPriority = lowestPriority;
        while (result.size() < wordsRemaining && currentPriority <= maxPriority) {
            for (SibOne tmpSibOne : myItems) {
                if (tmpSibOne.getDailyPriority() == currentPriority) {
                    result.add(tmpSibOne);
                }
            }
            currentPriority++;
        }

        return result;
    }

    /**
     * init if its a new day or if we haven't set dailyItems yet for this load
     */
    @Deprecated
    private void initDailiesOriginalOld() {
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
