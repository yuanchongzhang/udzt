package com.xmrxcaifu.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import com.tencent.smtt.sdk.QbSdk;
import com.xmrxcaifu.Constant;
import com.xmrxcaifu.MainActivity;

import com.xmrxcaifu.R;
import com.xmrxcaifu.StatusBarUtil;
import com.xmrxcaifu.http.MyOkhttp;
import com.xmrxcaifu.http.callback.StringNoDialogCallback;
import com.xmrxcaifu.http.request.BaseRequest;
import com.xmrxcaifu.httpmodel.GuangGaoBean;
import com.xmrxcaifu.service.FirstLoadingX5Service;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.util.TimeCount;
import com.xmrxcaifu.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 欢迎界面
 *
 * @author shenshao
 */

public class SplashActivity extends Activity {


    private static final String SHAREDPREFERENCES_NAME = "first_pref";
    //    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private static final long SPLASH_DELAY_MILLIS = 0;
    private int delayTime = 4;// 广告4秒倒计时
    private final int UPDATE_TEAY_TIME = 103;
    private String description;// 更新内容

    private Dialog dialog;
    private RelativeLayout rl;
    private TextView dTime;
    private TextView skipBtn;
    private String linkurl = "", imageurl = "";
    private RelativeLayout rl_jump;
    private TimeCount timeCount;
    /**
     * 手机信息收集
     */
    private ImageView welComeImg, adImg;
    private String version;// 版本号
    private String token;// 手机串号
    private String token2;// 手机串号
    private static final int REFRESH_COMPLETE = 11003;//获取网络图片呢
    private String channel;
    private Bitmap bitmap;
    LinearLayout layout_network;


    RelativeLayout top_layout;

    ImageView img_network;

    String zhiwendenglu1;

    String flag_shoushi1;


    TextView text_network;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME)) {
                finish();


            }
        }

    };
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
//                    bitmap = returnBitMap(getLinkurl);
                    bitmap1 = returnBitMap(getLinkurl);


                    break;
            }
        }

    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);

    }

    String phone;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
//        unregisterReceiver(mBroadcastReceiver);

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

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {


            switch (msg.what) {


                /*case GO_HOME:

                    goHome();
                    break;*/

                case 10:
//                    Toast.makeText(SplashActivity.this,"10",Toast.LENGTH_LONG).show();
                    SplashActivity.this.finish();
                    break;
                case UPDATE_TEAY_TIME:
//                    Toast.makeText(SplashActivity.this,"UPDATE_TEAY_TIME",Toast.LENGTH_LONG).show();
                    if (delayTime > 0) {
                        welComeImg.setVisibility(View.GONE);
                        rl.setVisibility(View.VISIBLE);
                        dTime.setText(delayTime + "");
                        handler.sendEmptyMessageDelayed(UPDATE_TEAY_TIME, 1000);
                        delayTime--;
                    } else {
//                        handler.sendEmptyMessage(GO_HOME);
                        goHome();
                    }


                    break;


            }
        }

        ;
    };
    SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().getDecorView().setBackgroundResource(R.drawable.v6splash3);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
//        registerBoradcastReceiver();

        MySharePreferenceUtil.put(SplashActivity.this, "isSpalash", "yes");

        WindowManager wm = (WindowManager) getBaseContext()
                .getSystemService(Context.WINDOW_SERVICE);
        zhiwendenglu1 = (String) MySharePreferenceUtil.get(SplashActivity.this, phone + "zhiwendenglu", "");
        flag_shoushi1 = (String) MySharePreferenceUtil.get(SplashActivity.this, "flag_shoushi", "");
        //取出数据,第一个参数是写入是的键，第二个参数是如果没有获取到数据就默认返回的值。
        try {
            phone = (String) MySharePreferenceUtil.get(SplashActivity.this, "phone1", "");
        } catch (Exception e) {
        }
        sharedPreferences = getSharedPreferences("mytest", MODE_PRIVATE);


