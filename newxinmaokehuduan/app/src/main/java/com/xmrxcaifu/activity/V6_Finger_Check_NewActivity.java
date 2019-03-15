package com.xmrxcaifu.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import com.xmrxcaifu.BitmapUtils;
import com.xmrxcaifu.Constant;
import com.xmrxcaifu.MainActivity;

import com.xmrxcaifu.R;
import com.xmrxcaifu.core.FingerprintCore;
import com.xmrxcaifu.http.MyOkhttp;
import com.xmrxcaifu.http.adapter.Call;
import com.xmrxcaifu.http.callback.StringNoDialogCallback;
import com.xmrxcaifu.http.model.Response;
import com.xmrxcaifu.http.request.BaseRequest;
import com.xmrxcaifu.httpmodel.LoginBean;
import com.xmrxcaifu.roundcircle.RoundImageView;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.Circle;
import com.xmrxcaifu.util.FingerprintUtil;
import com.xmrxcaifu.util.FingerprintUtil2;
import com.xmrxcaifu.util.KeyguardLockScreenManager;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.util.ToastUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

public class V6_Finger_Check_NewActivity extends BaseActivty2 implements OnClickListener {
    //    private ImageView img_finger_bg;
    private TelephonyManager tm;
    private SharedPreferences sha;
//    private CustomDialog_view_new2.Builder builder;

    private TextView text_login;
    private RoundImageView user_logo;
    private TextView text_welcome;

    TextView text_yue;

    TextView text_ri;
    SpannableString msp = null;


    //dialog弹框
    ImageView img_showdialog;

    public static V6_Finger_Check_NewActivity instance;

    String phone;
    private final String ACTION_NAME = "智投宝结束";
    ImageView user_existlogo;
    String flag_zhiwen = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v6_finger_check);
//        BbtApplication.getInstance().addActvity(this);
        sha = getSharedPreferences("config", MODE_PRIVATE);
        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);

        initFingerprintCore();
//        showFragment();

        ImmersionBar.with(this)
                .statusBarDarkFont(true, 1f)

                .init();
        layout_yijuhua = (LinearLayout) findViewById(R.id.layout_yijuhua);

        layout_yijuhua.setOnClickListener(this);
        if (!network()) {
            network = 1;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_yijuhua.performClick();
            }
        }, 100);
        dialog = new Dialog(V6_Finger_Check_NewActivity.this);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
                R.color.float_transparent)));
//        window.setGravity(Gravity.CENTER);
        window.setGravity(Gravity.CENTER);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setContentView(R.layout.popwindow_figer);
        dialog.show();
        message = (TextView) window.findViewById(R.id.message);
        cancel_btn = (Button) window.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                flag_zhiwen = "2";
//                FingerprintUtil.cancel();
                cancelFingerprintRecognition();
            }
        });

        text_login = (TextView) findViewById(R.id.text_login);
        text_yue = (TextView) findViewById(R.id.text_yue);
        user_existlogo = (ImageView) findViewById(R.id.user_existlogo);
        text_ri = (TextView) findViewById(R.id.text_ri);

        text_yue = (TextView) findViewById(R.id.text_yue);

        Calendar calendar = Calendar.getInstance();
        int monthOfYear = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        text_ri.setText(dayOfMonth + "");


        text_yue.setText(monthOfYear + "");
        text_welcome = (TextView) findViewById(R.id.text_welcome);

        img_showdialog = (ImageView) findViewById(R.id.img_showdialog);
        img_showdialog.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                flag_zhiwen = "1";
//                onFingerprintClick(null);
//                startFingerprintRecognition();

                dialog = new Dialog(V6_Finger_Check_NewActivity.this);
                dialog.setCancelable(false);
                Window window = dialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
                        R.color.float_transparent)));
//        window.setGravity(Gravity.CENTER);
                window.setGravity(Gravity.CENTER);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                window.setContentView(R.layout.popwindow_figer);
                dialog.show();
                message = (TextView) window.findViewById(R.id.message);
                cancel_btn = (Button) window.findViewById(R.id.cancel_btn);
                cancel_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        flag_zhiwen = "2";

                    }
                });
            }
        });
        phone = (String) MySharePreferenceUtil.get(this, "phone", "");
        token = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "mytoken", "");

        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();//

        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
