package com.jwstudios.flash.sync;

import java.io.IOException;
import java.util.Set;

import com.jwstudios.flash.SibOne;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * @author john.wright
 * @since 21
 */
public class SuggestNewWordsToServerTask
        extends AsyncTask<SyncParm, Integer, Long> {
    // Do the long-running work in here
    ResponseCode responseCode;
    Context context;

    @Override
    protected Long doInBackground(SyncParm... syncParmses) {
        if (syncParmses.length > 0) {
            final SyncParm parm = syncParmses[0];
            this.context = parm.getContext();
            final Set<SibOne> wordsToSync = parm.getWords();
            try {
                this.responseCode = WordSyncer.suggestNewWordsToServer(wordsToSync);
            }
            catch (IOException e) {
                this.responseCode = ResponseCode.ERROR;
            }
        }
        return 1L;
    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(Long result) {
        if (this.context != null && this.responseCode == ResponseCode.SUCCESS) {
            Toast.makeText(context, "Thanks for suggesting the word! ", Toast.LENGTH_SHORT).show();
        }
    }
}
