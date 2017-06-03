package com.example.awesoman.owo2_comic.ui.ComicLocal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.storage.ComicEntry;
import com.example.awesoman.owo2_comic.ui.BaseActivity;
import com.example.awesoman.owo2_comic.ui.ComicLocal.adapter.ChooseChapterAdapter;
import com.example.awesoman.owo2_comic.ui.ComicLocal.adapter.ChooseSurfaceAdapter;
import com.example.awesoman.owo2_comic.utils.FileManager;
import com.example.awesoman.owo2_comic.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Awesome on 2017/3/30.
 * 选取封面Activity
 */

public class ChooseSurfaceActivity extends BaseActivity
implements ChooseSurfaceAdapter.IChooseSurface{
    @Bind(R.id.rv_choose_surface)
    RecyclerView chooseSurfaceRV;

    private ChooseSurfaceAdapter adapter;
    private String chapter;
    private String path;
    private List<String> pages;
    private String mComicName;
    private Uri surfaceFile;

    @Override
    public int getContentViewID() {
        return R.layout.activity_choose_surface;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        mComicName = bundle.getString("comic_name");
        chapter = bundle.getString("chapter");
        path = bundle.getString("path");
        initRecyclerView();
    }
    public void initRecyclerView(){
        RecyclerView.LayoutManager manager = new GridLayoutManager(this,3);
        chooseSurfaceRV.setLayoutManager(manager);
        //配置Adapter
        adapter = new ChooseSurfaceAdapter(this);
        adapter.setChapterPath(path+File.separator+chapter);
        pages = FileManager.getInstance().getComicPhotoList(path,chapter);
        adapter.setPages(pages);
        adapter.setListener(this);

        chooseSurfaceRV.setAdapter(adapter);
    }

    @Override
    public void chooseSurface(int position) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        File file = new File(path+File.separator+chapter + File.separator+pages.get(position));
        Log.i("surface",file.getAbsolutePath());
        Log.i("surface",file .exists()+"");
        //获取选取图片的uri
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 120);
        intent.putExtra("aspectY", 150);
        // outputX outputY 是裁剪图片宽
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 750);

        surfaceFile = Uri.parse("file://"+"/"+path+File.separator+"surface.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, surfaceFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        Log.i("surface","end");
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("surface","back");
//        if(requestCode == 3){
//            switch (resultCode){
//                case Activity.RESULT_OK:
//                    FileUtils.copyFile(path+File.separator+"cache.jpg",path+File.separator+"surface.jpg");
//                    break;
//            }
//        }
//        if (requestCode == 3) {  //裁剪照片后处理
//            switch (resultCode) {
//                case Activity.RESULT_OK:
//                    Bitmap photo = null;
//                    try {
//                        Log.d("剪裁后的照片", surfaceFile.getPath());
//                        //photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
//                        photo = BitmapFactory.decodeFile(surfaceFile.getPath());
//                        Log.d("剪裁后的照片", surfaceFile.getPath());
//                    } catch (Exception e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//                    if (photo != null) {
//                        Log.d("tag", "photo:" + photo.toString());
//                        FileOutputStream b = null;
//                        /*图片的保存路径*/
//                        final String path = ComicEntry.getComicPath() + File.separator + mComicName + File.separator + "surface.jpg";
//                        try {
//                            b = new FileOutputStream(new File(path));
//                            photo.compress(Bitmap.CompressFormat.JPEG, 100, b);
//                            b.flush();
//                            b.close();
//                        } catch (FileNotFoundException e) {
//                            Log.d("tag", "" + e);
//                        } catch (IOException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        } finally {
//                        }
//                    } else {
//                    }
//                    break;
//
//                case Activity.RESULT_CANCELED:// 取消
//                    break;
//            }
//        }
    }
}
