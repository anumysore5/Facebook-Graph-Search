package com.example.anumy.searchonfb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class FavoritesActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private String QUERY_STRING = "";
    private ArrayList<Favorite> objectsList;
    private ListView objectsToDisplay;
    FavoritesAdapter favoritesAdapter;
    String type;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        objectsList = new ArrayList<>();
        type = "user";
        prefs = getSharedPreferences("FAVORITES", Context.MODE_PRIVATE);
        editor = prefs.edit();

        favoritesAdapter =new FavoritesAdapter(this,R.layout.row, objectsList);
        objectsToDisplay = (ListView) findViewById(R.id.fav_listview);
        objectsToDisplay.setAdapter(favoritesAdapter);
        getAllFavoriteObjects(type);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.fav_tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // called when tab selected
                if(tab.getText().toString().equals("Users")) {
                    type = "user";
                }
                if(tab.getText().toString().equals("Pages")) {
                    type = "page";
                }
                if(tab.getText().toString().equals("Events")) {
                    type = "event";
                }
                if(tab.getText().toString().equals("Places")) {
                    type = "place";
                }
                if(tab.getText().toString().equals("Groups")) {
                    type = "group";
                }
                getAllFavoriteObjects(type);
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

    private void getAllFavoriteObjects(String type) {
        objectsList.clear();
        Map<String, ?> all = prefs.getAll();
        Iterator it = all.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String key = pair.getKey().toString();
            String value = pair.getValue().toString();
            Gson gson = new Gson();
            Favorite favorite = gson.fromJson(value, Favorite.class);
            if(favorite.getType().equals(type)) {
                objectsList.add(favorite);
            } else {
                continue;
            }
        }
        favoritesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_favorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_aboutme) {
            Intent intent = new Intent(this, AboutMe.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        getAllFavoriteObjects(type);
        super.onRestart();
    }
}