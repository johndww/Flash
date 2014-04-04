package com.jwstudios.flash.listwords;

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
import com.jwstudios.flash.R;
import com.jwstudios.flash.SibOne;
import com.jwstudios.flash.ViewWord;
import com.jwstudios.flash.sibs.SibOneAdapter;
import com.jwstudios.flash.sibs.SibTwoAdapter;
import com.jwstudios.flash.sibs.SibTwoComparator;
import com.jwstudios.flash.util.Fconstant;
import com.jwstudios.flash.util.NotificationFactory;
import com.jwstudios.flash.util.PersistanceUtils;

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
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == Fconstant.REQUEST_ADDVERB) {
				//add a verb (still need to check if the data is there
				if ((data.hasExtra("item1")) && (data.hasExtra("item2"))) {
                    final String verbSibOneName = data.getExtras().getString("item1");
                    final String verbSibTwoName = data.getExtras().getString("item2");
                    final SibOne sibOne = myItems.get(data.getExtras().getInt("position"));

                    sibOne.getPair().addVerb(verbSibOneName, verbSibTwoName);

                    PersistanceUtils.updateSibs(getApplicationContext());
					
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
		myItems = PersistanceUtils.getSibOnesList(getApplicationContext());
		if (myItems != null && myItems.size() > 0) {
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
		}
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
        final SibOne sibOne = myItemsSorted.get(position);
        NotificationFactory.standardNotification(sibOne.getFullDescription(), "View Word", this);
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
					.findViewById(R.id.edit1);
			input1.setText(myItemsSorted.get(info.position).getName());
			EditText input2 = (EditText) dialog_layout
					.findViewById(R.id.edit2);
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

                                    final SibOne sibOne = myItemsSorted.get(info.position);
                                    int sibIndexMyItems = myItems.indexOf(sibOne);

                                    final String sibOneName = ((EditText) dialog_layout.findViewById(R.id.edit1)).getText().toString().trim();
                                    final String sibTwoName = ((EditText) dialog_layout.findViewById(R.id.edit2)).getText().toString().trim();

                                    sibOne.setName(sibOneName);
                                    sibOne.getPair().setName(sibTwoName);

                                    PersistanceUtils.updateSibs(getApplicationContext());

                                    //update the displays sibs
                                    sibOne.setName(sibOneName);
                                    sibOne.getPair().setName(sibTwoName);

									if (sibOneAdapter == null) {
										sibTwoAdapter.notifyDataSetChanged();
									} else {
										sibOneAdapter.notifyDataSetChanged();
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
							final SibOne deletedSibOne = myItems.remove(tmpIndex);
                            PersistanceUtils.deletePair(deletedSibOne.getUniqueId(), getApplicationContext());
							
							if (sibOneAdapter == null) {
								sibTwoAdapter.notifyDataSetChanged();
							} else {
								sibOneAdapter.notifyDataSetChanged();
							}
//
//							try {
//								Serializer.serialize(myItems);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
							

						}

					}).setNegativeButton("Cancel", null).show();

			//return true;
			break;
		}
		return super.onContextItemSelected(item);
	}
}
