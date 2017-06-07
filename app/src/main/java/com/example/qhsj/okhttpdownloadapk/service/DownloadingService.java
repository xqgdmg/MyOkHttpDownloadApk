package com.example.qhsj.okhttpdownloadapk.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.qhsj.okhttpdownloadapk.myinterface.DownloadCallback;
import com.example.qhsj.okhttpdownloadapk.pojo.AppVersion;
import com.example.qhsj.okhttpdownloadapk.pojo.UpdateConfig;
import com.example.qhsj.okhttpdownloadapk.utils.APIManager;
import com.example.qhsj.okhttpdownloadapk.utils.AndroidUtil;
import com.example.qhsj.okhttpdownloadapk.utils.DownloadNotificationManager;
import com.example.qhsj.okhttpdownloadapk.utils.FileUtil;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;


/**
 * Created by Chris on 2017/6/7.
 */
public class DownloadingService extends Service{

    private int progress = 0;
    private DownloadNotificationManager downloadNotificationManager;
    private boolean downloading = false;

    Handler notifityHandler = new Handler() {
        public void handleMessage(Message msg) {
            downloadNotificationManager.notifity(msg.what);
            super.handleMessage(msg);
        }

    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("haha","onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);  // 这个不能 super

        Log.e("haha","onStartCommand");
        AppVersion appVersion = (AppVersion) intent.getSerializableExtra("APP_VERSION");
        downloadNotificationManager = new DownloadNotificationManager(this,appVersion);
        startDownload(appVersion);
        return START_STICKY;
    }

    private void startDownload( final AppVersion appVersion) {
        Log.e("haha","startDownload");
        if (downloading) {
            return;
        }
        downloading = true;
        APIManager.downloadApk(appVersion.downloadUrl, new DownloadCallback() {
            @Override
             // 如果链接不是可以下载的文件，contentLength 是 -1
            public void update(long bytesRead, long contentLength, boolean done) {
//                Log.e("haha","bytesRead==" + bytesRead);
//                Log.e("haha","contentLength==" + contentLength);
//                Log.e("haha","done==" + done);
//                Log.e("haha","downloadUrl==" + appVersion.downloadUrl);
                Log.e("DownloadingService", String.format("%d%% done\n", (100 * bytesRead) / contentLength));
                int mProgress = (int) ((100 * bytesRead) / contentLength);
                if (Math.abs(mProgress - progress) >= 5 || (mProgress != progress && mProgress == 100)) {
                    progress = mProgress;
                    notifityHandler.sendEmptyMessage(progress);
                    if (progress == 100) {
                        String file = FileUtil.getApkPath("com.example.qhsj.okhttpdownloadapk",appVersion.downloadUrl);
                        installApk(new File(file));
                    }
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("DownloadingService", "请求下载失败");
                downloading = false;
                DownloadingService.this.stopSelf();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                 // 这个连接成功马上回执行一次
                Log.e("DownloadingService", "onResponse");
                if (response.isSuccessful()) {
                    notifityHandler.sendEmptyMessage(0);
                    FileUtil.saveApk(response, "com.example.qhsj.okhttpdownloadapk", appVersion.downloadUrl);
                } else {
                    throw new IOException("Unexpected code " + response);
                }
                downloading = false;
            }

        });
    }


    /**
     * 安装apk
     */
    private void installApk(File apkfile) {
        Log.e("haha","installApk");
        Intent installIntent = AndroidUtil.getInstallIntent(apkfile);
        this.startActivity(installIntent);
        DownloadingService.this.stopSelf();
    }


}
