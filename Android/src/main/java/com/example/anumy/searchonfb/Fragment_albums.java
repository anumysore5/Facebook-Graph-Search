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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anumy on 17-04-2017.
 */

public class Fragment_albums extends Fragment {
    ExpandableAlbumsListAdapter expandableAlbumsListAdapter;
    ExpandableListView expandableListView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.albums_fragment, container, false);

        expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListview);
        Bundle bundle = this.getArguments();

        ArrayList<Albums> albumsList= bundle.getParcelableArrayList("Test");
        expandableAlbumsListAdapter = new ExpandableAlbumsListAdapter(getContext(),albumsList);
        expandableListView.setAdapter(expandableAlbumsListAdapter);

        return v;
    }
}
