package com.example.qhsj.okhttpdownloadapk;

import android.app.Application;
import android.content.Context;

/**
 * Created by Chris on 2017/6/7.
 */
public class MyApplicaton extends Application{
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
