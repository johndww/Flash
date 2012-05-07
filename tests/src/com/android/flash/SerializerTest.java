package com.android.flash;

import com.android.flash.util.TestData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * User: johnwright
 * Date: 5/6/12
 * Time: 7:00 PM
 */
public class SerializerTest extends TestCase {
    public void testSerialize() throws Exception {
        ArrayList<SibOne> myItems = TestData.createData();

        assertEquals(myItems.get(0).getName(), "sibone data 0");
        assertEquals(myItems.get(1).getPair().getName(), "sibtwo data 1");

        //json stuff
        Gson gson = new Gson() ;
        String json = gson.toJson(myItems);
        System.out.println("my json " + json);

        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();

        //SibOne sibone = gson.fromJson(jsonArray.get(0), SibOne.class);
        //System.out.println("sibone from json " + sibone.getName());

       ArrayList<SibOne> myItems2 = new ArrayList<SibOne>();
        for (int i=0; i<jsonArray.size(); i++) {
            myItems2.add(gson.fromJson(jsonArray.get(i), SibOne.class));

        }
        //System.out.println("myitems2 name1 " + myItems2.get(0).getName());
        assertEquals(myItems2.get(0).getName(), "sibone data 0");
        assertEquals(myItems2.get(1).getPair().getName(), "sibtwo data 1");
        assertEquals(myItems2.get(2).getPair().getVerbs().get(0).getName(), "siboneverb data 2");
        assertEquals(myItems2.get(3).getPair().getVerbs().get(0).getPair().getName(), "sibtwoverb data 3");

    }

    public void testDeserialize() throws Exception {

    }
}
