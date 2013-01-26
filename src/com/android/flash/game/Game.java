package com.android.flash.game;

import java.util.*;

import com.android.flash.SibOne;
import com.android.flash.dailies.DailyCoordinator;

/**
 * Create a new game that includes every item in the system and shuffles them.
 *
 * @author johnwright
 */
public class Game {
    private SibOne sibOne;
    private Stack<SibOne> remainingWords;
    private Stack<SibOne> completedWords;
    private int lang;

    /**
     * Generates a new Game instances
     */
    public Game(ArrayList<SibOne> myItems, int type, int lang, boolean verbs) {
        remainingWords = new Stack<SibOne>();
        completedWords = new Stack<SibOne>();
        startGame(myItems, type, lang, verbs);
    }


    /**
     * Populates & initializes a new game with deserialized data and shuffles
     */
    public void startGame(ArrayList<SibOne> myItems, int type, int lang, boolean useVerbs) {

        ArrayList<SibOne> words = new ArrayList<SibOne>();
        this.lang = lang;

        if (myItems != null) {
            switch (type) {

                //build a normal game
                case 1:
                    for (SibOne tmpSibOne : myItems) {
                        words.add(tmpSibOne);
                        if ((tmpSibOne.getPair().getVerbs() != null) && (useVerbs)) {
                            for (SibOne tmpSibOne2 : tmpSibOne.getPair().getVerbs()) {
                                // add each verb sibone to words as well (for each eng
                                // word)
                                words.add(tmpSibOne2);
                            }
                        }
                    }
                    break;

                //build a top 50 game
                case 2:
                    //sort by date, most recent gets the lower indicies
                    Collections.sort(myItems, new Comparator<SibOne>() {
                        @Override
                        public int compare(SibOne o1, SibOne o2) {
                            return -o1.getDate().compareTo(o2.getDate());
                        }
                    });

                    //get the top 50
                    int count = 0;
                    for (SibOne tmpSibOne : myItems) {
                        if (count > 50) {
                            break;
                        }
                        count++;
                        words.add(tmpSibOne);
                        if ((tmpSibOne.getPair().getVerbs() != null) && (useVerbs)) {
                            for (SibOne tmpSibOne2 : tmpSibOne.getPair().getVerbs()) {
                                // add each verb sibone to words as well (for each eng
                                // word)
                                if (count > 50) {
                                    break;
                                }
                                count++;
                                words.add(tmpSibOne2);
                            }
                        }

                    }
                    break;

                //build a all verb game
                case 3:
                    for (SibOne tmpSibOne : myItems) {
                        if (tmpSibOne.getPair().getVerbs() != null) {
                            for (SibOne tmpSibOne2 : tmpSibOne.getPair().getVerbs()) {
                                // add each verb sibone to words as well (for each eng
                                // word)
                                words.add(tmpSibOne2);
                            }
                        }
                    }
                    break;

                //build a dailies game
                case 4:
                    words = DailyCoordinator.get().getDailyWords(false);
                    break;
            }
        }

        Collections.shuffle(words);

        // initialize game
        this.remainingWords.addAll(words);
    }

    /**
     * Gets the next item for the game
     *
     * @return SibOne
     */
    public SibOne getNext() {
        if (!remainingWords.empty()) {
            //corner case: haven't taken our first item yet, don't try and move sibOne(null) to completedWords
            if (sibOne != null) {
                completedWords.push(sibOne);
            }
            sibOne = remainingWords.pop();
        }
        return sibOne != null ? sibOne : SibOne.EMPTY;
    }

    /**
     * Gets the previous item for the game
     *
     * @return SibOne
     */
    public SibOne getLast() {
        if (!completedWords.empty()) {
            remainingWords.push(sibOne);
            sibOne = completedWords.pop();
        }
        return sibOne;
    }

    /**
     * Words remaining in the game
     *
     * @return int
     */
    public int wordsLeft() {
        return remainingWords.size();
    }

    public int getLang() {
        return lang;
    }
}
