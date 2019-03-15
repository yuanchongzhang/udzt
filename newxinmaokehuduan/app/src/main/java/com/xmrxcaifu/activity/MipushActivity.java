package com.xmrxcaifu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import com.umeng.message.UmengNotifyClickActivity;
import com.umeng.message.common.UmLog;
import com.umeng.message.entity.UMessage;
import com.xmrxcaifu.MainActivity;
import com.xmrxcaifu.R;


import org.android.agoo.common.AgooConstants;

import java.util.Map;

public class MipushActivity extends UmengNotifyClickActivity {
    private static String TAG = MipushActivity.class.getName();
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(MipushActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            addMessageToIntent(intent, (UMessage) msg.obj);


            Log.d("TEST", "uMessage：" + msg.obj.toString());
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.splash_activity);
    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        UmLog.i(TAG, body);
        Log.d("TEST", "body：" + body);
        UMessage uMessage = new Gson().fromJson(body, UMessage.class);
        Message message = Message.obtain();
        message.obj = uMessage;
        handler.sendMessage(message);
    }

    /**
     * 用于将UMessage中自定义参数的值放到intent中传到SplashActivity中，SplashActivity中对友盟推送时自定义消息作了专门处理
     *
     * @param intent 需要增加值得intent
     * @param msg    需要增加到intent中的msg
     */
    private void addMessageToIntent(Intent intent, UMessage msg) {

        if (intent == null || msg == null || msg.extra == null) {
            return;
        }

        for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null) {
                intent.putExtra(key, value);
                intent.putExtra("url", msg.extra.get("linkUrl"));


            }
        }

    }

}
