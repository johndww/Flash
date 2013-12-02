package com.android.flash.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.android.flash.R;
import com.android.flash.SibOne;
import com.android.flash.util.Fconstant;
import com.android.flash.util.PersistanceUtils;
import com.android.flash.util.Serializer;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Android activity page that displays a shuffled word game
 * 
 * @author johnwright
 *
 */
public class PlayRandom extends Activity {
	private static Game myGame;
	private SibOne word;
	private TextView input1;
	private TextView input2;
	private TextView remaining;
	private TextView dateView;
	private int status = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.playgame);
		
		//textfields that are updated on each button press
		input1 = (TextView) findViewById(R.id.input1);
		input2 = (TextView) findViewById(R.id.input2);
		remaining = (TextView) findViewById(R.id.remaining);
		dateView = (TextView) findViewById(R.id.date);
		
		if (myGame == null) {
			//no game made yet, create one
			restartGame(GameType.NORMAL);
		} else {
			//just initialize the page with the current game
			restartGame(GameType.INIT);
		}
		
		super.onCreate(savedInstanceState);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next:
			word = myGame.getNext();
			updateRound();
			break;
			
		case R.id.last:
			word = myGame.getLast();
			updateRound();
			break;
		case R.id.restart:
			//get the type of game to create!
			RadioGroup gameSelected = (RadioGroup) findViewById(R.id.gameTypeRadio);
			
			switch (gameSelected.getCheckedRadioButtonId()) {
			case R.id.game_normal:
				//restart random shuffle game
				restartGame(GameType.NORMAL);
				break;
			case R.id.game_new50:
				restartGame(GameType.NEW50);
				break;
			case R.id.game_verbs:
				restartGame(GameType.ALLVERB);
				break;
			}
			break;
		case R.id.showhide:
			if (status == 0) {
				// invisible, show it now
				input2.setVisibility(View.VISIBLE);
				status = 1;
			} else {
				input2.setVisibility(View.INVISIBLE);
				status = 0;
			}
			
			
			break;
	
		}
		remaining.setText("Words Remaining: " + myGame.wordsLeft());
	}
	
	/**
	 * Resets visibility and sets the game text
	 */
	private void updateRound() {
		//deal with the language choice
		if (myGame.getLang() == Fconstant.SIBONE) {
			input1.setText(word.getName());
			input2.setText(word.getPair().getName());
		} else {
			input1.setText(word.getPair().getName());
			input2.setText(word.getName());
		}
		
		input2.setVisibility(View.GONE);
		status = 0;
		
		dateView.setText(DateFormat.getDateInstance().format(word.getDate()));
	}

	/**
	 * Restarts the game.  Multiple modes based on type for the style of game to be created.
	 *
     */
	private void restartGame(GameType type) {
		//set language
		int lang = ((RadioGroup) findViewById(R.id.langRadio)).getCheckedRadioButtonId();
		lang = (lang == R.id.lang_eng) ? Fconstant.SIBONE : Fconstant.SIBTWO;
		
		boolean verbs = ((CheckBox)findViewById(R.id.verbs)).isChecked();
		
		if (type != GameType.INIT) {
            ArrayList<SibOne> myItems = null;
            myItems = PersistanceUtils.getSibOnesList();
            myGame = new Game(myItems, type, lang, verbs);
        }
		
		//initialize the textfields
		word = myGame.getNext();
		updateRound();
		remaining.setText("Words Remaining: " + myGame.wordsLeft());

	}

}
