package com.example.qhsj.okhttpdownloadapk.myinterface;

/**
 * Created by Chris on 2017/6/7.
 */
public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
