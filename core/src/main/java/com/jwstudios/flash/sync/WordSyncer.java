package com.jwstudios.flash.sync;

import android.content.Context;
import com.jwstudios.flash.SibOne;
import com.jwstudios.flash.data.Data;
import com.jwstudios.flash.util.PersistanceUtils;
import com.jwstudios.flash.util.SibCollectionUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author john.wright
 * @since 21
 */
public class WordSyncer {

    public static final String URL = Data.URL.toString();
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(SibOne.class, new SibOneJsonSerializer()).create();

    public static ResponseCode syncNewWordsToServer(final Set<SibOne> unSyncedItems, Context context)
            throws IOException {
        if (unSyncedItems.isEmpty()) {
            return ResponseCode.NO_WORDS;
        }
        String jsonItems = GSON.toJson(unSyncedItems);
        jsonItems = "{ \"sibs\": " + jsonItems + "}";

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL + "?do=syncWords");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("words", jsonItems));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        // Execute HTTP Post Request
        String response = httpclient.execute(httppost, new BasicResponseHandler());
        try {
            final int responseWordVersion = Integer.parseInt(response);
            if (responseWordVersion > 0) {
                // we synced successfully, mark all as synced and persist
                SibCollectionUtils.setAsSynced(unSyncedItems);
                SibCollectionUtils.setVersion(unSyncedItems, responseWordVersion);
                PersistanceUtils.updateSibs(context);
                return ResponseCode.PUT_NEW_WORDS;
            }
        } catch (NumberFormatException e) {
            return ResponseCode.ERROR;
        }
        return ResponseCode.ERROR;
    }

    public static ResponseCode syncNewWordsFromServer(Context context) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL + "?do=getNewWords");
        final String response = httpclient.execute(request, new BasicResponseHandler());

        final SibOne[] newWords = GSON.fromJson(response, SibOne[].class);
        if (newWords.length > 0) {

            final List<SibOne> words = Arrays.asList(newWords);
            SibCollectionUtils.setAsSynced(words);
            final int[] serverIds = PersistanceUtils.addWords(words, context);

            if (sendCompletedSyncToServer(serverIds)) {
                return ResponseCode.ADDED_NEW_WORDS;
            }
            return ResponseCode.ERROR;
        }
        return ResponseCode.NO_WORDS;
    }

    private static boolean sendCompletedSyncToServer(int[] serverIds) throws IOException {
        String jsonItems = GSON.toJson(serverIds);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL + "?do=getNewWordsComplete");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("ids", jsonItems));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        // Execute HTTP Post Request
        String response = httpclient.execute(httppost, new BasicResponseHandler());
        return response.equals("1");
    }

    /**
     * syncs up to the most recently published word pack
     *
     * ie: local has version 3, published max is v5. download v4 & v5 word packs
     *
     * @return response code from server sync
     * @param context
     */
    public static ResponseCode syncNewWordPacksFromServer(Context context) throws IOException {
        final int maxVersion = SibCollectionUtils.getMaxVersion(context);

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL + "?do=getNewWordPacks&v=" + maxVersion);
        final String response = httpclient.execute(request, new BasicResponseHandler());

        final SibOne[] newWords = GSON.fromJson(response, SibOne[].class);
        if (newWords != null && newWords.length > 0) {

            final List<SibOne> words = Arrays.asList(newWords);
            SibCollectionUtils.setAsSynced(words);
            PersistanceUtils.addWords(words, context);

            return ResponseCode.ADDED_NEW_WORDS;
        }
        return ResponseCode.NO_WORDS;
    }
}
