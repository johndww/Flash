package com.android.flash.listwords;

import android.app.AlertDialog;
import android.app.ListActivity;
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
import com.android.flash.R;
import com.android.flash.SibOne;
import com.android.flash.ViewWord;
import com.android.flash.sibs.SibOneAdapter;
import com.android.flash.sibs.SibTwoAdapter;
import com.android.flash.sibs.SibTwoComparator;
import com.android.flash.util.Fconstant;
import com.android.flash.util.Serializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListWords extends ListActivity {
	ArrayList<SibOne> myItems;
	ArrayList<SibOne> myItemsSorted;
	SibOneAdapter sibOneAdapter;
	SibTwoAdapter sibTwoAdapter;
	int selection = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//just use the resume logic
		onResume();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == Fconstant.REQUEST_ADDVERB) {
				//add a verb (still need to check if the data is there
				if ((data.hasExtra("item1")) && (data.hasExtra("item2"))) {
					myItems.get(data.getExtras().getInt("position")).getPair().addVerb(data.getExtras().getString("item1"),data.getExtras().getString("item2"));
					
					FileOutputStream fos;
					try {
						Serializer.serialize(myItems);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Intent intent = new Intent(this, ViewWord.class);
					intent.putExtra("position", data.getExtras().getInt("position"));
					startActivityForResult(intent, Fconstant.REQUEST_ADDVERB);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void onResume() {
        super.onResume();
		try {
			FileInputStream fis = openFileInput("flash_contents");
			myItems = Serializer.deserialize();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (myItems != null) {
			myItemsSorted = (ArrayList<SibOne>) myItems.clone();
			//set our adapter based on the type of list the user selected on the home page
			switch(getIntent().getExtras().getInt("listtype")) {
			case Fconstant.LISTTYPE_ENGALPH:
				Collections.sort(myItemsSorted);
				sibOneAdapter = new SibOneAdapter(this, R.layout.playengrow, myItemsSorted);
				setListAdapter(sibOneAdapter);
				break;
			case Fconstant.LISTTYPE_TELALPH:
				Collections.sort(myItemsSorted, new SibTwoComparator());
				sibTwoAdapter = new SibTwoAdapter(this, R.layout.playengrow, myItemsSorted);
				setListAdapter(sibTwoAdapter);
				break;
			case Fconstant.LISTTYPE_ENGDATE:
				Collections.sort(myItemsSorted, new Comparator<SibOne>() {
					@Override
					public int compare(SibOne o1, SibOne o2) {
						//sort by date, newest first
						return -o1.getDate().compareTo(o2.getDate());
					}
				});
				sibOneAdapter = new SibOneAdapter(this, R.layout.playengrow, myItemsSorted);
				setListAdapter(sibOneAdapter);
				break;
			case Fconstant.LISTTYPE_TELDATE:
				Collections.sort(myItemsSorted, new Comparator<SibOne>() {
					@Override
					public int compare(SibOne o1, SibOne o2) {
						//sort  by date, newest first
						return -o1.getDate().compareTo(o2.getDate());
					}
				});
				sibTwoAdapter = new SibTwoAdapter(this, R.layout.playengrow, myItemsSorted);
				setListAdapter(sibTwoAdapter);
				break;
			default:
				break;
			}

		this.registerForContextMenu(getListView());
		this.setSelection(selection);
		super.onResume();
		}
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String sibTwoName;
		String sibOneName;
		
		switch(getIntent().getExtras().getInt("listtype")) {
		case Fconstant.LISTTYPE_ENGALPH:
			sibTwoName = myItemsSorted.get(position).getPair().getName();
			Toast.makeText(getApplicationContext(), sibTwoName, Toast.LENGTH_SHORT)
					.show();
			break;
		case Fconstant.LISTTYPE_TELALPH:
			sibOneName = myItemsSorted.get(position).getName();
			Toast.makeText(getApplicationContext(), sibOneName, Toast.LENGTH_SHORT)
					.show();
			break;
		case Fconstant.LISTTYPE_ENGDATE:
			sibTwoName = myItemsSorted.get(position).getPair().getName();
			Toast.makeText(getApplicationContext(), sibTwoName, Toast.LENGTH_SHORT)
					.show();
			break;
		case Fconstant.LISTTYPE_TELDATE:
			sibOneName = myItemsSorted.get(position).getName();
			Toast.makeText(getApplicationContext(), sibOneName, Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.wordmenu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		selection = info.position;
		switch (item.getItemId()) {
		case R.id.view_verb:
			Intent intent = new Intent(this, ViewWord.class);
			intent.putExtra("position", myItems.indexOf(myItemsSorted.get(info.position)));
			startActivityForResult(intent, Fconstant.REQUEST_ADDVERB);
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
									
									//this is a generic way to save for a sibOne or sibTwo page
									int tmpIndex = myItems.indexOf(myItemsSorted.get(info.position));
									
									myItems.get(tmpIndex).setName(((EditText) dialog_layout.findViewById(R.id.input1)).getText().toString().trim());
									myItems.get(tmpIndex).getPair().setName(((EditText) dialog_layout.findViewById(R.id.input2)).getText().toString().trim());
									
									myItemsSorted.get(info.position).setName(((EditText) dialog_layout.findViewById(R.id.input1)).getText().toString().trim());
									myItemsSorted.get(info.position).getPair().setName(((EditText) dialog_layout.findViewById(R.id.input2)).getText().toString().trim());
									
									if (sibOneAdapter == null) {
										sibTwoAdapter.notifyDataSetChanged();
									} else {
										sibOneAdapter.notifyDataSetChanged();
									}
									try {
										Serializer.serialize(myItems);
									} catch (Exception e) {
										e.printStackTrace();
									}

								}

							}).setNegativeButton("Cancel", null).show();
			//return true;
			break;
		case R.id.delete:
			new AlertDialog.Builder(this)
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
							
							int tmpIndex = myItems.indexOf(myItemsSorted.get(info.position));

							myItemsSorted.remove(info.position);
							myItems.remove(tmpIndex);
							
							if (sibOneAdapter == null) {
								sibTwoAdapter.notifyDataSetChanged();
							} else {
								sibOneAdapter.notifyDataSetChanged();
							}

							try {
								Serializer.serialize(myItems);
							} catch (Exception e) {
								e.printStackTrace();
							}
							

						}

					}).setNegativeButton("Cancel", null).show();

			//return true;
			break;
		}
		return super.onContextItemSelected(item);
	}
}
