package com.android.flash;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

public class Serializer {

	/**
	 * Private constructor to prohibit any instances of this Serializer class
	 */
	private Serializer() {
		
	}
	
	/**
	 * Serialize an ArrayList<SibOne> myItems.  Needs a FileOutputStream param.
	 * 
	 * @param objToSerialize
	 * @param fileName
	 * @param fos FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
	 */
	public void serialize(ArrayList<SibOne> objToSerialize, String fileName, FileOutputStream fos) {
		//FileOutputStream fos;
		try {
			//fos = openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(objToSerialize);
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Deserializes an ArrayList<SibOne> myItems.  Needs a FileInputStream param. 
	 * 
	 * @param fileName
	 * @param fis FileInputStream fis = openFileInput(fileName);
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<SibOne> deserialize(String fileName, FileInputStream fis) {
		ArrayList<SibOne> deserializedObject = null;

		try {
			//FileInputStream fis = openFileInput(fileName);
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
