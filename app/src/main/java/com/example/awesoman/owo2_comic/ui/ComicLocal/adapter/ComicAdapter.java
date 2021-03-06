package com.example.awesoman.owo2_comic.ui.ComicLocal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.awesoman.owo2_comic.model.ComicHistoryInfo;
import com.example.awesoman.owo2_comic.model.ComicInfo;
import com.example.awesoman.owo2_comic.storage.ComicHistoryDao;
import com.example.awesoman.owo2_comic.utils.FileManager;
import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.utils.FileUtils;
import com.example.awesoman.owo2_comic.utils.MyLogger;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesome on 2016/12/14.
 * 漫画本地书架Fragment 的 Adapter
 */

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {
    public static final String TAG = "ComicAdapter";

    public static final String Chapter[] = new String[]{"卷","章","话"};

    public static final String Di = "第";

    private List<ComicInfo> mData = new ArrayList<>();
    private IComicHome listener;
    private FileManager comicDBManager;
    private Context context;
    //checkbox是否显示中(true:显示中  false:不可见)
    private boolean checkBoxIsVisible = false;
    //所有checkbox 标签
    private List<Boolean> checkBoxList = new ArrayList<>();

    private ComicHistoryDao mComicHistoryDao;

    public ComicAdapter(Context context, IComicHome listener) {
        this.listener = listener;
        comicDBManager = FileManager.getInstance();
        this.context = context;
        mComicHistoryDao = new ComicHistoryDao(context);
    }

    //获取 checkbox为ture的 漫画名List
    public List<String> choseForDelete() {
        List<String> choseForDelete = new ArrayList<>();
        for (int i = 0; i < checkBoxList.size(); i++) {
            if (checkBoxList.get(i))
                choseForDelete.add(mData.get(i).getComicPath());
        }
        return choseForDelete;
    }

    //将checkBoxList中所有值置统一
    public void setAllCheck(boolean flag) {
        int length = checkBoxList.size();
        for (int i = 0; i < length; i++) {
            checkBoxList.set(i, flag);
        }
        notifyDataSetChanged();
    }


    public boolean isCheckBoxIsVisible() {
        return checkBoxIsVisible;
    }

    public void setCheckBoxIsVisible(boolean checkBoxIsVisible) {
        this.checkBoxIsVisible = checkBoxIsVisible;
        notifyDataSetChanged();
    }

    public List<ComicInfo> getmData() {
        return mData;
    }

    public void setData(List<ComicInfo> mData) {
        this.mData = mData;
    }

    @Override
    public ComicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ComicViewHolder viewHolder = new ComicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic_list, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ComicViewHolder holder, final int position) {
        holder.tv_name.setText(mData.get(position).getComicName());
        holder.tv_type.setText(FileManager.getInstance().getComicTypeNameById(mData.get(position).getComicType()));
        holder.checkBox.setChecked(checkBoxList.get(position));

        final ComicHistoryInfo historyInfo = mComicHistoryDao.get(mData.get(position).getComicName());
        if (historyInfo != null) {
            holder.tv_history.setText("续看:" + cutOutName(historyInfo.getComicChapter()) + " - " + (historyInfo.getComicPage() + 1) + "页");
        } else {

        }
        holder.continuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (historyInfo != null) {
                    listener.onContinueClick(mData.get(position), historyInfo.getComicChapter(), historyInfo.getComicPage());
                } else {
                    listener.onContinueClick(mData.get(position), null, 0);
                }
            }
        });
        if (!checkBoxIsVisible) {
            holder.checkBox.setVisibility(View.GONE);
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onComicHomeItemClick(position);
                }
            });
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkBox.setChecked(!holder.checkBox.isChecked());
                    checkBoxList.set(position, holder.checkBox.isChecked());
                }
            });
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBoxList.set(position, holder.checkBox.isChecked());
                }
            });
        }

        //考虑用imageLoader替换/或glide
        Bitmap bitmap = comicDBManager.getSurface(mData.get(position).getComicPath());
        if (bitmap == null) {
            File fileDir = new File(mData.get(position).getComicPath());
            File[] dirs = fileDir.listFiles();
            if (dirs != null)
                for (int i = 0; i < dirs.length; i++) {
                    boolean isMake = false;
                    if (dirs[i].isDirectory()) {
                        File[] picts = dirs[i].listFiles();
                        for (int j = 0; j < picts.length; j++) {
                            if (FileUtils.judgePicOrVideo(picts[j]) == 2) {
                                try {
                                    MyLogger.ddLog(TAG).i("makeSurface -- start");
                                    FileUtils.copyFile(picts[j].getAbsolutePath(), mData.get(position).getComicPath() + File.separator + "surface.jpg");
                                    MyLogger.ddLog(TAG).i("makeSurface -- end");
                                    isMake = true;
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (isMake)
                            break;
                    }
                }
        }
        bitmap = comicDBManager.getSurface(mData.get(position).getComicPath());
        holder.img.setImageBitmap(comicDBManager.makeSurface(bitmap, context.getResources().getDimensionPixelSize(R.dimen.surface_comic_list_width), context.getResources().getDimensionPixelSize(R.dimen.surface_comic_list_height)));
    }

    @Override
    public int getItemCount() {
        checkBoxList.clear();
        for (int i = 0; i < mData.size(); i++) {
            checkBoxList.add(false);
        }
        return mData.size();
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

    class ComicViewHolder extends RecyclerView.ViewHolder {
        public ComicViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            img = (ImageView) itemView.findViewById(R.id.iv_comic_face);
            tv_name = (TextView) itemView.findViewById(R.id.tv_comic_name);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            tv_type = (TextView) itemView.findViewById(R.id.tv_comic_type);
            tv_history = (TextView) itemView.findViewById(R.id.historyComicTxt);
            continuBtn = (ImageView) itemView.findViewById(R.id.readComicBtn);
        }

        View container;
        ImageView img;
        TextView tv_name;
        TextView tv_history;
        TextView tv_type;
        CheckBox checkBox;
        ImageView continuBtn;
    }

    public interface IComicHome {
        void onComicHomeItemClick(int position);

        void onContinueClick(ComicInfo info, String chapter, int page);
    }
}
