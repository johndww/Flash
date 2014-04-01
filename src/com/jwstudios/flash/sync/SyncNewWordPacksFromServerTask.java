package com.jwstudios.flash.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.jwstudios.flash.SibOne;

import java.io.IOException;
import java.util.Set;

/**
 * User: johnwright
 * Date: 3/29/14
 * Time: 12:11 PM
 */

public class SyncNewWordPacksFromServerTask extends AsyncTask<SyncParm, Integer, Long> {
    // Do the long-running work in here
    ResponseCode responseCode;
    Context context;
    Button syncButton;

    @Override
    protected Long doInBackground(SyncParm... syncParmses) {
        if (syncParmses.length > 0) {
            final SyncParm parm = syncParmses[0];
            final Context context = parm.getContext();
            this.syncButton = parm.getButton();
            this.context = context;
            try {
                this.responseCode = WordSyncer.syncNewWordPacksFromServer(context);
            } catch (IOException e) {
                responseCode = ResponseCode.ERROR;
            }
        }
        return 1L;
    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(Long result) {
        if (this.context != null) {
            Toast.makeText(context, "From Server: " + responseCode.toString(), Toast.LENGTH_LONG).show();
        }

        if (this.syncButton != null) {
            syncButton.setVisibility(View.GONE);
        }
    }
}