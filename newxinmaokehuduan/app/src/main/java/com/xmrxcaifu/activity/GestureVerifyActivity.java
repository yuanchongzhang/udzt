package com.xmrxcaifu.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import com.xmrxcaifu.BitmapUtils;
import com.xmrxcaifu.Constant;

import com.xmrxcaifu.R;
import com.xmrxcaifu.common.LoadingDialog;
import com.xmrxcaifu.core.FingerprintCore;
import com.xmrxcaifu.dialog.CustomDialog_view_new;
import com.xmrxcaifu.gesture.GestureContentView;
import com.xmrxcaifu.gesture.GestureDrawline;
import com.xmrxcaifu.http.MyOkhttp;
import com.xmrxcaifu.http.callback.StringNoDialogCallback;
import com.xmrxcaifu.http.request.BaseRequest;
import com.xmrxcaifu.httpmodel.LoginBean;
import com.xmrxcaifu.inteface.OnDismissListener;
import com.xmrxcaifu.inteface.OnItemClickListener;
import com.xmrxcaifu.roundcircle.RoundImageView;
import com.xmrxcaifu.util.Circle;
import com.xmrxcaifu.util.FingerSetting;
import com.xmrxcaifu.util.FingerprintUtil;
import com.xmrxcaifu.util.FingerprintUtil2;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.util.ToastUtil;
import com.xmrxcaifu.view.MyLoadingDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Response;


/**
 * ���ƻ���/У�����
 */
public class GestureVerifyActivity extends GeActivity implements View.OnClickListener, OnItemClickListener, OnDismissListener {
    /**
     * �ֻ����
     */
    public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
    /**
     * ��ͼ
     */
    public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
    private RelativeLayout mTopLayout;
    private TextView mTextTitle;
    private TextView mTextCancel;
    private ImageView mImgUserLogo;
    private TextView mTextPhoneNumber;
    private TextView mTextTip;
    private int error = 0;
    private String csrf;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    int network = 0;
    private TextView mTextOther;
    private String mParamPhoneNumber;
    private long mExitTime = 0;
    private int mParamIntentCode;
    private SharedPreferences sha;

    private RoundImageView user_logo;


    private int[] screenDispaly;
    //手势密码页面

    String zhiwen;

    TextView text_figer;
    TextView text_other_account;
//这是用户名呢

    TextView text_welcome;

    ImageView user_existlogo;
    String phone;
    private CustomDialog_view_new.Builder builder;

    TextView text_ri;
    TextView text_yue;
    TextView text_forget_gesture;
    TextView text_other_account2;
    private Dialog dialog;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mFingerprintCore != null) {
            mFingerprintCore.onDestroy();
            mFingerprintCore = null;
        }
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_verify);
        setImmersionStatus();
        layout_yijuhua = (LinearLayout) findViewById(R.id.layout_yijuhua);
        zhiwen = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, phone + "zhiwendenglu", "");
        text_other_account = (TextView) findViewById(R.id.text_other_account);
        text_other_account.setOnClickListener(this);
        setUpViews();
        user_existlogo = (ImageView) findViewById(R.id.user_existlogo);
        text_welcome = (TextView) findViewById(R.id.text_welcome);
        phone = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "phone", "");
        String gender = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "gender", "");
        String appHeadUrl = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "appHeadUrl", "");

        if (!network()) {
            network = 1;
        }

        initFingerprintCore();
        text_ri = (TextView) findViewById(R.id.text_ri);
        Calendar calendar = Calendar.getInstance();
        text_yue = (TextView) findViewById(R.id.text_yue);
        int monthOfYear = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        text_ri.setText(dayOfMonth + "");
        text_other_account2 = (TextView) findViewById(R.id.text_other_account2);
        text_yue.setText(monthOfYear + "");
        String secName = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "secName", "");
        builder2 = new MyLoadingDialog.Builder(this)
                .setShowMessage(false)
                .setCancelable(false);
        dialog2 = builder2.create();
        if (appHeadUrl.isEmpty()) {
            user_logo.setVisibility(View.VISIBLE);
            if (gender.equals("1")) {
                user_logo.setImageBitmap(BitmapUtils.readBitMap(GestureVerifyActivity.this, R.drawable.img_girl));
            } else {
                user_logo.setImageBitmap(BitmapUtils.readBitMap(GestureVerifyActivity.this, R.drawable.img_boy));
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

        try {
            if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "idCard", ""))) {
                text_welcome.setText(phone.substring(0, 3) + "****" + phone.substring(7, phone.length()));
            } else {
                text_welcome.setText("欢迎您， " + secName);
            }
        } catch (Exception e) {

        }


        String zhiwen = (String) MySharePreferenceUtil.get(this, phone + "zhiwendenglu", "");

        layout_yijuhua.setOnClickListener(this);
        text_forget_gesture = (TextView) findViewById(R.id.text_forget_gesture);
        builder = new CustomDialog_view_new.Builder(GestureVerifyActivity.this);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FingerprintUtil.cancel();
                builder.create().dismiss();
                cancelFingerprintRecognition();
            }
        });
        if (zhiwen.equals("1")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    layout_yijuhua.performClick();
                }
            }, 100);
            dialog = new Dialog(GestureVerifyActivity.this);
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
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