//        onFingerprintClick(null);


        user_logo = (RoundImageView) findViewById(R.id.user_logo);

        String token = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "mytoken", "");


        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String sex = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "sex", "");
        String userName = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "userNameText", "");
        final String userMobile = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "userMobile", "");

        String welcomeName = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "welcomeName", "");
        String appHeadUrl = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "appHeadUrl", "");
        String gender = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "gender", "");
        if (appHeadUrl.isEmpty()) {
            user_logo.setVisibility(View.VISIBLE);
            if (gender.equals("1")) {
                user_logo.setImageBitmap(BitmapUtils.readBitMap(V6_Finger_Check_NewActivity.this, R.drawable.img_girl));
            } else {
                user_logo.setImageBitmap(BitmapUtils.readBitMap(V6_Finger_Check_NewActivity.this, R.drawable.img_boy));
            }

        } else {

            user_logo.setVisibility(View.GONE);
            user_existlogo.setVisibility(View.VISIBLE);
//            Glide.with(this).tra(new GlideCircleTransform(mContext)).load(appHeadUrl).into(user_existlogo);
            Glide.with(this)
                    .load(appHeadUrl)
                    .centerCrop()
                    .error(R.drawable.img_boy)
                    .placeholder(R.drawable.img_boy)
                    .transform(new Circle(this))
                    .into(user_existlogo);
        }

        String secName = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "secName", "");
        if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "idCard", ""))) {
            text_welcome.setText(phone.substring(0, 3) + "****" + phone.substring(7, phone.length()));
        } else {
            text_welcome.setText("欢迎您， " + secName);
        }

        text_login.setOnClickListener(new OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

//                onFingerprintClick(null);
                Intent intent = new Intent(V6_Finger_Check_NewActivity.this, LoginActivity2.class);
//                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "token");
                MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "token", "");
                MySharePreferenceUtil.remove(V6_Finger_Check_NewActivity.this, "username");
                MySharePreferenceUtil.remove(V6_Finger_Check_NewActivity.this, "phone");
                MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "newintentflag", "2");

                startActivity(intent);
                finish();
            }
        });

//        startFingerprintRecognition();
    }

    private long mLastBackTime = 0;
    private long TIME_DIFF = 2 * 1000;
    LinearLayout layout_yijuhua;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long now = new Date().getTime();
            if (now - mLastBackTime < TIME_DIFF) {
                EventBus.getDefault().post(886);
                android.os.Process.killProcess(android.os.Process.myPid());
                finish();
                return super.onKeyDown(keyCode, event);
            } else {
                mLastBackTime = now;
                Toast.makeText(this, "再按一次退出鑫茂荣信财富", 2000).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Bitmap getSdBitmap(String path) {
        File mFile = new File(path);
        Bitmap bitmap;
        //若该文件存在
        if (mFile.exists()) {
//	        	LogUtils.e(GestureVerifyActivity.this.this, "sd卡图片存在");
            bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_girl);
        }
        return bitmap;
    }

    private Dialog dialog;
    Button cancel_btn;

