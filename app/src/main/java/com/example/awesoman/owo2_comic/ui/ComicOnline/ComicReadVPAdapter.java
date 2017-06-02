package com.example.awesoman.owo2_comic.ui.ComicOnline;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.model.HttpImageInfo;
import com.example.awesoman.owo2_comic.view.ImageControl;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesome on 2016/12/15.
 * <p>
 * 漫画阅读Activity的PageAdapter
 */

public class ComicReadVPAdapter extends PagerAdapter {


    public ComicReadVPAdapter(Context context) {
        this.CTX = context;
        inflater = LayoutInflater.from(context);
        screenW = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        screenH = ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
        Rect frame = new Rect();
        ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
    }

    private List<HttpImageInfo> pages = new ArrayList<HttpImageInfo>();
    Context CTX;
    private int pageIndex;
    private LayoutInflater inflater ;

    private int screenW;
    private int screenH;
    private int statusBarHeight;

    private IComicReadVPListener comicReadVPListener;



    public void setComicReadVPListener(IComicReadVPListener comicReadVPListener) {
        this.comicReadVPListener = comicReadVPListener;
    }

    public List<HttpImageInfo> getPages() {
        return pages;
    }

    public void setPages(List<HttpImageInfo> pages) {
        this.pages = pages;
    }


    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
         View convertView = inflater.inflate(R.layout.item_comic_read_list,null);
//        final ImageControl view = new ImageControl(container.getContext());
        final ImageControl view = (ImageControl) convertView.findViewById(R.id.iv);
        TextView tv_page_index = (TextView)convertView.findViewById(R.id.tv_page_index);
//            view.setImageBitmap(imageLoader.getBitmapByPath(chapterPath+File.separator+pages));

        view.setClickable(true);

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                BitmapFactory.Options options = new BitmapFactory.Options();
//                Bitmap bitmap = BitmapFactory.decodeFile(chapterPath + File.separator + pages.get(position), options);
                Bitmap bitmap = ImageLoader.getInstance().loadImageSync(pages.get(position).getImageUrl());
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                view.imageInit(bitmap, screenW, screenH, statusBarHeight,
                        new ImageControl.ICustomMethod() {

                            @Override
                            public void customMethod(Boolean currentStatus) {
                                comicReadVPListener.doubleClick(view.isBig);
                            }
                        });

                view.setImageBitmap(bitmap);
            }
        }.execute();
        container.addView(convertView);
        pageIndex = position;
        comicReadVPListener.pageOn(pageIndex);
        tv_page_index.setText((pageIndex+1)+"");

        return convertView;
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

    public interface IComicReadVPListener {
        void doubleClick(boolean isBig);
        void pageOn(int pageIndex);
    }

}
