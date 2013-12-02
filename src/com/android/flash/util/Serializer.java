package com.android.flash.util;

import android.os.Environment;
import com.android.flash.SibOne;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.*;

/**
 * A static class used to persist SibOne data ArrayLists
 *
 * @author johnwright
 *
 */
public class Serializer {

    /**
     * Private constructor to prohibit any instances of this Serializer class
     */
    private Serializer() {

    }

    /**
     * Serialize an ArrayList<SibOne> myItems. Needs a FileOutputStream param.
     * Puts a backup in the external storage as well.
     *
     * @param objToSerialize
     *
     */
    protected static void serialize(Collection<SibOne> objToSerialize) {
        if (!objToSerialize.isEmpty()) {
            Gson gson = new Gson() ;
            String json = gson.toJson(objToSerialize);

            try {
                //json serialization
                String state = Environment.getExternalStorageState();

                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    // We can read and write the media
                    File sdCard = Environment.getExternalStorageDirectory();
                    ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sdCard.getAbsolutePath() + "/flashjson"));
                    os.writeObject(json);
                    os.flush();
                    os.close();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // if its monday, backup stuff to external
            Calendar cal = Calendar.getInstance();
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                try {
                    String state = Environment.getExternalStorageState();

                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        File sdCard = Environment.getExternalStorageDirectory();
                        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sdCard.getAbsolutePath() + "/flashjsonbackup"));
                        os.writeObject(json);
                        os.flush();
                        os.close();
                    } else {
                        System.out.println("Failed to save to SDcard: Not writeable.");
                    }
                } catch (Exception e) {
                    System.out.println("Failed to save to SDcard: Exception");
                }
            }
        }
    }

    protected static Set<SibOne> deserializeAsSet() {
        Set<SibOne> myItems = new HashSet<SibOne>();

        myItems.addAll(deserialize().values());
        return myItems;
    }

    protected static Map<UUID, SibOne> deserializeAsMap() {
        return deserialize();
    }

    protected static ArrayList<SibOne> deserializeAsList() {
        ArrayList<SibOne> myItems = new ArrayList<SibOne>();

        myItems.addAll(deserialize().values());
        return myItems;
    }

    private static Map<UUID, SibOne> deserialize() {
        final Map<UUID, SibOne> myItems = new HashMap<UUID, SibOne>();
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // We can read and write the media
                File sdCard = Environment.getExternalStorageDirectory();
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(sdCard.getAbsolutePath() + "/flashjson/"));
                String json = (String) is.readObject();
                is.close();

                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(json).getAsJsonArray();

                Gson gson = new Gson() ;
                for (int i=0; i<jsonArray.size(); i++) {
                    final SibOne sibOne = gson.fromJson(jsonArray.get(i), SibOne.class);
                    myItems.put(sibOne.getUniqueId(), sibOne);
                }
            }
        } catch (Exception e) {
             System.out.println("error deserializing " +  e);
        }
        return myItems;
    }
}
