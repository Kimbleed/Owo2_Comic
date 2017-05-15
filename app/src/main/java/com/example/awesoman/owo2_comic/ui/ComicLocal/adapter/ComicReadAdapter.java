package com.example.awesoman.owo2_comic.ui.ComicLocal.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.awesoman.owo2_comic.utils.MyImageLoader;
import com.example.awesoman.owo2_comic.view.ImageControl;

import java.io.File;
import java.util.List;

/**
 * Created by Awesome on 2016/12/15.
 * <p>
 * 漫画阅读Activity的PageAdapter
 */

public class ComicReadAdapter extends PagerAdapter {


    public ComicReadAdapter(Context context) {
        this.CTX = context;
        imageLoader = MyImageLoader.getInstance();

        screenW = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        screenH = ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
        Rect frame = new Rect();
        ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
    }

    private MyImageLoader imageLoader;
    private String chapterPath;
    private List<String> pages;
    Context CTX;

    private int screenW;
    private int screenH;
    private int statusBarHeight;

    private IDoubleClick listener;

    public void setListener(IDoubleClick listener) {
        this.listener = listener;
    }

    String[] imagType = {".jpg", ".png", ".bmp", ".gif", ".jpeg", ".tif", ".ico"};


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
        for(int i  = 0;i<pages.size();i++){
            if(!judgePhoto(pages.get(i))){
                pages.remove(i);
            }

        }
        this.pages = pages;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final ImageControl view = new ImageControl(container.getContext());
//            view.setImageBitmap(imageLoader.getBitmapByPath(chapterPath+File.separator+pages));

        view.setClickable(true);

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

                view.imageInit(bitmap, screenW, screenH, statusBarHeight,
                        new ImageControl.ICustomMethod() {

                            @Override
                            public void customMethod(Boolean currentStatus) {
                                listener.doubleClick();
                            }
                        });

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
        container.removeView((View) object);
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

    public interface IDoubleClick{
        void doubleClick();
    }

}
