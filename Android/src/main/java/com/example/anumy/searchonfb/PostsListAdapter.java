package com.example.anumy.searchonfb;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by anumy on 18-04-2017.
 */

public class PostsListAdapter extends ArrayAdapter<Posts> {
    private ArrayList<Posts> objectsList;
    ImageView posts_picture;
    TextView name;
    TextView dateTime;
    TextView message;

    public PostsListAdapter(@NonNull Context context, @LayoutRes int resource, List<Posts> objects) {
        super(context, resource, objects);
        objectsList = new ArrayList<>();
        objectsList = (ArrayList<Posts>) objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View  v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.posts_individual, null);

        posts_picture = (ImageView) v.findViewById(R.id.posts_pic);
        name = (TextView) v.findViewById(R.id.posts_name);
        dateTime = (TextView) v.findViewById(R.id.posts_dateTime);
        message = (TextView) v.findViewById(R.id.posts_message);

        loadImage(objectsList.get(position).getImageUrl());

        name.setText(objectsList.get(position).getName());
        dateTime.setText(objectsList.get(position).getDateTime());
        message.setText(objectsList.get(position).getMessage());

        return v;
    }

    private void loadImage(final String imageURL) {
        Picasso picasso = new Picasso.Builder(getContext())
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.d(TAG, "onImageLoadFailed: ");
                        final String changedUrl = imageURL.replace("http:", "https:");
                        picasso.load(changedUrl)
                                .error(R.drawable.brokenimage)
                                .placeholder(R.drawable.placeholder)
                                .into(posts_picture);
                    }
                })
                .build();

        picasso.load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(posts_picture);
    }
}
