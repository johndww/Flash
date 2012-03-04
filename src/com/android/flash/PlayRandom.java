package com.android.flash;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Android activity page that displays a word game in a shuffled itunes playlist fasion.
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
			restartGame(1);
		} else {
			//just initialize the page with the current game
			restartGame(0);
		}
		
		super.onCreate(savedInstanceState);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next:
			word = myGame.getNext();

			input1.setText(word.getName());
			input2.setVisibility(View.GONE);
			status = 0;
			input2.setText(word.getPair().getName());
			dateView.setText(word.getDate().toString());
			break;
			
		case R.id.last:
			word = myGame.getLast();
			
			input1.setText(word.getName());
			input2.setVisibility(View.GONE);
			status = 0;
			input2.setText(word.getPair().getName());
			dateView.setText(word.getDate().toString());
			break;
		case R.id.restart:
			//get the type of game to create!
			RadioGroup gameSelected = (RadioGroup) findViewById(R.id.gameTypeRadio);
			switch (gameSelected.getCheckedRadioButtonId()) {
			case R.id.game_normal:
				//restart random shuffle game
				restartGame(1);
				break;
			case R.id.game_new50:
				restartGame(2);
				break;
			case R.id.game_verbs:
				restartGame(3);
				break;
			}
			break;
		case R.id.showhide:
			if (status == 0) {
				// invisible, show it now
				input2.setVisibility(View.VISIBLE);
				status = 1;
			} else {
				input2.setVisibility(View.GONE);
				status = 0;
			}
			
			
			break;
	
		}
		remaining.setText("Words Remaining: " + myGame.wordsLeft());
	}
	
	/**
	 * Restarts the game.  Multiple modes based on type for the style of game to be created.
	 * 
	 * @param type
	 */
	private void restartGame(int type) {
		if (type != 0) {
			myGame = new Game(deserialize("flash_contents"), type);
		}
		
		//initialize the textfields
		word = myGame.getNext();
		input1.setText(word.getName());

		input2.setVisibility(View.GONE);
		status = 0;
		input2.setText(word.getPair().getName());
		remaining.setText("Words Remaining: " + myGame.wordsLeft());
		dateView.setText(word.getDate().toString());
		
	}
	
	/** Deserialize stuff here */
	@SuppressWarnings("unchecked")
	public ArrayList<SibOne> deserialize(String fileName) {
		ArrayList<SibOne> deserializedObject = null;

		try {
			FileInputStream fis = openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			deserializedObject = (ArrayList<SibOne>) is.readObject();
			is.close();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deserializedObject;
	}
}
