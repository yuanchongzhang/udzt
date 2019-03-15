package com.xmrxcaifu.activity;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;

import com.xmrxcaifu.Constant;

import com.xmrxcaifu.R;
import com.xmrxcaifu.http.MyOkhttp;
import com.xmrxcaifu.http.callback.BitmapCallback;
import com.xmrxcaifu.http.callback.StringNoDialogCallback;
import com.xmrxcaifu.http.request.BaseRequest;
import com.xmrxcaifu.httpmodel.RegisterCode;
import com.xmrxcaifu.modle.ImgCodeVavild;
import com.xmrxcaifu.modle.RegisterModel;
import com.xmrxcaifu.modle.RegisterValaiBean;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.Regular;
import com.xmrxcaifu.util.TimeCount;
import com.xmrxcaifu.util.ToastUtil;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/3/8.
 */

public class RegisterActivity extends BaseActivty2 {

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
    @BindView(R.id.user_imgcaptcha)
    EditText userImgcaptcha;
    @BindView(R.id.img_imgcaptcha)
    ImageView imgImgcaptcha;
    @BindView(R.id.rl_imgcaptcha)
    LinearLayout rlImgcaptcha;
    @BindView(R.id.user_captcha)
    EditText userCaptcha;
    @BindView(R.id.btn_send_mes)
    Button btnSendMes;
    @BindView(R.id.tv_voice_code)
    TextView tvVoiceCode;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.text_phone)
    TextView textPhone;
    @BindView(R.id.text_code)
    TextView textCode;
    @BindView(R.id.text_duanxincode)
    TextView textDuanxincode;
    @BindView(R.id.img_clear_user_code)
    ImageView imgClearUserCode;
    @BindView(R.id.img_clear_duanxin_code)
    ImageView imgClearDuanxinCode;
    private TimeCount timeCount;


    int a = 0;

    //       img_imgcaptcha.setImageBitmap(response);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ImmersionBar.with(RegisterActivity.this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        ButterKnife.bind(this);
//        Glide.with(RegisterActivity.this).load(Constant.URL + "captcha.png").error(R.mipmap.logo).placeholder(R.mipmap.logo).into((imgImgcaptcha));
        initcaptcha2();
        timeCount = new TimeCount(60000, 100, btnSendMes);


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
                    textPhone.setVisibility(View.INVISIBLE);
                } else {
                    textPhone.setVisibility(View.VISIBLE);
                    imgClearUser.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (userCaptcha.getText().toString().equals("")) {

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


        userImgcaptcha.addTextChangedListener(new TextWatcher() {

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
                if (userImgcaptcha.getText().toString().equals("")) {
//                    imgImgcaptcha.setVisibility(View.GONE);
//                    img_clear_user_code
                    textCode.setVisibility(View.INVISIBLE);
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                    imgClearUserCode.setVisibility(View.GONE);

                } else {
                    imgImgcaptcha.setVisibility(View.VISIBLE);
                    textCode.setVisibility(View.VISIBLE);
                    textCode.setVisibility(View.VISIBLE);
                    imgClearUserCode.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(userImgcaptcha.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else if (userCaptcha.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else {
                    btnNext.setEnabled(true);
                    btnNext.setBackgroundResource(R.drawable.v6button);

                }

            }
        });

        userImgcaptcha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(userImgcaptcha.getText().toString())) {
//                    img_clear_user_code

                    imgClearUserCode.setVisibility(View.VISIBLE);
                } else {
                    imgClearUserCode.setVisibility(View.GONE);
                }
            }
        });
//        user_captcha img_clear_duanxin_code
        userCaptcha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(userCaptcha.getText().toString())) {
                    imgClearDuanxinCode.setVisibility(View.VISIBLE);
                } else {
                    imgClearDuanxinCode.setVisibility(View.GONE);
                }
            }
        });

        userCaptcha.addTextChangedListener(new TextWatcher() {

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
                // TODO Auto-generated method stub   img_clear_duanxin_code

                if (userCaptcha.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                    textDuanxincode.setVisibility(View.INVISIBLE);
                    imgClearDuanxinCode.setVisibility(View.GONE);
                } else {
                    btnNext.setEnabled(true);
                    btnNext.setBackgroundResource(R.drawable.v6button);
                    textDuanxincode.setVisibility(View.VISIBLE);
                    textDuanxincode.setVisibility(View.VISIBLE);
                    imgClearDuanxinCode.setVisibility(View.VISIBLE);
                }


                if (userPhoneNum.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);


                } else if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
//                    textDuanxincode.setVisibility(View.VISIBLE);
                } else if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
