package com.xmrxcaifu.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.xmrxcaifu.R;
import com.xmrxcaifu.StatusBarUtil;
import com.xmrxcaifu.gesture.GestureContentView;
import com.xmrxcaifu.gesture.GestureDrawline;
import com.xmrxcaifu.gesture.LockIndicator;
import com.xmrxcaifu.inteface.OnDismissListener;
import com.xmrxcaifu.inteface.OnItemClickListener;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.MySharePreferenceUtil;

import de.greenrobot.event.EventBus;


/**
 * ���ƻ���/У�����
 */
public class GestureVerify_checkActivity extends GeActivity implements View.OnClickListener, OnItemClickListener, OnDismissListener {

    /**
     * 锟街伙拷锟斤拷锟�/ public static final String PARAM_PHONE_NUMBER =
     * "PARAM_PHONE_NUMBER"; /** 锟斤拷图
     */
    static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
    /**
     * 锟阶达拷锟斤拷示锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫，锟斤拷锟斤拷选锟斤拷锟斤拷锟�
     */
    public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
    private TextView mTextTitle;
    private TextView mTextCancel;
    private LockIndicator mLockIndicator;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;

    private String mParamSetUpcode = null;
    private String mParamPhoneNumber;
    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    private String mConfirmPassword = null;
    private int mParamIntentCode;

    private RelativeLayout top_layout;
    private SharedPreferences sha;

    StatusBarUtil statusBarUtil;
    private final String ACTION_NAME2 = "登录结束";

