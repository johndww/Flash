package com.android.flash.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Environment;

import com.android.flash.SibOne;

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
	 * @param fileName
	 * @param fos
	 *            FileOutputStream fos = openFileOutput(fileName,
	 *            Context.MODE_PRIVATE);
	 */
	public static void serialize(ArrayList<SibOne> objToSerialize, FileOutputStream fos) {
		if (!objToSerialize.isEmpty()) {
			try {
				ObjectOutputStream os = new ObjectOutputStream(fos);
				os.writeObject(objToSerialize);
				os.close();
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
						ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sdCard.getAbsolutePath() + "/flash/"));
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
	 * @param fis
	 *            FileInputStream fis = openFileInput(fileName);
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<SibOne> deserialize(FileInputStream fis) {
		ArrayList<SibOne> deserializedObject = null;

		try {
			// FileInputStream fis = openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			deserializedObject = (ArrayList<SibOne>) is.readObject();
			is.close();
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