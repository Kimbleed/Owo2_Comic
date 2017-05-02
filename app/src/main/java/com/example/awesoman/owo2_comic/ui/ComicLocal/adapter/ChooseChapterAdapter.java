package com.example.awesoman.owo2_comic.ui.ComicLocal.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.awesoman.owo2_comic.R;

import java.util.List;

/**
 * Created by Awesome on 2017/3/30.
 */

public class ChooseChapterAdapter extends BaseAdapter {

    private List<String> mData ;
    private LayoutInflater inflater;
    private IChooseChapter listener;

    public void setListener(IChooseChapter listener) {
        this.listener = listener;
    }

    public ChooseChapterAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setmData(List<String> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyHolder holder = null;
        if(convertView ==null){
            convertView =  inflater.inflate(R.layout.item_choose_chapter,null);
            holder = new MyHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (MyHolder)convertView.getTag();
        }
        holder.nameTxt .setText(mData.get(position));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.chooseChapter(position);
            }
        });
        return convertView;
    }

    class MyHolder{
        public MyHolder(View view) {
            this.view = view;
            nameTxt = (TextView) view.findViewById(R.id.name);
        }

        View view;
        TextView nameTxt;
    }

    public interface IChooseChapter{
        void chooseChapter(int position);
    }

}
