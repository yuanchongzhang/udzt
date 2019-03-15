package com.xmrxcaifu.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.xmrxcaifu.gesture.GestureContentView;
import com.xmrxcaifu.gesture.GestureDrawline;
import com.xmrxcaifu.gesture.LockIndicator;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.MySharePreferenceUtil;

import de.greenrobot.event.EventBus;


/**
 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟矫斤拷锟斤拷
 */
public class GestureMotifyActivity extends GeActivity implements OnClickListener {
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

    RelativeLayout relativitylayou_cancel;

    TextView text_tip2;
    String phone;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_motify);
        setImmersionStatus();
        statusBarUtil = new StatusBarUtil();
        text_tip2 = (TextView) findViewById(R.id.text_tip2);
        ImmersionBar.with(GestureMotifyActivity.this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        phone = (String) MySharePreferenceUtil.get(GestureMotifyActivity.this, "phone1", "");
        relativitylayou_cancel = (RelativeLayout) findViewById(R.id.relativitylayou_cancel);
        relativitylayou_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setUpViews();
        setUpListeners();

    }


    private final int REQUEST_CODE_CAMERA = 1, REQUEST_CODE_PERMISSIONS = 2;
    private final String[] PERMISSIONS = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.USE_FINGERPRINT};
    private AlertDialog alertDialog;

    //	private AlertDialog alertDialog1;
    private void requestPermission(int permission) {

        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_PERMISSIONS);

    }

    private boolean requestPermissionState = false;
    int zhiwen;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (string.equals("modify")) {
            EventBus.getDefault().post(75);
        } else {
            EventBus.getDefault().post(301);
        }
        return false;
    }

    private void setImmersionStatus() {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    String string;

    @SuppressLint("NewApi")
    private void setUpViews() {
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextCancel = (TextView) findViewById(R.id.text_cancel);
        mTextReset = (TextView) findViewById(R.id.text_reset);
        rl_gesture = (RelativeLayout) findViewById(R.id.rl_gesture);
        top_layout = (RelativeLayout) findViewById(R.id.top_layout);
        mTextReset.setClickable(false);


        mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);

        string = getIntent().getStringExtra("str1");
//        Toast.makeText(this, string + "", Toast.LENGTH_SHORT).show();

        if (string.equals("oncreate")) {
            mTextTitle.setText("创建手势密码");
        } else {
            mTextTitle.setText("设置手势密码");
        }


      /*
        if (gesture.equals("1")) {
            mTextTitle.setText("修改手势密码");
        } else {
            mTextTitle.setText("创建手势密码");
        }*/
        mGestureContentView = new GestureContentView(this, false, "",
                new GestureDrawline.GestureCallBack() {
                    @Override
                    public void onGestureCodeInput(String inputCode) {
                        if (!isInputPassValidate(inputCode)) {
                          /*  mTextTip.setText(Html
                                    .fromHtml("<font color='#c70c1e'>最少链接4个点, 请重新输入</font>"));*/
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
                                Toast.makeText(GestureMotifyActivity.this,
                                        "设置成功", Toast.LENGTH_SHORT).show();
                                mGestureContentView.clearDrawlineState(0L);
                                MySharePreferenceUtil.put(GestureMotifyActivity.this, phone+"shoushimima", inputCode);
                                MySharePreferenceUtil.put(GestureMotifyActivity.this, "flag_shoushi", "1");
//                                Intent intent = new Intent(GestureEditActivity.this, MainActivity.class);
//                                startActivity(intent);
                                MySharePreferenceUtil.put(GestureMotifyActivity.this, phone+"flaggesture", "1");
                                MySharePreferenceUtil.put(GestureMotifyActivity.this, "newintentflag", "2");
//                                startActivity(new Intent(GestureEditActivity.this, GestureVerifyActivity.class));

                                if (string.equals("oncreate")) {
                                    EventBus.getDefault().post(74);
                                } else {
                                    EventBus.getDefault().post(75);
                                }


                                finish();

                            } else {
//                                text_tip2
//                                mTextTip.setText(Html
//                                        .fromHtml("<font color='#c70c1e'>两次绘制不一致，请重新绘制</font>"));
                                text_tip2.setText(Html
                                        .fromHtml("<font color='#c70c1e'>与首次绘制不一致，请重新绘制</font>"));
                                // 锟斤拷锟斤拷锟狡讹拷锟斤拷锟斤拷
                                Animation shakeAnimation = AnimationUtils
                                        .loadAnimation(
                                                GestureMotifyActivity.this,
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
                MySharePreferenceUtil.put(GestureMotifyActivity.this, "flag_shoushi", "0");
                break;
            case R.id.text_reset:
                if (mTextReset.getText().toString().equals("暂不设置")) {
                    startMainActivity();
                    MySharePreferenceUtil.put(GestureMotifyActivity.this, "gesture", "0");
                    MySharePreferenceUtil.put(GestureMotifyActivity.this, "flag_shoushi", "0");
                    return;
                } else if (mTextReset.getText().toString().equals("重新设置")) {
                    text_tip2.setText("");
                }
                mIsFirstInput = true;
                updateCodeList("");
                mTextTip.setText(getString(R.string.set_gesture_pattern));
                mTextReset.setText("暂不设置");
                EventBus.getDefault().post(76);

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

}

