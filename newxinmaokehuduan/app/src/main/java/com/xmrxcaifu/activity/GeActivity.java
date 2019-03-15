package com.xmrxcaifu.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xmrxcaifu.StatusBarUtil;
import com.xmrxcaifu.util.MySharePreferenceUtil;

import java.util.List;

/**
 * 手势Activity
 *
 * @author shenshao
 */
public class GeActivity extends MyAutoLayoutActivity {

    private SharedPreferences sp;
    private TelephonyManager tm;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        StatusBarUtil.StatusBarLightMode(GeActivity.this);

    }

    protected void onStop() {
        super.onStop();
        if (isTopActivity("com.zprmb.udzt")) {
            Log.e("Ge", "在前台");
        } else {

        }
    }


    public boolean isTopActivity(String packageName) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // System.out.println("---------------包名-----------"+tasksInfo.get(0).topActivity.getPackageName());
            // 应用程序位于堆栈的顶层
            if (packageName.equals(tasksInfo.get(0).topActivity
                    .getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public String getAppVersion() {
        // 获取手机的包管理者
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packInfo = pm.getPackageInfo(getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            // 不可能发生.
            return "获取版本号失败";
        }
    }

    public boolean network() {
        boolean flag = false;

        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }

        return flag;
    }



    public String getDeviceToken() {
        String str_token = (String) MySharePreferenceUtil.get(GeActivity.this, "devicetoken", "");
        return str_token;

    }

    public String getdeviceId() {

     String deviceId= (String) MySharePreferenceUtil.get(GeActivity.this, "deviceid","");

        return deviceId;

    }
}
