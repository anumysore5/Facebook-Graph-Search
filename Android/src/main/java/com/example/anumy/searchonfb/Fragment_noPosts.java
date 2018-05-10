package com.example.anumy.searchonfb;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by anumy on 17-04-2017.
 */

public class Fragment_noPosts extends Fragment {
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.no_posts_fragment, container, false);
        textView = (TextView) v.findViewById(R.id.no_posts);
        textView.setText("No posts available to display");
        return v;
    }
}
