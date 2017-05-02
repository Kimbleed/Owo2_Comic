package com.example.awesoman.owo2_comic.ui.ComicLocal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.awesoman.owo2_comic.R;

import java.io.File;
import java.util.List;

/**
 * Created by Awesome on 2017/3/31.
 */

public class ChooseSurfaceAdapter extends RecyclerView.Adapter<ChooseSurfaceAdapter.MyHolder> {

    private List<String> pages;
    private String chapterPath;
    private IChooseSurface listener;
    private LayoutInflater inflater;

    public void setListener(IChooseSurface listener) {
        this.listener = listener;
    }

    public ChooseSurfaceAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setChapterPath(String chapterPath) {
        this.chapterPath = chapterPath;
    }

    public void setPages(List<String> pages) {
        this.pages = pages;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(inflater.inflate(R.layout.item_choose_surface,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        new AsyncTask<Void,Void,Bitmap>(){
            @Override
            protected Bitmap doInBackground(Void... params) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(chapterPath+ File.separator+pages.get(position),options);
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                holder.img.setImageBitmap(bitmap);
            }
        }.execute();

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.chooseSurface(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public MyHolder(View itemView) {
            super(itemView);
            view = itemView;
            img = (ImageView)view.findViewById(R.id.chooseSurfaceImg);
        }
        View view;
        ImageView img ;
    }

    public interface IChooseSurface{
        void chooseSurface(int position);
    }
}
