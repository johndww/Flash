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
import java.util.Collections;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class PlayTel extends ListActivity {

	ArrayList<SibOne> myItems;
	ArrayList<SibOne> myItemsSorted;
	SibTwoAdapter sibTwoAdapter;
	int selection = 0;
	private static final int REQUEST_ADDVERB = 25;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		myItems = deserialize("flash_contents");
		
		myItemsSorted = (ArrayList<SibOne>) myItems.clone();
		Collections.sort(myItemsSorted, new SibTwoComparator());


		sibTwoAdapter = new SibTwoAdapter(this, R.layout.playengrow, myItemsSorted);
		setListAdapter(sibTwoAdapter);

		this.registerForContextMenu(getListView());
	}
	
	@SuppressWarnings("unchecked")
	public void onResume() {
		myItems = deserialize("flash_contents");
		
		myItemsSorted = (ArrayList<SibOne>) myItems.clone();
		Collections.sort(myItemsSorted, new SibTwoComparator());


		sibTwoAdapter = new SibTwoAdapter(this, R.layout.playengrow, myItemsSorted);
		setListAdapter(sibTwoAdapter);

		this.registerForContextMenu(getListView());
		this.setSelection(selection);
		super.onResume();
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		//String sibOneName = myItems.get(position).getName();
		
		String sibOneName = myItemsSorted.get(position).getName();
		Toast.makeText(getApplicationContext(), sibOneName, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.wordmenu, menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == REQUEST_ADDVERB) {
				//add a verb (still need to check if the data is there
				if ((data.hasExtra("item1")) && (data.hasExtra("item2"))) {
					//get the index of the object in our main arraylist, myItems
					//myItems = deserialize("flash_contents");
					
					//myItemsSorted = (ArrayList<SibOne>) myItems.clone();
					//Collections.sort(myItemsSorted, new SibTwoComparator());
					
					//int tmpIndex = myItemsSorted.indexOf(myItems.get(data.getExtras().getInt("position")));
					
					myItems.get(data.getExtras().getInt("position")).getPair().addVerb(data.getExtras().getString("item1"),data.getExtras().getString("item2"));
					
					//myItemsSorted.get(tmpIndex).getPair().addVerb(data.getExtras().getString("item1"),data.getExtras().getString("item2"));
					serialize(myItems, "flash_contents");
					//Toast.makeText(getApplicationContext(), "Verb Added", Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent(this, ViewWord.class);
					intent.putExtra("position", data.getExtras().getInt("position"));
					startActivityForResult(intent, REQUEST_ADDVERB);
				}
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.view_verb:
			selection = info.position;
			Intent intent = new Intent(this, ViewWord.class);
			intent.putExtra("position", myItems.indexOf(myItemsSorted.get(info.position)));
			startActivityForResult(intent, REQUEST_ADDVERB);
			break;

		case R.id.edit:
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);

			final View dialog_layout = getLayoutInflater().inflate(
					R.layout.editword, null);

			EditText input1 = (EditText) dialog_layout
					.findViewById(R.id.input1);
			input1.setText(myItemsSorted.get(info.position).getName());
			EditText input2 = (EditText) dialog_layout
					.findViewById(R.id.input2);
			input2.setText(myItemsSorted.get(info.position).getPair().getName());

			alert.setView(dialog_layout)
					.setTitle("Edit Item")
					.setMessage(
							"Edit "
									+ myItemsSorted.get(info.position).getName()
									+ " / "
									+ myItemsSorted.get(info.position).getPair()
											.getName() + "?")
					.setPositiveButton("Edit",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

									Toast.makeText(getApplicationContext(),
											"Edited", Toast.LENGTH_LONG).show();
									
									//hack to update the adapter, mimic what we're going to persist
									//myItemsSorted.get(info.position).setName(((EditText) dialog_layout.findViewById(R.id.input1)).getText().toString().trim());
									//myItemsSorted.get(info.position).getPair().setName(((EditText) dialog_layout.findViewById(R.id.input2)).getText().toString().trim());
									
									//now take the data from our db, update it, and persist it
									//myItems = deserialize("flash_contents");
									
									//get the index of the object in our main arraylist, myItems
									int tmpIndex = myItems.indexOf(myItemsSorted.get(info.position));
									
									myItems.get(tmpIndex).setName(((EditText) dialog_layout.findViewById(R.id.input1)).getText().toString().trim());
									myItems.get(tmpIndex).getPair().setName(((EditText) dialog_layout.findViewById(R.id.input2)).getText().toString().trim());
									
									myItemsSorted.get(info.position).setName(((EditText) dialog_layout.findViewById(R.id.input1)).getText().toString().trim());
									myItemsSorted.get(info.position).getPair().setName(((EditText) dialog_layout.findViewById(R.id.input2)).getText().toString().trim());
									
									//myItemsSorted = (ArrayList<SibOne>) myItems.clone();
									//Collections.sort(myItemsSorted, new SibTwoComparator());
									
									sibTwoAdapter.notifyDataSetChanged();
									try {
										serialize(myItems, "flash_contents");
									} catch (NullPointerException e) {
										e.printStackTrace();
									}

								}

							}).setNegativeButton("Cancel", null).show();
			return true;
		case R.id.delete:
			new AlertDialog.Builder(PlayTel.this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Delete Item")
					.setMessage(
							"Delete " + myItemsSorted.get(info.position).getName()
									+ "?")
					.setPositiveButton("Delete",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

									// Delete the entry
									Toast.makeText(getApplicationContext(),
											"Deleted", Toast.LENGTH_LONG)
											.show();
									
									//myItems = deserialize("flash_contents");
									
									//myItemsSorted = (ArrayList<SibOne>) myItems.clone();
									//Collections.sort(myItemsSorted, new SibTwoComparator());
									
									//get the index of the object in our main arraylist, myItems
									int tmpIndex = myItems.indexOf(myItemsSorted.get(info.position));

									myItemsSorted.remove(info.position);
									myItems.remove(tmpIndex);
									
									

									try {
										serialize(myItems, "flash_contents");
									} catch (NullPointerException e) {
										e.printStackTrace();
									}
									sibTwoAdapter.notifyDataSetChanged();

								}

							}).setNegativeButton("Cancel", null).show();

			return true;
		}

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
