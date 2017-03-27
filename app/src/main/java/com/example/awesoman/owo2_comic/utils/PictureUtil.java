package com.example.awesoman.owo2_comic.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesome on 2016/12/17.
 */

public class PictureUtil {

    private static PictureUtil pictureUtil;

    public static PictureUtil getInstance(){
        if(pictureUtil == null){
            pictureUtil = new PictureUtil();
        }
        return pictureUtil;
    }

    /**
     * 压缩图片
     *
     * @param bitmap
     * @param newHeight
     * @return
     */
    public Bitmap compressImg(Bitmap bitmap, double newHeight) {

        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        //创建操作图片用的Matrix对象
        Matrix matrix = new Matrix();
        float scaleHeight = ((float) newHeight) / height;
        float scaleWidth = scaleHeight;
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int)width,
                (int)height, matrix, true);
        return newBitmap;
    }


    public Bitmap cripPicture(Bitmap bitmap,int newWidth){
        bitmap = bitmap.createBitmap(bitmap,0,0,newWidth,bitmap.getHeight());
        return bitmap;
    }


    /**
     * 分割图片
     *
     * @param bitmap
     * @param xPiece
     * @param yPiece
     * @return
     */
    public List<Bitmap> split(Bitmap bitmap, int xPiece, int yPiece) {
        List<Bitmap> pieces = new ArrayList<Bitmap>(xPiece * yPiece);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = width / xPiece;
        int pieceHeight = height / yPiece;
        for (int i = 0; i < yPiece; i++) {
            for (int j = 0; j < xPiece; j++) {
                int xValue = j * pieceWidth;
                int yValue = i * pieceHeight;
                Bitmap b;
                if (i != 0)
                    b = Bitmap.createBitmap(bitmap, xValue, yValue + 10,
                            pieceWidth, pieceHeight - 10);
                else
                    b = Bitmap.createBitmap(bitmap, xValue, yValue,
                            pieceWidth, pieceHeight);

//                pieces.add(compressImg(b, b.getWidth(), SCREEN_HEIGHT / 2));
            }
        }
        return pieces;
    }

}
