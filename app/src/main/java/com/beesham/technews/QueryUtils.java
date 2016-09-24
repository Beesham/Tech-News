package com.beesham.technews;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beesham on 9/22/2016.
 */
public final class QueryUtils {
    private QueryUtils() {
    }

    public static List fetchNewsData(String stringUrl){
        URL url = createUrl(stringUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractStories(jsonResponse);
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static ArrayList<Story> extractStories(String jsonResponse){

        ArrayList<Story> newsArrayList = new ArrayList<>();

        try {
            JSONObject response = new JSONObject(jsonResponse).getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for(int i=0; i < results.length(); i++){
                JSONObject storyJson = results.getJSONObject(i);
                String title = storyJson.getString("webTitle");
                String pubDate = storyJson.getString("webPublicationDate");
                String url = storyJson.getString("webUrl");

                StringBuilder contributors = new StringBuilder();

                JSONArray tags = storyJson.getJSONArray("tags");
                if(tags.length() != 0){
                    for(int j=0; j < tags.length(); j++){
                        JSONObject tag = tags.getJSONObject(j);
                        if(tag.getString("type").equals("contributor")){
                            if(j == tags.length()-1){
                                contributors.append(tag.getString("webTitle"));
                            }
                            else{
                                contributors.append(tag.getString("webTitle") + ", ");
                            }
                        }
                    }
                }

                newsArrayList.add(new Story(title, url, decodeDate(pubDate), contributors.toString()));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsArrayList;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == 200){
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(httpURLConnection != null) httpURLConnection.disconnect();
            if(inputStream != null) inputStream.close();
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static String decodeDate(String string){
        String date = string.substring(0,10);
        return date;
    }

}