    RelativeLayout relativitylayou_cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_verifynew);
        setImmersionStatus();
        statusBarUtil = new StatusBarUtil();
        ImmersionBar.with(GestureVerify_checkActivity.this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        relativitylayou_cancel = (RelativeLayout) findViewById(R.id.relativitylayou_cancel);
        relativitylayou_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setUpViews();
        setUpListeners();

    }

    private final String[] PERMISSIONS = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.USE_FINGERPRINT};


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
      finish();
        return false;
    }

    private void setImmersionStatus() {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    String string;
String str_cod="";
    @SuppressLint("NewApi")
    private void setUpViews() {
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextCancel = (TextView) findViewById(R.id.text_cancel);

        top_layout = (RelativeLayout) findViewById(R.id.top_layout);


        mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);

        string = getIntent().getStringExtra("str1");

        if (string.equals("oncreate")) {
            mTextTitle.setText("创建手势密码");
        } else {
            mTextTitle.setText("修改手势密码");
        }
          String phone1 = (String) MySharePreferenceUtil.get(GestureVerify_checkActivity.this, "phone", "");

        String mima = (String) MySharePreferenceUtil.get(GestureVerify_checkActivity.this, phone1+"shoushimima", "");
//        Toast.makeText(this, mima+"", Toast.LENGTH_SHORT).show();

        mGestureContentView = new GestureContentView(this, true, mima,
                new GestureDrawline.GestureCallBack() {

                    @Override
                    public void onGestureCodeInput(String inputCode) {

                    }

                    @Override
                    public void checkedSuccess() {
                        mGestureContentView.clearDrawlineState(0L);
//                        startActivity(new Intent(GestureVerify_checkActivity.this, GestureMotifyActivity.class));


                        Intent intent = new Intent(GestureVerify_checkActivity.this, GestureMotifyActivity.class);
                        intent.putExtra("str1", getIntent().getStringExtra("str1"));
                        startActivity(intent);

                        finish();
                    }

                    @Override
                    public void checkedFail() {
                        error++;
                        mTextTip.setVisibility(View.VISIBLE);
//                        updateCodeList(str_cod);
//                        Toast.makeText(GestureVerify_checkActivity.this, str_cod+"", Toast.LENGTH_SHORT).show();
                        mLockIndicator.setPath(str_cod);
                     /*   if (error == 5) {
                            mTextTip.setText(Html.fromHtml("<font color='#E55835'>密码错误5次,请重新登录</font>"));
                        } else {
                            mTextTip.setText(Html.fromHtml("<font color='#E55835'>错误次数" + error + "次，" + "您还可以输入" + (5 - error) + "次</font>"));
                        }*/
                        mTextTip.setText(Html.fromHtml("<font color='#E55835'>原手势密码不正确</font>"));
                        mGestureContentView.clearDrawlineState(1000L);
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerify_checkActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);


                    }
                });

      /*  mGestureContentView = new GestureContentView(this, false, mima,
                new GestureDrawline.GestureCallBack() {
                    @Override
                    public void onGestureCodeInput(String inputCode) {

                        updateCodeList(inputCode);

                    }

                    @Override
                    public void checkedSuccess() {
                        mGestureContentView.clearDrawlineState(0L);
//                        startActivity(new Intent(GestureVerify_checkActivity.this, GestureMotifyActivity.class));


                        Intent intent = new Intent(GestureVerify_checkActivity.this, GestureMotifyActivity.class);
                        intent.putExtra("str1", getIntent().getStringExtra("str1"));
                        startActivity(intent);

                        finish();
                    }

                    @Override
                    public void checkedFail() {
                        Toast.makeText(GestureVerify_checkActivity.this, "失败", Toast.LENGTH_SHORT).show();

                        error++;
                        mTextTip.setVisibility(View.VISIBLE);

                        if (error == 5) {
                            mTextTip.setText(Html.fromHtml("<font color='#E55835'>密码错误5次,请重新登录</font>"));
                        } else {
                            mTextTip.setText(Html.fromHtml("<font color='#E55835'>错误次数" + error + "次，" + "您还可以输入" + (5 - error) + "次</font>"));
                        }
                        mGestureContentView.clearDrawlineState(1000L);
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerify_checkActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                        updateCodeList("");

                    }
                });*/
        // 锟斤拷锟斤拷锟斤拷锟狡斤拷锟斤拷锟斤拷示锟斤拷锟侥革拷锟斤拷锟斤拷锟斤拷锟斤拷
        mGestureContentView.setParentView(mGestureContainer);
        updateCodeList("");
    }

    private void setUpListeners() {
        mTextCancel.setOnClickListener(this);

    }

    private void updateCodeList(String inputCode) {
        // 锟斤拷锟斤拷选锟斤拷锟酵硷拷锟�
        mLockIndicator.setPath(inputCode);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    private int error = 0;

    private void startMainActivity() {
//        EventBus.getDefault().post(75);
        if (string.equals("modify")) {
            EventBus.getDefault().post(75);
        } else {
            EventBus.getDefault().post(301);
        }

        finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_cancel:
//                this.finish();
                startMainActivity();

                break;

            default:
                break;
        }
    }


    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(Object o, int position) {

    }

    @Override
    public void onDismiss(Object o) {

    }


    /*
    public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";

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

    private TextView mTextOther;
    private String mParamPhoneNumber;
    private long mExitTime = 0;
    private int mParamIntentCode;
    private SharedPreferences sha;




    private int[] screenDispaly;
    //手势密码页面

    String zhiwen;

    TextView text_figer;
    TextView text_other_account;
//这是用户名呢



    ImageView user_existlogo;
    String phone;

    RelativeLayout relativitylayou_cancel;

    TextView text_cancel;
    private LockIndicator mLockIndicator;
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().post(102);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_verifynew);
        setImmersionStatus();

        zhiwen = (String) MySharePreferenceUtil.get(GestureVerify_checkActivity.this, "zhiwendenglu", "");
        text_other_account = (TextView) findViewById(R.id.text_other_account);
        text_other_account.setOnClickListener(this);
        setUpViews();
        user_existlogo = (ImageView) findViewById(R.id.user_existlogo);
        mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
        phone = (String) MySharePreferenceUtil.get(GestureVerify_checkActivity.this, "phone", "");
        String gender = (String) MySharePreferenceUtil.get(GestureVerify_checkActivity.this, "gender", "");
        String appHeadUrl = (String) MySharePreferenceUtil.get(GestureVerify_checkActivity.this, "appHeadUrl", "");
        String secName = (String) MySharePreferenceUtil.get(GestureVerify_checkActivity.this, "secName", "");
        builder2 = new MyLoadingDialog.Builder(this)
                .setShowMessage(false)
                .setCancelable(false);
        dialog2 = builder2.create();

        relativitylayou_cancel= (RelativeLayout) findViewById(R.id.relativitylayou_cancel);
        relativitylayou_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text_cancel= (TextView) findViewById(R.id.text_cancel);
        text_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void updateCodeList(String inputCode) {
        // 锟斤拷锟斤拷选锟斤拷锟酵硷拷锟�
        mLockIndicator.setPath(inputCode);
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



    private void setUpViews() {
        mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextCancel = (TextView) findViewById(R.id.text_cancel);
//		mImgUserLogo = (ImageView) findViewById(R.id.user_logo);
        sha = getSharedPreferences("config", MODE_PRIVATE);


        mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);

        setUpListeners();
        mTextTip.setText("请绘制手势密码");

        String mima = (String) MySharePreferenceUtil.get(GestureVerify_checkActivity.this, "shoushimima", "");

        mGestureContentView = new GestureContentView(this, true, mima,
                new GestureDrawline.GestureCallBack() {

                    @Override
                    public void onGestureCodeInput(String inputCode) {

                    }

                    @Override
                    public void checkedSuccess() {
                        mGestureContentView.clearDrawlineState(0L);
//                        startActivity(new Intent(GestureVerify_checkActivity.this, GestureMotifyActivity.class));


                        Intent intent = new Intent(GestureVerify_checkActivity.this, GestureMotifyActivity.class);
                        intent.putExtra("str1", getIntent().getStringExtra("str1"));
                        startActivity(intent);

                        finish();
                    }

                    @Override
                    public void checkedFail() {
                        error++;
                        mTextTip.setVisibility(View.VISIBLE);

                        if (error == 5) {
                            mTextTip.setText(Html.fromHtml("<font color='#E55835'>密码错误5次,请重新登录</font>"));
                        } else {
                            mTextTip.setText(Html.fromHtml("<font color='#E55835'>错误次数" + error + "次，" + "您还可以输入" + (5 - error) + "次</font>"));
                        }
                        mGestureContentView.clearDrawlineState(1000L);
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerify_checkActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);


                    }*//*
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
                MySharePreferenceUtil.put(GestureVerify_checkActivity.this, "newintentflag", "2");
                EventBus.getDefault().post(2);

                this.finish();
                break;

            case R.id.text_other_account:

                Intent intent = new Intent(GestureVerify_checkActivity.this, LoginActivity2.class);

                startActivity(intent);
                finish();


                break;


        }
    }

    public void showToast(String name) {
        Toast.makeText(GestureVerify_checkActivity.this, name, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {

    }


    MyLoadingDialog.Builder builder2;
    LoadingDialog dialog2;

    public void myshowloading() {

        dialog2.show();
    }
    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }
    public void mydismissloading() {
        dialog2.dismiss();
    }*/
}
