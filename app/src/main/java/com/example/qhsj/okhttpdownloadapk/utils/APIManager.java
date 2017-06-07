package com.example.qhsj.okhttpdownloadapk.utils;

import java.io.IOException;

/**
 * Created by Chris on 2017/6/7.
 *
 */
import com.example.qhsj.okhttpdownloadapk.ProgressResponseBody;
import com.example.qhsj.okhttpdownloadapk.myinterface.DownloadCallback;
import com.example.qhsj.okhttpdownloadapk.myinterface.ProgressListener;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class APIManager {

    /**
     * @param downloadUrl      下載地址
     * @param downloadCallback 下載回調
     */
    public static OkHttpClient downloadApk(String downloadUrl, final DownloadCallback downloadCallback) {

        ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                downloadCallback.update(bytesRead, contentLength, done);
            }
        };

        Callback callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                downloadCallback.onFailure(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                downloadCallback.onResponse(response);
            }
        };

        OkHttpClient oktttpClient = downloadApk(downloadUrl, progressListener, callback);

        return oktttpClient;
    }

    /**
     * @param downloadUrl      下載地址
     * @param progressListener 進度監聽
     * @param callback         請求回調
     */
    private static OkHttpClient downloadApk(String downloadUrl, final ProgressListener progressListener, final Callback callback) {
        OkHttpClient oktttpClient = new OkHttpClient();

        //添加拦截器，自定义ResponseBody，添加下载进度
        oktttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(
                        new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        });

        //封装请求
        Request request = new Request.Builder()
                //下载地址
                .url(downloadUrl)
                .build();

        //发送异步请求
        oktttpClient.newCall(request).enqueue(callback);

        return oktttpClient;
    }
}