//2、取出数据
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        top_layout = (RelativeLayout) findViewById(R.id.top_layout);
        img_network = (ImageView) findViewById(R.id.img_network);
        text_network = (TextView) findViewById(R.id.text_network);
        token = (String) MySharePreferenceUtil.get(SplashActivity.this, "token", "");
        layout_network = (LinearLayout) findViewById(R.id.layout_network);
        rl = (RelativeLayout) findViewById(R.id.rl_show_ad);
        adImg = (ImageView) findViewById(R.id.iv_ad_img);
        dTime = (TextView) findViewById(R.id.tv_time);
        skipBtn = (TextView) findViewById(R.id.ll_ad_skip_btn);
        rl_jump = (RelativeLayout) findViewById(R.id.rl_jump);
        ImmersionBar.with(SplashActivity.this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        final String phone = (String) MySharePreferenceUtil.get(SplashActivity.this, "phone1", "");
        String flaggesture = (String) MySharePreferenceUtil.get(SplashActivity.this, phone + "flaggesture", "");

        if (flaggesture.equals("1")) {
            MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "1");
        } else {
            MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "2");
        }

        welComeImg = (ImageView) findViewById(R.id.iv_welcome_img);
        String str = ((String) MySharePreferenceUtil.get(SplashActivity.this, "guide", ""));
        layout_network = (LinearLayout) findViewById(R.id.layout_network);
        layout_network.setVisibility(View.GONE);
//            iv_welcome_img.
        welComeImg.setVisibility(View.VISIBLE);
