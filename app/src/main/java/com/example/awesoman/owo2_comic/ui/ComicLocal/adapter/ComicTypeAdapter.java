package com.example.awesoman.owo2_comic.ui.ComicLocal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.model.ComicTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesome on 2016/12/14.
 */

public class ComicTypeAdapter extends RecyclerView.Adapter<ComicTypeAdapter.ComicTypeViewHolder> {

    private Context context ;
    private IComicType listener;
    private boolean checkBoxIsVisible;

    private List<ComicTypeBean> mData = new ArrayList<>();

    public ComicTypeAdapter(Context context,IComicType listener){
        this.context = context;
        this.listener = listener;
    }

    public List<ComicTypeBean> getmData() {
        return mData;
    }

    public void setmData(List<ComicTypeBean> mData) {
        this.mData = mData;
    }

    public boolean isCheckBoxIsVisible() {
        return checkBoxIsVisible;
    }

    public void setCheckBoxIsVisible(boolean checkBoxIsVisible) {
        this.checkBoxIsVisible = checkBoxIsVisible;
        notifyDataSetChanged();
    }

    @Override
    public ComicTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ComicTypeViewHolder viewHolder = new ComicTypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_list,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ComicTypeViewHolder holder,final int position) {
        holder.tv.setText(mData.get(position).getComicTypeName());
        if(checkBoxIsVisible)
            holder.itemDeleteBtn.setVisibility(View.VISIBLE);
        else
            holder.itemDeleteBtn.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkBoxIsVisible)
                    listener.onComicTypeItemClick(position);
                else {
                    listener.onComicTypeDelete(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ComicTypeViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView tv;
        ImageView itemDeleteBtn;
        boolean flag = false;
        public ComicTypeViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tv = (TextView)itemView.findViewById(R.id.tv_comic_type);
            this.itemDeleteBtn = (ImageView)itemView.findViewById(R.id.itemDeleteBtn);
        }
    }

    public interface IComicType{
        void onComicTypeItemClick(int position);
        void onComicTypeDelete(int position);
    }
}
