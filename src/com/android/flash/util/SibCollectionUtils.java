package com.android.flash.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.android.flash.SibOne;

/**
 * User: johnwright
 * Date: 2/1/14
 * Time: 12:53 AM
 */
public class SibCollectionUtils {
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

    public static int getMaxVersion() {
        final Set<SibOne> myItems = PersistanceUtils.getSibOnesSet();

        int maxVersion = 0;
        for (SibOne sibOne : myItems) {
            final int version = sibOne.getVersion();
            if (version > maxVersion) {
                maxVersion = version;
            }
        }
        return maxVersion;
    }
}
