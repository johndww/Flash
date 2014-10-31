package com.jwstudios.flash;

import java.util.List;
import java.util.UUID;

import com.jwstudios.flash.util.PersistanceUtils;

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

public class ViewWord
        extends Activity {
    SibOne sibone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewsibone);

        final String uuid = getIntent().getExtras().getString("uuid");
        this.sibone = PersistanceUtils.getSibOnesMap(getApplicationContext()).get(UUID.fromString(uuid));

        ((TextView) findViewById(R.id.EngWord)).setText(sibone.getName());
        ((TextView) findViewById(R.id.TelWord)).setText(sibone.getPair()
                .getName());

        List<SibOne> myVerbs = this.sibone.getPair().getVerbs();

        if (myVerbs != null) {

            LinearLayout layout = (LinearLayout) findViewById(R.id.verbs);

            LayoutInflater vi = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for (final SibOne myVerb : myVerbs) {
                // inflate the verb layout

                View view = vi.inflate(R.layout.verb_layout, null);

                this.registerForContextMenu(view);

                TextView tmpEngVerb = (TextView) view
                        .findViewById(R.id.engverb);
                TextView tmpTelVerb = (TextView) view
                        .findViewById(R.id.telverb);

                tmpEngVerb.setText(myVerb.getName());
                tmpTelVerb.setText(myVerb.getPair().getName());

                layout.addView(view);
            }
        }
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, 0, "View Verbs");
    }

    public void onClick(View v) {
        // do some validating to make sure they actually filled the fields
        final String verbSibOneName = ((EditText) findViewById(R.id.verb_eng))
                .getText().toString();
        final String verbSibTwoName = ((EditText) findViewById(R.id.verb_tel))
                .getText().toString();

        if (!verbSibOneName
                .trim().equals("")
                && !verbSibTwoName.trim().equals("")) {
            addVerb(verbSibOneName, verbSibTwoName, this.sibone.getUniqueId());
        }
        reload();
    }

    private void reload() {
        finish();
        startActivity(getIntent());
    }

    private void addVerb(final String verbSibOneName, final String verbSibTwoName, final UUID uuid) {
        //add a verb (still need to check if the data is there
        final SibOne sibOne = PersistanceUtils.getSibOnesMap(getApplicationContext()).get(uuid);

        sibOne.getPair().addVerb(verbSibOneName, verbSibTwoName);

        PersistanceUtils.updateSibs(getApplicationContext());

        Intent intent = new Intent(this, ViewWord.class);
        intent.putExtra("uuid", uuid.toString());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Intent to ViewVerbs.class
        Intent intent = new Intent(this, ViewVerbs.class);
        intent.putExtra("uuid", this.sibone.getUniqueId().toString());
        startActivity(intent);
        return super.onContextItemSelected(item);
    }
}
