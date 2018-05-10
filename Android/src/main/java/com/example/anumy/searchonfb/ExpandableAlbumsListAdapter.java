package com.example.anumy.searchonfb;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by anumy on 18-04-2017.
 */

public class ExpandableAlbumsListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> expandableListHeaders;
    private HashMap<String, ArrayList<String>> expandableListChildren;
    ImageView expandedListImageView;

    public ExpandableAlbumsListAdapter(Context context, ArrayList<Albums> albums) {
        this.context = context;
        this.expandableListHeaders = new ArrayList<>();
        this.expandableListChildren = new HashMap<>();
        for(int i=0; i<albums.size(); i++) {
            String albumName = albums.get(i).getAlbumName();
            ArrayList<String> hdImageUrls = albums.get(i).getPictureId();
            this.expandableListHeaders.add(albumName);
            this.expandableListChildren.put(albumName, hdImageUrls);
        }
    }

    @Override
    public int getGroupCount() {
        return this.expandableListHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expandableListChildren.get(expandableListHeaders.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandableListChildren.get(this.expandableListHeaders.get(groupPosition)).get(childPosition);

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.albums_list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.albums_list_group);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String hdImageUrl = (String) getChild(groupPosition, childPosition);
//        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.albums_list_item, null);
//        }
        expandedListImageView = (ImageView) convertView
                .findViewById(R.id.albums_image1);
//         expandedListImageView.setText(expandedListText);
        loadImage(hdImageUrl);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private void loadImage(final String hdImageUrl) {
        Picasso picasso = new Picasso.Builder(context)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.d(TAG, "onImageLoadFailed: ");
                        final String changedUrl = hdImageUrl.replace("http:", "https:");
                        picasso.load(changedUrl)
                                .error(R.drawable.brokenimage)
                                .placeholder(R.drawable.placeholder)
                                .into(expandedListImageView);
                    }
                })
                .build();

        picasso.load(hdImageUrl)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(expandedListImageView);
    }
}
