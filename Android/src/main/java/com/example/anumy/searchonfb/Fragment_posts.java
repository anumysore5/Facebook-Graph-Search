package com.example.anumy.searchonfb;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by anumy on 17-04-2017.
 */

public class Fragment_posts extends Fragment {
    PostsListAdapter postsListAdapter;
    ListView listView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.posts_fragment, container, false);

        listView = (ListView) v.findViewById(R.id.posts_listview);
        Bundle bundle = this.getArguments();

        ArrayList<Posts> postsList= bundle.getParcelableArrayList("Test");
        postsListAdapter = new PostsListAdapter(getContext(), R.layout.posts_fragment ,postsList);
        listView.setAdapter(postsListAdapter);

        return v;
    }
}
