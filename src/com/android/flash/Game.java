package com.android.flash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

/**
 * Create a new game that includes every item in the system and shuffles them.
 * 
 * @author johnwright
 */
public class Game {
	private ArrayList<SibOne> words;
	private SibOne sibOne;
	private ListIterator<SibOne> myItr;
	private int wordsLeft = 0;
	private int dir = 1;

	/**
	 * Generates a new Game instances
	 * 
	 * @param myItems
	 */
	public Game(ArrayList<SibOne> myItems, int type) {
		if (words == null) {
			// first time a game has been started,
			words = new ArrayList<SibOne>();

		}
		startGame(myItems, type);
	}
	
	
	/**
	 * Populates & initializes a new game with deserialized data and shuffles
	 * 
	 * @param myItems
	 */
	public void startGame(ArrayList<SibOne> myItems, int type) {
		if (myItems != null) {
			switch (type) {
			
			//build a normal game
			case 1:
				for (SibOne tmpSibOne : myItems) {
					words.add(tmpSibOne);
					if (tmpSibOne.getPair().getVerbs() != null) {
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
					public int compare(SibOne o1, SibOne o2) {
						return o1.getDate().compareTo(o2.getDate());
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
					if (tmpSibOne.getPair().getVerbs() != null) {
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
			}
			myItems = null;
		}
		
		Collections.shuffle(words);

		// initialize game
		myItr = words.listIterator();
		wordsLeft = words.size();

	}

	/**
	 * Gets the next item for the game
	 * 
	 * @return SibOne
	 */
	public SibOne getNext() {
		if (myItr.hasNext()) {
			sibOne = myItr.next();
			if (dir == 0) {
				//if we were going backwards, need to go forward an extra word
				sibOne = myItr.next();
				
				//set direction to backwards
				dir = 1;
			}
			wordsLeft--;
		}
		return sibOne;
	}
	
	/**
	 * Gets the previous item for the game
	 * 
	 * @return SibOne
	 */
	public SibOne getLast() {
		if (myItr.previousIndex() == 0) {
			// do nothing, corner case when the last button hit on first item
		} else {
			if (myItr.hasPrevious()) {
				sibOne = myItr.previous();
				if ((dir == 1) && (myItr.hasPrevious())) {
					// if we were going forward, need to go backwards an extra
					// word
					sibOne = myItr.previous();

					// set direction to backwards
					dir = 0;
				}
				wordsLeft++;
			}
		}
		return sibOne;
	}
	
	/**
	 * Words remaining in the game
	 * 
	 * @return int
	 */
	public int wordsLeft() {
		return wordsLeft;
	}
}
