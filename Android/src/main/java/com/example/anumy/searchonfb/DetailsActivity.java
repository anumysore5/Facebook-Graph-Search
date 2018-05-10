package com.example.anumy.searchonfb;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    ArrayList<Albums> albumsList;
    ArrayList<Posts> postsList;
    Fragment_albums fragment_albums = new Fragment_albums();
    Fragment_posts fragment_posts = new Fragment_posts();
    Fragment_noAlbums fragment_noAlbums = new Fragment_noAlbums();
    Fragment_noPosts fragment_noPosts = new Fragment_noPosts();
    String name = "";
    String imageUrl = "";
    public static final String NAME = "name";
    public static final String IMAGEURL = "imageurl";
    public static final String IS_FAVORITED = "is_favorited";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String btnId;
    List<String> favoritedIds;
    boolean isFavorited;
    ShareDialog shareDialog;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albums_posts_tablayout);

        albumsList = new ArrayList<>();
        postsList = new ArrayList<>();
        favoritedIds = new ArrayList<>();
        prefs = getSharedPreferences("FAVORITES", Context.MODE_PRIVATE);
        editor = prefs.edit();
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        getAllFavoritedObjects();
        Intent intent = getIntent();
        btnId = intent.getStringExtra(MyAdapter.ROW_ID);
        if(favoritedIds.contains(btnId)) {
            isFavorited = true;
        } else {
            isFavorited = false;
        }

        String queryString = "http://androidapphw9-env.us-west-2.elasticbeanstalk.com/index.php?rowDetails="+btnId;
        executeQuery(queryString);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.albums_posts_tablayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Bundle bundle=new Bundle();

                switch (tab.getText().toString()) {
                    case "Posts":
                        if(postsList.size() == 0) {
                            ft.replace(R.id.fragment_container, fragment_noPosts);
                        } else {
                            bundle.putParcelableArrayList("Test", postsList);
                            fragment_posts.setArguments(bundle);
                            ft.replace(R.id.fragment_container, fragment_posts);
                        }
                        break;
                    case "Albums":
                        if(albumsList.size() == 0) {
                            ft.replace(R.id.fragment_container, fragment_noAlbums);
                        } else {
                            bundle.putParcelableArrayList("Test", albumsList);
                            fragment_albums.setArguments(bundle);
                            ft.replace(R.id.fragment_container, fragment_albums);
                        }
                        break;
                }
                ft.commit();

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

    private void getAllFavoritedObjects() {
        // Add the ids of all the favorited items to a list
        Map<String, ?> all = prefs.getAll();
        Iterator it = all.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String key = pair.getKey().toString();
            favoritedIds.add(key);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(favoritedIds.contains(btnId)) {
            MenuItem removeFromFav = menu.add(0, 1, 1, "Remove from Favorites");
        } else {
            MenuItem addToFav = menu.add(0, 1, 1, "Add to Favorites");
        }
        MenuItem share = menu.add(0, 3, 2, "Share");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            Intent intent2 = new Intent();
            intent2.putExtra("isFavorited", isFavorited);
            intent2.putExtra("objectId", btnId);
            setResult(1, intent2);
            finish();
        } else {
            String name = item.getTitle().toString();

            switch (name) {
                case "Add to Favorites":
                    addToFavorites();
                    displayAddedMessage();
                    changeMenuItemName(item);
                    break;
                case "Remove from Favorites":
                    removeFromFavorites();
                    displayRemovedMessage();
                    changeMenuItemName(item);
                    break;
                case "Share":
                    showFacebookShareDialog(btnId);
                    break;
                default:
                    Intent intent2 = new Intent();
                    intent2.putExtra("isFavorited", isFavorited);
                    intent2.putExtra("objectId", btnId);
                    setResult(1, intent2);
                    finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeFromFavorites() {
        isFavorited = false;
        Intent intent = getIntent();
        String key = intent.getStringExtra(MyAdapter.ROW_ID);
        editor.remove(key);
        editor.apply();
    }

    private void changeMenuItemName(MenuItem item) {
        if(item.getTitle().equals("Add to Favorites")) {
            item.setTitle("Remove from Favorites");
        }
        else if(item.getTitle().equals("Remove from Favorites")) {
            item.setTitle("Add to Favorites");
        }
    }

    private void addToFavorites() {
        isFavorited = true;
        Intent intent = getIntent();
        String key = intent.getStringExtra(MyAdapter.ROW_ID);
        String name = intent.getStringExtra(MyAdapter.ROW_NAME);
        String image = intent.getStringExtra(MyAdapter.ROW_IMAGE);
        String type = intent.getStringExtra(MyAdapter.ROW_TYPE);
        Favorite favObj = new Favorite(key, name, image, type);
        Gson gson = new Gson();
        String obj = gson.toJson(favObj);
        editor.putString(key, obj);
        editor.apply();
    }

    private void displayAddedMessage() {
        Context context = getApplicationContext();
        CharSequence text = "Added to favorites";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void displayRemovedMessage() {
        Context context = getApplicationContext();
        CharSequence text = "Removed from favorites";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void executeQuery(String QUERY_STRING) {
        URL url = null;

        try {
            url = new URL(QUERY_STRING);
        } catch (MalformedURLException ex) {
            //do exception handling here
        }
        new AlbumsAndPostsDownloader(this).execute(url);
    }

    public void onDetailsDownloadComplete(String result) throws JSONException {
        ArrayList<Albums> albumsObjList = new ArrayList<>();

        // For "events" type, result is null
        if(result.equals("null")) {
            populateAlbumsFragment(albumsObjList);
        } else {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has("name")) {
                name = jsonObject.getString("name");
            }
            if (jsonObject.has("picture")) {
                JSONObject obj = (jsonObject.getJSONObject("picture")).getJSONObject("data");
                imageUrl = obj.getString("url");
            }

            if (jsonObject.has("albums")) {
                JSONObject albums = jsonObject.getJSONObject("albums");
                if (albums.has("data")) {
                    JSONArray albumsData = albums.getJSONArray("data");

                    int numberOfAlbums = albumsData.length();
                    if (albumsData.length() > 5) {
                        numberOfAlbums = 5;
                    }

                    for (int i = 0; i < numberOfAlbums; i++) {
                        JSONObject album = (JSONObject) albumsData.get(i);
                        String albumName = "";
                        ArrayList<String> url = new ArrayList<>();
                        if (album.has("name")) {
                            albumName = album.getString("name");
                        }
                        if (album.has("photos")) {
                            JSONObject photos = album.getJSONObject("photos");
                            JSONArray photosData = photos.getJSONArray("data");

                            int numberOfPhotosPerAlbum = photosData.length();
                            if (numberOfPhotosPerAlbum > 2) {
                                numberOfPhotosPerAlbum = 2;
                            }
                            for (int j = 0; j < numberOfPhotosPerAlbum; j++) {
                                JSONObject photo = (JSONObject) photosData.get(j);
//                            url.add("https://graph.facebook.com/v2.8/".concat(photo.getString("id")).concat("/picture?redirect=false&access_token=EAAGg2LWlrb8BAEc6HWrdKly0pAtKc09YCwJPsqS9fC1NEKX7oFFDukANXTGcIANyTZBhMYPmTLCZAocJNpZAqnmZCNEMCRbIlETHqXh69SSTsXnpnwPOGAjUV30ZBOCj03pY1x5EqQZAYzntzfKsju6KEa04ZCEEbkZD"));
                                url.add("http://androidapphw9-env.us-west-2.elasticbeanstalk.com/index.php?pictureId=" + photo.getString("id"));
                            }
                        }
                        Albums albumObj = new Albums(albumName, url);
                        albumsObjList.add(i, albumObj);
                    }
                    new HdImageUrlDownloader(DetailsActivity.this, albumsObjList).execute();
                }
            } else {
                // Display "No albums available to display"
                populateAlbumsFragment(albumsObjList);
            }

            if (jsonObject.has("posts")) {
                JSONObject posts = jsonObject.getJSONObject("posts");
                if (posts.has("data")) {
                    JSONArray postsData = posts.getJSONArray("data");

                    int numberOfPOsts = postsData.length();
                    if (numberOfPOsts > 5) {
                        numberOfPOsts = 5;
                    }

                    for (int i = 0; i < numberOfPOsts; i++) {
                        JSONObject postObj = postsData.getJSONObject(i);
                        String message = "";
                        String dateTime = "";
                        if (postObj.has("message")) {
                            message = postObj.getString("message");
                        } else if (postObj.has("story")) {
                            message = postObj.getString("story");
                        }
                        if (postObj.has("created_time")) {
                            String createdTime = postObj.getString("created_time");
                            dateTime = (createdTime.split("T")[0]) + " " + (createdTime.split("T")[1].split("\\+")[0]);
                        }

                        Posts postsObj = new Posts(name, dateTime, message, imageUrl);
                        postsList.add(postsObj);
                    }
                }
            }
        }
    }

    public void onHDImageDownloadComplete(ArrayList<Albums> result) {
        populateAlbumsFragment(result);
    }

    private void populateAlbumsFragment(ArrayList<Albums> objList) {
        albumsList.clear();
        for(Albums obj:objList) {
            albumsList.add(obj);
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // If there are no albums, show "No albums to display" fragment
        if(albumsList.size() == 0) {
            ft.add(R.id.fragment_container, fragment_noAlbums);
        } else {
            Bundle bundle=new Bundle();
            bundle.putParcelableArrayList("Test",albumsList);

            fragment_albums.setArguments(bundle);
            ft.add(R.id.fragment_container, fragment_albums);
        }
        ft.commit();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void showFacebookShareDialog(String id) {
        String url = "http://facebook.com/"+ id;
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(url))
                    .setContentDescription("FB SEARCH FROM USC CSCI571...")
                    .build();
            shareDialog.show(linkContent);
        }

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            CharSequence text;
            @Override
            public void onSuccess(Sharer.Result result) {
                text = "You shared this post.";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onCancel() {
                text = "You cancelled this post.";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onError(FacebookException error) {
                text = "Error posting to facebook";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
}
