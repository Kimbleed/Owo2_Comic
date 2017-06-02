package com.example.awesoman.owo2_comic.ui.ComicOnline;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.model.HttpComicChapterInfo;
import com.example.awesoman.owo2_comic.utils.FileManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesome on 2016/12/16.
 * 章节页Activity 的 Adapter
 */

public class ComicChapterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<HttpComicChapterInfo> mData = new ArrayList<>();
    IComicChapterListener IComicChapterListener;
    private String mTitle;
    private String mSurPath;
    private String mType;


    public static final String Chapter[] = new String[]{"卷","章","话"};

    public static final String Di = "第";

    private static final int HEAD_TYPE = 3;
    private static final int CHAPTER_TYPE = 1;

    private FileManager comicDBManager ;

    private Context CTX;

    public ComicChapterAdapter(Context context, IComicChapterListener IComicChapterListener) {
        CTX = context;
        this.IComicChapterListener = IComicChapterListener;
        comicDBManager = FileManager.getInstance();
    }

    public interface IComicChapterListener {
        void onChapterClick(int position);
        void onSurClick();
    }

    public List<HttpComicChapterInfo> getmData() {
        return mData;
    }

    public void setmData(List<HttpComicChapterInfo> mData) {
        this.mData = mData;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSurPath() {
        return mSurPath;
    }

    public void setmSurPath(String mSurPath) {
        this.mSurPath = mSurPath;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return HEAD_TYPE;
        }
        return CHAPTER_TYPE;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == HEAD_TYPE){
            viewHolder = new ComicChapterHeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chapter_head, parent, false));
        }
        else if(viewType == CHAPTER_TYPE) {
            viewHolder = new ComicChapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic_chapter, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(position >0) {
            ((ComicChapterViewHolder)holder).tv.setText(mData.get(position-1).getName());
            ((ComicChapterViewHolder)holder).tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IComicChapterListener.onChapterClick(position);
                }
            });
        }
        else{
            Bitmap bitmap =comicDBManager.getSurface(mSurPath);
            ((ComicChapterHeadViewHolder)holder).tv_comic_name.setText(mTitle);

            ((ComicChapterHeadViewHolder)holder).tv_comic_type.setText("种类:"+mType);
            ImageLoader.getInstance().displayImage(mSurPath,((ComicChapterHeadViewHolder)holder).iv_comic_face);
            ((ComicChapterHeadViewHolder)holder).iv_comic_face.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IComicChapterListener.onSurClick();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mData.size()+1;
    }

    class ComicChapterViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public ComicChapterViewHolder(View itemView) {
            super(itemView);
            tv =(TextView) itemView.findViewById(R.id.tv_chapter);
        }
    }

    class ComicChapterHeadViewHolder extends RecyclerView.ViewHolder{
        public ComicChapterHeadViewHolder(View itemView) {
            super(itemView);
            this.iv_comic_face = (ImageView )itemView.findViewById(R.id.iv_comic_face);
            this.tv_comic_name = (TextView)itemView.findViewById(R.id.tv_comic_name);
            this.tv_comic_type = (TextView)itemView.findViewById(R.id.tv_comic_type);
        }

        ImageView iv_comic_face;
        TextView tv_comic_name;
        TextView tv_comic_type;
    }

    public String cutOutName(String comicName){
        int indexDi=-1,indexChapter=-1;
        for(String str:Chapter) {
            if(comicName.contains(Di))
                 indexDi = comicName.lastIndexOf(Di);
            if(comicName.contains(str)) {
                indexChapter = comicName.lastIndexOf(str);
            }

            if(!(indexDi==-1 || indexChapter == -1)) {
                StringBuffer stb = new StringBuffer();
                for(int i = indexDi;i<=indexChapter;i++){
                    stb.append(comicName.charAt(i));
                }
                return stb.toString();
            }
            else if(indexDi ==-1 && indexChapter!=-1){
                StringBuffer stb = new StringBuffer();
                for(int i = indexChapter-2;i<=indexChapter;i++){
                    stb.append(comicName.charAt(i));
                }
                return stb.toString();
            }

        }
        return comicName;
    }

}
