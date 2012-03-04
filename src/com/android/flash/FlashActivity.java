package com.android.flash;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class FlashActivity extends Activity {

	/**
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
			// Toast.makeText(getApplicationContext(),
			// R.string.button_create_item, Toast.LENGTH_SHORT).show();
			request_id = REQUEST_CREATE;
			intent = new Intent(this, CreateItem.class);
			startActivityForResult(intent, request_id);
			break;
		case R.id.play_button:
			// Toast.makeText(getApplicationContext(), R.string.button_play,
			// Toast.LENGTH_SHORT).show();
			intent = new Intent(this, PlayRandom.class);
			startActivity(intent);
			break;
		case R.id.eng_play_button:
			// Toast.makeText(getApplicationContext(), R.string.button_play,
			// Toast.LENGTH_SHORT).show();
			intent = new Intent(this, PlayEng.class);
			startActivity(intent);
			break;
		case R.id.tel_play_button:
			// Toast.makeText(getApplicationContext(), R.string.button_play,
			// Toast.LENGTH_SHORT).show();
			// intent = new Intent(this, PlayTel.class);
			intent = new Intent(this, PlayTel.class);
			startActivity(intent);
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			myItems = deserialize("flash_contents");
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

					// dereference the tmp values
					tmpSibOne = null;
					tmpSibTwo = null;

					Toast.makeText(getApplicationContext(),
							R.string.item_created, Toast.LENGTH_SHORT).show();

					//serialize myItems
					try {
						FileOutputStream fos;
						fos = openFileOutput("flash_contents", Context.MODE_PRIVATE);
						Serializer.serialize(myItems, fos);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/** Serialize stuff here */
	public void serialize(ArrayList<SibOne> objToSerialize, String fileName) {
		FileOutputStream fos;
		try {
			fos = openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(objToSerialize);
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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