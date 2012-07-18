package com.android.flash.util;

import android.os.Environment;
import com.android.flash.SibOne;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

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
    public static void serialize(ArrayList<SibOne> objToSerialize) {
        if (!objToSerialize.isEmpty()) {
            Gson gson = new Gson() ;
            String json = gson.toJson(objToSerialize);

            try {
                //json serialization
                String state = Environment.getExternalStorageState();

                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    // We can read and write the media
                    File sdCard = Environment.getExternalStorageDirectory();
                    ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sdCard.getAbsolutePath() + "/ext_sd/flashjson/"));
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
                        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sdCard.getAbsolutePath() + "/ext_sd/flashjsonbackup/"));
                        os.writeObject(json);
                        os.flush();
                        os.close();
                    } else {
                        System.out
                                .println("Failed to save to SDcard: Not writeable.");
                    }
                } catch (Exception e) {
                    System.out.println("Failed to save to SDcard: Exception");
                }
            }
        }
    }

    /**
     * Deserializes an ArrayList<SibOne> myItems. Needs a FileInputStream param.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<SibOne> deserialize() {
        ArrayList<SibOne> deserializedObject = null;

        try {
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // We can read and write the media
                File sdCard = Environment.getExternalStorageDirectory();
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(sdCard.getAbsolutePath() + "/ext_sd/flashjson/"));
                String json = (String) is.readObject();
                is.close();

                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(json).getAsJsonArray();

                Gson gson = new Gson() ;
                deserializedObject = new ArrayList<SibOne>();
                for (int i=0; i<jsonArray.size(); i++) {
                    deserializedObject.add(gson.fromJson(jsonArray.get(i), SibOne.class));

                }
            }
        } catch (Exception e) {

        }
        return deserializedObject;
    }
}
