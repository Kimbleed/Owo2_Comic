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
import com.example.awesoman.owo2_comic.view.ImageControl;

import java.io.File;
import java.util.List;

/**
 * Created by Awesome on 2017/5/15.
 */

public class ComicReadRVAdapter extends RecyclerView.Adapter<ComicReadRVAdapter.MyViewHolder> {

    private String chapterPath;
    private List<String> pages;

    private Context CTX;
    private LayoutInflater inflater;
    private int pageIndex;


    String[] imagType = {".jpg", ".png", ".bmp", ".gif", ".jpeg", ".tif", ".ico"};

    public ComicReadRVAdapter(Context CTX) {
        this.CTX = CTX;
        inflater = LayoutInflater.from(CTX);
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setChapterPath(String chapterPath) {
        this.chapterPath = chapterPath;
    }

    public void setPages(List<String> pages) {
        pageIndex = 0;
        this.pages = pages;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_comic_read_list,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(chapterPath + File.separator + pages.get(position), options);

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                holder.imageView.setImageBitmap(bitmap);
            }
        }.execute();
        pageIndex = position;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView = (ImageView) itemView.findViewById(R.id.iv);
        }
        ImageView imageView;
    }

    /**
     * 判断1.视频 或  2.图片
     */
    public boolean judgePhoto(String str) {
        for (String type : imagType) {
            if (str.contains(type))
                return true;
        }
        return false;
    }
}
