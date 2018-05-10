package com.example.anumy.searchonfb;

import android.app.Activity;
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

public class InfoDownloader extends AsyncTask<URL,Void, String> {

    private ResultActivity resultActivity;

    public InfoDownloader(ResultActivity callingActivity) {
        resultActivity = callingActivity;
    }

    @Override
    protected  String doInBackground(URL...urls) {
        String fbResponse = "";

        try {
            fbResponse = makeHttpRequests(urls[0]);
        } catch (IOException e) {
            // do some handling
        }

        return fbResponse;
    }

    private String makeHttpRequests(URL url) throws IOException{

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        String response = "";

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return response;
    }

    private  String readFromStream(InputStream inputStream) throws IOException{

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

    @Override
    protected void onPostExecute(String result) {
        try {
          /*  boolean daA=resultActivity instanceof DetailsActivity;
            boolean r=resultActivity instanceof ResultActivity;

            if(r==true)
            {
                ResultActivity rA=(ResultActivity)resultActivity;
                rA.onDataDownloadComplete(result);
            }
            else if(daA==true)
            {
                DetailsActivity dA=(DetailsActivity)resultActivity;

                    dA.onDataDownloadComplete(result);

            }*/

           resultActivity.onDataDownloadComplete(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}