//                    textDuanxincode.setVisibility(View.VISIBLE);
                } else if (userCaptcha.getText().toString().equals("")) {

                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
//                    textDuanxincode.setVisibility(View.INVISIBLE);
                    imgClearDuanxinCode.setVisibility(View.GONE);
                } else {
                    btnNext.setEnabled(true);
                    btnNext.setBackgroundResource(R.drawable.v6button);
                    textDuanxincode.setVisibility(View.VISIBLE);
                    textDuanxincode.setVisibility(View.VISIBLE);
                    imgClearDuanxinCode.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    @OnClick({R.id.img_back, R.id.rl_back, R.id.tv_title, R.id.rl_top_layout, R.id.user_phone_num, R.id.img_clear_user, R.id.user_imgcaptcha, R.id.img_imgcaptcha, R.id.rl_imgcaptcha, R.id.user_captcha, R.id.btn_send_mes, R.id.tv_voice_code, R.id.tv_comment, R.id.btn_next, R.id.img_clear_user_code, R.id.img_clear_duanxin_code})
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
                textPhone.setVisibility(View.INVISIBLE);
                break;
            case R.id.user_imgcaptcha:
                break;
            case R.id.img_imgcaptcha:
                initcaptcha2();
                break;
            case R.id.rl_imgcaptcha:

                break;
            case R.id.user_captcha:
                break;
            case R.id.btn_send_mes:
                if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                    ToastUtil.showCenterToast(RegisterActivity.this, "手机号不能为空");
                } else if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {
                    ToastUtil.showCenterToast(RegisterActivity.this, "请输入正确的手机号码");
                } else if (TextUtils.isEmpty(userImgcaptcha.getText().toString())) {
                    ToastUtil.showCenterToast(RegisterActivity.this, "图形验证码不能为空");
                } else {
                    valid();

                }


                break;
            case R.id.tv_voice_code:
                break;
            case R.id.tv_comment:
                break;
            case R.id.btn_next:
              /*  Intent intent1 = new Intent(RegisterActivity.this, MainActivity2.class);
                intent1.putExtra("url", Constant.URL + "#/regisxy");
                startActivity(intent1);*/

                if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                    ToastUtil.showCenterToast(RegisterActivity.this, "手机号不能为空");
                } else if (!Regular.isMobileNO(userPhoneNum.getText().toString())) {
                    ToastUtil.showCenterToast(RegisterActivity.this, "请输入正确的手机号码");
                } else if (TextUtils.isEmpty(userImgcaptcha.getText().toString())) {
                    ToastUtil.showCenterToast(RegisterActivity.this, "图形验证码不能为空");
                } else if (TextUtils.isEmpty(userCaptcha.getText().toString())) {
                    ToastUtil.showCenterToast(RegisterActivity.this, "短信验证码不能为空");
                } else if (a == 0) {
                    ToastUtil.showCenterToast(RegisterActivity.this, "请获取短信验证码");
                } else {
                    valid_smsCode();
                }


