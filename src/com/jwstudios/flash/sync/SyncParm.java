package com.jwstudios.flash.sync;

import android.content.Context;
import com.jwstudios.flash.SibOne;

import java.util.Set;

/**
 * User: johnwright
 * Date: 3/29/14
 * Time: 12:16 PM
 */
public class SyncParm {
    final Set<SibOne> myItems;
    final Context context;

    public SyncParm(final Set<SibOne> myItems, final Context context) {
        this.myItems = myItems;
        this.context = context;
    }

    public SyncParm(final Context context) {
        this.context = context;
        this.myItems = null;
    }

    public Set<SibOne> getWords() {
        return this.myItems;
    }

    public Context getContext() {
        return this.context;
    }
}
