package com.example.awesoman.owo2_comic.ui.Music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.awesoman.owo2_comic.R;

import java.util.List;

/**
 * Created by Awesome on 2017/3/3.
 * MusicFragment中的Adapter
 */

public class MusicAdapter extends BaseAdapter {
    List<String> data;
    LayoutInflater inflater = null;
    IMusicItem listener;

    public MusicAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void setListener(IMusicItem listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MusicItemHolder holder = null;
        if(convertView ==null){
            convertView = inflater.inflate(R.layout.item_music,null);
            holder = new MusicItemHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (MusicItemHolder) convertView.getTag();
        }
        holder.musicNameTxt.setText(data.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMusicItemClick(position);
            }
        });
        return convertView;
    }

    class MusicItemHolder {
        public MusicItemHolder(View itemView) {
            this.itemView = itemView;
            this.musicSurfaceImg = (ImageView)itemView.findViewById(R.id.musicSurfaceImg);
            this.musicNameTxt = (TextView)itemView.findViewById(R.id.musicNameTxt);
        }
        View itemView;
        ImageView musicSurfaceImg;
        TextView musicNameTxt;
    }

    interface IMusicItem{
        void onMusicItemClick(int index);
    }
}
