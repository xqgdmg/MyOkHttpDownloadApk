package com.example.qhsj.okhttpdownloadapk.myinterface;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * Created by Chris on 2017/6/7.
 *
 */
public interface DownloadCallback {
    void update(long bytesRead, long contentLength, boolean done);

    void onFailure(Request request, IOException e);

    void onResponse(Response response) throws IOException;

}
