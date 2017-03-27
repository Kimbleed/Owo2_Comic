package com.example.awesoman.owo2_comic.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Awesome on 2017/3/5.
 */

public class DateUtils {
    public static String getMinuteDateFormat(int time){
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        return sdf.format(new Date(time));
    }
}