/*    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onFingerprintClick(View v) {

        FingerprintUtil.callFingerPrint(new FingerprintUtil.OnCallBackListenr() {

            @Override
            public void onSupportFailed() {
//                ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, "当前设备不支持指纹");
            }

            @Override
            public void onInsecurity() {

//                ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, "当前设备未处于安全保护中");
            }

            @Override
            public void onEnrollFailed() {
//                ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, "请到设置中设置指纹");

            }

            @Override
            public void onAuthenticationStart() {

                dialog = new Dialog(V6_Finger_Check_NewActivity.this);
                dialog.setCancelable(false);
                Window window = dialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
                        R.color.float_transparent)));
//        window.setGravity(Gravity.CENTER);
                window.setGravity(Gravity.CENTER);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                window.setContentView(R.layout.popwindow_figer);
                dialog.show();
                message = (TextView) window.findViewById(R.id.message);
                cancel_btn = (Button) window.findViewById(R.id.cancel_btn);
                cancel_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        flag_zhiwen = "2";
//                        FingerprintUtil.cancel();
                        cancelFingerprintRecognition();
                    }
                });
            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
//                showToast(errString.toString());
              *//*  ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, errString.toString());
//   Log.e("dialog", builder.create()+"");
                if (builder.create() != null) {
                    builder.create().mdissmis();
                }*//*

                *//*builder.setMessage("再试一次");
                builder.setMessage2("请验证已有指纹验证");
//                builder.setMessage("再试一次");
                showToast("解锁失败");*//*
//                showToast("解锁失败");

                if (flag_zhiwen.equals("2")) {

                } else if (flag_zhiwen.equals("1")) {

                } else {
                    Intent intent = new Intent(V6_Finger_Check_NewActivity.this, LoginActivity2.class);
//                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "token");
                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "token", "");
                    MySharePreferenceUtil.remove(V6_Finger_Check_NewActivity.this, "username");
                    MySharePreferenceUtil.remove(V6_Finger_Check_NewActivity.this, "phone");
                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "newintentflag", "2");

                    startActivity(intent);
                    finish();
                }


            }

            @Override
            public void onAuthenticationFailed() {

                message.setVisibility(View.VISIBLE);
                message.setText("再试一次");
//                message.setVisibility(View.VISIBLE);
//                message.setText("再试一次");
//                layout_shuirumima.setVisibility(View.GONE);
//                layout_mima.setVisibility(View.GONE);
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
//                showToast(helpString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {

//                ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, "解锁成功");
                dialog.dismiss();

                final String pwd = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "password", "");

                MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "newintentflag", "2");
                MyOkhttp.post(Constant.URL + "api/v1/system/login")
                        .tag(this)

                        .params("username", phone)
                        .params("password", pwd)
                        .params("did", getdeviceId())
                        .params("deviceToken", getDeviceToken())
                        .execute(new StringNoDialogCallback() {
                            @Override
                            public void onBefore(BaseRequest request) {
                                super.onBefore(request);

                            }

                            @Override
                            public void onSuccess(String s, okhttp3.Call call, okhttp3.Response response) {
                                Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
//                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
                                Gson gson = new Gson();
                                LoginBean loginBean = new LoginBean();
                                loginBean = gson.fromJson(s, LoginBean.class);
                                LoginBean.DataBean dataBean = loginBean.getData();
//                                popWindow2.dismiss();
                                if (loginBean.getMeta().isSuccess() == false) {

                                    ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, "登录信息失效,请重新登录");
                                    startActivity(new Intent(V6_Finger_Check_NewActivity.this, LoginActivity2.class));
                                    MySharePreferenceUtil.remove(V6_Finger_Check_NewActivity.this, "token");
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "token", "");
                                    MySharePreferenceUtil.remove(V6_Finger_Check_NewActivity.this, "username");


                                    MySharePreferenceUtil.remove(V6_Finger_Check_NewActivity.this, "phone");

                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "newintentflag", "2");
                                    MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, phone + "flaggesture", "2");
                                    finish();
                                } else {


                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "id", loginBean.getData().getId());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "username", loginBean.getData().getUsername());
//                            MySharePreferenceUtil.put(LoginActivity.this, "idCard", loginBean.getData().getIdCard());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "phone", loginBean.getData().getPhone());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "phone1", loginBean.getData().getPhone());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "token", loginBean.getData().getToken());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "secName", loginBean.getData().getSecName());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "password", pwd);
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "token1", loginBean.getData().getToken());
                                    EventBus.getDefault().post(998);


                                    try {
                                        if (loginBean.getData().getIdCard().length() < 5) {
                                            MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "idCard", "");
                                        } else {
                                            MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "idCard", loginBean.getData().getIdCard());
                                        }
                                        MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "gender", loginBean.getData().getGender());
//                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "appHeadUrl", loginBean.getData().getAppHeadUrl());
                                    } catch (Exception e) {
                                    }


                                    if (TextUtils.isEmpty(loginBean.getData().getAppHeadUrl())) {
                                        MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "appHeadUrl", "");
                                    } else {
                                        MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "appHeadUrl", loginBean.getData().getAppHeadUrl());
                                    }

                                    Intent mIntent = new Intent(ACTION_NAME);
                                    mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    hashMap.put("登录", "登录");

                                    // 设置控件附加属性，用于无埋点对控件添加数据；控件绑定事件附加属性依赖于view.setTag(key, value)支持，使用key值：-96000；
                                    // 如果view已经绑定过此key值，则此设置不生效
                                    StatService.setAttributes(user_existlogo, hashMap);
//                                    finish();
                                    //发送广播
                                    String newintentflag = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "newintentflag", "");
                                    if (newintentflag.equals("5")) {
                                        Intent intent = new Intent(V6_Finger_Check_NewActivity.this, MainActivity.class);
                                        intent.putExtra("url", getIntent().getStringExtra("url"));
                                        startActivity(intent);
                                        sendBroadcast(mIntent);
                                        finish();

                                    } else {

                                        String wailianfou = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "wailianfou", "");
                                        ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, wailianfou);

                                        if (wailianfou.equals("1")) {
                                            Intent intent = new Intent(V6_Finger_Check_NewActivity.this, MainActivity5.class);
                                            intent.putExtra("url", (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "getLinkurl", ""));
                                            startActivity(intent);
                                            sendBroadcast(mIntent);
                                            finish();
                                            MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "newintentflag", "2");
//                                            EventBus.getDefault().post(998);

                                        } else {

                                            sendBroadcast(mIntent);

                                            finish();
                                            MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "newintentflag", "2");



                                        }

                                    }


                                }

                            }

                            @Override
                            public void onError(okhttp3.Call call, okhttp3.Response response, Exception e) {
                                super.onError(call, response, e);
                                mydismissloading();
                            }

                            @Override
                            public void onAfter(String s, Exception e) {
                                super.onAfter(s, e);
//                        dismissLoading();

                                mydismissloading();
                            }
                        });
            }
        });
    }*/

    String token;


    public static int count = 0;

    public void method() {

        count++;

    }

    private boolean isDestroyed = false;

    private void destroy() {
        if (isDestroyed) {
            return;
        }
        isDestroyed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            destroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroy();
    }

    private void setImmersionStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private PopupWindow popWindow2;
    TextView message;
    //线
    View layout_mima;

    Button layout_shuirumima;
    private FingerprintCore mFingerprintCore;
    private KeyguardLockScreenManager mKeyguardLockScreenManager;

    int network = 0;
    private FingerprintCore.IFingerprintResultListener mResultListener = new FingerprintCore.IFingerprintResultListener() {
        @Override
        public void onAuthenticateSuccess() {
            dialog.dismiss();
            if (!network()) {
                Toast toast = new Toast(V6_Finger_Check_NewActivity.this);
                View view1 = LayoutInflater.from(V6_Finger_Check_NewActivity.this).inflate(R.layout.toast_layout, null);
                toast.setView(view1);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(2000);
                toast.show();
                network = 1;
                finish();
            } else {

                final String pwd = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "password", "");

                MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "newintentflag", "2");
                MyOkhttp.post(Constant.URL + "api/v1/system/login")
                        .tag(this)

                        .params("username", phone)
                        .params("password", pwd)
                        .params("did", getdeviceId())
                        .params("deviceToken", getDeviceToken())
                        .execute(new StringNoDialogCallback() {
                            @Override
                            public void onBefore(BaseRequest request) {
                                super.onBefore(request);

                            }


                            @Override
                            public void onSuccess(String s, okhttp3.Call call, okhttp3.Response response) {
                                Log.d(s, "eeeeeeeeeeeeeeeeeeeee");

//                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
                                Gson gson = new Gson();
                                LoginBean loginBean = new LoginBean();
                                loginBean = gson.fromJson(s, LoginBean.class);
                                LoginBean.DataBean dataBean = loginBean.getData();
//                                popWindow2.dismiss();
                                if (loginBean.getMeta().isSuccess() == false) {

                                    ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, "登录信息失效,请重新登录");
                                    startActivity(new Intent(V6_Finger_Check_NewActivity.this, LoginActivity2.class));
                                    MySharePreferenceUtil.remove(V6_Finger_Check_NewActivity.this, "token");
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "token", "");
                                    MySharePreferenceUtil.remove(V6_Finger_Check_NewActivity.this, "username");
                                    MySharePreferenceUtil.remove(V6_Finger_Check_NewActivity.this, "phone");

                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "newintentflag", "2");
                                    MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, phone + "flaggesture", "2");
                                    finish();
                                } else {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    hashMap.put("登录", "登录");

                                    // 设置控件附加属性，用于无埋点对控件添加数据；控件绑定事件附加属性依赖于view.setTag(key, value)支持，使用key值：-96000；
                                    // 如果view已经绑定过此key值，则此设置不生效
                                    StatService.setAttributes(user_existlogo, hashMap);

                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "id", loginBean.getData().getId());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "username", loginBean.getData().getUsername());
