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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewWord extends Activity {
	SibOne sibone;
	int position;
	ArrayList<SibOne> myItems;
	ArrayList<SibOne> myVerbs;
	private static final int REQUEST_ADDVERB = 25;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.viewsibone);

		position = getIntent().getExtras().getInt("position");
		myItems = deserialize("flash_contents");
		sibone = myItems.get(position);

		((TextView) findViewById(R.id.EngWord)).setText(sibone.getName());
		((TextView) findViewById(R.id.TelWord)).setText(sibone.getPair()
				.getName());

		myVerbs = sibone.getPair().getVerbs();

		if (myVerbs != null) {

			LinearLayout layout = (LinearLayout) findViewById(R.id.verbs);

			LayoutInflater vi = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			//for (SibOne siboneverb : myVerbs) {
			for (int i=0; i < myVerbs.size(); i++) {
				// inflate the verb layout

				View view = vi.inflate(R.layout.verb_layout, null);
				//view.setTag(i);
				
				/**view.setOnLongClickListener(new View.OnLongClickListener() {
					  public boolean onLongClick(View v) {
					    //System.out.println(v.getTag());
					    return true;
					  }
					});*/
				
				this.registerForContextMenu(view);
				

				TextView tmpEngVerb = (TextView) view
						.findViewById(R.id.engverb);
				TextView tmpTelVerb = (TextView) view
						.findViewById(R.id.telverb);
				// TextView tmpEngVerb = new TextView(getApplicationContext());
				// TextView tmpTelVerb = new TextView(getApplicationContext());

				tmpEngVerb.setText(myVerbs.get(i).getName());
				tmpTelVerb.setText(myVerbs.get(i).getPair().getName());

				// TableRow row = new TableRow(this);
				// row.addView(view);

				layout.addView(view);
				// layout.addView(tmpTelVerb);
			}
		}
	}
	
	public void onResume() {
		//LinearLayout layout = (LinearLayout) findViewById(R.id.verbs);
		//layout.invalidate();
		super.onResume();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.layout.verbmenu, menu);
		menu.add(0, 0, 0, "View Verbs");
	}

	public void onClick(View v) {
		// do some validating to make sure they actually filled the fields
		dofinish();
	}

	public void dofinish() {
		// if the user entered some values, then send them back to the main
		// activity
		if (((EditText) findViewById(R.id.verb_eng)).getText().toString()
				.trim().equals("")
				|| ((EditText) findViewById(R.id.verb_tel)).getText()
						.toString().trim().equals("")) {
			setResult(RESULT_CANCELED);
		} else {
			Intent data = new Intent();
			// Return some hard-coded values
			data.putExtra("item1", ((EditText) findViewById(R.id.verb_eng))
					.getText().toString());
			data.putExtra("item2", ((EditText) findViewById(R.id.verb_tel))
					.getText().toString());
			data.putExtra("position", position);
			setResult(REQUEST_ADDVERB, data);
		}
		super.finish();
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//Intent to ViewVerbs.class
		Intent intent = new Intent(this, ViewVerbs.class);
		intent.putExtra("position", position);
		startActivity(intent);
		return super.onContextItemSelected(item);

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