//                FingerprintUtil.cancel();
                    cancelFingerprintRecognition();
                }
            });

            text_forget_gesture.setVisibility(View.VISIBLE);
            text_other_account2.setVisibility(View.VISIBLE);
            text_other_account.setVisibility(View.GONE);
        } else {
            text_forget_gesture.setVisibility(View.GONE);
            text_other_account2.setVisibility(View.GONE);
            text_other_account.setVisibility(View.VISIBLE);
        }
        text_other_account2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GestureVerifyActivity.this, LoginActivity2.class);
//                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "token");
                MySharePreferenceUtil.put(GestureVerifyActivity.this, "token", "");
                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "username");


                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "phone");

                MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");
                MySharePreferenceUtil.get(GestureVerifyActivity.this, phone + "flaggesture", "2");
                startActivity(intent);
                finish();


            }
        });

        text_forget_gesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onFingerprintClick(null);
            }
        });
    }


    TextView[] tv = new TextView[5];
    private int postion = 0;

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

    public void onEventMainThread(Integer type) {

    }


    String token;
    private final String ACTION_NAME = "智投宝结束";

    private void setUpViews() {
        mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextCancel = (TextView) findViewById(R.id.text_cancel);
//		mImgUserLogo = (ImageView) findViewById(R.id.user_logo);
        sha = getSharedPreferences("config", MODE_PRIVATE);
        user_logo = (RoundImageView) findViewById(R.id.user_logo);
        mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        setUpListeners();
        mTextTip.setText("请绘制手势密码");
//        String mima = (String) MySharePreferenceUtil.getout(GestureVerifyActivity.this, "shoushimima", "");

        final String phone1 = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "phone", "");

        Log.d(phone1 + "shoushimima", "090909");
        String mima = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, phone1 + "shoushimima", "");
        mGestureContentView = new GestureContentView(this, true, mima,
                new GestureDrawline.GestureCallBack() {
                    @Override
                    public void onGestureCodeInput(String inputCode) {
                    }

                    @Override
                    public void checkedSuccess() {
                        mGestureContentView.clearDrawlineState(0L);
                        if (!network()) {
                            Toast toast = new Toast(GestureVerifyActivity.this);
                            View view1 = LayoutInflater.from(GestureVerifyActivity.this).inflate(R.layout.toast_layout, null);
                            toast.setView(view1);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.setDuration(2000);
                            toast.show();
                            network = 1;
                            finish();
                        } else {
                            final String pwd = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "password", "");
                            MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");
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
                                        public void onSuccess(String s, Call call, Response response) {
                                            Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
//                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
                                            Gson gson = new Gson();
                                            LoginBean loginBean = new LoginBean();
                                            loginBean = gson.fromJson(s, LoginBean.class);
                                            LoginBean.DataBean dataBean = loginBean.getData();
                                            if (loginBean.getMeta().isSuccess() == false) {

                                                ToastUtil.showCenterToast(GestureVerifyActivity.this, "登录信息失效,请重新登录");
                                                startActivity(new Intent(GestureVerifyActivity.this, LoginActivity2.class));
                                                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "token");
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "token", "");
                                                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "username");


                                                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "phone");

                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, phone1 + "flaggesture", "1");
                                                finish();
                                            } else {
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "id", loginBean.getData().getId());
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "username", loginBean.getData().getUsername());
//                            MySharePreferenceUtil.put(LoginActivity.this, "idCard", loginBean.getData().getIdCard());
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "phone", loginBean.getData().getPhone());
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "phone1", loginBean.getData().getPhone());
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "token", loginBean.getData().getToken());
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "secName", loginBean.getData().getSecName());
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "password", pwd);
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "token1", loginBean.getData().getToken());

                                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                                hashMap.put("登录", "登录");

                                                // 设置控件附加属性，用于无埋点对控件添加数据；控件绑定事件附加属性依赖于view.setTag(key, value)支持，使用key值：-96000；
                                                // 如果view已经绑定过此key值，则此设置不生效
                                                StatService.setAttributes(mGestureContentView, hashMap);
                                                try {
                                                    if (loginBean.getData().getIdCard().length() < 5) {
                                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "idCard", "");
                                                    } else {
                                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "idCard", loginBean.getData().getIdCard());
                                                    }
                                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "gender", loginBean.getData().getGender());
