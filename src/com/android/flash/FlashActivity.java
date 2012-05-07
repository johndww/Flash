package com.android.flash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.android.flash.game.PlayRandom;
import com.android.flash.listwords.ListWords;
import com.android.flash.util.Fconstant;
import com.android.flash.util.Serializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class FlashActivity extends Activity {

	/**
	 * Home page for the Flash App
	 * 
	 */
	private static final int REQUEST_CREATE = 10;

	SibOne tmpSibOne;
	SibTwo tmpSibTwo;

	ArrayList<SibOne> myItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

	}

	/** persist data if the android wants to kill us =( */
	protected void onPause() {
		super.onPause();

	}

	/** resurrect our content (using internal storage) */
	protected void onResume() {
		super.onResume();
	}

	/** go to the separate pages if a button is clicked LIKE A BOSS */
	public void onClick(View v) {

		Intent intent = null;
		int request_id = 0;

		switch (v.getId()) {
		case R.id.create_item_button:
			request_id = REQUEST_CREATE;
			intent = new Intent(this, CreateItem.class);
			startActivityForResult(intent, request_id);
			break;
		case R.id.play_button:
			intent = new Intent(this, PlayRandom.class);
			startActivity(intent);
			break;
		case R.id.list_button:
			intent = new Intent(this, ListWords.class);
			RadioGroup lang = (RadioGroup) findViewById(R.id.lang);
			RadioGroup sort = (RadioGroup) findViewById(R.id.sort);
			switch (sort.getCheckedRadioButtonId()) {
			case R.id.alphabetical:
				if (lang.getCheckedRadioButtonId() == R.id.english) {
					intent.putExtra("listtype", Fconstant.LISTTYPE_ENGALPH);
				} else {
					intent.putExtra("listtype", Fconstant.LISTTYPE_TELALPH);
				}
				break;
			case R.id.date:
				if (lang.getCheckedRadioButtonId() == R.id.english) {
					intent.putExtra("listtype", Fconstant.LISTTYPE_ENGDATE);
				} else {
					intent.putExtra("listtype", Fconstant.LISTTYPE_TELDATE);
				}
			}
			startActivity(intent);
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
            try {
                FileInputStream fis = openFileInput("flash_contents");
                myItems = Serializer.deserialize();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
			if (myItems == null) {
				myItems = new ArrayList<SibOne>();
			}
			if (requestCode == REQUEST_CREATE) {
				if (data.hasExtra("item1") && data.hasExtra("item2")) {
					// process the two strings returned

					tmpSibOne = new SibOne(data.getExtras().getString("item1"));
					tmpSibTwo = new SibTwo(data.getExtras().getString("item2"));
					tmpSibOne.updatePair(tmpSibTwo);

					myItems.add(tmpSibOne);

					Toast.makeText(getApplicationContext(),
							R.string.item_created, Toast.LENGTH_SHORT).show();

					//serialize myItems
					try {
						Serializer.serialize(myItems);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}