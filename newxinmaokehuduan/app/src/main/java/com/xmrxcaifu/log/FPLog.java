package com.xmrxcaifu.log;

import android.util.Log;

import com.xmrxcaifu.BuildConfig;


/**
 * Created by 77423 on 2016/11/7.
 */

public class FPLog {

    public static void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.i("FPLog", message);
        }
    }
}
