package com.android.flash.dailies;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.android.flash.R;
import com.android.flash.SibOne;
import com.android.flash.game.Game;
import com.android.flash.game.GameType;
import com.android.flash.util.Fconstant;
import com.android.flash.util.Serializer;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * User: johnwright
 * Date: 1/15/13
 * Time: 10:59 PM
 */
public class Dailies extends Activity {
    private static Game myGame;
    private static final DailyCoordinator COORDINATOR = DailyCoordinator.get();
    private SibOne word;
    private TextView input1;
    private TextView input2;
    private TextView remaining;
    private TextView dateView;
    private int status = 0;

    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dailies);

        //textfields that are updated on each button press
        input1 = (TextView) findViewById(R.id.input1);
        input2 = (TextView) findViewById(R.id.input2);
        remaining = (TextView) findViewById(R.id.remaining);
        dateView = (TextView) findViewById(R.id.date);

        if (COORDINATOR.isFinished()) {
            super.finish();
        } else {

            if (myGame == null) {
                //no game made yet, create one
                restartGame(GameType.DAILIES);
            } else {
                //just initialize the page with the current game
                restartGame(GameType.INIT);
            }
        }

        super.onCreate(savedInstanceState);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.correct:
                COORDINATOR.completeWord(word, true);
                if (myGame.wordsLeft() == 0) {
                    //switch activities to summary view
                    finish();
                }
                word = myGame.getNext();
                updateRound();
                break;

            case R.id.incorrect:
                COORDINATOR.completeWord(word, false);
                if (myGame.wordsLeft() == 0) {
                    //switch activities to summary view
                    finish();
                }
                word = myGame.getNext();
                updateRound();
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
     * @param type
     */
    private void restartGame(GameType type) {
        //set language
        int lang = Fconstant.SIBONE;

        if (type != GameType.INIT) {
            ArrayList<SibOne> myItems = null;
            myItems = Serializer.deserialize();
            myGame = new Game(myItems, type, lang, false);
        }

        //initialize the textfields
        word = myGame.getCurrent();

        updateRound();
        remaining.setText("Words Remaining: " + myGame.wordsLeft());
    }
}