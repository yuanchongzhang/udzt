package com.xmrxcaifu.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xmrxcaifu.Constant;
import com.xmrxcaifu.R;
import com.xmrxcaifu.http.MyOkhttp;
import com.xmrxcaifu.http.callback.StringNoDialogCallback;
import com.xmrxcaifu.http.request.BaseRequest;
import com.xmrxcaifu.httpmodel.LoginBean;
import com.xmrxcaifu.httpmodel.RegisterBean;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.util.Regular;
import com.xmrxcaifu.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/3/9.
 */

public class RegisterShopSettingsActivity extends BaseActivty2 {


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
    @BindView(R.id.check)
    CheckBox check;
    @BindView(R.id.check_mAgreement)
    TextView checkMAgreement;
    @BindView(R.id.tv_mAgreement)
    TextView tvMAgreement;
    @BindView(R.id.li_dexterity)
    LinearLayout liDexterity;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.img_clear2)
    ImageView imgClear2;
    String phone;
    String deviceId;

    //    agreementBack注册协议交互方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwdsettings);
        ButterKnife.bind(this);
        ImmersionBar.with(RegisterShopSettingsActivity.this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        phone = getIntent().getStringExtra("phone");
        deviceId = (String) MySharePreferenceUtil.get(RegisterShopSettingsActivity.this, "deviceid", "");
        registerBoradcastReceiver();
        check.setChecked(true);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (userPhoneNum.getText().toString().length() >= 6 && isChecked && editPhone.getText().length() >= 1) {
                    btnNext.setBackgroundResource(R.drawable.v6button);
                    btnNext.setEnabled(true);
                } else {
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                    btnNext.setEnabled(false);
                }
            }
        });
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
                    imgClearUser.setVisibility(View.GONE);

                } else if (check.isChecked() == false) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else {
                    btnNext.setEnabled(true);
                    btnNext.setBackgroundResource(R.drawable.v6button);
                    imgClearUser.setVisibility(View.VISIBLE);

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

        editPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(editPhone.getText().toString())) {
                    imgClear2.setVisibility(View.VISIBLE);
                } else {
                    imgClear2.setVisibility(View.GONE);
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
//                img_clear2
                if (userPhoneNum.getText().toString().equals("")) {
                    imgClearUser.setVisibility(View.GONE);
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                    imgClear2.setVisibility(View.GONE);
                } else if (check.isChecked() == false) {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.btn_unclick);
                } else {
                    btnNext.setEnabled(true);
                    btnNext.setBackgroundResource(R.drawable.v6button);

                    imgClear2.setVisibility(View.VISIBLE);
                }


            }
        });
    }

    @OnClick({R.id.img_back, R.id.rl_back, R.id.tv_title, R.id.rl_top_layout, R.id.user_phone_num, R.id.img_clear_user, R.id.edit_phone, R.id.check, R.id.check_mAgreement, R.id.tv_mAgreement, R.id.li_dexterity, R.id.btn_next, R.id.img_clear2})
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
            case R.id.check:
                break;
            case R.id.check_mAgreement:
                break;
            case R.id.tv_mAgreement:
//                startActivity(new Intent(RegisterSettingsActivity.this, MainActivity2.class));
                Intent intent = new Intent(RegisterShopSettingsActivity.this, MainActivity2.class);
                intent.putExtra("url", Constant.URL + "#/regisxy");
                startActivity(intent);