//                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "appHeadUrl", loginBean.getData().getAppHeadUrl());
                                                } catch (Exception e) {
                                                }


                                                if (TextUtils.isEmpty(loginBean.getData().getAppHeadUrl())) {
                                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "appHeadUrl", "");
                                                } else {
                                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "appHeadUrl", loginBean.getData().getAppHeadUrl());
                                                }

                                                Intent mIntent = new Intent(ACTION_NAME);
                                                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                                                //发送广播
                                                String newintentflag = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "newintentflag", "");
                                                if (newintentflag.equals("5")) {
                                                    Intent intent = new Intent(GestureVerifyActivity.this, MainActivity4.class);
                                                    intent.putExtra("url", getIntent().getStringExtra("url"));
                                                    startActivity(intent);
                                                    sendBroadcast(mIntent);
                                                    finish();

                                                } else {
                                                  /*  sendBroadcast(mIntent);
                                                    finish();
                                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2")*/
                                                    ;
                                                    String wailianfou = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "wailianfou", "");
//                                                    ToastUtil.showCenterToast(GestureVerifyActivity.this, wailianfou);
//                                                    ToastUtil.showCenterToast(GestureVerifyActivity.this, wailianfou);
                                                    if (wailianfou.equals("1")) {
                                                        Intent intent = new Intent(GestureVerifyActivity.this, MainActivity6.class);
                                                        intent.putExtra("url", (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "getLinkurl", ""));
                                                        startActivity(intent);
                                                        sendBroadcast(mIntent);
                                                        finish();
                                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");

                                                    } else {
//                                                        10
//                                                            ToastUtil.showCenterToast(GestureVerifyActivity.this,"到这里了么");
                                                        EventBus.getDefault().post(10);
//                                                        sendBroadcast(mIntent);
                                                        finish();
                                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");

                                                    }

                                                }


                                            }


                                        }

                                        @Override
                                        public void onError(Call call, Response response, Exception e) {
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
                    public void checkedFail() {
                        error++;
                        mTextTip.setVisibility(View.VISIBLE);

                        if (error == 5) {
                            mTextTip.setText(Html.fromHtml("<font color='#E55835'>密码错误5次,请重新登录</font>"));
//                            startActivity(new Intent(GestureVerifyActivity.this, LoginActivity.class));
                            Intent intent = new Intent(GestureVerifyActivity.this, LoginActivity2.class);
                            MySharePreferenceUtil.remove(GestureVerifyActivity.this, "token");
                            MySharePreferenceUtil.put(GestureVerifyActivity.this, "token", "");
                            MySharePreferenceUtil.remove(GestureVerifyActivity.this, "username");


                            MySharePreferenceUtil.remove(GestureVerifyActivity.this, "phone");

                            MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");
                            MySharePreferenceUtil.put(GestureVerifyActivity.this, phone1 + "flaggesture", "1");
                            startActivity(intent);
                            finish();


                        } else {
                            mTextTip.setText(Html.fromHtml("<font color='#E55835'>错误次数" + error + "次，" + "您还可以输入" + (5 - error) + "次</font>"));
                        }
                        mGestureContentView.clearDrawlineState(1000L);
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);

                    }
                });

        mGestureContentView.setParentView(mGestureContainer);
    }

    private void setImmersionStatus() {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private long mLastBackTime = 0;
    private long TIME_DIFF = 2 * 1000;
    private final String ACTION_NAME1 = "结束程序";


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
//                Toast.makeText(this, "再按一次退出优点智投", 2000).show();
                Toast.makeText(this, "再按一次退出鑫茂荣信财富", 2000).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出鑫茂荣信财富", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {


            user_logo.setImageBitmap((Bitmap) msg.obj);


        }

        ;
    };

    private void setUpListeners() {
        mTextCancel.setOnClickListener(this);

    }

    private String getProtectedMobile(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(phoneNumber.subSequence(0, 3));
        builder.append("****");
        builder.append(phoneNumber.subSequence(7, 11));
        return builder.toString();
    }

    public String getDeviceType() {
        Build build = new Build();
        return build.MODEL;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_cancel:
                MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");

                this.finish();
                break;
            case R.id.layout_yijuhua:
                startFingerprintRecognition();
                break;
            case R.id.text_other_account:

                Intent intent = new Intent(GestureVerifyActivity.this, LoginActivity2.class);
//                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "token");
                MySharePreferenceUtil.put(GestureVerifyActivity.this, "token", "");
                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "username");


                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "phone");

                MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");
                MySharePreferenceUtil.get(GestureVerifyActivity.this, phone + "flaggesture", "2");
                startActivity(intent);
                finish();


                break;


        }
    }

    public void showToast(String name) {
        Toast.makeText(GestureVerifyActivity.this, name, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {

    }


    MyLoadingDialog.Builder builder2;
    LoadingDialog dialog2;
    LinearLayout layout_yijuhua;


    public void myshowloading() {

        dialog2.show();
    }

    public void mydismissloading() {
        dialog2.dismiss();
    }


    Button cancel_btn;

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    public void onFingerprintClick(View v) {

        FingerprintUtil.callFingerPrint(new FingerprintUtil.OnCallBackListenr() {

            @Override
            public void onSupportFailed() {
//                showToast("当前设备不支持指纹");
            }

            @Override
            public void onInsecurity() {
                FingerSetting.openFingerPrintSettingPage(GestureVerifyActivity.this);
            }

            @Override
            public void onEnrollFailed() {
//                showToast("请到设置中设置指纹");
            }

            @Override
            public void onAuthenticationStart() {

                dialog = new Dialog(GestureVerifyActivity.this);
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
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        FingerprintUtil.cancel();
                    }
                });
            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
//                ToastUtil.showCenterToast(GestureVerifyActivity.this, "传感器次数太多");
                if (dialog != null) {
                    dialog.cancel();
                    dialog.dismiss();
                    FingerprintUtil.cancel();
                }

            }

            @Override
            public void onAuthenticationFailed() {
                message.setVisibility(View.VISIBLE);
                message.setText("再试一次");
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
//                showToast(helpString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
//                showToast("到这里了么");
                Log.e("dialog", builder.create() + "");
//
                dialog.dismiss();

                if (!network()) {

                    Toast toast = new Toast(GestureVerifyActivity.this);
                    View view1 = LayoutInflater.from(GestureVerifyActivity.this).inflate(R.layout.toast_layout, null);
                    toast.setView(view1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(2000);
                    toast.show();
                    network = 1;
                    finish();
                } else {

                    final String pwd = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "password", "");

                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");
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
                                public void onSuccess(String s, Call call, Response response) {
                                    Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
//                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
                                    Gson gson = new Gson();
                                    LoginBean loginBean = new LoginBean();
                                    loginBean = gson.fromJson(s, LoginBean.class);
                                    LoginBean.DataBean dataBean = loginBean.getData();
                                    if (loginBean.getMeta().isSuccess() == false) {

                                        ToastUtil.showCenterToast(GestureVerifyActivity.this, "登录信息失效,请重新登录");
                                        startActivity(new Intent(GestureVerifyActivity.this, LoginActivity2.class));
                                        MySharePreferenceUtil.remove(GestureVerifyActivity.this, "token");
                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "token", "");
                                        MySharePreferenceUtil.remove(GestureVerifyActivity.this, "username");


                                        MySharePreferenceUtil.remove(GestureVerifyActivity.this, "phone");

                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");
                                        MySharePreferenceUtil.get(GestureVerifyActivity.this, phone + "flaggesture", "2");
                                        finish();
                                    } else {
                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "id", loginBean.getData().getId());
                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "username", loginBean.getData().getUsername());
//                            MySharePreferenceUtil.put(LoginActivity.this, "idCard", loginBean.getData().getIdCard());
                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "phone", loginBean.getData().getPhone());
                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "phone1", loginBean.getData().getPhone());
                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "token", loginBean.getData().getToken());
                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "secName", loginBean.getData().getSecName());
                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "password", pwd);
                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "token1", loginBean.getData().getToken());

                                        try {
                                            if (loginBean.getData().getIdCard().length() < 5) {
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "idCard", "");
                                            } else {
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "idCard", loginBean.getData().getIdCard());
                                            }
                                            MySharePreferenceUtil.put(GestureVerifyActivity.this, "gender", loginBean.getData().getGender());
