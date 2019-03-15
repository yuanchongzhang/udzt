package com.xmrxcaifu.activity;

import android.app.Activity;
import android.os.Bundle;

import com.xmrxcaifu.common.LoadingDialog;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.view.MyLoadingDialog;
import com.xmrxcaifu.view.MyNewLoadingDialog;


/**
 * Created by shenshao on 2018/2/23.
 */
public class MyBaseActivty extends Activity {

    private LoadingDialog mLoadingDialog;

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

    MyLoadingDialog.Builder builder2;
    LoadingDialog dialog2;


    MyNewLoadingDialog myNewLoadingDialog;

    MyNewLoadingDialog.Builder myNewLoadingBuilder;
    public void myshowloading() {

//        dialog2.show();
        myNewLoadingDialog.show();
    }

    public void mydismissloading() {
//        dialog2.dismiss();
        myNewLoadingDialog.dismiss();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder2 = new MyLoadingDialog.Builder(this)
                .setShowMessage(false)
                .setCancelable(false);
        dialog2 = builder2.create();

        myNewLoadingDialog = new MyNewLoadingDialog(MyBaseActivty.this);
        myNewLoadingBuilder=new MyNewLoadingDialog.Builder(MyBaseActivty.this);
        myNewLoadingDialog = myNewLoadingBuilder.create();
    }


    public String getDeviceToken() {
        String str_token = (String) MySharePreferenceUtil.get(MyBaseActivty.this, "devicetoken", "");
        return str_token;

    }



    public String getdeviceId() {

        String deviceId= (String) MySharePreferenceUtil.get(MyBaseActivty.this, "deviceid","");

        return deviceId;

    }
}
