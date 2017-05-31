package com.example.awesoman.owo2_comic.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Awesome on 2017/2/26.
 */

public class FileUtils {

    public static String[] imagType = {".jpg", ".png", ".bmp", ".gif", ".jpeg", ".tif", ".ico"};
    public static String[] videoType = {".mp4",".avi",".rm"};

    //设置视频第一帧图片
    public static void setLocalVideoSur(Context context, final String path, final ImageView iv) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                //设置资源位置
                mmr.setDataSource(path);

                //获取第一帧图像的bitmap对象
                Bitmap bitmap = mmr.getFrameAtTime();

                iv.setImageBitmap(bitmap);
            }
        });
    }

    /**
     * 判断1.视频 或  2.图片  0其他文件
     */
    public static int judgePicOrVideo(File file) {
        String fileName = file.getName();
        for (String type : imagType) {
            if (fileName.contains(type))
                return 2;
        }
        for(String type:videoType){
            if(fileName.contains(type)){
                return 1;
            }
        }
        return 0;
    }

    /**
     * 删除文件夹里的全部文件
     *
     * @param file
     */
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param filePath
     * @param deleteThisPath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!StringUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除某个文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        if (!StringUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isFile()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取文件夹的大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSize(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            size = size + flist[i].length();
        }
        return size;
    }

    public static long getFileSize(String path) throws Exception {
        long size = 0;
        if (!StringUtils.isEmpty(path)) {
            File file = new File(path);
            File flist[] = file.listFiles();
            for (int i = 0; i < flist.length; i++) {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 复制文件到某一目录下
     * @param in
     * @param path
     */
    public static void copyFile(InputStream in,String fileName,String path){
        try {
            File outFile = new File(path,fileName);
            OutputStream out = new FileOutputStream(outFile);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            BufferedInputStream bis = new BufferedInputStream(in);
            byte[] buffer = new byte[1024];
            int length = 0;
            while((length = bis.read(buffer))>0){
                bos.write(buffer,0,length);
                bos.flush();
            }
            bos.close();
            bis.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 判断1.视频 或  2.图片
     */
    public static boolean judgePhoto(String str) {
        for (String type : imagType) {
            if (str.contains(type))
                return true;
        }
        return false;
    }
}
