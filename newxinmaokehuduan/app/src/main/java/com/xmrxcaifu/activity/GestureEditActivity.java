package com.xmrxcaifu.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.xmrxcaifu.R;
import com.xmrxcaifu.StatusBarUtil;
import com.xmrxcaifu.core.FingerprintCore;
import com.xmrxcaifu.dialog.CustomDialog;
import com.xmrxcaifu.dialog.CustomDialog_view_new;
import com.xmrxcaifu.gesture.GestureContentView;
import com.xmrxcaifu.gesture.GestureDrawline;
import com.xmrxcaifu.gesture.LockIndicator;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.FingerPrintHelp;
import com.xmrxcaifu.util.FingerSetting;
import com.xmrxcaifu.util.FingerprintUtil;
import com.xmrxcaifu.util.KeyguardLockScreenManager;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.util.ToastUtil;

import de.greenrobot.event.EventBus;


/**
 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟矫斤拷锟斤拷
 */
public class GestureEditActivity extends GeActivity implements OnClickListener {
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
    private TextView mTextReset;
    private String mParamSetUpcode = null;
    private String mParamPhoneNumber;
    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    private String mConfirmPassword = null;
    private int mParamIntentCode;
    private RelativeLayout rl_gesture;
    private RelativeLayout top_layout;
    private SharedPreferences sha;

    StatusBarUtil statusBarUtil;
    private final String ACTION_NAME2 = "登录结束";

