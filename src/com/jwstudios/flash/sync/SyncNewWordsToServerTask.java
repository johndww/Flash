package com.jwstudios.flash.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.jwstudios.flash.SibOne;

import java.io.IOException;
import java.util.Set;

/**
 * User: johnwright
 * Date: 3/29/14
 * Time: 12:11 PM
 */

public class SyncNewWordsToServerTask extends AsyncTask<SyncParm, Integer, Long> {
    // Do the long-running work in here
    ResponseCode responseCode;
    Context context;

    @Override
    protected Long doInBackground(SyncParm... syncParmses) {
        if (syncParmses.length > 0) {
            final SyncParm parm = syncParmses[0];
            final Context context = parm.getContext();
            this.context = context;
            final Set<SibOne> wordsToSync = parm.getWords();
            try {
                this.responseCode = WordSyncer.syncNewWordsToServer(wordsToSync, context);
            } catch (IOException e) {
                this.responseCode = ResponseCode.ERROR;
            }
        }
        return 1L;
    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(Long result) {
        if (this.context != null) {
            Toast.makeText(context, "To Server: " + responseCode.toString(), Toast.LENGTH_LONG).show();
        }
    }
}