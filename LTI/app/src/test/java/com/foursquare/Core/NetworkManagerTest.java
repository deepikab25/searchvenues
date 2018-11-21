package com.foursquare.Core;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

public class NetworkManagerTest {


    private Context mContext =  mock(Context.class);
    @Mock
    private boolean isConnected=true;
    @Mock
    private InputStream inputStream;


    /**
     * Test Function to trigger network request if network is connected
     * */
    @Test
    public void inputStreamPostData() {
        String mUrl = "https://api.foursquare.com/v2/venues/search?ll=40.7,-74&client_id=F25D5GWMQGN5BQUNM0C02G4NGFVBBWY21C05MGE4U4BQ30BP&client_secret=FUKHVW24QGB4GDQA1DLXDTR0APU0PY2SZE2Q5D51TEJ5TBQK&v=20181117";

        final ConnectivityManager connManager = mock(ConnectivityManager.class);
        final NetworkInfo networkInfo = mock(NetworkInfo.class);
        Mockito.when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connManager);
        Mockito.when(connManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        Mockito.when(networkInfo.isConnected()).thenReturn(isConnected);
        if(isConnected)
            assertNotEquals(null,NetworkManager.inputStreamPostData(mUrl));

    }

    /**
     * Test Function to test the search query and response
     * */
    @Test
    public void TestWordSearch() {

        String searchWord = "Coffee";
        String mUrl = "https://api.foursquare.com/v2/venues/search?ll=40.7,-74&client_id=F25D5GWMQGN5BQUNM0C02G4NGFVBBWY21C05MGE4U4BQ30BP&client_secret=FUKHVW24QGB4GDQA1DLXDTR0APU0PY2SZE2Q5D51TEJ5TBQK&v=20181117&query="+searchWord;
        final ConnectivityManager connManager = mock(ConnectivityManager.class);
        final NetworkInfo networkInfo = mock(NetworkInfo.class);
        JSONArray venues=null;
        Mockito.when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connManager);
        Mockito.when(connManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        Mockito.when(networkInfo.isConnected()).thenReturn(isConnected);
        if(isConnected) {
            inputStream = NetworkManager.inputStreamPostData(mUrl);
            String targetString ="";
            Reader reader = new InputStreamReader(inputStream);
           try{
               char[] arr = new char[8 * 1024];
               StringBuilder buffer = new StringBuilder();
               int numCharsRead;
               while ((numCharsRead = reader.read(arr, 0, arr.length)) != -1) {
                   buffer.append(arr, 0, numCharsRead);
               }
               reader.close();
               targetString = buffer.toString();
               JsonParser jsonParser = new JsonParser();
               JsonObject mJsonObject=(JsonObject) jsonParser.parse(targetString);
               JsonObject mJsonResponse = mJsonObject.getAsJsonObject("response");
               JsonArray venuesList = mJsonResponse.getAsJsonArray("venues");
               JsonObject venueItem =(JsonObject) venuesList.get(0);
               JsonElement temp=venueItem.get("name");
               Mockito.when((temp.getAsString()).contains(searchWord)).thenReturn(true);
           } catch (Exception e){

           }
        }else{
            //no network
        }

    }
}