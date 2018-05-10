package com.example.anumy.searchonfb;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class HdImageUrlDownloader extends AsyncTask<URL,Void, String> {

    private DetailsActivity detailsActivity;
    private ArrayList<Albums> albumObjs;

    public HdImageUrlDownloader(DetailsActivity callingActivity, ArrayList<Albums> albumsList) {
        detailsActivity = callingActivity;
        albumObjs = new ArrayList<>();
        for (Albums obj: albumsList) {
            albumObjs.add(obj);
        }
    }

    @Override
    protected  String doInBackground(URL...urls) {
        String fbResponse = "";

        try {
            for(int i=0; i<albumObjs.size(); i++) {
                Albums obj = albumObjs.get(i);
                ArrayList<String> hdUrls = new ArrayList<>();
                for(int j=0; j<obj.getPictureId().size(); j++) {
                    String str = obj.getPictureId().get(j);
                    Log.d("old Url : " , str);
                    URL url = new URL(str);
                    String response = makeHttpRequests(url);
                    String hdImageUrl = parseJsonObj(response);
                    Log.d("HD url : " , hdImageUrl);
                    hdUrls.add(hdImageUrl);
                }
                albumObjs.get(i).getPictureId().clear();
                albumObjs.get(i).setPictureId(hdUrls);
            }

        } catch (IOException e) {
            // do some handling
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fbResponse;
    }

    private String parseJsonObj(String response) throws JSONException {
        String hdUrl = "";
        JSONObject jsonObject = new JSONObject(response);
        if(jsonObject.has("data")) {
            JSONObject data = jsonObject.getJSONObject("data");
            if(data.has("url")) {
                hdUrl = data.getString("url");
            }
        }
        return hdUrl;
    }

    private String makeHttpRequests(URL url) throws IOException{

        HttpURLConnection httpUrlConnection = null;
        InputStream inputStream = null;

        String response = "";

        try {
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("GET");

            if(httpUrlConnection.getResponseCode() == 200) {
                inputStream = httpUrlConnection.getInputStream();
                response = readFromInputStream(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        detailsActivity.onHDImageDownloadComplete(albumObjs);
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}

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