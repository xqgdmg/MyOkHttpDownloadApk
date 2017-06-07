package com.example.qhsj.okhttpdownloadapk.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.example.qhsj.okhttpdownloadapk.R;
import com.example.qhsj.okhttpdownloadapk.pojo.AppVersion;
import com.example.qhsj.okhttpdownloadapk.pojo.UpdateConfig;

import java.io.File;

/**
 * Created by Chris on 2017/6/7.
 */
public class DownloadNotificationManager {

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private Context mContext;
    private final int notifityId = 10086;
    private String packageName = "com.example.qhsj.okhttpdownloadapk";
    private String path;

    public DownloadNotificationManager(Context context, AppVersion appVersion) {
        this.path = appVersion.downloadUrl;

        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mContext = context.getApplicationContext();
        mBuilder.setContentTitle("更新")
                .setContentText("努力下载更新中～")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
    }

    public void notifity(int progress) {
        if (progress == 100) {
            mBuilder.setContentText("下載完成,点击安装最新apk！")
                    .setDefaults( Notification.DEFAULT_VIBRATE)
                    //移除進度條
                    .setProgress(0, 0, false);
            setInstallPending();
            mNotifyManager.notify(notifityId, mBuilder.build());
        } else {
            mBuilder.setProgress(100, progress, false)
                    .setContentText("努力下载更新中～(" + progress + "%)");
            mNotifyManager.notify(notifityId, mBuilder.build());
        }
    }

    /**
     * 点击安装
     */
    private void setInstallPending() {
        File appFile = new File(FileUtil.getApkPath(packageName, path));
        Intent installIntent = AndroidUtil.getInstallIntent(appFile);
        if (installIntent != null) {
            PendingIntent updatePendingIntent = PendingIntent.getActivity(mContext, 0, installIntent, 0);
            mBuilder.setContentIntent(updatePendingIntent);
        }
    }


}
