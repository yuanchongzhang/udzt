package com.xmrxcaifu.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;

import com.xmrxcaifu.Constant;

import com.xmrxcaifu.R;
import com.xmrxcaifu.http.MyOkhttp;
import com.xmrxcaifu.http.adapter.Call;
import com.xmrxcaifu.http.callback.StringNoDialogCallback;
import com.xmrxcaifu.http.model.Response;
import com.xmrxcaifu.http.request.BaseRequest;
import com.xmrxcaifu.httpmodel.LoginBean;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.util.Regular;
import com.xmrxcaifu.util.ToastUtil;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


/**
 * Created by Administrator on 2018/3/8.
 */

public class LoginActivity extends BaseActivty2 {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_top_layout)
    RelativeLayout rlTopLayout;
    @BindView(R.id.user_phone_num)
    EditText userPhoneNum;
    @BindView(R.id.img_clear_user)
    ImageView imgClearUser;

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_messagelogin)
    TextView tvMessagelogin;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    @BindView(R.id.user_password)
    EditText userPassword;
    @BindView(R.id.img_clear)
    ImageView imgClear;
    @BindView(R.id.img_pass)
    ImageView imgPass;
    @BindView(R.id.li_opera)
    LinearLayout liOpera;
    String deviceId;
    private final String ACTION_NAME2 = "登录结束";
    @BindView(R.id.text_phone)
    TextView textPhone;
    @BindView(R.id.text_duanxincode)
    TextView textDuanxincode;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME2)) {
                Intent mIntent = new Intent(ACTION_NAME);
                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                //发送广播
                sendBroadcast(mIntent);
                finish();
            }
        }

    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME2);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        deviceId = (String) MySharePreferenceUtil.get(LoginActivity.this, "deviceid", "");
        registerBoradcastReceiver();
//        StatusBarUtil.StatusBarLightMode(LoginActivity.this);
        ImmersionBar.with(LoginActivity.this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        if (userPhoneNum.getText().toString().equals("")) {
            imgClearUser.setVisibility(View.GONE);
        } else {
            imgClearUser.setVisibility(View.VISIBLE);
        }
        userPhoneNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (userPhoneNum.getText().toString().equals("")) {
                    imgClearUser.setVisibility(View.GONE);
//                    btnLogin.setEnabled(false);
//                    btnLogin.setBackgroundResource(R.drawable.btn_unclick);
                    textPhone.setVisibility(View.GONE);
                } else {
                    imgClearUser.setVisibility(View.VISIBLE);
                    textPhone.setVisibility(View.VISIBLE);
                }
                if (userPassword.getText().toString().equals("")) {
                } else if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                } else {
                    int a = userPhoneNum.getSelectionEnd();
                }
            }
        });
        userPhoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !userPassword.getText().toString().isEmpty()) {
//                    img_clearuser_phone_num
                    imgClear.setVisibility(View.GONE);
                    imgClearUser.setVisibility(View.VISIBLE);
                } else {
                    imgClear.setVisibility(View.VISIBLE);
                    imgClearUser.setVisibility(View.GONE);
                }
            }
        });

        userPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imgClear.setVisibility(View.VISIBLE);
                    imgClearUser.setVisibility(View.GONE);
                } else {
                    imgClear.setVisibility(View.GONE);
                    imgClearUser.setVisibility(View.VISIBLE);
                }
            }
        });
        userPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (userPassword.getText().toString().equals("")) {
                    liOpera.setVisibility(View.GONE);
                    textDuanxincode.setVisibility(View.GONE);
//                    btnLogin.setEnabled(false);
//                    btnLogin.setBackgroundResource(R.drawable.btn_unclick);
                } else {
                    liOpera.setVisibility(View.VISIBLE);
                    textDuanxincode.setVisibility(View.VISIBLE);
                    textDuanxincode.setVisibility(View.VISIBLE);
                }
                if (userPassword.getText().toString().equals("")) {

//                    btnLogin.setEnabled(false);
//                    btnLogin.setBackgroundResource(R.drawable.btn_unclick);
//                    return;
                } else if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {

//                    btnLogin.setEnabled(false);
//                    btnLogin.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {

//                    btnLogin.setEnabled(false);
//                    btnLogin.setBackgroundResource(R.drawable.btn_unclick);
                } else {
                    btnLogin.setEnabled(true);
                    btnLogin.setBackgroundResource(R.drawable.v6button);
                }

            }
        });


