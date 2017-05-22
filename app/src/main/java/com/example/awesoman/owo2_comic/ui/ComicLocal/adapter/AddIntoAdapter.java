package com.example.awesoman.owo2_comic.ui.ComicLocal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.model.ComicBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesome on 2017/3/1.
 * 加入漫画到种类下的list 的 adapter
 */

public class AddIntoAdapter extends BaseAdapter {
    List<ComicBean> data = new ArrayList<>();
    LayoutInflater inflater = null;
    boolean[] flagArr =null;

    public AddIntoAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<ComicBean> data) {
        this.data = data;
        flagArr = new boolean[data.size()];
    }

    public List<ComicBean> getChoseComicList(){
        List<ComicBean> chose = new ArrayList<>();
        for(int i = 0;i<flagArr.length;i++)
            if(flagArr[i])
                chose.add(data.get(i));
        return chose;
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
        MyViewHolder holder = null;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_add_comic_into,null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.tv.setText(data.get(position).getComicName());
        holder.cb.setChecked(flagArr[position]);
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagArr[position] = !flagArr[position];
            }
        });
        return convertView;
    }



    class MyViewHolder{
        public MyViewHolder(View view) {
            tv = (TextView)view.findViewById(R.id.comicNameTxt);
            cb = (CheckBox)view.findViewById(R.id.comicChoseCK);
        }
        TextView tv;
        CheckBox cb;
    }
}
