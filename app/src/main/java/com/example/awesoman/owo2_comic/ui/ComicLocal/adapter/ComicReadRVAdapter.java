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
import android.widget.TextView;

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

    private IComicReadRVClick listener;

    public IComicReadRVClick getListener() {
        return listener;
    }

    public void setListener(IComicReadRVClick listener) {
        this.listener = listener;
    }

    public ComicReadRVAdapter(Context CTX) {
        this.CTX = CTX;
        inflater = LayoutInflater.from(CTX);
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setChapterPath(String chapterPath) {
        this.chapterPath = chapterPath;
    }

    public void setPages(List<String> pages) {
        this.pages = pages;
    }

    public List<String> getPages() {
        return pages;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_comic_read_list2,null);
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
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onComicReadRvItemClick();
            }
        });
        holder.tv_page_index.setText((position+1)+"");
        pageIndex = position;
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv);
            tv_page_index = (TextView)itemView.findViewById(R.id.tv_page_index);
        }
        ImageView imageView;
        TextView tv_page_index;
    }


    public interface IComicReadRVClick{
        void onComicReadRvItemClick();
    }
}
