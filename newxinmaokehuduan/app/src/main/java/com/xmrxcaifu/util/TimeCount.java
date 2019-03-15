package com.xmrxcaifu.util;

import android.os.CountDownTimer;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;


public class TimeCount extends CountDownTimer {
    private Button btn;
    private TextView tv_voice_code;

    public TimeCount(long millisInFuture, long countDownInterval, Button button, TextView tv_voice_code) {
        super(millisInFuture, countDownInterval);
        btn = button;
        this.tv_voice_code = tv_voice_code;
    }

    public TimeCount(long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        btn = button;

    }

    @Override
    public void onFinish() {// 计时完毕
        btn.setText("发送验证码");
//		btn.setBackgroundResource(R.drawable.v6button);
        btn.setClickable(true);
        if (tv_voice_code != null) {

            tv_voice_code.setText(Html.fromHtml("收不到短信？点击获取" + "<font color='#00aaee'>语音验证码</font>"));
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程
        btn.setClickable(false);//防止重复点击
//		btn.setBackgroundResource(R.drawable.v6_unclick);
        btn.setText("已发送" + "(" + millisUntilFinished / 1000 + "秒)");
        if (tv_voice_code != null) {

//			tv_voice_code.setText(Html.fromHtml("您将会接到电话"+"<font color='#00aaee'>getResources().getString(R.string.voice_phone)</font>"+"为您播报验证码"));
        }
    }
}
