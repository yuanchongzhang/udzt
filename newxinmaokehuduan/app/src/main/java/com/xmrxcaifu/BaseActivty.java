package com.xmrxcaifu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.umeng.message.UmengNotifyClickActivity;
import com.xmrxcaifu.activity.BaseActivty2;
import com.xmrxcaifu.activity.GestureVerifyActivity;
import com.xmrxcaifu.activity.V6_Finger_Check_NewActivity;
import com.xmrxcaifu.common.LoadingDialog;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.util.ToastUtil;
import com.xmrxcaifu.view.MyLoadingDialog;
import com.xmrxcaifu.view.MyNewLoadingDialog;

/**
 * Created by shenshao on 2018/2/23.
 */
public class BaseActivty extends UmengNotifyClickActivity {

    private LoadingDialog mLoadingDialog;


    App application;
    String phone;

    MyNewLoadingDialog myNewLoadingDialog;

    MyNewLoadingDialog.Builder myNewLoadingBuilder;
    public void showLoading() {
        mLoadingDialog = new LoadingDialog(this);
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) return;
        mLoadingDialog.show();
    }

    public void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void myshowloading() {

//        dialog2.show();
        myNewLoadingDialog.show();
    }

    public void mydismissloading() {
//        dialog2.dismiss();
        myNewLoadingDialog.dismiss();
    }

    private BaseActivty oContext;
    MyLoadingDialog.Builder builder2;
    LoadingDialog dialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(BaseActivty.this);
        builder2 = new MyLoadingDialog.Builder(this)
                .setShowMessage(false)
                .setCancelable(false);
        dialog2 = builder2.create();

        myNewLoadingDialog = new MyNewLoadingDialog(BaseActivty.this);
        myNewLoadingBuilder=new MyNewLoadingDialog.Builder(BaseActivty.this);
        myNewLoadingDialog = myNewLoadingBuilder.create();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    public final String[] PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_COARSE_LOCATION};
    private OnBooleanListener onPermissionListener;
    private final String ACTION_NAME = "欢迎页失效";

    @Override
    protected void onResume() {
        super.onResume();

        try {
            phone = (String) MySharePreferenceUtil.get(BaseActivty.this, "phone1", "");
        } catch (Exception e) {

        }


        String flaggesture = (String) MySharePreferenceUtil.get(BaseActivty.this, phone + "flaggesture", "");
        String newintentflag = (String) MySharePreferenceUtil.get(BaseActivty.this, "newintentflag", "");
        String zhiwendenglu = (String) MySharePreferenceUtil.get(BaseActivty.this, phone + "zhiwendenglu", "");
//        ToastUtil.showCenterToast(this, "手势" + flaggesture + "newintentflag" + newintentflag + "指纹" + zhiwendenglu);



        if (newintentflag.equals("1")) {
            if (flaggesture.equals("1")) {
                Log.e("ggg","我是手势");
//                intent.putExtra("flag_gest", "2");
//                startActivity(new Intent(BaseActivty.this, GestureVerifyActivity.class));
                Intent intent = new Intent(BaseActivty.this, GestureVerifyActivity.class);
                intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));

                intent.putExtra("url", getIntent().getStringExtra("url"));
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in,
                        R.anim.slide_out);
                MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
                MySharePreferenceUtil.put(BaseActivty.this, "flagloading", "1");
