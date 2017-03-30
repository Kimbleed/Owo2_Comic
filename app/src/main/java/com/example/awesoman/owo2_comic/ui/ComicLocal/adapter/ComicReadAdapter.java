package com.example.awesoman.owo2_comic.ui.ComicLocal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.awesoman.owo2_comic.utils.MyImageLoader;

import java.io.File;
import java.util.List;

/**
 * Created by Awesome on 2016/12/15.
 *
 * 漫画阅读Activity的PageAdapter
 */

public class ComicReadAdapter extends PagerAdapter {


    public ComicReadAdapter(Context context) {
        this.CTX = context;
        imageLoader = MyImageLoader.getInstance();
    }

    private MyImageLoader imageLoader;
    private String chapterPath;
    private List<String> pages;
    Context CTX;

    public String getChapterPath() {
        return chapterPath;
    }

    public void setChapterPath(String chapterPath) {
        this.chapterPath = chapterPath;
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = pages;
    }

    @Override
    public Object instantiateItem(final ViewGroup container,final  int position) {
        final ImageView view = new ImageView(container.getContext());
//            view.setImageBitmap(imageLoader.getBitmapByPath(chapterPath+File.separator+pages));
            new AsyncTask<Void,Void,Bitmap>(){
                @Override
                protected Bitmap doInBackground(Void... params) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap bitmap = BitmapFactory.decodeFile(chapterPath+File.separator+pages.get(position),options);
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    view.setImageBitmap(bitmap);
                }
            }.execute();
            container.addView(view);
            return view;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

}