//                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "appHeadUrl", loginBean.getData().getAppHeadUrl());
                                        } catch (Exception e) {
                                        }

                                        HashMap<String, String> hashMap = new HashMap<String, String>();
                                        hashMap.put("登录", "登录");
                                        // 设置控件附加属性，用于无埋点对控件添加数据；控件绑定事件附加属性依赖于view.setTag(key, value)支持，使用key值：-96000；
                                        // 如果view已经绑定过此key值，则此设置不生效
                                        StatService.setAttributes(mGestureContentView, hashMap);
                                        if (TextUtils.isEmpty(loginBean.getData().getAppHeadUrl())) {
                                            MySharePreferenceUtil.put(GestureVerifyActivity.this, "appHeadUrl", "");
                                        } else {
                                            MySharePreferenceUtil.put(GestureVerifyActivity.this, "appHeadUrl", loginBean.getData().getAppHeadUrl());
                                        }

                                        Intent mIntent = new Intent(ACTION_NAME);
                                        mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                                        //发送广播
                                        String newintentflag = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "newintentflag", "");
                                        if (newintentflag.equals("5")) {
                                            Intent intent = new Intent(GestureVerifyActivity.this, MainActivity6.class);
                                            intent.putExtra("url", getIntent().getStringExtra("url"));
                                            startActivity(intent);
                                            sendBroadcast(mIntent);
                                            finish();

                                        } else {
//                                            sendBroadcast(mIntent);
//                                            finish();
//                                            MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");

                                            String wailianfou = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "wailianfou", "");
