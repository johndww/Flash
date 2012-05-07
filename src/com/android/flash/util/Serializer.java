package com.android.flash.util;

import android.os.Environment;
import com.android.flash.SibOne;
import com.google.gson.Gson;

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
			try {
                /*java serialization
				ObjectOutputStream os = new ObjectOutputStream(fos);
				os.writeObject(objToSerialize);
				os.close();
                */

                //json serialization
                String state = Environment.getExternalStorageState();

                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    // We can read and write the media
                    File sdCard = Environment.getExternalStorageDirectory();
                    ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sdCard.getAbsolutePath() + "/flashjson/"));
                    os.writeObject(objToSerialize);
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
						// We can read and write the media
						File sdCard = Environment.getExternalStorageDirectory();
                        /*java serialization
						ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sdCard.getAbsolutePath() + "/flash/"));
						os.writeObject(objToSerialize);
						os.close();
						*/

                        //json serialization
                        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sdCard.getAbsolutePath() + "/flashjsonbackup/"));
                        os.writeObject(objToSerialize);
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
            /*java deserialization
			ObjectInputStream is = new ObjectInputStream(fis);
			deserializedObject = (ArrayList<SibOne>) is.readObject();
			is.close();
            */

            //json deserialization
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // We can read and write the media
                File sdCard = Environment.getExternalStorageDirectory();
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(sdCard.getAbsolutePath() + "/flashjson/"));
                deserializedObject = (ArrayList<SibOne>) is.readObject();
                is.close();
            }

		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deserializedObject;
	}
}