//                            MySharePreferenceUtil.put(LoginActivity.this, "idCard", loginBean.getData().getIdCard());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "phone", loginBean.getData().getPhone());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "phone1", loginBean.getData().getPhone());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "token", loginBean.getData().getToken());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "secName", loginBean.getData().getSecName());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "password", pwd);
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "token1", loginBean.getData().getToken());

                                    EventBus.getDefault().post(998);
                                    try {
                                        if (loginBean.getData().getIdCard().length() < 5) {
                                            MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "idCard", "");
                                        } else {
                                            MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "idCard", loginBean.getData().getIdCard());
                                        }
                                        MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "gender", loginBean.getData().getGender());
//                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "appHeadUrl", loginBean.getData().getAppHeadUrl());
                                    } catch (Exception e) {
                                    }

                                    HashMap<String, String> hashMap1 = new HashMap<String, String>();
                                    hashMap1.put("登录", "登录");

                                    // 设置控件附加属性，用于无埋点对控件添加数据；控件绑定事件附加属性依赖于view.setTag(key, value)支持，使用key值：-96000；
                                    // 如果view已经绑定过此key值，则此设置不生效
                                    StatService.setAttributes(user_existlogo, hashMap1);
                                    if (TextUtils.isEmpty(loginBean.getData().getAppHeadUrl())) {
                                        MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "appHeadUrl", "");
                                    } else {
                                        MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "appHeadUrl", loginBean.getData().getAppHeadUrl());
                                    }

                                    Intent mIntent = new Intent(ACTION_NAME);
                                    mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                                    //发送广播
                                    String newintentflag = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "newintentflag", "");


                                    if (network == 0) {

                                    } else {
                                        EventBus.getDefault().post(666);
                                    }


                                    if (newintentflag.equals("5")) {
                                        Intent intent = new Intent(V6_Finger_Check_NewActivity.this, MainActivity.class);
                                        intent.putExtra("url", getIntent().getStringExtra("url"));
                                        startActivity(intent);
                                        sendBroadcast(mIntent);
                                        finish();

                                    } else {
                                        String wailianfou = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "wailianfou", "");