//                                            ToastUtil.showCenterToast(GestureVerifyActivity.this, wailianfou);

                                            if (wailianfou.equals("1")) {

                                                Intent intent = new Intent(GestureVerifyActivity.this, MainActivity6.class);
                                                intent.putExtra("url", (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "getLinkurl", ""));
                                                startActivity(intent);
                                                sendBroadcast(mIntent);
                                                finish();
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");

                                            } else {



                                                sendBroadcast(mIntent);
                                                finish();
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");

                                            }


                                        }


                                    }


                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    if (!network()) {
                                        Toast toast = new Toast(GestureVerifyActivity.this);
                                        View view1 = LayoutInflater.from(GestureVerifyActivity.this).inflate(R.layout.toast_layout, null);
                                        toast.setView(view1);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.setDuration(2000);
                                        toast.show();
                                        network = 1;
                                        mydismissloading();
                                        finish();
                                    }


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
        });
    }


    TextView message;
    private FingerprintCore mFingerprintCore;



    private void cancelFingerprintRecognition() {
        if (mFingerprintCore.isAuthenticating()) {
            mFingerprintCore.cancelAuthenticate();
//            resetGuideViewState();
        }
    }

    private void initFingerprintCore() {
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);

    }

    private FingerprintCore.IFingerprintResultListener mResultListener = new FingerprintCore.IFingerprintResultListener() {
        @Override
        public void onAuthenticateSuccess() {
            dialog.dismiss();


            if (!network()) {
                Toast toast = new Toast(GestureVerifyActivity.this);
                View view1 = LayoutInflater.from(GestureVerifyActivity.this).inflate(R.layout.toast_layout, null);
                toast.setView(view1);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(2000);
                toast.show();
                network = 1;
                finish();
            } else {
                final String pwd = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "password", "");
                MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");
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
                            public void onSuccess(String s, Call call, Response response) {
                                Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
//                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
//                                network = 1;
                                Gson gson = new Gson();
                                LoginBean loginBean = new LoginBean();
                                loginBean = gson.fromJson(s, LoginBean.class);
                                LoginBean.DataBean dataBean = loginBean.getData();
                                if (loginBean.getMeta().isSuccess() == false) {

                                    ToastUtil.showCenterToast(GestureVerifyActivity.this, "登录信息失效,请重新登录");
                                    startActivity(new Intent(GestureVerifyActivity.this, LoginActivity2.class));
                                    MySharePreferenceUtil.remove(GestureVerifyActivity.this, "token");
                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "token", "");
                                    MySharePreferenceUtil.remove(GestureVerifyActivity.this, "username");
                                    MySharePreferenceUtil.remove(GestureVerifyActivity.this, "phone");
                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");
                                    MySharePreferenceUtil.get(GestureVerifyActivity.this, phone + "flaggesture", "2");
                                    finish();
                                } else {
                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "id", loginBean.getData().getId());
                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "username", loginBean.getData().getUsername());
//                            MySharePreferenceUtil.put(LoginActivity.this, "idCard", loginBean.getData().getIdCard());
                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "phone", loginBean.getData().getPhone());
                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "phone1", loginBean.getData().getPhone());
                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "token", loginBean.getData().getToken());
                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "secName", loginBean.getData().getSecName());
                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "password", pwd);
                                    MySharePreferenceUtil.put(GestureVerifyActivity.this, "token1", loginBean.getData().getToken());
                                    if (network == 0) {
                                    } else {
                                        EventBus.getDefault().post(666);
                                    }
                                    try {
                                        if (loginBean.getData().getIdCard().length() < 5) {
                                            MySharePreferenceUtil.put(GestureVerifyActivity.this, "idCard", "");
                                        } else {
                                            MySharePreferenceUtil.put(GestureVerifyActivity.this, "idCard", loginBean.getData().getIdCard());
                                        }
                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "gender", loginBean.getData().getGender());
