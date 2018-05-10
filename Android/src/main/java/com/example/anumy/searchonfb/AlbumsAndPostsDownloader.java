package com.example.anumy.searchonfb;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class AlbumsAndPostsDownloader extends AsyncTask<URL,Void, String> {

    private DetailsActivity detailsActivity;

    public AlbumsAndPostsDownloader(DetailsActivity callingActivity) {
        detailsActivity = callingActivity;
    }

    @Override
    protected  String doInBackground(URL...urls) {
        String responseStr = "";

        try {
            responseStr = getHttpResponse(urls[0]);
        } catch (IOException e) {
            // do some handling
            e.getStackTrace();
        }

        return responseStr;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            detailsActivity.onDetailsDownloadComplete(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}

    private String getHttpResponse(URL url) throws IOException{

        HttpURLConnection connectionObj = null;
        InputStream inputStream = null;
        String response = "";

        try {
            connectionObj = (HttpURLConnection) url.openConnection();
            connectionObj.setRequestMethod("GET");

            if(connectionObj.getResponseCode() == 200) {
                inputStream = connectionObj.getInputStream();
                response = readFromInputStream(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connectionObj != null) {
                connectionObj.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return response;
    }

    private  String readFromInputStream(InputStream inputStream) throws IOException{

        StringBuilder output = new StringBuilder();

        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while(line!=null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}