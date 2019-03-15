package com.xmrxcaifu.http.callback;


import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Window;


import com.xmrxcaifu.http.convert.StringConvert;
import com.xmrxcaifu.http.request.BaseRequest;

import okhttp3.Call;
import okhttp3.Response;

/**
 * ================================================
 * 作    者：张宇
 * 版    本：1.0
 * 创建日期：2016/9/11
 * 描    述：返回字符串类型的数据
 * 修订历史：
 * ================================================
 */
public abstract class StringCallback extends AbsCallback<String> {
    private ProgressDialog dialog;


    private void initDialog(Activity activity) {
        dialog = new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求网络中...");
    }

    public StringCallback(Activity activity) {
        super();
        initDialog(activity);
    }

    @Override
    public String convertSuccess(Response response) throws Exception {
        String s = StringConvert.create().convertSuccess(response);
        response.close();
        return s;
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        //网络请求前显示对话框
        if (dialog != null && !dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        //网络请求前显示对话框
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onAfter(String s, Exception e) {
        super.onAfter(s, e);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    /* @Override
    public void onAfter(@Nullable T t, @Nullable Exception e) {
        super.onAfter(t, e);
        //网络请求结束后关闭对话框
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }*/
}