//                EventBus.getDefault().post(3);
                break;
            case R.id.li_dexterity:
                break;
            case R.id.btn_next:
                if (TextUtils.isEmpty(userPhoneNum.getText().toString())) {
                    ToastUtil.showCenterToast(RegisterShopSettingsActivity.this, "密码不能为空");
                } else if (TextUtils.isEmpty(editPhone.getText().toString())) {
                    ToastUtil.showCenterToast(RegisterShopSettingsActivity.this, "确认密码不能为空");
                } else if (!userPhoneNum.getText().toString().equals(editPhone.getText().toString())) {
                    ToastUtil.showCenterToast(RegisterShopSettingsActivity.this, "两次输入密码不一致");
                } else if (Regular.isNumeric(userPhoneNum.getText().toString())) {
                    ToastUtil.showCenterToast(RegisterShopSettingsActivity.this, "密码必须为数字和字母组合");
                } else if (Regular.isLetter(userPhoneNum.getText().toString())) {
                    ToastUtil.showCenterToast(RegisterShopSettingsActivity.this, "密码必须为数字和字母组合");
                } else if (Regular.containSpecialCharacter(userPhoneNum.getText().toString())) {
                    ToastUtil.showCenterToast(RegisterShopSettingsActivity.this, "密码必须为数字和字母组合");
                } else if (userPhoneNum.getText().toString().length() < 6) {
                    ToastUtil.showCenterToast(RegisterShopSettingsActivity.this, "密码必须为数字和字母组合");
                } else if (check.isChecked() == false) {
                    ToastUtil.showCenterToast(RegisterShopSettingsActivity.this, "请同意注册协议");
                } else {
                    register();
                }


                break;

            case R.id.img_clear2:
                editPhone.setText("");
                break;
        }
    }

    static Toast mToast = null;

    public void register() {
        MyOkhttp.post(Constant.URL + "api/v1/system/registerSubmit")
                .tag(this)
                .params("mobile", phone)
                .params("password", editPhone.getText().toString())

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

                        RegisterBean registerBean = new RegisterBean();
                        registerBean = gson.fromJson(s, RegisterBean.class);
                        if (registerBean.getMeta().isSuccess() == false) {
//                            Toast.makeText(RegisterSettingsActivity.this, registerBean.getMeta().getMessage() + "", Toast.LENGTH_SHORT).show();
                            ToastUtil.showCenterToast(RegisterShopSettingsActivity.this, registerBean.getMeta().getMessage());


                        } else {
//                            Toast.makeText(RegisterSettingsActivity.this, registerBean.getMeta().getMessage() + "", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(RegisterSettingsActivity.this, registerBean.getMeta().getMessage() + "", Toast.LENGTH_SHORT).show();
//                            ToastUtil.showCenterToast(RegisterSettingsActivity.this, registerBean.getMeta().getMessage());
//                            mToast = new Toast(RegisterSettingsActivity.this);
//                            mToast.setText(registerBean.getMeta().getMessage());
                            mToast = Toast.makeText(RegisterShopSettingsActivity.this, registerBean.getMeta().getMessage(), Toast.LENGTH_SHORT);

                            mToast.setDuration(1000);
                            mToast.setGravity(Gravity.CENTER, 0, 0);
                            mToast.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mToast.cancel();
                                    login();
                                }
                            }, 1000);


                        }


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.d(response.toString(), "333333333333");
                        mydismissloading();
                        Toast.makeText(RegisterShopSettingsActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        mydismissloading();
                    }
                });
    }

    private final String ACTION_NAME2 = "登录结束";
    private final String ACTION_NAME = "智投宝结束";

    public void login() {
        MyOkhttp.post(Constant.URL + "api/v1/system/login")
                .tag(this)
                .params("username", phone)
                .params("password", editPhone.getText().toString())
                .params("did", deviceId)
                .params("deviceToken", getDeviceToken())
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
                        LoginBean loginBean = new LoginBean();
                        loginBean = gson.fromJson(s, LoginBean.class);
                        LoginBean.DataBean dataBean = loginBean.getData();
                        if (loginBean.getMeta().isSuccess() == false) {
                            ToastUtil.showCenterToast(RegisterShopSettingsActivity.this, loginBean.getMeta().getMessage());
                        } else {
                            EventBus.getDefault().post(250);
//                            EventBus.getDefault().post(10);
//                            ToastUtil.showCenterToast(RegisterSettingsActivity.this, loginBean.getMeta().getMessage());
                            MySharePreferenceUtil.put(RegisterShopSettingsActivity.this, "id", loginBean.getData().getId());
                            MySharePreferenceUtil.put(RegisterShopSettingsActivity.this, "username", loginBean.getData().getUsername());
                            MySharePreferenceUtil.put(RegisterShopSettingsActivity.this, "phone", loginBean.getData().getPhone());
                            MySharePreferenceUtil.put(RegisterShopSettingsActivity.this, "appHeadUrl", "");
                            MySharePreferenceUtil.put(RegisterShopSettingsActivity.this, "token", loginBean.getData().getToken());
                            MySharePreferenceUtil.put(RegisterShopSettingsActivity.this, "idCard", "");
                    /*        Intent mIntent = new Intent(ACTION_NAME2);
                            mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                            //发送广播
                            sendBroadcast(mIntent);*/
//                            EventBus.getDefault().post(9999);
                       /*     Intent intent = new Intent(RegisterShopSettingsActivity.this, RegisterShopActivity.class);
                            intent.putExtra("loginshop", "success");
                            setResult(5000, intent);*/

                            EventBus.getDefault().post(9999);

                            finish();
                        }


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.d(response.toString(), "333333333333");
                        mydismissloading();
//                        Toast.makeText(RegisterSettingsActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        mydismissloading();
                    }
                });
    }

    private final String ACTION_NAME1 = "注册协议";
    private BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME1)) {
                check.setChecked(true);
            }
        }

    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME1);
        //注册广播
        registerReceiver(mBroadcastReceiver1, myIntentFilter);


    }

    public void onEventMainThread(Integer type) {
        Log.e("---", "EventBus收到int:" + type);
        ToastUtil.showCenterToast(RegisterShopSettingsActivity.this, type + "");

        if (type == 100) {
            check.setChecked(true);
            check.setSelected(true);
        }

    }


}
