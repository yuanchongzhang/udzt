package com.xmrxcaifu.activity;

import android.os.Bundle;

import com.xmrxcaifu.common.LoadingDialog;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.view.MyLoadingDialog;
import com.xmrxcaifu.view.MyNewLoadingDialog;


/**
 * Created by shenshao on 2018/2/23.
 */
public class BaseActivty2 extends MyAutoLayoutActivity {

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

        myNewLoadingDialog = new MyNewLoadingDialog(BaseActivty2.this);
        myNewLoadingBuilder=new MyNewLoadingDialog.Builder(BaseActivty2.this);
        myNewLoadingDialog = myNewLoadingBuilder.create();
    }


    public String getDeviceToken() {
        String str_token = (String) MySharePreferenceUtil.get(BaseActivty2.this, "devicetoken", "");

        return str_token;

    }



    public String getdeviceId() {

        String deviceId= (String) MySharePreferenceUtil.get(BaseActivty2.this, "deviceid","");

        return deviceId;

    }
}