//        http://47.92.32.79:8063/Help/Api/GET-api-MyWorks_Skip_Take


    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @OnClick({R.id.img_back, R.id.rl_back, R.id.tv_title, R.id.rl_top_layout, R.id.user_phone_num, R.id.img_clear_user, R.id.btn_login, R.id.tv_messagelogin, R.id.tv_forget, R.id.user_password, R.id.img_clear, R.id.img_pass, R.id.li_opera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                closeKeyboard();
                MySharePreferenceUtil.put(LoginActivity.this, "token", "");
                EventBus.getDefault().post(222);
                finish();
                break;
            case R.id.rl_back:
                closeKeyboard();
                MySharePreferenceUtil.put(LoginActivity.this, "token", "");
                EventBus.getDefault().post(222);
                finish();
                break;
            case R.id.tv_title:
                break;
            case R.id.rl_top_layout:
                break;
            case R.id.user_phone_num:
                break;
            case R.id.img_clear_user:
                userPhoneNum.setText("");
//                userPhoneNum.setCursorVisible(false);text_phone
                textPhone.setVisibility(View.GONE);

                break;

            case R.id.btn_login:
//                Toast.makeText(this, "登录了", Toast.LENGTH_SHORT).show();


                if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                    ToastUtil.showCenterToast(LoginActivity.this, "手机号不能为空");
//                    btnLogin.setEnabled(false);
//                    btnLogin.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {
                    ToastUtil.showCenterToast(LoginActivity.this, "手机号不符合规则");

                } else if (userPassword.getText().toString().equals("")) {
                    ToastUtil.showCenterToast(LoginActivity.this, "密码不能为空");
//                    btnLogin.setEnabled(false);
//                    btnLogin.setBackgroundResource(R.drawable.btn_unclick);
//                    return;
                } else if (Regular.isNumeric(userPassword.getText().toString())) {
                    ToastUtil.showCenterToast(LoginActivity.this, "密码必须为数字和字母组合");
                } else if (Regular.isLetter(userPassword.getText().toString())) {
                    ToastUtil.showCenterToast(LoginActivity.this, "密码必须为数字和字母组合");
                } else if (Regular.containSpecialCharacter(userPassword.getText().toString())) {
                    ToastUtil.showCenterToast(LoginActivity.this, "密码必须为数字和字母组合");
                } else if (userPassword.getText().toString().length() < 6) {
                    ToastUtil.showCenterToast(LoginActivity.this, "密码必须为数字和字母组合");
                } else {

                    btnLogin.setEnabled(true);
                    btnLogin.setBackgroundResource(R.drawable.v6button);
                    if (userPhoneNum.getText().toString().equals(MySharePreferenceUtil.get(
                            LoginActivity.this, "phone1", ""))) {
                        str_flag = "1";
                    } else {
                        str_flag = "0";
                    }


                    login();
                }


                break;
            case R.id.tv_messagelogin:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                finish();
                break;
            case R.id.tv_forget:

                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));

                break;
            case R.id.user_password:
                break;
            case R.id.img_clear:
                userPassword.setText("");
                textDuanxincode.setVisibility(View.GONE);

