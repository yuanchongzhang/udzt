package com.xmrxcaifu.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;

import com.xmrxcaifu.Constant;

import com.xmrxcaifu.R;
import com.xmrxcaifu.http.MyOkhttp;
import com.xmrxcaifu.http.callback.BitmapCallback;
import com.xmrxcaifu.http.callback.StringNoDialogCallback;
import com.xmrxcaifu.http.request.BaseRequest;
import com.xmrxcaifu.httpmodel.ForgetBean;
import com.xmrxcaifu.httpmodel.RegisterCode;
import com.xmrxcaifu.modle.ImgCodeVavild;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.Regular;
import com.xmrxcaifu.util.TimeCount;
import com.xmrxcaifu.util.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Administrator on 2018/3/12.
 */

public class ForgetActivity extends BaseActivty2 {


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
    @BindView(R.id.edit_phone)
    EditText editPhone;
    @BindView(R.id.text_smscode)
    EditText textSmscode;
    @BindView(R.id.btn_send_mes)
    Button btnSendMes;
    @BindView(R.id.text_pwd)
    EditText textPwd;
    @BindView(R.id.text_repwd)
    EditText textRepwd;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.img_tupian)
    ImageView imgTupian;
    @BindView(R.id.img_pwd)
    ImageView imgPwd;
    @BindView(R.id.img_repwd)
    ImageView imgRepwd;
    @BindView(R.id.img_imgcaptcha)
    ImageView imgImgcaptcha;
    @BindView(R.id.img_clear_user56)
    ImageView imgClearUser56;
    @BindView(R.id.img_clear_sms)
    ImageView imgClearSms;
    private TimeCount timeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ButterKnife.bind(this);
        timeCount = new TimeCount(60000, 100, btnSendMes);
        initcaptcha2();

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
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else {
                    imgClearUser.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (editPhone.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (textPwd.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (textRepwd.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!textPwd.getText().toString().equals(textRepwd.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else {
                    btnNext.setEnabled(true);
                    btnNext.setBackgroundResource(R.drawable.v6button);
                }

            }
        });
        userPhoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                    imgClearUser.setVisibility(View.VISIBLE);
                } else {
                    imgClearUser.setVisibility(View.GONE);
                }
            }
        });


        editPhone.addTextChangedListener(new TextWatcher() {

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
                if (editPhone.getText().toString().equals("")) {
                    imgTupian.setVisibility(View.GONE);
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
//                    img_clear_user56
                    imgClearUser56.setVisibility(View.GONE);
                } else {
                    imgTupian.setVisibility(View.GONE);
                    imgClearUser56.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (editPhone.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (textPwd.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (textRepwd.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!textPwd.getText().toString().equals(textRepwd.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else {
                    btnNext.setEnabled(true);
                    btnNext.setBackgroundResource(R.drawable.v6button);
                }

            }
        });
        editPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(editPhone.getText().toString())) {
                    imgClearUser56.setVisibility(View.VISIBLE);
                } else {
                    imgClearUser56.setVisibility(View.GONE);
                }
            }
        });
        textSmscode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(textSmscode.getText().toString())) {
                    imgClearSms.setVisibility(View.VISIBLE);
                } else {
                    imgClearSms.setVisibility(View.GONE);
                }
            }
        });
        textSmscode.addTextChangedListener(new TextWatcher() {

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
                if (textSmscode.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
//                    img_clear_sms
                    imgClearSms.setVisibility(View.GONE);

                } else {
                    imgClearSms.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (editPhone.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (textPwd.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (textRepwd.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!textPwd.getText().toString().equals(textRepwd.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else {
                    btnNext.setEnabled(true);
                    btnNext.setBackgroundResource(R.drawable.v6button);
                }

            }
        });
        textPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(textPwd.getText().toString())) {
                    imgPwd.setVisibility(View.VISIBLE);
                } else {
                    imgPwd.setVisibility(View.GONE);
                }
            }
        });

        textPwd.addTextChangedListener(new TextWatcher() {

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
                if (textPwd.getText().toString().equals("")) {
                    imgPwd.setVisibility(View.GONE);
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else {
                    imgPwd.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (editPhone.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (textPwd.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (textRepwd.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!textPwd.getText().toString().equals(textRepwd.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else {
                    btnNext.setEnabled(true);
                    btnNext.setBackgroundResource(R.drawable.v6button);
                }

            }
        });
        textRepwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(textRepwd.getText().toString())) {
                    imgRepwd.setVisibility(View.VISIBLE);
                } else {
                    imgRepwd.setVisibility(View.GONE);
                }
            }
        });
        textRepwd.addTextChangedListener(new TextWatcher() {

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
                if (textRepwd.getText().toString().equals("")) {
                    imgRepwd.setVisibility(View.GONE);
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else {
                    imgRepwd.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (editPhone.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (textPwd.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (textRepwd.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!textPwd.getText().toString().equals(textRepwd.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else {
                    btnNext.setEnabled(true);
                    btnNext.setBackgroundResource(R.drawable.v6button);
                }

            }
        });

        ImmersionBar.with(this)
                .fullScreen(true)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .init();
    }

    @OnClick({R.id.img_back, R.id.rl_back, R.id.tv_title, R.id.rl_top_layout, R.id.user_phone_num, R.id.img_clear_user, R.id.edit_phone, R.id.text_smscode, R.id.btn_send_mes, R.id.text_pwd, R.id.text_repwd, R.id.btn_next, R.id.img_tupian, R.id.img_pwd, R.id.img_repwd, R.id.img_imgcaptcha, R.id.img_clear_user56, R.id.img_clear_sms})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_back:
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
                break;
            case R.id.edit_phone:
                break;
            case R.id.text_smscode:
                break;
            case R.id.btn_send_mes:
                //获取验证码
                if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {

//                    btnLogin.setEnabled(false);
//                    btnLogin.setBackgroundResource(R.drawable.btn_unclick);
                } else if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                    ToastUtil.showCenterToast(ForgetActivity.this, "手机号不能为空");
                } else if (TextUtils.isEmpty(editPhone.getText().toString())) {
                    ToastUtil.showCenterToast(ForgetActivity.this, "手机验证码不能为空");
                } else {
//                    login();
                    valid();
                }

                break;
            case R.id.text_pwd:
                break;
            case R.id.text_repwd:
                break;
            case R.id.btn_next:
                if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {
                    ToastUtil.showCenterToast(ForgetActivity.this, "手机号不符合规则");

                } else if (Regular.isNumeric(textPwd.getText().toString())) {
                    ToastUtil.showCenterToast(ForgetActivity.this, "密码不符合规则");
                } else if (Regular.isLetter(textPwd.getText().toString())) {
                    ToastUtil.showCenterToast(ForgetActivity.this, "密码不符合规则");
                } else if (Regular.containSpecialCharacter(textPwd.getText().toString())) {
                    ToastUtil.showCenterToast(ForgetActivity.this, "密码不符合规则");
                } else if (textPwd.getText().toString().length() < 6) {
                    ToastUtil.showCenterToast(ForgetActivity.this, "密码不符合规则");
                } else if (flag == 0) {
                    ToastUtil.showCenterToast(ForgetActivity.this, "请获取手机验证码");
                } else {
                    repass();
                }


                break;

            case R.id.img_tupian:
                editPhone.setText("");
                break;
            case R.id.img_pwd:
                textPwd.setText("");
                break;
            case R.id.img_repwd:
                textRepwd.setText("");
                break;

            case R.id.img_imgcaptcha:
                initcaptcha2();
                break;


            case R.id.img_clear_user56:
                editPhone.setText("");
                break;
            case R.id.img_clear_sms:
                textSmscode.setText("");

                break;
        }
    }

    public void valid() {
//       text_smscode
//        String myurl = Constant.URL + "api/v1/system/validatePictureCaptcha/" + userImgcaptcha.getText().toString();


        String myurl = Constant.URL + "api/v1/system/validatePictureCaptcha/" + editPhone.getText().toString();
//        userImgcaptcha.getText().toString()
        MyOkhttp.get(myurl)
                .tag(this)
                .execute(new StringNoDialogCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");

                        Gson gson = new Gson();

                        ImgCodeVavild imgCodeVavild = new ImgCodeVavild();
                        imgCodeVavild = gson.fromJson(s, ImgCodeVavild.class);
                        if (imgCodeVavild.getMeta().isSuccess() == false) {
                            ToastUtil.showCenterToast(ForgetActivity.this, imgCodeVavild.getMeta().getMessage());
                            initcaptcha2();

                        } else {
                            login();
                        }


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.d(response.toString(), "333333333333");

//                        Toast.makeText(RegisterActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);

                    }
                });
    }

    int flag = 0;

    public void repass() {
//        text_smscode
//        String url = Constant.URL + "api/v1/system/forgetSubmit/" + textSmscode.getText().toString() + "/" + textPwd.getText().toString();
        String url = Constant.URL + "api/v1/system/forgetSubmit/" + userPhoneNum.getText().toString() + "/" + textSmscode.getText().toString() + "/" + textPwd.getText().toString();
        MyOkhttp.get(url)
                .tag(this)
                .execute(new StringNoDialogCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);

                        showLoading();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
//                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
                        Gson gson = new Gson();
                        ForgetBean forgetBean = new ForgetBean();
                        forgetBean = gson.fromJson(s, ForgetBean.class);
                        if (forgetBean.getMeta().isSuccess() == false) {
                            Toast.makeText(ForgetActivity.this, forgetBean.getMeta().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgetActivity.this, forgetBean.getMeta().getMessage(), Toast.LENGTH_SHORT).show();
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("忘记密码", "忘记密码");

                            // 设置控件附加属性，用于无埋点对控件添加数据；控件绑定事件附加属性依赖于view.setTag(key, value)支持，使用key值：-96000；
                            // 如果view已经绑定过此key值，则此设置不生效
                            StatService.setAttributes(btnNext, hashMap);
                            finish();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.d(response.toString(), "333333333333");

//                        Toast.makeText(ForgetActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        dismissLoading();
                    }
                });
    }

    public void login() {
//        String url = Constant.URL + "api/v1/system/getMobileCaptcha/" + editPhone.getText().toString() + "/0";
        String url = Constant.URL + "api/v1/system/getMobileCaptcha/" + userPhoneNum.getText().toString() + "/0";
        MyOkhttp.get(url)
                .tag(this)

                .execute(new StringNoDialogCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);

//                        showLoading();
//myshowloading();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
//                        Toast.makeText(ForgetActivity.this, "请求验证码成功", Toast.LENGTH_SHORT).show();
                        flag = 1;

                        Gson gson = new Gson();
                        RegisterCode registerCode = new RegisterCode();
                        registerCode = gson.fromJson(s, RegisterCode.class);
                        if (registerCode.getMeta().isSuccess() == true) {
//                            timeCount.cancel();
//                            btn_send_mes.setClickable(true);
                            btnSendMes.setClickable(true);
                            ToastUtil.showCenterToast(ForgetActivity.this, registerCode.getMeta().getMessage());
                            timeCount.start();
                        } else {
                            ToastUtil.showCenterToast(ForgetActivity.this, registerCode.getMeta().getMessage());
                        }


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(ForgetActivity.this, "请求验证码失败", Toast.LENGTH_SHORT).show();
                        Log.d(response.toString(), "333333333333");
//                        mydismissloading();

//                        timeCount.cancel();
                        flag = 0;
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
//                        dismissLoading();
                        mydismissloading();
//                        timeCount.cancel();
                    }
                });
    }


    public void initcaptcha2() {
//        userImgcaptcha
        String myurl = Constant.URL + "captcha.png";
//        userImgcaptcha.getText().toString()
        MyOkhttp.get(myurl)
                .tag(this)
                .execute(new BitmapCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onSuccess(Bitmap bitmap, Call call, Response response) {
                        Log.d(response.toString(), "33333333333");

                        imgImgcaptcha.setImageBitmap(bitmap);
                    }


                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.d(response.toString(), "333333333333");

//
                    }

                });
    }

}