//                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "appHeadUrl", loginBean.getData().getAppHeadUrl());
                                    } catch (Exception e) {
                                    }


                                    if (TextUtils.isEmpty(loginBean.getData().getAppHeadUrl())) {
                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "appHeadUrl", "");
                                    } else {
                                        MySharePreferenceUtil.put(GestureVerifyActivity.this, "appHeadUrl", loginBean.getData().getAppHeadUrl());
                                    }

                                    Intent mIntent = new Intent(ACTION_NAME);
                                    mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                                    //发送广播
                                    String newintentflag = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "newintentflag", "");
                                    if (newintentflag.equals("5")) {
                                        Intent intent = new Intent(GestureVerifyActivity.this, MainActivity4.class);
                                        intent.putExtra("url", getIntent().getStringExtra("url"));
                                        startActivity(intent);
                                        sendBroadcast(mIntent);
                                        finish();

                                    } else {
//                                        ToastUtil.showCenterToast(GestureVerifyActivity.this,"执行到这里了么");

                                        String wailianfou = (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "wailianfou", "");
                                        if (wailianfou.equals("1")) {

                                            Intent intent = new Intent(GestureVerifyActivity.this, MainActivity6.class);
                                            intent.putExtra("url", (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "getLinkurl", ""));
                                            startActivity(intent);
                                            sendBroadcast(mIntent);
                                            finish();
                                            MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");

                                        } else {

                                            if (wailianfou.equals("1")) {

                                                Intent intent = new Intent(GestureVerifyActivity.this, MainActivity6.class);
                                                intent.putExtra("url", (String) MySharePreferenceUtil.get(GestureVerifyActivity.this, "getLinkurl", ""));
                                                startActivity(intent);
                                                sendBroadcast(mIntent);
                                                finish();
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");

                                            } else {

                                                sendBroadcast(mIntent);
                                                finish();
                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");

                                            }
                                          /*  sendBroadcast(mIntent);
                                            finish();
                                            MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");*/

                                        }


                                    }


                                }


                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
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
//            ToastUtil.showCenterToast(GestureVerifyActivity.this, helpId + "");
            if (helpId == 9) {
                dialog.dismiss();
                dialog = null;
            }

        }

        @Override
        public void onAuthenticateError(int errMsgId) {

//            ToastUtil.showCenterToast(GestureVerifyActivity.this, errMsgId + "");
            if (errMsgId == 7) {
                dialog.dismiss();
                dialog = null;
                Intent intent = new Intent(GestureVerifyActivity.this, LoginActivity2.class);
//                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "token");
                MySharePreferenceUtil.put(GestureVerifyActivity.this, "token", "");
                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "username");
                MySharePreferenceUtil.remove(GestureVerifyActivity.this, "phone");

                MySharePreferenceUtil.put(GestureVerifyActivity.this, "newintentflag", "2");

                startActivity(intent);
                finish();
            } else if (errMsgId == 9) {
                dialog.dismiss();
                dialog = null;
            }

//            ToastUtil.showCenterToast(GestureVerifyActivity.this, "授权失败");
        }

        @Override
        public void onStartAuthenticateResult(boolean isSuccess) {
//            ToastUtil.showCenterToast(GestureVerifyActivity.this, isSuccess + "");

        }
    };

    /**
     * 开始指纹识别
     */
    private void startFingerprintRecognition() {

        if (mFingerprintCore.isSupport()) {
            if (!mFingerprintCore.isHasEnrolledFingerprints()) {
//                toastTipMsg(R.string.fingerprint_recognition_not_enrolled);
//                ToastUtil.showCenterToast(GestureVerifyActivity.this, "没有开启指纹识别");
                FingerprintUtil2.openFingerPrintSettingPage(this);
                return;
            }

            mFingerprintCore.startAuthenticate();
            /* if (mFingerprintCore.isAuthenticating()) {
                ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this,"授权成功");
            } else {
                mFingerprintCore.startAuthenticate();
            }*/
        } else {
            ToastUtil.showCenterToast(GestureVerifyActivity.this, "设备不支持指纹");
        }
    }


}
