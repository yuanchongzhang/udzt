package com.xmrxcaifu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import com.xmrxcaifu.BitmapUtils;
import com.xmrxcaifu.Constant;
import com.xmrxcaifu.MainActivity;

import com.xmrxcaifu.R;
import com.xmrxcaifu.http.MyOkhttp;
import com.xmrxcaifu.http.callback.StringNoDialogCallback;
import com.xmrxcaifu.http.request.BaseRequest;
import com.xmrxcaifu.httpmodel.LoginBean;
import com.xmrxcaifu.roundcircle.RoundImageView;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.Circle;
import com.xmrxcaifu.util.FingerprintUtil;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.util.ToastUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

public class V6_Finger_Check_NewActivity2 extends BaseActivty2 {
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

    public static V6_Finger_Check_NewActivity2 instance;

    String phone;
    private final String ACTION_NAME = "智投宝结束";
    ImageView user_existlogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v6_finger_check);
//        BbtApplication.getInstance().addActvity(this);
        sha = getSharedPreferences("config", MODE_PRIVATE);
        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
//        img_finger_bg = (ImageView) findViewById(img_finger_bg);
//
//        setImmersionStatus();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 1f)

                .init();
        layout_yijuhua = (LinearLayout) findViewById(R.id.layout_yijuhua);
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
                onFingerprintClick(null);
            }
        });
        phone = (String) MySharePreferenceUtil.get(this, "phone", "");
        token = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity2.this, "mytoken", "");

        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();//

        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        onFingerprintClick(null);
        user_logo = (RoundImageView) findViewById(R.id.user_logo);

        String token = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity2.this, "mytoken", "");


        try {
            token = URLEncoder.encode(token, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String sex = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity2.this, "sex", "");
        String userName = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity2.this, "userNameText", "");
        final String userMobile = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity2.this, "userMobile", "");

        String welcomeName = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity2.this, "welcomeName", "");
        String appHeadUrl = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity2.this, "appHeadUrl", "");
        String gender = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity2.this, "gender", "");
        if (appHeadUrl.isEmpty()) {
            user_logo.setVisibility(View.VISIBLE);
            if (gender.equals("1")) {
                user_logo.setImageBitmap(BitmapUtils.readBitMap(V6_Finger_Check_NewActivity2.this, R.drawable.img_girl));
            } else {
                user_logo.setImageBitmap(BitmapUtils.readBitMap(V6_Finger_Check_NewActivity2.this, R.drawable.img_boy));
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




        /*if (userName.equals("") || userName.equals("null")) {
//                        text_welcome.setText(sha.getString("mobile", "").substring(0, 3) + "****" + sha.getString("mobile", "").substring(7, sha.getString("mobile", "").length()));
            text_welcome.setText(userMobile.substring(0, 3) + "****" + userMobile.substring(7, userMobile.length()));
        } else {
            text_welcome.setText("欢迎您， " + welcomeName);
        }*/
        String secName = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity2.this, "secName", "");
        if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity2.this, "idCard", ""))) {
            text_welcome.setText(phone.substring(0, 3) + "****" + phone.substring(7, phone.length()));
        } else {
            text_welcome.setText("欢迎您， " + secName);
        }

        text_login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent1 = null;
                intent1 = new Intent(V6_Finger_Check_NewActivity2.this, LoginActivity.class);
                intent1.putExtra("url", getIntent().getStringExtra("url"));
                intent1.putExtra("flag", 1);
                intent1.putExtra("share", 1);
                intent1.putExtra("splash", 1);
                intent1.putExtra("titleview", 1);
                intent1.putExtra("title", "优点智投");
                startActivity(intent1);
                finish();


                V6_Finger_Check_NewActivity2.this.finish();
            }
        });


    }

    private long mLastBackTime = 0;
    private long TIME_DIFF = 2 * 1000;
    LinearLayout layout_yijuhua;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long now = new Date().getTime();
            if (now - mLastBackTime < TIME_DIFF) {


                android.os.Process.killProcess(android.os.Process.myPid());
                //BbtApplication.getInstance().exit();

                finish();
                return super.onKeyDown(keyCode, event);
            } else {
                mLastBackTime = now;
                Toast.makeText(this, "再按一次退出优点智投", 2000).show();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onFingerprintClick(View v) {

        FingerprintUtil.callFingerPrint(new FingerprintUtil.OnCallBackListenr() {

            @Override
            public void onSupportFailed() {
                ToastUtil.showCenterToast(V6_Finger_Check_NewActivity2.this, "当前设备不支持指纹");
            }

            @Override
            public void onInsecurity() {

                ToastUtil.showCenterToast(V6_Finger_Check_NewActivity2.this, "当前设备未处于安全保护中");
            }

            @Override
            public void onEnrollFailed() {
                ToastUtil.showCenterToast(V6_Finger_Check_NewActivity2.this, "请到设置中设置指纹");

            }

            @Override
            public void onAuthenticationStart() {
                layout_yijuhua.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showPopupWindow2(layout_yijuhua);
                    }
                }, 1);

            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
//                showToast(errString.toString());
              /*  ToastUtil.showCenterToast(V6_Finger_Check_NewActivity.this, errString.toString());
//   Log.e("dialog", builder.create()+"");
                if (builder.create() != null) {
                    builder.create().mdissmis();
                }*/

                /*builder.setMessage("再试一次");
                builder.setMessage2("请验证已有指纹验证");
//                builder.setMessage("再试一次");
                showToast("解锁失败");*/
//                showToast("解锁失败");

            }

            @Override
            public void onAuthenticationFailed() {


                message.setVisibility(View.VISIBLE);
                message.setText("再试一次");
                layout_shuirumima.setVisibility(View.GONE);
                layout_mima.setVisibility(View.GONE);
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
//                showToast(helpString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {

                ToastUtil.showCenterToast(V6_Finger_Check_NewActivity2.this, "解锁成功");


                final String pwd = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity2.this, "password", "");

                MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "newintentflag", "2");
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

                                    ToastUtil.showCenterToast(V6_Finger_Check_NewActivity2.this, "登录信息失效,请重新登录");
                                    startActivity(new Intent(V6_Finger_Check_NewActivity2.this, LoginActivity2.class));
                                    MySharePreferenceUtil.remove(V6_Finger_Check_NewActivity2.this, "token");
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "token", "");
                                    MySharePreferenceUtil.remove(V6_Finger_Check_NewActivity2.this, "username");


                                    MySharePreferenceUtil.remove(V6_Finger_Check_NewActivity2.this, "phone");

                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "newintentflag", "2");
                                    MySharePreferenceUtil.get(V6_Finger_Check_NewActivity2.this, phone+"flaggesture", "2");
                                    finish();
                                } else {


                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "id", loginBean.getData().getId());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "username", loginBean.getData().getUsername());
