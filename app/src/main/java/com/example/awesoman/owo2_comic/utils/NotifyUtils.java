package com.example.awesoman.owo2_comic.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

/**
 * Created by Awesome on 2017/5/3.
 */

public class NotifyUtils {

    public static void showNotification(Context context,String title){
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //用户点击后自动消失
        PendingIntent pintent = null;

    }
}
