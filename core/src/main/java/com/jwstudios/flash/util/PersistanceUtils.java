package com.jwstudios.flash.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.jwstudios.flash.SibOne;
import com.jwstudios.flash.SibTwo;

import android.content.Context;

/**
 * User: johnwright
 * Date: 11/30/13
 */
public class PersistanceUtils {
    // cached version of up to date deserialized myitems. do not access directly
    // TODO will only work if all persistance is done through here
    static Map<UUID, SibOne> MY_ITEMS;

    public static boolean addPair(final String sibOneName, final String sibTwoName, Context context) {
        final SibOne sibOne = new SibOne(sibOneName, new SibTwo(sibTwoName), 0, 0);

        if (alreadyExists(sibOne, context)) {
            return false;
        }

        final Map<UUID, SibOne> myItems = getSibOnesMap(context);
        myItems.put(sibOne.getUniqueId(), sibOne);
        save(myItems, context);
        return true;
    }

    private static boolean alreadyExists(final SibOne sibOne, final Context context) {
        final Set<SibOne> items = getSibOnesSet(context);
        return items.contains(sibOne);
    }

    public static int[] addWords(final Collection<SibOne> words, Context context) {
        final Map<UUID, SibOne> myItems = getSibOnesMap(context);
        final Set<SibOne> myItemsSet = getSibOnesSet(context);

        int serverIds[] = new int[words.size()];

        int i = 0;
        for (final SibOne sibOne : words) {
            // only add if we dont already have that word
            if (!myItemsSet.contains(sibOne)) {
                myItems.put(sibOne.getUniqueId(), sibOne);
                serverIds[i] = sibOne.getServerId();
                i++;
            }
        }
        save(myItems, context);
        return serverIds;
    }

    public static boolean deletePair(final UUID uniqueID, Context context) {
        final Map<UUID, SibOne> myItems = getSibOnesMap(context);
        myItems.remove(uniqueID);
        save(myItems, context);
        return true;
    }

    /**
     * Updates all sibOne and persists the changes. Will not add new sibs, only current
     * @param context
     */
    public static boolean updateSibs(Context context) {
        final Map<UUID, SibOne> myItems = getSibOnesMap(context);
        save(myItems, context);
        return true;
    }

    private static void save(Map<UUID, SibOne> myItems, Context context) {
        MY_ITEMS = myItems;
        Serializer.serialize(myItems.values(), context);
    }

    /**
     * Get a set of up to date sibs.
     * @return non null set
     * @param context
     */
    public static Set<SibOne> getSibOnesSet(Context context) {
        Set<SibOne> myItemsSet =  new HashSet<SibOne>();
        if (MY_ITEMS != null) {
            myItemsSet.addAll(MY_ITEMS.values());
            return myItemsSet;
        }
        MY_ITEMS = Serializer.deserializeAsMap(context);
        if (MY_ITEMS == null) {
            MY_ITEMS = new HashMap<UUID, SibOne>();
        }
        myItemsSet.addAll(MY_ITEMS.values());
        return myItemsSet;
    }
    /**
     * Get a map of up to date sibs.
     * @return non null map
     * @param context
     */
    public static Map<UUID, SibOne> getSibOnesMap(Context context) {
        if (MY_ITEMS != null) {
            return MY_ITEMS;
        }
        MY_ITEMS = Serializer.deserializeAsMap(context);
        if (MY_ITEMS == null) {
            MY_ITEMS = new HashMap<UUID, SibOne>();
        }
        return MY_ITEMS;
    }
    /**
     *
     * Get a set of up to date sibs as a list.
     * @return non null set
     * @param context
     */
    public static ArrayList<SibOne> getSibOnesList(Context context) {
        ArrayList<SibOne> myItemsList;
        if (MY_ITEMS != null) {
            myItemsList = new ArrayList<SibOne>();
            myItemsList.addAll(MY_ITEMS.values());
            return myItemsList;
        }
        MY_ITEMS = Serializer.deserializeAsMap(context);
        if (MY_ITEMS == null) {
            MY_ITEMS = new HashMap<UUID, SibOne>();
        }
        myItemsList = new ArrayList<SibOne>();
        myItemsList.addAll(myItemsList);
        return myItemsList;
    }
}
