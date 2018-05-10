package com.example.anumy.searchonfb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by anumy on 22-04-2017.
 */

public class FavoritesAdapter extends ArrayAdapter<Favorite> {

    private ArrayList<Favorite> objectsList = new ArrayList<>();
    TextView textView;
    ImageView profile_picture;
    ImageView favorite_btn;
    ImageView details_btn;
    public static final String ROW_ID = "row_id";
    public static final String ROW_NAME = "row_name";
    public static final String ROW_IMAGE = "row_image";
    public static final String ROW_TYPE = "row_type";

    public FavoritesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Favorite> objects) {
        super(context, resource, objects);
        objectsList = (ArrayList<Favorite>) objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final int rowPosition = position;
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.row, null);

        textView = (TextView) v.findViewById(R.id.name);
        profile_picture = (ImageView) v.findViewById(R.id.profile_pic);
        favorite_btn = (ImageView) v.findViewById(R.id.fav_btn);
        details_btn = (ImageView) v.findViewById(R.id.details_btn);

        // set textview
        textView.setText(objectsList.get(position).getName());

        // load image for profile picture
        loadImage(objectsList.get(position).getImageUrl());

        // set favorite button
        Drawable drawable_favBtn = getContext().getResources().getDrawable(R.drawable.favorite_on);
        favorite_btn.setImageDrawable(drawable_favBtn);

        // set details button
        Drawable drawable_detailsBtn = getContext().getResources().getDrawable(R.drawable.result_activity_details_btn);
        details_btn.setImageDrawable(drawable_detailsBtn);

        details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnId = objectsList.get(rowPosition).getId();
                String name = objectsList.get(rowPosition).getName();
                String imageUrl = objectsList.get(rowPosition).getImageUrl();
                String type = objectsList.get(rowPosition).getType();
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra(ROW_ID, btnId);
                intent.putExtra(ROW_NAME, name);
                intent.putExtra(ROW_IMAGE, imageUrl);
                intent.putExtra(ROW_TYPE, type);
                getContext().startActivity(intent);
            }
        });

        return v;
    }

    private void loadImage(final String imageURL) {
        Picasso picasso = new Picasso.Builder(getContext())
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.d(TAG, "onImageLoadFailed: ");
                        picasso.load(R.drawable.brokenimage)
                                .into(profile_picture);
                    }
                })
                .build();

        picasso.load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(profile_picture);
    }
}
