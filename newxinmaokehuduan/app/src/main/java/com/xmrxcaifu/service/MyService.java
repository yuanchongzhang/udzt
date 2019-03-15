package com.xmrxcaifu.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.xmrxcaifu.util.MySharePreferenceUtil;

public class MyService extends Service {

    Context context;
    public static final String TAG = "MyService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "in onCreate");
        context = this;
        MySharePreferenceUtil.put(context, "serviceflag", "1");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "in onStartCommand");
        Log.w(TAG, "MyService:" + this);
        String name = intent.getStringExtra("name");
        Log.w(TAG, "name:" + name);
        MySharePreferenceUtil.put(context, "serviceflag", "1");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "in onDestroy");
        MySharePreferenceUtil.put(context, "serviceflag", "2");
    }

}