//                finish();

                Intent mIntent = new Intent(ACTION_NAME);
                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                //发送广播
                sendBroadcast(mIntent);

            } else if (flaggesture.equals("0") && zhiwendenglu.equals("1")) {
                Log.e("ggg","我是指纹我是指纹");
                Intent intent = new Intent(BaseActivty.this, V6_Finger_Check_NewActivity.class);
                intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));
                intent.putExtra("url", getIntent().getStringExtra("url"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in,
                        R.anim.slide_out);
                MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
                MySharePreferenceUtil.put(BaseActivty.this, "flagloading", "1");
                MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
                MySharePreferenceUtil.put(BaseActivty.this, "flagloading", "1");
//                finish();

                Intent mIntent = new Intent(ACTION_NAME);
                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                //发送广播
                sendBroadcast(mIntent);

            } else if (newintentflag.equals("5")) {
                Log.e("ggg","我是什么");
                Intent intent = new Intent(BaseActivty.this, GestureVerifyActivity.class);
                intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));

                intent.putExtra("url", getIntent().getStringExtra("url"));
                startActivity(intent);

            } /*else if (newintentflag.equals("6")){
                Intent intent = new Intent(BaseActivty.this, V6_Finger_Check_NewActivity.class);
                intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));
                intent.putExtra("url", getIntent().getStringExtra("url"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in,
                        R.anim.slide_out);
                MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
                MySharePreferenceUtil.put(BaseActivty.this, "flagloading", "1");
                MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
                MySharePreferenceUtil.put(BaseActivty.this, "flagloading", "1");
//                finish();

                Intent mIntent = new Intent(ACTION_NAME);
                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                //发送广播
                sendBroadcast(mIntent);
            }*/

            else {

            }
        } else {

            if (newintentflag.equals("6")){
                if (flaggesture.equals("0") && zhiwendenglu.equals("1")) {
                    Intent intent = new Intent(BaseActivty.this, V6_Finger_Check_NewActivity.class);
                    intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));
                    intent.putExtra("url", getIntent().getStringExtra("url"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in,
                            R.anim.slide_out);
                    MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
                    MySharePreferenceUtil.put(BaseActivty.this, "flagloading", "1");
                    MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
                    MySharePreferenceUtil.put(BaseActivty.this, "flagloading", "1");
//                finish();

                    Intent mIntent = new Intent(ACTION_NAME);
                    mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                    //发送广播
                    sendBroadcast(mIntent);

                }
            }


        }




      /*  if (TextUtils.isEmpty(flaggesture)) {
            if (!shoushimima.isEmpty()) {
                Intent intent = new Intent(BaseActivty.this, GestureVerifyActivity.class);
                intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));
                intent.putExtra("url", getIntent().getStringExtra("url"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in,
                        R.anim.slide_out);
                MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
                MySharePreferenceUtil.put(BaseActivty.this, "flagloading", "1");
//                finish();

                Intent mIntent = new Intent(ACTION_NAME);
                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                //发送广播
                sendBroadcast(mIntent);
            } else if (shoushimima.isEmpty() && zhiwendenglu.equals("1")) {
                Intent intent = new Intent(BaseActivty.this, V6_Finger_Check_NewActivity.class);
                intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));
                intent.putExtra("url", getIntent().getStringExtra("url"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in,
                        R.anim.slide_out);
                MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
                MySharePreferenceUtil.put(BaseActivty.this, "flagloading", "1");
                MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
            } else if (newintentflag.equals("5")) {
                Intent intent = new Intent(BaseActivty.this, GestureVerifyActivity.class);
                intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));

                intent.putExtra("url", getIntent().getStringExtra("url"));
                startActivity(intent);

            } else {

            }


        } else {
            if (newintentflag.equals("1")) {
                if (flaggesture.equals("1")) {




                    Intent intent = new Intent(BaseActivty.this, GestureVerifyActivity.class);
                    intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));
                    intent.putExtra("url", getIntent().getStringExtra("url"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in,
                            R.anim.slide_out);
                    MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
                    MySharePreferenceUtil.put(BaseActivty.this, "flagloading", "1");
//                finish();

                    Intent mIntent = new Intent(ACTION_NAME);
                    mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                    //发送广播
                    sendBroadcast(mIntent);

                } else if (flaggesture.equals("0") && zhiwendenglu.equals("1")) {
                    Intent intent = new Intent(BaseActivty.this, V6_Finger_Check_NewActivity.class);
                    intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));
                    intent.putExtra("url", getIntent().getStringExtra("url"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in,
                            R.anim.slide_out);
                    MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
                    MySharePreferenceUtil.put(BaseActivty.this, "flagloading", "1");
                    MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
                    MySharePreferenceUtil.put(BaseActivty.this, "flagloading", "1");
//                finish();

                    Intent mIntent = new Intent(ACTION_NAME);
                    mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                    //发送广播
                    sendBroadcast(mIntent);

                } else if (newintentflag.equals("5")) {
                    Intent intent = new Intent(BaseActivty.this, GestureVerifyActivity.class);
                    intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));

                    intent.putExtra("url", getIntent().getStringExtra("url"));
                    startActivity(intent);

                } else {

                }
            } else if (newintentflag.equals("6")) {
                Intent intent = new Intent(BaseActivty.this, V6_Finger_Check_NewActivity.class);
                intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));
                intent.putExtra("url", getIntent().getStringExtra("url"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in,
                        R.anim.slide_out);
                MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");
                MySharePreferenceUtil.put(BaseActivty.this, "flagloading", "1");
                MySharePreferenceUtil.put(BaseActivty.this, "newintentflag", "2");

            }
                   }*/


    }

    public void onPermissionRequests(String permission, OnBooleanListener onBooleanListener) {
        onPermissionListener = onBooleanListener;
        Log.d("MainActivity", "0");
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            Log.d("MainActivity", "1");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                //权限已有
                onPermissionListener.onClick(true);
            } else {
                //没有权限，申请一下
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        1);
            }
        } else {
            onPermissionListener.onClick(true);
            Log.d("MainActivity", "2" + ContextCompat.checkSelfPermission(this,
                    permission));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限通过
                if (onPermissionListener != null) {
                    onPermissionListener.onClick(true);
                }
            } else {
                //权限拒绝
                if (onPermissionListener != null) {
                    onPermissionListener.onClick(false);
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public String getDeviceToken() {
        String str_token = (String) MySharePreferenceUtil.get(BaseActivty.this, "devicetoken", "");
        return str_token;

    }

    public String getdeviceId() {

        String deviceId = (String) MySharePreferenceUtil.get(BaseActivty.this, "deviceid", "");

        return deviceId;

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

}
