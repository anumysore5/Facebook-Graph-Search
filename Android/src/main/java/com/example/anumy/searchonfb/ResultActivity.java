package com.example.anumy.searchonfb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private String QUERY_STRING = "";
    private ArrayList<User> objectsList;
    private ListView objectsToDisplay;
    private String nextUrl;
    private String prevUrl;
    MyAdapter myAdapter;
    String type;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    List<String> favoritedIds = new ArrayList<>();
    String center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        prefs = getSharedPreferences("FAVORITES", Context.MODE_PRIVATE);
        editor = prefs.edit();
        getAllFavoritedObjectIds();

        // Get the Intent from Mainactivity and extract the search keyword
        Intent intent = getIntent();
        final String keyword = intent.getStringExtra(MainActivity.SEARCH_KEYWORD);
        center = intent.getStringExtra(MainActivity.LATITUDE) + "," + intent.getStringExtra(MainActivity.LONGITUDE);

        objectsList = new ArrayList<>();
        myAdapter=new MyAdapter(this,R.layout.row,objectsList);
        objectsToDisplay = (ListView) findViewById(R.id.listview);
        objectsToDisplay.setAdapter(myAdapter);

        // formulate the query with the keyword and the default type "user"
        type = "user";
        QUERY_STRING = "http://androidapphw9-env.us-west-2.elasticbeanstalk.com/index.php?keyword="+keyword+"&type="+type;

        executeQuery(QUERY_STRING);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // called when tab selected
                if(tab.getText().toString().equals("Users")) {
                    type = "user";
                    QUERY_STRING = "http://androidapphw9-env.us-west-2.elasticbeanstalk.com/index.php?keyword="+keyword+"&type=user";
                }
                if(tab.getText().toString().equals("Pages")) {
                    type = "page";
                    QUERY_STRING = "http://androidapphw9-env.us-west-2.elasticbeanstalk.com/index.php?keyword="+keyword+"&type=page";
                }
                if(tab.getText().toString().equals("Events")) {
                    type = "event";
                    QUERY_STRING = "http://androidapphw9-env.us-west-2.elasticbeanstalk.com/index.php?keyword="+keyword+"&type=event";
                }
                if(tab.getText().toString().equals("Places")) {
                    type = "place";
                    QUERY_STRING = "http://androidapphw9-env.us-west-2.elasticbeanstalk.com/index.php?keyword="+keyword+"&type="+type+"&amp;center="+center;
                }
                if(tab.getText().toString().equals("Groups")) {
                    type = "group";
                    QUERY_STRING = "http://androidapphw9-env.us-west-2.elasticbeanstalk.com/index.php?keyword="+keyword+"&type=group";
                }

                objectsList.clear();
//                QUERY_STRING = "http://androidapphw9-env.us-west-2.elasticbeanstalk.com/index.php?keyword="+keyword+"&type="+type;
                executeQuery(QUERY_STRING);
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // called when tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // called when a tab is reselected
            }
        });
    }

    public void  test(Intent intent){
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            boolean isFavorited = data.getBooleanExtra("isFavorited", false);
            String objectId = data.getStringExtra("objectId");
            for(int i=0; i<objectsList.size(); i++) {
                if(objectsList.get(i).getId().equals(objectId)) {
                    objectsList.get(i).setFavorited(isFavorited);
                    break;
                } else {
                    continue;
                }
            }
            myAdapter.notifyDataSetChanged();
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    private void executeQuery(String QUERY_STRING) {
        URL url = null;

        try {
            url = new URL(QUERY_STRING);
        } catch (MalformedURLException ex) {
            //do exception handling here
        }
        new InfoDownloader(this).execute(url);
    }

    public void onDataDownloadComplete(String result) throws JSONException {
        // convert the result from String to JSON array
        ArrayList<User> objList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        JSONObject pagingObj = jsonObject.getJSONObject("paging");
        nextUrl = null;
        prevUrl = null;

        if(pagingObj.has("next")) {
            nextUrl = pagingObj.getString("next");
//            nextUrl = "http://androidapphw9-env.us-west-2.elasticbeanstalk.com/index.php?nextPageUrl=".concat(pagingObj.getString("next"));
        }

        if(pagingObj.has("previous")) {
            prevUrl = pagingObj.getString("previous");
//            prevUrl = "http://androidapphw9-env.us-west-2.elasticbeanstalk.com/index.php?previousPageUrl="+pagingObj.getString("previous");
        }

        if(pagingObj.has("next") && pagingObj.has("previous")) {
            findViewById(R.id.prev_button).setEnabled(true);
            findViewById(R.id.next_button).setEnabled(true);
        } else if(pagingObj.has("next") && !pagingObj.has("previous")) {
            findViewById(R.id.prev_button).setEnabled(false);
            findViewById(R.id.next_button).setEnabled(true);
        } else if(!pagingObj.has("next") && pagingObj.has("previous")) {
            findViewById(R.id.prev_button).setEnabled(true);
            findViewById(R.id.next_button).setEnabled(false);
        }

        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject innerJsonObj=(JSONObject)jsonArray.get(i);
            String id=innerJsonObj.getString("id");
            String name=innerJsonObj.getString("name");
            JSONObject picOuterObj = innerJsonObj.getJSONObject("picture");
            JSONObject picInnerObj = picOuterObj.getJSONObject("data");
            String pictureUrl = picInnerObj.getString("url");
            boolean isFavorited;
            if(favoritedIds.contains(id)) {
                isFavorited = true;
            } else {
                isFavorited = false;
            }

            User userObj = new User(id, name, pictureUrl, type, isFavorited);
            objList.add(i, userObj);
        }
        setObjectsToListView(objList);
    }

    private void setObjectsToListView(ArrayList<User> objList) {
        objectsList.clear();
        for(User obj:objList) {
            objectsList.add(obj);
        }
        myAdapter.notifyDataSetChanged();
    }

    public void nextButtonClick(View view) {
        executeQuery(nextUrl);
    }

    public void prevButtonClick(View view) {
        executeQuery(prevUrl);
    }

    public void getAllFavoritedObjectIds() {
        // Add the ids of all the favorited items to a list
        Map<String, ?> all = prefs.getAll();
        Iterator it = all.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String key = pair.getKey().toString();
            favoritedIds.add(key);
        }
    }
}