package com.example.awesoman.owo2_comic.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.ui.Music.MusicService;

import java.util.Calendar;

/**
 * Created by Awesome on 2017/5/3.
 */

public class NotifyUtils {

    private static final int NOTIFICATION_FLAG = 1001;

    public static void showNotification(Context context,String title){

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.loock_logo);
        builder.setContentTitle("builder.setContentTitle");
        builder.setContentText("自定义通知栏示例");

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
//        remoteViews.setTextViewText(R.id.text,"Content");


        //继续播放
        Intent intentContinue = new Intent(MusicService.BROADCAST_ACTION);
        intentContinue.putExtra("method","play");
        PendingIntent pendingIntentContinue = PendingIntent.getBroadcast(context,1001,intentContinue,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_play,pendingIntentContinue);

        //下一首
        Intent intentNext = new Intent(MusicService.BROADCAST_ACTION);
        intentNext.putExtra("method","next");
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context,1002,intentNext,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_next,pendingIntentNext);

        //上一首
        Intent intentPrev = new Intent(MusicService.BROADCAST_ACTION);
        intentPrev.putExtra("method","next");
        PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(context,1003,intentPrev,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_prev,pendingIntentPrev);
        builder.setContent(remoteViews);
        Notification notification = builder.build();
        nm.notify(NOTIFICATION_FLAG,notification);

    }

    public static void showTextNotify(Context context,String title){
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notify = null;
        if (Build.VERSION.SDK_INT >= 11) {
            Notification.Builder builder = new Notification.Builder(context);
            notify = builder.setSmallIcon(R.drawable.loock_logo).setContentText(title).setContentTitle(title)
                    .setAutoCancel(true).setContentIntent(null).getNotification();
        } else {
            /*notify = new Notification(R.drawable.ding_icon, desc, System.currentTimeMillis());
            notify.setLatestEventInfo(context, title, desc, pintent);*/

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle(title)
                    .setSmallIcon(R.drawable.loock_logo);
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.loock_logo);
            builder.setLargeIcon(bm);
            notify = builder.build();
        }
        notify.defaults = Notification.DEFAULT_ALL;
        nm.notify((int) Calendar.getInstance().getTimeInMillis(), notify);
    }
}
