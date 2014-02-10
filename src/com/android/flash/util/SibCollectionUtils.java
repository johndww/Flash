package com.android.flash.util;

import com.android.flash.SibOne;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: johnwright
 * Date: 2/1/14
 * Time: 12:53 AM
 */
public class SibCollectionUtils<T> {
    public static Set<SibOne> getUnSyncedWords() {
        final Set<SibOne> myItems = PersistanceUtils.getSibOnesSet();
        final Set<SibOne> unSyncedItems = new HashSet<SibOne>();

        for (final SibOne sibOne : myItems) {
            if (!sibOne.isSynced()) {
                unSyncedItems.add(sibOne);
            }
        }
       return unSyncedItems;
    }

    public static void setAsSynced(Collection<SibOne> unSyncedItems) {
        for (final SibOne sibOne : unSyncedItems) {
            sibOne.setSynced();
        }
    }
}
