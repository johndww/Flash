package com.jwstudios.flash.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import com.jwstudios.flash.SibOne;

/**
 * User: johnwright
 * Date: 2/1/14
 * Time: 12:53 AM
 */
public class SibCollectionUtils {
    public static Set<SibOne> getUnSyncedWords(Context context) {
        final Set<SibOne> myItems = PersistanceUtils.getSibOnesSet(context);
        final Set<SibOne> unSyncedItems = new HashSet<SibOne>();

        for (final SibOne sibOne : myItems) {
            if (!sibOne.isSynced()) {
                unSyncedItems.add(sibOne);
            }
        }
       return unSyncedItems;
    }

    public static int wordCount(final Context context) {
        return PersistanceUtils.getSibOnesSet(context).size();
    }

    public static void setAsSynced(Collection<SibOne> unSyncedItems) {
        for (final SibOne sibOne : unSyncedItems) {
            sibOne.setSynced();
        }
    }

    public static void setVersion(Collection<SibOne> unSyncedItems, final int version) {
        for (final SibOne sibOne : unSyncedItems) {
            sibOne.setVersion(version);
        }
    }

    public static int getMaxVersion(Context context) {
        final Set<SibOne> myItems = PersistanceUtils.getSibOnesSet(context);

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