    String phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_edit);
        setImmersionStatus();
        statusBarUtil = new StatusBarUtil();
        initFingerprintCore();
        /**
         * 获取SharedPreferenced对象
         * 第一个参数是生成xml的文件名
         * 第二个参数是存储的格式（**注意**本文后面会讲解）
         */
        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        ImmersionBar.with(this)
                .fullScreen(true)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .init();
        setUpViews();
        setUpListeners();
        phone = (String) MySharePreferenceUtil.get(GestureEditActivity.this, "phone1", "");
    }


    private final int REQUEST_CODE_CAMERA = 1, REQUEST_CODE_PERMISSIONS = 2;
    private final String[] PERMISSIONS = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.USE_FINGERPRINT};
    private AlertDialog alertDialog;

    //	private AlertDialog alertDialog1;
    private void requestPermission(int permission) {

        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_PERMISSIONS);

    }

    private boolean requestPermissionState = false;
    String zhiwen;
    CustomDialog.Builder customDialog;
    private CustomDialog_view_new.Builder builder;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    private void setImmersionStatus() {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    String zhiwendenglu;
    SharedPreferences sharedPreferences;

    @SuppressLint("NewApi")
    private void setUpViews() {
        zhiwendenglu = (String) MySharePreferenceUtil.get(this, phone + "zhiwendenglu", "");
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextCancel = (TextView) findViewById(R.id.text_cancel);
        mTextReset = (TextView) findViewById(R.id.text_reset);
        rl_gesture = (RelativeLayout) findViewById(R.id.rl_gesture);
        top_layout = (RelativeLayout) findViewById(R.id.top_layout);
        mTextReset.setClickable(false);
        sha = getSharedPreferences("config", MODE_PRIVATE);
        zhiwen = (String) MySharePreferenceUtil.get(this, "finger", "");


        mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
//		rl_gesture.setBackground(new BitmapDrawable(BitmapUtils.readBitMap(
//				GestureEditActivity.this, R.drawable.shoushi)));
        // 锟斤拷始锟斤拷一锟斤拷锟斤拷示锟斤拷锟斤拷锟斤拷锟絭iewGroup
        mGestureContentView = new GestureContentView(this, false, "",
                new GestureDrawline.GestureCallBack() {
                    @Override
                    public void onGestureCodeInput(String inputCode) {
                        if (!isInputPassValidate(inputCode)) {
                            mTextTip.setText(Html
                                    .fromHtml("<font color='#c70c1e'>最少链接4个点, 请重新输入</font>"));
                            mGestureContentView.clearDrawlineState(0L);
                            return;
                        }
                        if (mIsFirstInput) {
                            mFirstPassword = inputCode;
                            updateCodeList(inputCode);
                            mGestureContentView.clearDrawlineState(0L);
                            mTextReset.setClickable(true);
                            mTextReset
                                    .setText("重新设置");
                            mTextTip.setText("再次绘制解锁图案");
                        } else {
                            if (inputCode.equals(mFirstPassword)) {

                                Log.d("mima", inputCode);
                                Log.d("mima", inputCode);
                                Toast.makeText(GestureEditActivity.this,
                                        "设置成功", Toast.LENGTH_SHORT).show();
                                mGestureContentView.clearDrawlineState(0L);
                                MySharePreferenceUtil.put(GestureEditActivity.this, phone + "shoushimima", inputCode);
                                MySharePreferenceUtil.put(GestureEditActivity.this, phone + "flaggesture", "1");
                                MySharePreferenceUtil.put(GestureEditActivity.this, "newintentflag", "2");

                                MySharePreferenceUtil.put(GestureEditActivity.this, "flag_shoushi", "1");

                                MySharePreferenceUtil.put(GestureEditActivity.this, "new_intent", "1");
//获取到edit对象
                                MySharePreferenceUtil.put(GestureEditActivity.this, "shoushisuccess", "1");
                                Intent mIntent2 = new Intent(ACTION_NAME);
                                mIntent2.putExtra("yaner", "发送广播，相当于在这里传送数据");
                                //发送广播
                                sendBroadcast(mIntent2);
//                                zhiwen
                                if (zhiwen.equals("0")) {
                                    Intent mIntent = new Intent(ACTION_NAME);
                                    mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                                    //发送广播
//                                    sendBroadcast(mIntent);

                                    MySharePreferenceUtil.put(GestureEditActivity.this, "newintentflag", "2");


                                    MySharePreferenceUtil.put(GestureEditActivity.this, phone + "flaggesture", "1");


                                    finish();
                                } else if (zhiwen.equals("1")) {
//                                     && !zhiwendenglu.equals("1")

                                    showDialog();
                                } else {
                                    startMainActivity();
//                                    MySharePreferenceUtil.putout(GestureEditActivity.this,"flag_shoushi","0");

                                }


                            } else {

                                mTextTip.setText(Html
                                        .fromHtml("<font color='#c70c1e'>两次绘制不一致，请重新绘制</font>"));
                                // 锟斤拷锟斤拷锟狡讹拷锟斤拷锟斤拷
                                Animation shakeAnimation = AnimationUtils
                                        .loadAnimation(
                                                GestureEditActivity.this,
                                                R.anim.shake);
                                mTextTip.startAnimation(shakeAnimation);
                                // 锟斤拷锟街伙拷锟狡碉拷锟竭ｏ拷1.5锟斤拷锟斤拷锟斤拷
                                mGestureContentView.clearDrawlineState(1300L);
                            }
                        }
                        mIsFirstInput = false;
                    }

                    @Override
                    public void checkedSuccess() {

                    }

                    @Override
                    public void checkedFail() {

                    }
                });
        // 锟斤拷锟斤拷锟斤拷锟狡斤拷锟斤拷锟斤拷示锟斤拷锟侥革拷锟斤拷锟斤拷锟斤拷锟斤拷
        mGestureContentView.setParentView(mGestureContainer);
        updateCodeList("");
    }

    private void setUpListeners() {
        mTextCancel.setOnClickListener(this);
        mTextReset.setOnClickListener(this);
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

    private final String ACTION_NAME = "智投宝结束";

    private void startMainActivity() {
/*/*
        Intent mIntent = new Intent(ACTION_NAME);
        mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
        //发送广播
        sendBroadcast(mIntent);*/


        MySharePreferenceUtil.put(GestureEditActivity.this, "newintentflag", "2");


        finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_cancel:
//                this.finish();
                startMainActivity();
                break;
            case R.id.text_reset:
                if (mTextReset.getText().toString().equals("暂不设置")) {

                    if (zhiwen.equals("1")) {
                        showDialog();
                    } else {
                        startMainActivity();
                    }
                    MySharePreferenceUtil.put(GestureEditActivity.this, "gesture", "0");
                    MySharePreferenceUtil.put(GestureEditActivity.this, phone + "flaggesture", "0");
                    MySharePreferenceUtil.put(GestureEditActivity.this, "flag_shoushi", "0");
                    MySharePreferenceUtil.put(GestureEditActivity.this, "shoushisuccess", "0");
                    return;
                }
                mIsFirstInput = true;
                updateCodeList("");
                mTextTip.setText(getString(R.string.set_gesture_pattern));
                mTextReset.setText("暂不设置");
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


    private void showDialog() {
//        startFingerprintRecognition();
        customDialog = new CustomDialog.Builder(GestureEditActivity.this);
        String flaggesture = (String) MySharePreferenceUtil.get(GestureEditActivity.this, phone + "flaggesture", "");

        if (flaggesture.equals("1")) {
            customDialog.setMessage("手势密码设置成功，是否启用指纹解锁");
        } else if (flaggesture.equals("0")) {
            customDialog.setMessage("是否启用指纹解锁");
        } else {
            customDialog.setMessage("是否启用指纹解锁");
        }


        customDialog.create().setCancelable(false);

        customDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                onFingerprintClick(null);

                /*   customDialog.create().dismiss();
                builder = new CustomDialog_view_new.Builder(GestureEditActivity.this);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post(998);

                        FingerprintUtil.cancel();
                        builder.create().dismiss();
                        MySharePreferenceUtil.put(GestureEditActivity.this, phone + "zhiwendenglu", 0 + "");
//                        startMainActivity();
                        cancelFingerprintRecognition();
                    }
                });
                builder.setMessage("请验证已有手机指纹");

                builder.setMessage2("");
                builder.create().setCancelable(false);
                builder.create().show();
*/
            }
        });
        customDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

//                EventBus.getDefault().post(10);

                MySharePreferenceUtil.put(GestureEditActivity.this, "openfinger", "0");
                MySharePreferenceUtil.put(GestureEditActivity.this, phone + "zhiwendenglu", 0 + "");
                Intent mIntent = new Intent(ACTION_NAME);
                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                //发送广播
                sendBroadcast(mIntent);
                MySharePreferenceUtil.put(GestureEditActivity.this, "newintentflag", "2");
                finish();
            }
        });
        customDialog.create().show();
    }


    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    public void onFingerprintClick(View v) {

        FingerPrintHelp.callFingerPrint(new FingerPrintHelp.OnCallBackListenr() {

            @Override
            public void onSupportsuccess() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSupportFailed() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onInsecurity() {
                // TODO Auto-generated method stub
//			FingerSetting.openFingerPrintSettingPage(GestureEditActivity.this);
//			FingerprintUtil.cancel();
                customDialog.create().dismiss();
                customDialog = new CustomDialog.Builder(GestureEditActivity.this);

                customDialog.setMessage("请在系统设置中开启指纹功能");
                customDialog.create().setCancelable(false);
                customDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        FingerSetting.openFingerPrintSettingPage(GestureEditActivity.this);
                        startMainActivity();
                    }
                });
                customDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        customDialog.create().dismiss();
                        startMainActivity();

                        Intent mIntent = new Intent(ACTION_NAME);
                        mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                        //发送广播
                        sendBroadcast(mIntent);
                    }
                });
                customDialog.create().show();


            }

            @Override
            public void onFInsecurity() {
                // TODO Auto-generated method stub
                customDialog.create().dismiss();
                onFingerprint(null);


            }

            @Override
            public void onEnrollFailed() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAuthenticationStart() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAuthenticationFailed() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                // TODO Auto-generated method stub

            }
        });
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    public void onFingerprint(View v) {

        FingerprintUtil.callFingerPrint(new FingerprintUtil.OnCallBackListenr() {

            @Override
            public void onSupportFailed() {
//                showToast("当前设备不支持指纹");

                ToastUtil.showCenterToast(GestureEditActivity.this, "当前设备不支持指纹");
            }

            @Override
            public void onInsecurity() {

            }

            @Override
            public void onEnrollFailed() {
//                showToast("请到设置中设置指纹");
//                ToastUtil.showCenterToast(GestureEditActivity.this, "请到设置中设置指纹");
            }

            @Override
            public void onAuthenticationStart() {
                Log.e("start", "finger_start");
                builder = new CustomDialog_view_new.Builder(GestureEditActivity.this);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FingerprintUtil.cancel();
                        builder.create().dismiss();
                        MySharePreferenceUtil.put(GestureEditActivity.this, phone + "zhiwendenglu", 0 + "");
//                        startMainActivity();
                        cancelFingerprintRecognition();
                    }
                });
                builder.setMessage("请验证已有手机指纹");

                builder.setMessage2("");
                builder.create().setCancelable(false);
                builder.create().show();
            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {


//                ToastUtil.showCenterToast(GestureEditActivity.this, errString.toString());
//            Log.e("dialog", builder.create()+"");

            }

            @Override
            public void onAuthenticationFailed() {

//                ToastUtil.showCenterToast(GestureEditActivity.this, "认证失败");

            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {


//                ToastUtil.showCenterToast(GestureEditActivity.this, helpString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {

//                ToastUtil.showCenterToast(GestureEditActivity.this, "认证成功");
                MySharePreferenceUtil.put(GestureEditActivity.this, phone + "zhiwendenglu", 1 + "");
                MySharePreferenceUtil.put(GestureEditActivity.this, "openfinger", "1");
                Intent mIntent = new Intent(ACTION_NAME);
                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                //发送广播
                sendBroadcast(mIntent);

                MySharePreferenceUtil.put(GestureEditActivity.this, "newintentflag", "2");

                finish();
            }
        });
    }


    private FingerprintCore mFingerprintCore;
    private KeyguardLockScreenManager mKeyguardLockScreenManager;

    private void initFingerprintCore() {
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
        mKeyguardLockScreenManager = new KeyguardLockScreenManager(this);
    }

    private FingerprintCore.IFingerprintResultListener mResultListener = new FingerprintCore.IFingerprintResultListener() {
        @Override
        public void onAuthenticateSuccess() {

//            ToastUtil.showCenterToast(GestureEditActivity.this, "认证成功");

            MySharePreferenceUtil.put(GestureEditActivity.this, phone + "zhiwendenglu", 1 + "");
            MySharePreferenceUtil.put(GestureEditActivity.this, "openfinger", "1");
            Intent mIntent = new Intent(ACTION_NAME);
            mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
            //发送广播
//            sendBroadcast(mIntent);

            MySharePreferenceUtil.put(GestureEditActivity.this, "newintentflag", "2");

            finish();
        }

        @Override
        public void onAuthenticateFailed(int helpId) {
//            ToastUtil.showCenterToast(GestureEditActivity.this, "识别失败");
            builder.create().dismiss();


            if (builder.create() != null) {
                builder.create().cancel();
                builder.create().mdissmis();
                cancelFingerprintRecognition();
            }

        }

        @Override
        public void onAuthenticateError(int errMsgId) {
//            ToastUtil.showCenterToast(GestureEditActivity.this, "识别错误");
        }

        @Override
        public void onStartAuthenticateResult(boolean isSuccess) {

        }
    };

    /**
     * 开始指纹识别
     */
    private void startFingerprintRecognition() {
        mFingerprintCore.startAuthenticate();
    }

    private void cancelFingerprintRecognition() {
        if (mFingerprintCore.isAuthenticating()) {
            mFingerprintCore.cancelAuthenticate();

        }
    }
}

