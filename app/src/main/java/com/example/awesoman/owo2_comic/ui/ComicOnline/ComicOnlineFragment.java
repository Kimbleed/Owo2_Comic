package com.example.awesoman.owo2_comic.ui.ComicOnline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.awesoman.owo2_comic.R;

/**
 * Created by Awesome on 2017/3/3.
 */

public class ComicOnlineFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comic_online,null);
        return view;
    }
}