//        goOn();
//        requestContactPermission(Manifest.permission.READ_PHONE_STATE);

        layout_network.setFitsSystemWindows(false);
        welComeImg.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(str)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goGuide();
                }
            }, 2000);

        } else {
            welComeImg.setVisibility(View.VISIBLE);
            adv();

        }

        dTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtil.showCenterToast(SplashActivity.this, "到这里了么");
                MySharePreferenceUtil.put(SplashActivity.this, "advtisement", "0");
                String phone = (String) MySharePreferenceUtil.get(SplashActivity.this, "phone1", "");
                String flaggesture = (String) MySharePreferenceUtil.get(
                        SplashActivity.this, phone + "flaggesture", "");
                String zhiwendenglu = (String) MySharePreferenceUtil.get(
                        SplashActivity.this, phone + "zhiwendenglu", "");
                if (flaggesture.equals("1")) {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("flag_gest", "1");
                    intent.putExtra("url", Constant.URL);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    overridePendingTransition(R.anim.slide_in_from_bottom,
                            R.anim.slide_out_to_bottom);
                    MySharePreferenceUtil.put(SplashActivity.this, "flagadv", "2");
                    MySharePreferenceUtil.put(SplashActivity.this, "flagloading", "10");
                    MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "1");
                    finish();
                    handler.removeCallbacksAndMessages(null);

                } else if (flaggesture.equals("0") && zhiwendenglu.equals("1")) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("url", getLinkurl);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "1");
                    finish();
                } else {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("flag_gest", "1");
                    intent.putExtra("url", Constant.URL);
                    intent.putExtra("flage", "10");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_from_bottom,
                            R.anim.slide_out_to_bottom);
                    MySharePreferenceUtil.put(SplashActivity.this, "flagadv", "2");
                    MySharePreferenceUtil.put(SplashActivity.this, "flagloading", "10");
                    MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "2");
                    finish();
                    handler.removeCallbacksAndMessages(null);

                }

            }
        });
        rl_jump.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtil.showCenterToast(SplashActivity.this, "到这里了么");

                MySharePreferenceUtil.put(SplashActivity.this, "advtisement", "0");
                String phone = (String) MySharePreferenceUtil.get(SplashActivity.this, "phone1", "");
                String flaggesture = (String) MySharePreferenceUtil.get(SplashActivity.this, phone + "flaggesture", "");
                String zhiwendenglu = (String) MySharePreferenceUtil.get(SplashActivity.this, phone + "zhiwendenglu", "");
                if (flaggesture.equals("1")) {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("flag_gest", "1");
                    intent.putExtra("url", Constant.URL);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    overridePendingTransition(R.anim.slide_in_from_bottom,
                            R.anim.slide_out_to_bottom);
                    MySharePreferenceUtil.put(SplashActivity.this, "flagadv", "2");
                    MySharePreferenceUtil.put(SplashActivity.this, "flagloading", "10");
                    MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "1");
                    finish();
                    handler.removeCallbacksAndMessages(null);

                } else if (flaggesture.equals("0") && zhiwendenglu.equals("1")) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("url", getLinkurl);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "1");
                    finish();
                } else {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("flag_gest", "1");
                    intent.putExtra("url", Constant.URL);
                    intent.putExtra("flage", "10");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_from_bottom,
                            R.anim.slide_out_to_bottom);
                    MySharePreferenceUtil.put(SplashActivity.this, "flagadv", "2");
                    MySharePreferenceUtil.put(SplashActivity.this, "flagloading", "10");
                    MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "2");
                    finish();
                    handler.removeCallbacksAndMessages(null);

                }

            }
        });

        adImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String flaggesture = (String) MySharePreferenceUtil.get(SplashActivity.this, phone + "flaggesture", "");
                String newintentflag = (String) MySharePreferenceUtil.get(SplashActivity.this, "newintentflag", "");
                String zhiwendenglu = (String) MySharePreferenceUtil.get(SplashActivity.this, phone + "zhiwendenglu", "");

                MySharePreferenceUtil.put(SplashActivity.this, "advtisement", "1");



                if (TextUtils.isEmpty(getLinkurl)) {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("url", Constant.URL);
                    startActivity(intent);
                } else if (flaggesture.equals("0") && zhiwendenglu.equals("1")) {
                    MySharePreferenceUtil.put(SplashActivity.this, "wailianfou", "1");

                    MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "1");
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                    intent.putExtra("url", getLinkurl);
                    if (getLinkurl.contains(Constant.URL)) {
                        intent.putExtra("url", getLinkurl);
                        MySharePreferenceUtil.put(SplashActivity.this, "wailianfou", "0");
                    } else {
                        intent.putExtra("url", Constant.URL);
                        MySharePreferenceUtil.put(SplashActivity.this, "wailianfou", "1");
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "6");

                } else if (flaggesture.equals("1")) {
                    MySharePreferenceUtil.put(SplashActivity.this, "wailianfou", "1");
                    MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "1");
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    if (getLinkurl.contains(Constant.URL)) {
                        MySharePreferenceUtil.put(SplashActivity.this, "wailianfou", "0");
                        intent.putExtra("url", getLinkurl);
                    } else {
                        intent.putExtra("url", Constant.URL);
                        MySharePreferenceUtil.put(SplashActivity.this, "wailianfou", "1");
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    MySharePreferenceUtil.put(SplashActivity.this, "wailianfou", "1");
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                    intent.putExtra("url", getLinkurl);
                    if (getLinkurl.contains(Constant.URL)) {
                        intent.putExtra("url", getLinkurl);
                        MySharePreferenceUtil.put(SplashActivity.this, "wailianfou", "0");
                    } else {
                        intent.putExtra("url", Constant.URL);
                        MySharePreferenceUtil.put(SplashActivity.this, "wailianfou", "1");
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                handler.removeCallbacksAndMessages(null);
                finish();
            }
        });
        layout_network.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!network()) {
                    ToastUtil.showCenterToast(SplashActivity.this, "当前无网络");
                } else {
                    String str = ((String) MySharePreferenceUtil.get(SplashActivity.this, "guide", ""));
                    goOn();
                    layout_network.setVisibility(View.GONE);
//            iv_welcome_img.
                    layout_network.setFitsSystemWindows(false);
                    welComeImg.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(str)) {
                        goGuide();
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adv();
                            }
                        }, 2000);
                    }
                }
            }
        });
        top_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!network()) {
                    ToastUtil.showCenterToast(SplashActivity.this, "当前无网络");
                } else {
                    String str = ((String) MySharePreferenceUtil.get(SplashActivity.this, "guide", ""));
                    goOn();
                    layout_network.setVisibility(View.GONE);
//            iv_welcome_img.
                    layout_network.setFitsSystemWindows(false);
                    welComeImg.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(str)) {
                        goGuide();
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adv();
                            }
                        }, 2000);
                    }
                }
            }
        });

        img_network.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!network()) {
                    ToastUtil.showCenterToast(SplashActivity.this, "当前无网络");
                } else {
                    String str = ((String) MySharePreferenceUtil.get(SplashActivity.this, "guide", ""));
                    goOn();
                    layout_network.setVisibility(View.GONE);
//            iv_welcome_img.
                    layout_network.setFitsSystemWindows(false);
                    welComeImg.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(str)) {
                        goGuide();
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adv();
                            }
                        }, 2000);


                    }
                }
            }
        });
        text_network.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!network()) {
                    ToastUtil.showCenterToast(SplashActivity.this, "当前无网络");
                } else {
                    String str = ((String) MySharePreferenceUtil.get(SplashActivity.this, "guide", ""));
                    goOn();
                    layout_network.setVisibility(View.GONE);
//            iv_welcome_img.
                    layout_network.setFitsSystemWindows(false);
                    welComeImg.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(str)) {
                        goGuide();
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adv();
                            }
                        }, 2500);

                    }
                }
            }
        });
        try {
            // 程序在内存清理的时候执行（回收内存）
            String path = getFilesDir().getParent();
//            deleteFolder(new File(path + "/app_webview"));
//
//            deleteFolder(new File(path + "/app_tbs"));
            deleteFolder(new File(path + "/cache"));

            deleteFolder(new File(path + "/databases"));
            deleteFolder(new File(path + "/files"));
        } catch (Exception e) {
        }

    }

    String getLinkurl = "";
    Bitmap bitmap1;


    private void requestContactPermission(String permission) {
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            //申请 WRITE_CONTACTS 权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
//            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        } else {

            goOn();
        }
    }


    public void adv() {
        StatusBarUtil.transparencyBar(SplashActivity.this);
        MyOkhttp.get(Constant.URL + "api/v1/choiceNess/bannerByObject/6")
                .tag(this)
                .execute(new StringNoDialogCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
//                        Log.d(s.toString(), "3333333");
                        try {
                            Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
//                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
                            Gson gson = new Gson();
                            GuangGaoBean guangGaoBean = new GuangGaoBean();
                            guangGaoBean = gson.fromJson(s, GuangGaoBean.class);
                            getLinkurl = guangGaoBean.getData().getLinkUrl();
                            MySharePreferenceUtil.put(SplashActivity.this, "getLinkurl", getLinkurl);
//                            bitmap1 = returnBitMap(getLinkurl);

//                        Toast.makeText(SplashActivity.this, getLinkurl + "", Toast.LENGTH_SHORT).show();
                            if (guangGaoBean.getMeta().isSuccess() == true) {
//                                welComeImg.setVisibility(View.GONE);
                                ImmersionBar.with(SplashActivity.this)
                                        .statusBarDarkFont(true, 0.2f)
                                        .init();
                                rl.setVisibility(View.VISIBLE);

                                final GuangGaoBean finalGuangGaoBean = guangGaoBean;

                                Glide.with(SplashActivity.this)
                                        .load(guangGaoBean.getData().getPictureUrl())
                                        .asBitmap()
                                        .placeholder(R.mipmap.v6splash32)
                                        .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                                            @Override
                                            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                                                //得到bitmap
//                                        adImg.setImageBitmap(bitmap);
                                                Log.d(bitmap.toString(), "333333332121");
                                                adImg.setImageBitmap(bitmap);
                                            }
                                        });
//                                Glide.with(SplashActivity.this).load(guangGaoBean.getData().getPictureUrl()).centerCrop().placeholder(R.mipmap.v6splash32).into(adImg);
                                //图片加载出来前，显示的图片
                                if (delayTime > 0) {
//                                    welComeImg.setVisibility(View.GONE);
                                    rl.setVisibility(View.VISIBLE);
                                    dTime.setText(delayTime + "");
                                    handler.sendEmptyMessageDelayed(UPDATE_TEAY_TIME, 1000);
                                    delayTime--;
                                } else {
//                                    handler.sendEmptyMessage(GO_HOME);
                                    goHome();
                                }
                            } else {
//                                handler.sendEmptyMessage(GO_HOME);
                                goHome();
                            }
                        } catch (Exception e) {
                            goHome();
                        }


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);

                        welComeImg.setVisibility(View.VISIBLE);
                        goHome();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);

                    }
                });
    }

    private final String ACTION_NAME = "欢迎页失效";

    private void goOn() {
        String path = getFilesDir().getParent();
//        Toast.makeText(this, path+"", Toast.LENGTH_SHORT).show();
        MySharePreferenceUtil.put(SplashActivity.this, "advtisement", "0");
        skipBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtil.showCenterToast(SplashActivity.this, "跳过广告");
                String phone = (String) MySharePreferenceUtil.get(SplashActivity.this, "phone1", "");
                String flaggesture = (String) MySharePreferenceUtil.get(SplashActivity.this, phone + "flaggesture", "");
                String zhiwendenglu = (String) MySharePreferenceUtil.get(SplashActivity.this, phone + "zhiwendenglu", "");
                if (flaggesture.equals("1")) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("flag_gest", "1");
                    intent.putExtra("url", Constant.URL);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_from_bottom,
                            R.anim.slide_out_to_bottom);
                    MySharePreferenceUtil.put(SplashActivity.this, "flagadv", "2");
                    MySharePreferenceUtil.put(SplashActivity.this, "flagloading", "10");
                    MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "1");
                    finish();
                    handler.removeCallbacksAndMessages(null);

                } else if (flaggesture.equals("0") && zhiwendenglu.equals("1")) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("url", getLinkurl);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "1");

                } else {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("flag_gest", "1");
                    intent.putExtra("url", Constant.URL);
                    intent.putExtra("flage", "10");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    overridePendingTransition(R.anim.slide_in_from_bottom,
                            R.anim.slide_out_to_bottom);
                    MySharePreferenceUtil.put(SplashActivity.this, "flagadv", "2");
                    MySharePreferenceUtil.put(SplashActivity.this, "flagloading", "10");
                    MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "2");
                    finish();
                    handler.removeCallbacksAndMessages(null);

                }


            }
        });


    }

    private void goHome() {
        MySharePreferenceUtil.put(SplashActivity.this, "wailianfou", "0");
        String phone = "";
        try {
            phone = (String) MySharePreferenceUtil.get(SplashActivity.this, "phone1", "");
        } catch (Exception e) {

        }
        String flaggesture = (String) MySharePreferenceUtil.get(SplashActivity.this, phone + "flaggesture", "");
        String zhiwendenglu = (String) MySharePreferenceUtil.get(SplashActivity.this, phone + "zhiwendenglu", "");
        if (token.length() < 5) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("flage", "10");
            intent.putExtra("flag_gest", "2");
            intent.putExtra("url", Constant.URL);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_from_bottom,
                    R.anim.slide_out_to_bottom);
            MySharePreferenceUtil.put(SplashActivity.this, "flagadv", "2");
            MySharePreferenceUtil.put(SplashActivity.this, "flagloading", "10");
            MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "2");
