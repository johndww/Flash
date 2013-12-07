package com.android.flash.util;

import com.android.flash.SibOne;
import com.android.flash.SibTwo;

import java.util.*;

/**
 * User: johnwright
 * Date: 11/30/13
 */
public class PersistanceUtils {
    // cached version of up to date deserialized myitems. do not access directly
    // TODO will only work if all persistance is done through here
    static Map<UUID, SibOne> MY_ITEMS;

    public static boolean addPair(final String sibOneName, final String sibTwoName) {
        final SibOne sibOne = new SibOne(sibOneName, new SibTwo(sibTwoName), 0);

        final Map<UUID, SibOne> myItems = getSibOnesMap();
        myItems.put(sibOne.getUniqueId(), sibOne);
        save(myItems);
        return true;
    }


    public static boolean deletePair(final UUID uniqueID) {
        final Map<UUID, SibOne> myItems = getSibOnesMap();
        myItems.remove(uniqueID);
        save(myItems);
        return true;
    }

    /**
     * Updates a sibOne and persists it.  SibOne name must not be changed.
     */
    public static boolean updateSibs() {
        final Map<UUID, SibOne> myItems = getSibOnesMap();
        save(myItems);
        return true;
    }

    private static void save(Map<UUID, SibOne> myItems) {
        MY_ITEMS = myItems;
        Serializer.serialize(myItems.values());
    }

    /**
     * Get a set of up to date sibs.
     * @return non null set
     */
    public static Set<SibOne> getSibOnesSet() {
        Set<SibOne> myItemsSet =  new HashSet<SibOne>();
        if (MY_ITEMS != null) {
            myItemsSet.addAll(MY_ITEMS.values());
            return myItemsSet;
        }
        MY_ITEMS = Serializer.deserializeAsMap();
        if (MY_ITEMS == null) {
            MY_ITEMS = new HashMap<UUID, SibOne>();
        }
        myItemsSet.addAll(MY_ITEMS.values());
        return myItemsSet;
    }
    /**
     * Get a map of up to date sibs.
     * @return non null map
     */
    public static Map<UUID, SibOne> getSibOnesMap() {
        if (MY_ITEMS != null) {
            return MY_ITEMS;
        }
        MY_ITEMS = Serializer.deserializeAsMap();
        if (MY_ITEMS == null) {
            MY_ITEMS = new HashMap<UUID, SibOne>();
        }
        return MY_ITEMS;
    }
    /**
     *
     * Get a set of up to date sibs as a list.
     * @return non null set
     */
    public static ArrayList<SibOne> getSibOnesList() {
        ArrayList<SibOne> myItemsList;
        if (MY_ITEMS != null) {
            myItemsList = new ArrayList<SibOne>();
            myItemsList.addAll(MY_ITEMS.values());
            return myItemsList;
        }
        MY_ITEMS = Serializer.deserializeAsMap();
        if (MY_ITEMS == null) {
            MY_ITEMS = new HashMap<UUID, SibOne>();
        }
        myItemsList = new ArrayList<SibOne>();
        myItemsList.addAll(myItemsList);
        return myItemsList;
    }
}
