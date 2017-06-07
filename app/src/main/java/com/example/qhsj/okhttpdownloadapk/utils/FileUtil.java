package com.example.qhsj.okhttpdownloadapk.utils;

import android.os.Environment;
import android.util.Log;

import com.example.qhsj.okhttpdownloadapk.MyApplicaton;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Chris on 2017/6/7.
 */
public class FileUtil {

    /**
     * 获取apk的缓存路径
     *
     * @param packageName
     * @param path
     * @return
     */
    public static String getApkPath(String packageName, String path) {
        return getApkCachePath(packageName) + "/" + getFileName(path);
    }

    //获取apk的缓存目录
    public static String getApkCachePath(String packageName) {
         // 这个需要手机有 sdcard，为什么没有内存卡的手机也是可以的。。。
         // 这个 /apk 是文件夹名字，一般 apk 的连接后面都是 .apk ，直接用链接存文件夹就可以了
        String cachePath = new File(Environment.getExternalStorageDirectory(), packageName) + "/apk";

         // 放内存是比较好的，这样不可行，会出现解析包是出现问题，因为权限问题
//        String cachePath = new File(MyApplicaton.getContext().getFilesDir(), packageName) + "/apk";
        makeRootDirectory(cachePath);
        Log.e("cachePath","cachePath==" + cachePath);
        return cachePath;
    }

    private static String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    /**
     * 保存apk文件
     */
    public static boolean saveApk(Response response, String packageName, String path) {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            File file = new File(getApkPath(packageName, path));
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

}
