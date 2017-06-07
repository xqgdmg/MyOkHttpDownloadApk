package com.example.qhsj.okhttpdownloadapk.utils;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * Created by Chris on 2017/6/7.
 */
public class AndroidUtil {

    /**
     * 獲取apk安裝的intent
     *
     * @param apkfile
     * @return
     */
    public static Intent getInstallIntent(File apkfile) {
        if (!apkfile.exists()) {
            return null;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(apkfile);
        i.setDataAndType(uri, "application/vnd.android.package-archive");
        Log.e("haha","AndroidUtil getInstallIntent");
        return i;
    }


}