//                userPassword.setCursorVisible(false); text_duanxincode
                break;
            case R.id.img_pass:
                if (userPassword.getTransformationMethod().equals(PasswordTransformationMethod
                        .getInstance())) {
                    userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgPass.setBackgroundResource(R.drawable.yan1);
                } else {
                    userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgPass.setBackgroundResource(R.drawable.yan2);
                }
                userPassword.setSelection(userPassword.length());


                break;
            case R.id.li_opera:
                break;
        }
    }

    private final String ACTION_NAME = "智投宝结束";

    String str_flag = "0";

    public void login() {
        MyOkhttp.post(Constant.URL + "api/v1/system/login")
                .tag(this)
                .params("username", userPhoneNum.getText().toString())
                .params("password", userPassword.getText().toString())
                .params("did", deviceId)
                .params("deviceToken", getDeviceToken())
                .execute(new StringNoDialogCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        myshowloading();
                    }

                    @Override
                    public void onSuccess(String s, okhttp3.Call call, okhttp3.Response response) {
                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
//                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
                        Gson gson = new Gson();
                        LoginBean loginBean = new LoginBean();
                        loginBean = gson.fromJson(s, LoginBean.class);
                        LoginBean.DataBean dataBean = loginBean.getData();
                        MySharePreferenceUtil.put(LoginActivity.this, userPhoneNum.getText().toString().trim() + "phone", userPhoneNum.getText().toString().trim());
                     /*   Intent mIntent1 = new Intent(ACTION_NAME);
                        mIntent1.putExtra("yaner", "发送广播，相当于在这里传送数据");
                        //发送广播
                        sendBroadcast(mIntent1);*/

                        if (loginBean.getMeta().isSuccess() == false) {
                            ToastUtil.showCenterToast(LoginActivity.this, loginBean.getMeta().getMessage());
                        } else {
                            ZhugeSDK.getInstance().openLog();
                            ZhugeSDK.getInstance().init(LoginActivity.this);
                            //定义与事件相关的属性信息
                            JSONObject eventObject = new JSONObject();
                            try {
                                eventObject.put("登录", "登录");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


//记录事件,以购买为例
                            ZhugeSDK.getInstance().track(getApplicationContext(), "登录", eventObject);

                            MySharePreferenceUtil.put(LoginActivity.this, "id", loginBean.getData().getId());
                            MySharePreferenceUtil.put(LoginActivity.this, "username", loginBean.getData().getUsername());
//                            MySharePreferenceUtil.put(LoginActivity.this, "idCard", loginBean.getData().getIdCard());
                            MySharePreferenceUtil.put(LoginActivity.this, "phone", loginBean.getData().getPhone());
                            MySharePreferenceUtil.put(LoginActivity.this, "phone1", loginBean.getData().getPhone());
//                            MySharePreferenceUtil.put(LoginActivity.this, "phone2", loginBean.getData().getPhone());
                            MySharePreferenceUtil.put(LoginActivity.this, "token", loginBean.getData().getToken());
                            MySharePreferenceUtil.put(LoginActivity.this, "secName", loginBean.getData().getSecName());
                            MySharePreferenceUtil.put(LoginActivity.this, "password", userPassword.getText().toString());

                            MySharePreferenceUtil.put(LoginActivity.this, "token1", loginBean.getData().getToken());
                            try {

                                if (TextUtils.isEmpty(loginBean.getData().getIdCard())) {
                                    MySharePreferenceUtil.put(LoginActivity.this, "idCard", "");
                                } else {
                                    MySharePreferenceUtil.put(LoginActivity.this, "idCard", loginBean.getData().getIdCard());
                                }


                                MySharePreferenceUtil.put(LoginActivity.this, "gender", loginBean.getData().getGender());

                            } catch (Exception e) {

                            }
                            if (TextUtils.isEmpty(loginBean.getData().getAppHeadUrl())) {
                                MySharePreferenceUtil.put(LoginActivity.this, "appHeadUrl", "");
                            } else {
                                MySharePreferenceUtil.put(LoginActivity.this, "appHeadUrl", loginBean.getData().getAppHeadUrl());
                            }
//                            Toast.makeText(LoginActivity.this, MySharePreferenceUtil.get(LoginActivity.this,"appHeadUrl","") + "", Toast.LENGTH_SHORT).show();
                            String flaggesture = (String) MySharePreferenceUtil.get(LoginActivity.this, MySharePreferenceUtil.get(LoginActivity.this, "phone1", "") + "flaggesture", "");
//                            EventBus.getDefault().post(2);
                            Intent mIntent = new Intent(ACTION_NAME);
                            mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                            //发送广播

//                            sendBroadcast(mIntent);
                            if (flaggesture.isEmpty()) {
                                Intent intent = new Intent(LoginActivity.this, GestureEditActivity.class);

                                intent.putExtra("flag", "2");
                                startActivity(intent);

                                finish();
                            } else {
//                                EventBus.getDefault().post(2);
                            /*    Intent mIntent = new Intent(ACTION_NAME);
                                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");*/
                                //发送广播
//                                sendBroadcast(mIntent);
                                sendBroadcast(mIntent);
                            }
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("登录", "登录");

                            // 设置控件附加属性，用于无埋点对控件添加数据；控件绑定事件附加属性依赖于view.setTag(key, value)支持，使用key值：-96000；
                            // 如果view已经绑定过此key值，则此设置不生效
                            StatService.setAttributes(btnLogin, hashMap);
                            finish();
                        }


                    }


                    @Override
                    public void onError(okhttp3.Call call, okhttp3.Response response, Exception e) {
                        super.onError(call, response, e);
                        mydismissloading();
                    }

                    /*    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);

                        mydismissloading();
//                        Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }*/

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
//                        dismissLoading();

                        mydismissloading();
                    }
                });
    }

    public void onEventMainThread(Integer type) {
        Log.d("eventbus", type + "");

        if (type == 5) {
            LoginActivity.this.finish();
            Intent mIntent = new Intent(ACTION_NAME);
            mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
            //发送广播
            sendBroadcast(mIntent);


        } else if (type == 30) {
            finish();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        MySharePreferenceUtil.put(LoginActivity.this, "token", "");
        EventBus.getDefault().post(222);
        Intent mIntent = new Intent(ACTION_NAME);
        mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
//                                //发送广播
//        sendBroadcast(mIntent);
        return super.onKeyDown(keyCode, event);
    }
}