//                            MySharePreferenceUtil.put(LoginActivity.this, "idCard", loginBean.getData().getIdCard());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "phone", loginBean.getData().getPhone());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "phone1", loginBean.getData().getPhone());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "token", loginBean.getData().getToken());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "secName", loginBean.getData().getSecName());
                                    MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "password", pwd);

                                    try {
                                        if (loginBean.getData().getIdCard().length() < 5) {
                                            MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "idCard", "");
                                        } else {
                                            MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "idCard", loginBean.getData().getIdCard());
                                        }
                                        MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "gender", loginBean.getData().getGender());
//                                                MySharePreferenceUtil.put(GestureVerifyActivity.this, "appHeadUrl", loginBean.getData().getAppHeadUrl());
                                    } catch (Exception e) {
                                    }


                                    if (TextUtils.isEmpty(loginBean.getData().getAppHeadUrl())) {
                                        MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "appHeadUrl", "");
                                    } else {
                                        MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "appHeadUrl", loginBean.getData().getAppHeadUrl());
                                    }

                                    Intent mIntent = new Intent(ACTION_NAME);
                                    mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                                    //发送广播
                                    String newintentflag = (String) MySharePreferenceUtil.get(V6_Finger_Check_NewActivity2.this, "newintentflag", "");
                                    if (newintentflag.equals("5")) {
                                        Intent intent = new Intent(V6_Finger_Check_NewActivity2.this, MainActivity.class);
                                        intent.putExtra("url", getIntent().getStringExtra("url"));
                                        startActivity(intent);
                                        sendBroadcast(mIntent);
                                        finish();

                                    } else {
                                        sendBroadcast(mIntent);
                                        finish();
                                        MySharePreferenceUtil.put(V6_Finger_Check_NewActivity2.this, "newintentflag", "2");

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
        });
    }

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

    private void showPopupWindow2(View parent) {
        if (popWindow2 == null) {
            View view2 = LayoutInflater.from(this)
                    .inflate(R.layout.popwindow_figer, null);
            //LayoutParams相当于一个Layout的信息包，它封装了Layout的位置、高、宽等信息。
            popWindow2 = new PopupWindow(view2, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            initPop2(view2);
        }

        //设置动画效果
        popWindow2.setAnimationStyle(android.R.style.Animation_InputMethod);
        //获取popwindow焦点
        popWindow2.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        popWindow2.setOutsideTouchable(true);
        popWindow2.setBackgroundDrawable(new BitmapDrawable());
        //实现软键盘不自动弹出,ADJUST_RESIZE属性表示Activity的主窗口总是会被调整大小，从而保证软键盘显示空间。
        popWindow2.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popwindow显示位置
        popWindow2.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public void initPop2(View view) {
        Button cancel_btn = (Button) view.findViewById(R.id.cancel_btn);
        message = (TextView) view.findViewById(R.id.message);
        layout_mima = view.findViewById(R.id.layout_mima);
        layout_shuirumima = (Button) view.findViewById(R.id.layout_shuirumima);
        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow2.dismiss();
                layout_mima.setVisibility(View.GONE);
                layout_mima.setVisibility(View.GONE);
                popWindow2 = null;
                ToastUtil.showCenterToast(V6_Finger_Check_NewActivity2.this, "指纹操作已取消");

            }
        });
        layout_shuirumima.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popWindow2.dismiss();
                layout_mima.setVisibility(View.GONE);
                layout_mima.setVisibility(View.GONE);
                popWindow2 = null;
            }
        });

    }

}

