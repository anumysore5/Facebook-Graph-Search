package com.example.anumy.searchonfb;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anumy on 17-04-2017.
 */

public class Fragment_noAlbums extends Fragment {
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.no_albums_fragment, container, false);
        textView = (TextView) v.findViewById(R.id.no_albums);
        textView.setText("No albums available to display");
        return v;
    }
}