//                register();


                break;


            case R.id.img_clear_user_code:
                userImgcaptcha.setText("");

                break;
            case R.id.img_clear_duanxin_code:
                userCaptcha.setText("");
                break;
        }
    }


    public void validateMobileCaptcha() {
//        http://xxx.com/api/v1/system/validateMobileCaptcha/18888888888/1234
        String url = Constant.URL + "api/v1/system/validateMobileCaptcha/" + userPhoneNum.getText().toString() + "/" + userCaptcha.getText().toString();

        MyOkhttp.get(url)
                .tag(this)
                .execute(new StringNoDialogCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);

//                        showLoading();
//                        myshowloading();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        Gson gson = new Gson();

                        Log.d("5555555555555", s);
                        RegisterValaiBean registerValaiBean = new RegisterValaiBean();
                        registerValaiBean = gson.fromJson(s, RegisterValaiBean.class);

                        if (registerValaiBean.getMeta().isSuccess() == true) {


                            getCode();
                        } else {
                            ToastUtil.showCenterToast(RegisterActivity.this, registerValaiBean.getMeta().getMessage());
//                            initcaptcha2();
                        }


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.d("5555555555555", response.toString());
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
//                        dismissLoading();
//                        mydismissloading();
//                        timeCount.cancel();
                    }
                });
    }


    public void getCode() {
//        18888888888/0
        String url = Constant.URL + "api/v1/system/getMobileCaptcha/" + userPhoneNum.getText().toString() + "/" + "2";

        MyOkhttp.get(url)
                .tag(this)

                .execute(new StringNoDialogCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);

//                        showLoading();

                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {


                        Log.d("5555555555555", s);

                        Gson gson = new Gson();

                        RegisterCode registerCode = new RegisterCode();
                        registerCode = gson.fromJson(s, RegisterCode.class);
                        if (registerCode.getMeta().isSuccess() == true) {
                            validateMobileCaptcha();
                            timeCount.start();
                            ToastUtil.showCenterToast(RegisterActivity.this, registerCode.getMeta().getMessage());
                            a = 1;
                        } else {
//                            btnNext.setEnabled(false);
//                            btnNext.setBackgroundResource(R.drawable.btn_unclick);
                            ToastUtil.showCenterToast(RegisterActivity.this, registerCode.getMeta().getMessage());
                            initcaptcha2();
                        }


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.d("5555555555555", response.toString());
                        a = 0;
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
//                        dismissLoading();
//                        timeCount.cancel();
                    }
                });
    }

    public void onEventMainThread(Integer type) {
        if (type == 90) {
            finish();
        }
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


    public void valid() {
//        userImgcaptcha
//        String myurl = Constant.URL + "api/v1/system/validatePictureCaptcha/" + userImgcaptcha.getText().toString();
        String myurl = Constant.URL + "api/v1/system/validatePictureCaptcha/" + userImgcaptcha.getText().toString();
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
                        Log.d(s, userImgcaptcha.getText().toString());
                        Gson gson = new Gson();

                        ImgCodeVavild imgCodeVavild = new ImgCodeVavild();
                        imgCodeVavild = gson.fromJson(s, ImgCodeVavild.class);
                        if (imgCodeVavild.getMeta().isSuccess() == false) {
//
                            ToastUtil.showCenterToast(RegisterActivity.this, imgCodeVavild.getMeta().getMessage());
                            initcaptcha2();
                        } else {
                            getCode();
                        }


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.d(response.toString(), "333333333333");
                        initcaptcha2();
//                        Toast.makeText(RegisterActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);

                    }
                });
    }


    public void valid_smsCode() {
//        userImgcaptcha   http://xxx.com/api/v1/system/validateMobileCaptcha/18888888888/1234
        String myurl = Constant.URL + "api/v1/system/validateMobileCaptcha/" + userPhoneNum.getText().toString() + "/" + userCaptcha.getText().toString();
//        userImgcaptcha.getText().toString()
        MyOkhttp.get(myurl)
                .tag(this)
                .execute(new StringNoDialogCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        myshowloading();

                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
//                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
                        Gson gson = new Gson();
//                        Toast.makeText(RegisterActivity.this, "成功", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(RegisterActivity.this, RegisterSettingsActivity.class));
                        RegisterModel registerModel = new RegisterModel();
                        registerModel = gson.fromJson(s, RegisterModel.class);

                        if (registerModel.getMeta().isSuccess() == true) {
                            ToastUtil.showCenterToast(RegisterActivity.this, registerModel.getMeta().getMessage());
                            Intent intent = new Intent(RegisterActivity.this, RegisterSettingsActivity.class);
                            intent.putExtra("phone", userPhoneNum.getText().toString());
                            startActivity(intent);
                            finish();
                            ZhugeSDK.getInstance().openLog();

//        ZhugeSDK.getInstance().openDebug();

                            ZhugeSDK.getInstance().init(RegisterActivity.this);

                            //定义与事件相关的属性信息
                            JSONObject eventObject = new JSONObject();
                            try {
                                eventObject.put("注册", "注册");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("注册", "注册");

                            // 设置控件附加属性，用于无埋点对控件添加数据；控件绑定事件附加属性依赖于view.setTag(key, value)支持，使用key值：-96000；
                            // 如果view已经绑定过此key值，则此设置不生效
                            StatService.setAttributes(btnNext, hashMap);
//记录事件,以购买为例
                            ZhugeSDK.getInstance().track(getApplicationContext(), "注册", eventObject);


                        } else {
//                            Intent intent = new Intent(RegisterActivity.this, RegisterSettingsActivity.class);
//                            intent.putExtra("phone", userPhoneNum.getText().toString());
//                            startActivity(intent);
//                            finish();
                            ToastUtil.showCenterToast(RegisterActivity.this, registerModel.getMeta().getMessage());
                        }


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.d(response.toString(), "333333333333");

                        mydismissloading();

//                        Toast.makeText(RegisterActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        mydismissloading();
                    }
                });
    }


}
