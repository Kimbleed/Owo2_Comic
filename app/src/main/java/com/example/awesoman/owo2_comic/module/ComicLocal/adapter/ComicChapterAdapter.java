package com.example.awesoman.owo2_comic.module.ComicLocal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.awesoman.owo2_comic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesome on 2016/12/16.
 * 章节页Activity 的 Adapter
 */

public class ComicChapterAdapter extends RecyclerView.Adapter<ComicChapterAdapter.ComicChapterViewHolder> {

    List<String> mData = new ArrayList<>();
    Listener listener;

    public ComicChapterAdapter(Listener listener) {
        this.listener = listener;
    }

    public interface Listener{
        void onChapterClick(String chapter);
    }

    public List<String> getmData() {
        return mData;
    }

    public void setmData(List<String> mData) {
        this.mData = mData;
    }

    @Override
    public ComicChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ComicChapterViewHolder viewHolder = new ComicChapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic_chapter,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ComicChapterViewHolder holder, final int position) {
        holder.tv.setText(mData.get(position));
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChapterClick(mData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ComicChapterViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public ComicChapterViewHolder(View itemView) {
            super(itemView);
            tv =(TextView) itemView.findViewById(R.id.tv_chapter);
        }
    }
}
