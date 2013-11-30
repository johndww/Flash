package com.android.flash.util;

import com.android.flash.SibOne;
import com.android.flash.SibTwo;

import java.util.ArrayList;

/**
 * User: johnwright
 * Date: 11/30/13
 */
public class PersistanceUtils {
    public static boolean addPair(final String sibOneName, final String sibTwoName) {
        //TODO should just append the json

        final SibOne sibOne = new SibOne(sibOneName);
        final SibTwo sibTwo = new SibTwo(sibTwoName);
        sibOne.updatePair(sibTwo);

        ArrayList<SibOne> myItems = Serializer.deserialize();
        if (myItems == null) {
            myItems = new ArrayList<SibOne>();
        }
        myItems.add(sibOne);

        Serializer.serialize(myItems);
        return true;
    }
}