//            handler.removeCallbacksAndMessages(null);
            finish();
        } else {
            if (flaggesture.equals("1")) {

//                    Toast.makeText(SplashActivity.this, "走的是这里", Toast.LENGTH_SHORT).show();

//                        Intent intent = new Intent(SplashActivity.this, GestureVerifyActivity.class); newintentflag
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("flag_gest", "2");
                intent.putExtra("url", Constant.URL);
                intent.putExtra("flage", "10");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_from_bottom,
                        R.anim.slide_out_to_bottom);
                MySharePreferenceUtil.put(SplashActivity.this, "flagadv", "2");
                MySharePreferenceUtil.put(SplashActivity.this, "flagloading", "10");
                MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "1");
//                handler.removeCallbacksAndMessages(null);
                finish();
            } else if (zhiwendenglu.equals("1") && flaggesture.equals("0")) {

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("flag_gest", "2");
                intent.putExtra("url", Constant.URL);
                intent.putExtra("flage", "10");
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_from_bottom,
                        R.anim.slide_out_to_bottom);
                MySharePreferenceUtil.put(SplashActivity.this, "flagadv", "2");
                MySharePreferenceUtil.put(SplashActivity.this, "flagloading", "10");
                MySharePreferenceUtil.put(SplashActivity.this, "newintentflag", "1");
