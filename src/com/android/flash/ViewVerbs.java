package com.android.flash;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
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
import com.android.flash.sibs.SibOneAdapter;
import com.android.flash.util.PersistanceUtils;
import com.android.flash.util.Serializer;

import java.util.ArrayList;

public class ViewVerbs extends ListActivity {

	ArrayList<SibOne> myItems;
	ArrayList<SibOne> myVerbs;
	SibOneAdapter sibOneAdapter;
	int position;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        myItems = PersistanceUtils.getSibOnesList();
		position = getIntent().getExtras().getInt("position");
		myVerbs = myItems.get(position).getPair().getVerbs();

		sibOneAdapter = new SibOneAdapter(this, R.layout.verbrow, myVerbs);
		setListAdapter(sibOneAdapter);

		this.registerForContextMenu(getListView());
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		//String sibTwoName = myItems.get(position).getPair().getName();
		
		String sibTwoName = myVerbs.get(position).getPair().getName();
		Toast.makeText(getApplicationContext(), sibTwoName, Toast.LENGTH_SHORT)
				.show();
	}

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.verbmenu, menu);
    }

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.edit:
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			
			
			final View dialog_layout = getLayoutInflater().inflate(R.layout.editword, null);
			
			EditText input1 = (EditText) dialog_layout.findViewById(R.id.input1);
			input1.setText(myVerbs.get(info.position).getName());
			EditText input2 = (EditText) dialog_layout.findViewById(R.id.input2);
			input2.setText(myVerbs.get(info.position).getPair().getName());
			
			alert.setView(dialog_layout).setTitle("Edit Item").setMessage("Edit " + myVerbs.get(info.position).getName() + " / " + myVerbs.get(info.position).getPair().getName() + "?").setPositiveButton("Edit",
				new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

									// Edit the entry
									Toast.makeText(getApplicationContext(),
											"Edited", Toast.LENGTH_LONG)
											.show();

                                    final String sibOneName = ((EditText) dialog_layout.findViewById(R.id.input1)).getText().toString().trim();
                                    final String sibTwoName = ((EditText) dialog_layout.findViewById(R.id.input2)).getText().toString().trim();
                                    myVerbs.get(info.position).setName(sibOneName);
                                    myVerbs.get(info.position).getPair().setName(sibTwoName);
									
									sibOneAdapter.notifyDataSetChanged();
                                    //serialize myItems
                                    PersistanceUtils.updateSibs();

								}

							}
			).setNegativeButton("Cancel", null).show();
			return true;
		case R.id.delete:
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Delete Item")
					.setMessage(
							"Delete " + myVerbs.get(info.position).getName()
									+ "?")
					.setPositiveButton("Delete",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

									// Delete the entry
									Toast.makeText(getApplicationContext(),
											"Deleted", Toast.LENGTH_LONG)
											.show();
									
									//get the index of the object in our main arraylist, myItems
									//int tmpIndex = myItems.indexOf(myVerbs.get(info.position));

									myVerbs.remove(info.position);
									//myItems.remove(tmpIndex);
									sibOneAdapter.notifyDataSetChanged();

                                    //serialize myItems
                                    PersistanceUtils.updateSibs();

								}

							}).setNegativeButton("Cancel", null).show();

			return true;
		}

		return super.onContextItemSelected(item);

	}
}