//                                        ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this,wailianfou);

                                        if (wailianfou.equals("1")) {
                                            Intent intent = new Intent(V6_Finger_Check_NewActivity.this, MainActivity5.class);
                                            intent.putExtra("url", (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity.this, "getLinkurl", ""));
                                            startActivity(intent);
                                            sendBroadcast(mIntent);
                                            finish();
                                            MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "newintentflag", "2");
                                            EventBus.getDefault().post(999);

                                        } else {

                                            sendBroadcast(mIntent);
                                            finish();
                                            MySharePreferenceUtil.put(V6_Finger_Check_NewActivity.this, "newintentflag", "2");


                                        }

                                    }


                                }

                            }

                            @Override
                            public void onError(okhttp3.Call call, okhttp3.Response response, Exception e) {
                                super.onError(call, response, e);
                                mydismissloading();
                            }

                            @Override
                            public void onAfter(String s, Exception e) {
                                super.onAfter(s, e);
//                        dismissLoading();

                                mydismissloading();
                            }
                        });
            }


        }

        @Override
        public void onAuthenticateFailed(int helpId) {
            ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, "识别失败");
        }

        @Override
        public void onAuthenticateError(int errMsgId) {
            ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, "授权失败");
        }

        @Override
        public void onStartAuthenticateResult(boolean isSuccess) {

        }
    };

    private void initFingerprintCore() {
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
        mKeyguardLockScreenManager = new KeyguardLockScreenManager(this);
    }

    /**
     * 开始指纹识别
     */
    private void startFingerprintRecognition() {

        if (mFingerprintCore.isSupport()) {
            if (!mFingerprintCore.isHasEnrolledFingerprints()) {
//                toastTipMsg(R.string.fingerprint_recognition_not_enrolled);
                ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, "没有开启指纹识别");
                FingerprintUtil2.openFingerPrintSettingPage(this);
                return;
            }
            ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, "开启指纹识别");
            mFingerprintCore.startAuthenticate();
            /* if (mFingerprintCore.isAuthenticating()) {
                ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this,"授权成功");
            } else {
                mFingerprintCore.startAuthenticate();
            }*/
        } else {
            ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, "设备不支持指纹");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_yijuhua:
                ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, "到这里了么");
                startFingerprintRecognition();
                break;
        }

    }


    private void cancelFingerprintRecognition() {
        if (mFingerprintCore.isAuthenticating()) {
            mFingerprintCore.cancelAuthenticate();
//            resetGuideViewState();
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

}