//                handler.removeCallbacksAndMessages(null);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("flage", "10");
                intent.putExtra("flag_gest", "2");
                intent.putExtra("url", Constant.URL);
//                        intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_bottom,
                        R.anim.slide_out_to_bottom);
                MySharePreferenceUtil.put(SplashActivity.this, "flagadv", "2");
                MySharePreferenceUtil.put(SplashActivity.this, "flagloading", "10");
//                handler.removeCallbacksAndMessages(null);
                finish();
            }
        }


    }


    private void goGuide() {
        Intent intent = new Intent(SplashActivity.this, V4GuideActivity.class);// GuideActivity
        MySharePreferenceUtil.put(SplashActivity.this, "flagadv", "2");
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }


    /**
     * 判断某activity是否处于栈顶
     *
     * @return true在栈顶 false不在栈顶
     */
    private boolean isActivityTop(Class cls, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(cls.getName());
    }


    public Bitmap returnBitMap(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return bitmap;

    }


    private void preinitX5WithService() {

        Intent intent = new Intent(this, FirstLoadingX5Service.class);

        startService(intent);

    }

    /**
     * 递归删除
     */
    public void deleteFolder(File file) {
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFolder(files[i]);
            }
        }
        file.delete();
    }

    private void preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {

            // preinit只需要调用一次，如果已经完成了初始化，那么就直接构造viewQbSdk.preInit(MainActivity.this, null);// 设置X5初始化完成的回调接口}
            QbSdk.preInit(SplashActivity.this, null);
        }
    }


}

