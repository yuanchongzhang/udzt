package com.xmrxcaifu.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.umeng.message.common.UmLog;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.xmrxcaifu.BaseActivty;
import com.xmrxcaifu.BitmapUtils;
import com.xmrxcaifu.Constant;
import com.xmrxcaifu.ContactBean;
import com.xmrxcaifu.ContactInfoService;
import com.xmrxcaifu.OnBooleanListener;
import com.xmrxcaifu.R;
import com.xmrxcaifu.Rom;
import com.xmrxcaifu.common.LoadingDialog;
import com.xmrxcaifu.core.FingerprintCore;
import com.xmrxcaifu.dialog.CustomDialog;
import com.xmrxcaifu.dialog.CustomDialog_view_new;
import com.xmrxcaifu.http.MyOkhttp;
import com.xmrxcaifu.http.callback.StringNoDialogCallback;
import com.xmrxcaifu.http.request.BaseRequest;
import com.xmrxcaifu.modle.BeanGesture;
import com.xmrxcaifu.modle.DianhuaModel;
import com.xmrxcaifu.modle.EventBean;
import com.xmrxcaifu.modle.FaceModel;
import com.xmrxcaifu.modle.PdfModel;
import com.xmrxcaifu.modle.ShareModel;
import com.xmrxcaifu.modle.ShopDetialModel;
import com.xmrxcaifu.modle.ShopModel;
import com.xmrxcaifu.modle.ShopTokenModel;
import com.xmrxcaifu.modle.UdrmbBean;
import com.xmrxcaifu.modle.UpDateBean;
import com.xmrxcaifu.modle.User;
import com.xmrxcaifu.modle.Wailian;
import com.xmrxcaifu.photopick.ImageInfo;
import com.xmrxcaifu.photopick.PhotoPickActivity;
import com.xmrxcaifu.service.MyService;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.FingerprintUtil;
import com.xmrxcaifu.util.KeyguardLockScreenManager;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.util.NewTextView;
import com.xmrxcaifu.util.ToastUtil;
import com.xmrxcaifu.util.UpdateManager;
import com.xmrxcaifu.view.MyLoadingDialog;
import com.xmrxcaifu.view.MyNewLoadingDialog2;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;
import okhttp3.Response;

public class ShopActivity extends BaseActivty implements TencentLocationListener {

    //    RelativeLayout rl_back;
    private BridgeWebView mWebView;
    private final static int REQUEST_IDIMAGE = 100;
    private final static int REQUEST_DRIVEIMAGE = 200;
    private final static int REQUEST_BANKIMAGE = 300;
    private final static int REQUEST_PHOTO = 400;
    ValueCallback<Uri> mUploadMessage;
    private Bitmap bitmap = null;
    private int nCurrentCartType = 0;
    int RESULT_CODE = 0;
    private List<ContactBean> mContactBeanList;//所有联系人集合
    private TencentLocationManager mLocationManager;
    public final String USER_IMAGE_NAME = "image.png";
    public final String USER_CROP_IMAGE_NAME = "temporary.png";
    public final String USER_CROP_IMAGE_NAME2 = "temporary.png1";
    private static final String TAG = "MainActivity";
    private CallBackFunction contactFunction, cameraFunction, photoFunction, locationFunction, idCardFunction, driveCardFunction, bankCardFunction, openGestVCFuction, FigerVC;
    public Uri imageUriFromCamera;
    public Uri cropImageUri;
    public Uri imageUriFromPhoto;
    //    getGestInfo
    public final int GET_IMAGE_BY_CAMERA_U = 5001;
    public final int CROP_IMAGE_U = 5003;
    private long mLastBackTime = 0;
    private long TIME_DIFF = 2 * 1000;
    private String p = null;
    private TelephonyManager tm;
    public final int CROP_IMAGE_U2 = 5004;
    int progeress = 0;

    Button btn_login;
    String token;
    String url;
    String myphone;

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    private final String ACTION_NAME = "智投宝结束";
    private final String ACTION_NAME1 = "结束程序";
    View title_view_new;

    String str = "0";
    String flag_open_close = "";
    String imei = "";
    String ANDROID_ID = "";
    String mytoken;
    public static ShopActivity instance = null;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME)) {
                newmyflag = 1;
//ToastUtil.showCenterToast(MainActivity.this,"到这里了么");
                myNewLoadingDialog2 = new MyNewLoadingDialog2(ShopActivity.this);
                myNewLoadingBuilder2 = new MyNewLoadingDialog2.Builder(ShopActivity.this);
                myNewLoadingDialog2 = myNewLoadingBuilder.create();

                token = (String) MySharePreferenceUtil.get(ShopActivity.this, "token", "");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "token" + "','" + token + "');");
                if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(ShopActivity.this, "idCard", ""))) {
                    //未实名
                    mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(ShopActivity.this, "phone", "") + "');");
                    mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(ShopActivity.this, "phone", "") + "');");
                } else {
                    mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(ShopActivity.this, "username", "") + "');");
                    mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(ShopActivity.this, "phone", "") + "');");
                }
                mWebView.loadUrl("javascript:updateData()");

                myIntentFlag = "1";


            }
            myphone = (String) MySharePreferenceUtil.get(ShopActivity.this, "phone1", "");
        }

    };
    private BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME1)) {
                android.os.Process.killProcess(android.os.Process.myPid());
                ToastUtil.showCenterToast(ShopActivity.this, "这里了");

                finish();
            }
        }

    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);


    }

    public void registerBoradcastReceiver1() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME1);
        //注册广播
        registerReceiver(mBroadcastReceiver1, myIntentFilter);


    }

    int flag = 0;

    String myIntentFlag = "";

    ImmersionBar immersionBar;

    int newmyflag = 1;
    FrameLayout mFrameLayout;
    LinearLayout layout_network;
    WebChromeClient chromeClient = new WebChromeClient() {
        private View mCustomView;
        private IX5WebChromeClient.CustomViewCallback mCustomViewCallback;

        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            Toast.makeText(MainActivity.this,"调用之类1",Toast.LENGTH_LONG).show();

         /*   if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            mFrameLayout.addView(mCustomView);
            mCustomViewCallback = callback;
//            mWebView.setVisibility(View.GONE);

//横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*/

        }


        @Override
        public void onHideCustomView() {
      /*      mWebView.setVisibility(View.VISIBLE);
//            Toast.makeText(MainActivity.this,"调用之类2",Toast.LENGTH_LONG).show();
            if (mCustomView == null) {
                return;
            }
            //竖屏
            mCustomView.setVisibility(View.GONE);
            mFrameLayout.removeView(mCustomView);
            mCustomViewCallback.onCustomViewHidden();
            mCustomView = null;

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/
            super.onHideCustomView();
        }


        @Override
        public void onProgressChanged(WebView webView, int i) {
            Log.e("进度", i + "");
//            Toast.makeText(MainActivity.this, newmyflag+"", Toast.LENGTH_SHORT).show();


            Animation animation = AnimationUtils.loadAnimation(ShopActivity.this, R.anim.imganmi);

            LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
//            animation.setInterpolator(lin);


            if (newmyflag == 1) {

                animation.setDuration(500);
                imageView_loading.startAnimation(animation);
                if (i == 100) {
                    try {
                        myNewLoadingDialog.cancel();
                        myNewLoadingDialog.dismiss();
                        myNewLoadingDialog = null;
//                        mWebView.setVisibility(View.VISIBLE);
                        newmyflag = 3;
                    } catch (Exception e) {
                        newmyflag = 3;
                    }

                } else {
//                    mWebView.setVisibility(View.GONE);
                    try {
                        myNewLoadingDialog.show();
                    } catch (Exception e) {

                    }

                }
            } else if (newmyflag == 2) {
//                ToastUtil.showCenterToast(MainActivity.this,"2");
            } else {
//                ToastUtil.showCenterToast(MainActivity.this,"3");

            }


        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
            this.openFileChooser(uploadMsg);
        }

        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
            this.openFileChooser(uploadMsg);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            pickFile();
        }

    };

    private Intent serviceIntent;
    NewTextView text_Devicetoken;

    TextView text_shijian;
    private boolean loadError = false;
    int errmyCode = 0;
    int myflag = 0;
    ImageView img_network;
    int mynetworkflag = 0;
    TextView text_network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setImmersionStatus();
//        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        img_network = (ImageView) findViewById(R.id.img_network);
        text_network = (TextView) findViewById(R.id.text_network);
        registerBoradcastReceiver();
        tm = (TelephonyManager) getApplication().getSystemService(TELEPHONY_SERVICE);
        mFrameLayout = (FrameLayout) findViewById(R.id.mFrameLayout);
        serviceIntent = new Intent(ShopActivity.this, MyService.class);
        startService(serviceIntent);
        Log.d("devicetoken", getDeviceToken());
        mLocationManager = TencentLocationManager.getInstance(this);
        EventBus.getDefault().register(this);
        myphone = (String) MySharePreferenceUtil.get(ShopActivity.this, "phone1", "");
        ImmersionBar.with(ShopActivity.this)
                .statusBarDarkFont(false, 1f)
                .init();
        layout_network = (LinearLayout) findViewById(R.id.layout_network);
        immersionBar = ImmersionBar.with(this);
        title_view_new = findViewById(R.id.title_view_new);
        text_Devicetoken = (NewTextView) findViewById(R.id.text_Devicetoken);
        text_Devicetoken.setText(getDeviceToken());


        text_Devicetoken.setTextIsSelectable(true);
        ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
//        cmb.setText(content.trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        cmb.getText();


        newmyflag = 1;
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        mWebView = (BridgeWebView) findViewById(R.id.webView);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        token = (String) MySharePreferenceUtil.get(ShopActivity.this, "token", "");
        Log.d("7827838238", token);

        String flagloading = (String) MySharePreferenceUtil.get(ShopActivity.this, "flagloading", "");

        if (flagloading.equals("10")) {
            flag = 0;
        } else {
            flag = 0;
        }


        ZhugeSDK.getInstance().openLog();

//        ZhugeSDK.getInstance().openDebug();

        ZhugeSDK.getInstance().init(this);

        //定义与事件相关的属性信息
        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put("首页", "首页");

        } catch (JSONException e) {
            e.printStackTrace();
        }


//记录事件,以购买为例
        ZhugeSDK.getInstance().track(getApplicationContext(), "首页", eventObject);
        getdeviceId();
//        ToastUtil.showCenterToast(this,getdeviceId());
        text_shijian = (TextView) findViewById(R.id.text_shijian);
        initWebView();
        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mWebView.flushMessageQueue();
        if (!network()) {
//            startActivity(new Intent(MainActivity.this, NoNetActivity.class));
            layout_network.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            img_network.setImageResource(R.drawable.network);
            text_network.setText("数据加载失败,请检查一下你的网络");
            text_shijian.setText("点击重试");
        } else {
            layout_network.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
        }


        String updateUrl = Constant.URL + "/api/v1/appVersion/getCurrentVersion/1";
        MyOkhttp.get(updateUrl)
                .tag(this)
                .execute(new StringNoDialogCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
                        UpDateBean upDateBean = new UpDateBean();
                        Gson gson = new Gson();
                        upDateBean = gson.fromJson(s, UpDateBean.class);
                        try {
                            int intVer = Integer.parseInt(upDateBean.getData().getVersion().replace(".", ""));
                            int appVer = Integer.parseInt(getAppVersion().replace(".", ""));
                            if (appVer < intVer) {
                                showUpVersionDialog(upDateBean.getData().getDescription(), upDateBean.getData().getAppUrl(), upDateBean.getData().getForceUpdate(), upDateBean.getData().getVersion());
                            } else {
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);


                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);

                    }
                });

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);

        try {
            imei = telephonyManager.getDeviceId();
            ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        } catch (Exception e) {
        }

    }

    public static int count = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (errmyCode == -2) {
            long now = new Date().getTime();
            if (now - mLastBackTime < TIME_DIFF) {
                ShopActivity.this.finish();
                finish();
//                                        System.exit(0);
            } else {
                mLastBackTime = now;
                Toast.makeText(ShopActivity.this, "再按一次退出鑫茂荣信财富", 2000).show();
            }
        }
        if (errmyCode == -8) {
            long now = new Date().getTime();
            if (now - mLastBackTime < TIME_DIFF) {
                ShopActivity.this.finish();
                finish();
//                                        System.exit(0);
            } else {
                mLastBackTime = now;
                Toast.makeText(ShopActivity.this, "再按一次退出鑫茂荣信财富", 2000).show();
            }
        }


        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (title_view_new.getVisibility() == View.VISIBLE) {
                newmyflag = 1;
                mWebView.loadUrl(Constant.URL);
                title_view_new.setVisibility(View.GONE);

            } else {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        mWebView.evaluateJavascript("javascript:goBackForAndroid()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                Log.d("zzp", "--s--=" + s);

                                if ("\"finish\"".equals(s)) {

                                    long now = new Date().getTime();
                                    if (now - mLastBackTime < TIME_DIFF) {
                                        ShopActivity.this.finish();
                                        finish();
//                                        System.exit(0);
                                    } else {
                                        mLastBackTime = now;
                                        Toast.makeText(ShopActivity.this, "再按一次退出鑫茂荣信财富", 2000).show();
                                    }

                                } else if (s.equals("null")) {

                                }
                            }
                        });

                    }
                });
            }


            return true;
        }
        return false;
    }

    public void method() {

        count++;


    }


    /**
     * 获取手机的包管理者
     *
     * @return
     */
    public String getAppVersion() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packInfo = pm.getPackageInfo(getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // 不可能发生.
            return "获取版本号失败";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver);

//        mWebView.destroy();

        String path = getFilesDir().getParent();
//        Toast.makeText(this, path+"", Toast.LENGTH_SHORT).show();
        stopService(serviceIntent);


    }


    public void setImmersionStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void selectImage(int result) {
        MultiImageSelector.create(ShopActivity.this).showCamera(true) // 是否显示相机. 默认为显示
//                .count(1) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .single() // 单选模式
//                .multi() // 多选模式, 默认模式;
//                .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
                .start(ShopActivity.this, result);
    }

    String androidExit;
    MyLoadingDialog.Builder builder5;
    LoadingDialog dialog5;
    MyNewLoadingDialog2 myNewLoadingDialog;
    MyNewLoadingDialog2.Builder myNewLoadingBuilder;
    MyNewLoadingDialog2 myNewLoadingDialog6;
    MyNewLoadingDialog2.Builder myNewLoadingBuilder6;
    MyNewLoadingDialog2 myNewLoadingDialog2;
    MyNewLoadingDialog2.Builder myNewLoadingBuilder2;
    MyNewLoadingDialog2 myNewLoadingDialog4;
    MyNewLoadingDialog2.Builder myNewLoadingBuilder4;
    ImageView imageView_loading;
    MyNewLoadingDialog2 myNewLoadingDialog3;

    private void initWebView() {
        // 设置具体WebViewClient
        token = (String) MySharePreferenceUtil.get(ShopActivity.this, "token", "");
        token = (String) MySharePreferenceUtil.get(ShopActivity.this, "token", "");
        url = getIntent().getStringExtra("url");

        mWebView.loadUrl(url);
        text_shijian.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        text_shijian.getPaint().setAntiAlias(true);//抗锯齿
        text_shijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myflag = 1;
                if (!network()) {
                    mWebView.setVisibility(View.GONE);
                    ImmersionBar.with(ShopActivity.this)
                            .statusBarDarkFont(true, 1f)
                            .init();
                    img_network.setImageResource(R.drawable.network);
                    text_network.setText("数据加载失败,请检查一下你的网络");
                    text_shijian.setText("点击重试");
                    layout_network.setVisibility(View.VISIBLE);
                } else {
                    myflag = 1;
                    mWebView.loadUrl(Constant.URL);
                }
            }
        });
        mWebView.setWebViewClient(new MyWebViewClient(mWebView));
        // set HadlerCallBack
        mWebView.setDefaultHandler(new myHadlerCallBack());

        builder5 = new MyLoadingDialog.Builder(ShopActivity.this)
                .setShowMessage(false)
                .setCancelable(false);
        dialog5 = builder5.create();


        myNewLoadingDialog = new MyNewLoadingDialog2(ShopActivity.this);
        myNewLoadingBuilder = new MyNewLoadingDialog2.Builder(ShopActivity.this);
        myNewLoadingDialog = myNewLoadingBuilder.create();


        Window window = myNewLoadingDialog.getWindow();
        imageView_loading = (ImageView) window.findViewById(R.id.img_zhuanquan);

        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);

        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();

//        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setSavePassword(false);
//   mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//  mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        mWebView.getSettings().setDefaultTextEncodingName("UTF -8");

//        mWebView.loadData("", "text/html; charset=UTF-8", null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        //webview在安卓5.0之前默认允许其加载混合网络协议内容
// 在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setWebChromeClient(chromeClient);


        mWebView.registerHandler("submitFromWeb", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

                String str = "这是html返回给java的数据:" + data;
                // 例如你可以对原始数据进行处理
                str = str + ",Java经过处理后截取了一部分：" + str.substring(0, 5);
                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                Toast.makeText(ShopActivity.this, str, Toast.LENGTH_SHORT).show();
                //回调返回给Js
                function.onCallBack(str + ",Java经过处理后截取了一部分：" + str.substring(0, 5));
            }

        });
//        mWebView.addBridgeInterface(new MyJavaSctiptInterface(mWebView, this));//注册桥梁类，该类负责H5和android通信


        mWebView.registerHandler("functionOpen", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(ShopActivity.this, "网页在打开你的下载文件预览", Toast.LENGTH_SHORT).show();
                pickFile();

            }

        });

        mWebView.registerHandler("toXinDouLogin", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
//ToastUtil.showCenterToast(ShopActivity.this, "toXinDouLogin");
                        Log.d(data, "toXinDouLogintoXinDouLogin");
                        MySharePreferenceUtil.put(ShopActivity.this, "token", "");
                        Gson gson = new Gson();
                        ShopDetialModel shopDetialModel = new ShopDetialModel();
                        shopDetialModel = gson.fromJson(data, ShopDetialModel.class);

                        MySharePreferenceUtil.put(ShopActivity.this, "shopUrl", shopDetialModel.getExternalLink());

                        startActivity(new Intent(ShopActivity.this, LoginShopActivity.class));
                    }
                });
        mWebView.registerHandler("msgSend", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Gson gson = new Gson();
                DianhuaModel dianhuaModel = new DianhuaModel();
                dianhuaModel = gson.fromJson(data, DianhuaModel.class);


                Uri smsToUri = Uri.parse("smsto:" + dianhuaModel.getTelphone());

                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

//                intent.putExtra("sms_body", smsBody);

                startActivity(intent);
            }

        });

        /**
         * 获取版本号
         */
        mWebView.registerHandler("getVersion", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
            /*    function.onCallBack(MySharePreferenceUtil
                        .get(MainActivity.this, "versionName", "").toString());*/

                function.onCallBack(getAppVersion());
            }
        });


        mWebView.registerHandler("repass", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

                Toast.makeText(ShopActivity.this, data + "", Toast.LENGTH_SHORT).show();


            }
        });


        mWebView.registerHandler("goBackForAndroid", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(ShopActivity.this, "返回上一级页面", Toast.LENGTH_SHORT).show();
                mWebView.loadUrl(String.format("javascript:WebViewJavascriptBridge._handleMessageFromNative(%s)", data));

            }

        });


//跳转积分商城外链
        mWebView.registerHandler("goXindouWebView", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
//                ToastUtil.showCenterToast(MainActivity.this,data);
                Log.d(data, "323223232");
                Gson gson = new Gson();
                ShopModel shopModel = new ShopModel();
                shopModel = gson.fromJson(data, ShopModel.class);
                String token1 = (String) MySharePreferenceUtil.get(ShopActivity.this, "token1", "");
                MySharePreferenceUtil.put(ShopActivity.this, "shopUrl", shopModel.getData());
                MySharePreferenceUtil.put(ShopActivity.this, "shopUrl2", shopModel.getData());
                Log.d(token1, "3242342342342688hgh");
                if (TextUtils.isEmpty(token1)) {
                    Intent intent = new Intent(ShopActivity.this, ShopActivity2.class);
                    intent.putExtra("url", shopModel.getData());
                    intent.putExtra("url2", shopModel.getData());
                    MySharePreferenceUtil.put(ShopActivity.this, "shopUrl", shopModel.getData());
                    MySharePreferenceUtil.put(ShopActivity.this, "shopUrl2", shopModel.getData());
                    startActivity(intent);
                } else if (token1.length() < 5) {
                    Intent intent = new Intent(ShopActivity.this, ShopActivity2.class);
                    intent.putExtra("url", shopModel.getData());
                    intent.putExtra("url2", shopModel.getData());
                    MySharePreferenceUtil.put(ShopActivity.this, "shopUrl", shopModel.getData());
                    startActivity(intent);
                } else {
                    String updateUrl = Constant.URL + "api/v1/shoppingMall/getMallToken";
                    final ShopModel finalShopModel = shopModel;
                    MyOkhttp.get(updateUrl)
                            .tag(this)
                            .headers("token", token1)
                            .execute(new StringNoDialogCallback() {
                                @Override
                                public void onBefore(BaseRequest request) {
                                    super.onBefore(request);
                                }

                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Log.d(s, "5656565656");

                                    Gson gson1 = new Gson();
                                    ShopTokenModel shopTokenModel = new ShopTokenModel();
                                    shopTokenModel = gson1.fromJson(s, ShopTokenModel.class);
//                                    Log.d("34343434",finalShopModel.getData() + "?token=" + shopTokenModel.getData().getToken());
                                    mytoken = shopTokenModel.getData().getToken();

                                    Intent intent = new Intent(ShopActivity.this, ShopActivity2.class);

                                    if (finalShopModel.getData().contains("?")) {
                                        intent.putExtra("url", finalShopModel.getData() + "&token=" + mytoken);
                                    } else {
                                        intent.putExtra("url", finalShopModel.getData() + "?token=" + mytoken);
                                    }
                                    Log.d("83283828", finalShopModel.getData() + "&token=" + mytoken);
                                    Log.d("83283828", finalShopModel.getData() + "&token=" + mytoken);
                                    Log.d("83283828", finalShopModel.getData() + "&token=" + mytoken);

                                    intent.putExtra("url2", finalShopModel.getData());
                                    startActivity(intent);
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    Log.d(response.toString(), "333333333333");
                                }

                                @Override
                                public void onAfter(String s, Exception e) {
                                    super.onAfter(s, e);
                                }
                            });


                }
            }
        });

        mWebView.registerHandler("toUdrmbWeb", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                if (isAppInstalled("com.zprmb.udzt")) {
//                    unstallApp();
                    final Dialog dialog_udrmb;
                    dialog_udrmb = new Dialog(ShopActivity.this);
                    dialog_udrmb.setCancelable(false);
                    Window window = dialog_udrmb.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
                            R.color.float_transparent)));
//        window.setGravity(Gravity.CENTER);
                    window.setGravity(Gravity.CENTER);
                    dialog_udrmb.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    window.setContentView(R.layout.item_udrmb_dialog);
                    dialog_udrmb.show();

                    TextView text_cancel = (TextView) window.findViewById(R.id.text_cancel);
                    text_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_udrmb.dismiss();
                        }
                    });


                    TextView text_submit = (TextView) window.findViewById(R.id.text_submit);
                    text_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            ComponentName comp = new ComponentName("com.zprmb.udzt",
                                    "com.zprmb.udzt.SplashActivity");
                            intent.setComponent(comp);

                            int launchFlags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;

                            intent.setFlags(launchFlags);

                            intent.setAction("android.intent.action.VIEW");

                            Bundle bundle = new Bundle();
                            bundle.putString("from", "来自测试应用");
                            intent.putExtras(bundle);
                            startActivity(intent);
                            dialog_udrmb.dismiss();
                        }
                    });


                } else {
                    Intent intent = new Intent(ShopActivity.this, MainActivity3.class);
                    intent.putExtra("url", "https://m.udrmb.com/h5/index");
                    startActivity(intent);

                }

            }

        });
        mWebView.registerHandler("AndroidDeviceID", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("imei", imei);
                    jsonObject.put("ANDROID_ID", ANDROID_ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                function.onCallBack(jsonObject.toString());

            }

        });
        mWebView.registerHandler("toWebDetail", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

                Log.d(data, "77777777");
                try {
                    Gson gson = new Gson();
                    UdrmbBean udrmbBean = new UdrmbBean();
                    udrmbBean = gson.fromJson(data.toString(), UdrmbBean.class);
                    Intent intent = new Intent(ShopActivity.this, MainActivity5.class);
                    intent.putExtra("url", udrmbBean.getData());
                    startActivity(intent);

                } catch (Exception e) {

                }


            }

        });
        mWebView.registerHandler("getAndroidExit", new

                BridgeHandler() {

                    @Override
                    public void handler(String data, CallBackFunction function) {
                        Toast.makeText(ShopActivity.this, "返回上一级页面", Toast.LENGTH_SHORT).show();


                    }

                });


        mWebView.registerHandler("agreementBack", new

                BridgeHandler() {

                    @Override
                    public void handler(String data, CallBackFunction function) {
                        Toast.makeText(ShopActivity.this, "返回", Toast.LENGTH_SHORT).show();
                        pickFile();

                    }

                });


        mWebView.registerHandler("contact", new

                BridgeHandler() {

                    @Override
                    public void handler(String data, CallBackFunction function) {
                        contactFunction = function;
                        requestContactPermission(Manifest.permission.READ_CONTACTS, function);

                    }

                });
        mWebView.registerHandler("useragent", new

                BridgeHandler() {

                    @Override
                    public void handler(String data, CallBackFunction function) {
                        function.onCallBack("xxc_android");

                    }

                });

        mWebView.registerHandler("deviceId", new

                BridgeHandler() {

                    @Override
                    public void handler(String data, CallBackFunction function) {

                        function.onCallBack(getdeviceId());

                    }

                });
        mWebView.registerHandler("mobileType", new

                BridgeHandler() {

                    @Override
                    public void handler(String data, CallBackFunction function) {
                        Build build = new Build();

                        function.onCallBack(build.MODEL);

                    }

                });


        mWebView.registerHandler("toPDFVC", new

                BridgeHandler() {

                    @Override
                    public void handler(String data, CallBackFunction function) {
//                Toast.makeText(MainActivity.this, "pdf", Toast.LENGTH_SHORT).show();
                        Log.d(data.toString(), "333333333333333333");
                        Log.d(data.toString(), "333333333333333333");
                        Gson gson = new Gson();
                        PdfModel pdfModel = new PdfModel();
                        pdfModel = gson.fromJson(data, PdfModel.class);
//                Intent intent = new Intent(MainActivity.this, PdfActivity2.class);
                        Intent intent;

                        if (pdfModel.getPdfUrl().contains("viewContract.api")) {
                            intent = new Intent(ShopActivity.this, HongBaoWebActivity.class);
                        } else {
//                            intent = new Intent(MainActivity.this, PDFActivity6.class);
                            intent = new Intent(ShopActivity.this, PDFWebActivity.class);
                        }

                        intent.putExtra("url", pdfModel.getPdfUrl());
                        intent.putExtra("title", pdfModel.getTitle());
                        intent.putExtra("name", pdfModel.getName());
                        intent.putExtra("teleNum", pdfModel.getTeleNum());
                        intent.putExtra("pdfShareUrl", pdfModel.getPdfShareUrl());
                        intent.putExtra("shareImg", pdfModel.getShareImg());
                        intent.putExtra("isShare", pdfModel.getIsShare());

                        startActivity(intent);


                        Log.d("666666", data);
                    }

                });

        mWebView.registerHandler("statusBlack", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {

                        //白的

                        ImmersionBar.with(ShopActivity.this)

                                .statusBarDarkFont(true, 1f)
                                .init();


                    }
                });

        mWebView.registerHandler("statusWhite", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {


                        //黑的
                        int a = 0;
                        if (Rom.check(Rom.ROM_MIUI)) {
                            a = 1;
                        } else if (Rom.check(Rom.ROM_FLYME)) {
                            a = 2;
                        } else {
                            a = 3;
                        }
//                StatusBarUtil.StatusBarDarkMode(MainActivity.this, a);


                        ImmersionBar.with(ShopActivity.this)

                                .statusBarDarkFont(false, 1f)
                                .init();
                    }
                });

        mWebView.registerHandler("idCard", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        selectImage(REQUEST_IDIMAGE);
                        nCurrentCartType = Integer.parseInt(data);
                        idCardFunction = function;
                    }
                });
        mWebView.registerHandler("bankCard", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        selectImage(REQUEST_BANKIMAGE);
                        nCurrentCartType = Integer.parseInt(data);
                        bankCardFunction = function;
                    }
                });
      /*  mWebView.registerHandler("driverCard", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        selectImage(REQUEST_DRIVEIMAGE);
                        nCurrentCartType = Integer.parseInt(data);
                        driveCardFunction = function;
                    }
                });*/
        mWebView.registerHandler("photo", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
//                selectImage(REQUEST_PHOTO);
//                PhotoPickActivity.REQUEST_PHOTO_CROP
                        selectImage(REQUEST_PHOTO);
                        photoFunction = function;
                    }
                });
//        toLogin

        mWebView.registerHandler("toLogin", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        ToastUtil.showCenterToast(ShopActivity.this, "toLogin");
                        MySharePreferenceUtil.put(ShopActivity.this, "newintentflag", "2");
                        MySharePreferenceUtil.put(ShopActivity.this, "token", "");
                        MySharePreferenceUtil.put(ShopActivity.this, "token1", "");
                        startActivity(new Intent(ShopActivity.this, LoginActivity.class));

                    }
                });

        mWebView.registerHandler("saveHeadImageUrl", new

                BridgeHandler() {

                    @Override
                    public void handler(String data, CallBackFunction function) {
                        Log.d("33333333333333", data);
                        Gson gson = new Gson();
                        FaceModel faceModel = new FaceModel();
                        faceModel = gson.fromJson(data, FaceModel.class);
                        String string = faceModel.getData();
                        MySharePreferenceUtil.put(ShopActivity.this, "appHeadUrl", string);


                    }

                });


      /*  mWebView.registerHandler("location", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        locationFunction = function;
                        startLocation(function);
                    }
                });*/
        mWebView.registerHandler("settings", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                });
        mWebView.registerHandler("activity", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        try {
                            startActivity(new Intent(ShopActivity.this, Class.forName(data)));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
        //模拟用户信息 获取本地位置，用户名返回给html
        User user = new User();
        user.setLocation("上海");
        user.setName("Bruce");
        // 回调 "functionInJs"
        mWebView.callHandler("toast", new

                Gson().

                toJson(user), new

                CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {

                        Toast.makeText(ShopActivity.this, data, Toast.LENGTH_SHORT).show();

                    }
                });
        mWebView.send("hello");
        mWebView.registerHandler("camera", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {


                        cameraFunction = function;
                        if (Build.VERSION.SDK_INT >= 24) {  // 或者 android.os.Build.VERSION_CODES.KITKAT这个常量的值是19
                            onPermissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
                                @Override
                                public void onClick(boolean bln) {
                                    if (bln) {
                                        Log.d("MainActivity", "进入权限");
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        File photoFile = createImagePathFile(ShopActivity.this);
                                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

//                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

//                                com.xmrxcaifu.jsbridge.fileprovider
                                        imageUriFromCamera = FileProvider.getUriForFile(ShopActivity.this, "com.xmrxcaifu.jsbridge.fileprovider", photoFile);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);

                                        startActivityForResult(intent, GET_IMAGE_BY_CAMERA_U);
                                    } else {
                                        Toast.makeText(ShopActivity.this, "扫码拍照或无法正常使用", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {


                            imageUriFromCamera = createImagePathUri(ShopActivity.this);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);
                            startActivityForResult(intent, GET_IMAGE_BY_CAMERA_U);
                        }
                    }
                });


        /**
         * 获取手势密码信息
         * 、success 为1已设置0未设置
         *
         * // 获取手势密码信息 getGestInfo
         //                success 为1已设置0未设置
         */

        mWebView.registerHandler("getGestInfo", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        // TODO: 2018/3/27  参数值待修改

                        Log.d(data, "3333333333333");
//手势密码信息入口
                        String gesture = (String) MySharePreferenceUtil.get(ShopActivity.this, myphone + "flaggesture", "");

                        JSONObject jsonObject = new JSONObject();

                        try {
                            jsonObject.put("success", gesture);
                        } catch (Exception e) {

                        }

                        function.onCallBack(jsonObject.toString());

                        Log.d(jsonObject.toString(), "3333333333333");


                    }
                });


//        // 获取手势密码信息 getGestInfo
//        success 为1已设置0未设置
//// 关闭手势密码 closeGestVC
//        执行该方法关闭手势登录
//// 打开手势密码 openGestVC
//        opentype 为create 创建手势密码
//        opentype 为modify 修改手势密码
//
        /**
         * 关闭手势密码
         *
         */

        mWebView.registerHandler("closeGestVC", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {

                        String gesture = (String) MySharePreferenceUtil.get(ShopActivity.this, myphone + "flaggesture", "");
                        JSONObject jsonObject = new JSONObject();
                        try {

                            if (gesture.equals("1")) {
                                jsonObject.put("success", gesture);
                                MySharePreferenceUtil.put(ShopActivity.this, myphone + "flaggesture", "0");
                            } else {
                                jsonObject.put("success", "0");
                                MySharePreferenceUtil.put(ShopActivity.this, myphone + "flaggesture", "0");
                            }


                        } catch (Exception e) {

                        }
                        function.onCallBack(jsonObject.toString());


                    }
                });
        /**
         *  打开手势密码
         */
        mWebView.registerHandler("openGestVC", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
//                "finish"

                        Log.d(data, "3333333333333");
//
                        openGestVCFuction = function;
                        Gson gson = new Gson();
                        BeanGesture beanGesture = new BeanGesture();
                        beanGesture = gson.fromJson(data, BeanGesture.class);
                        String str1 = beanGesture.getOpentype();

                        if (str1.equals("oncreate")) {

                            Intent intent = new Intent(ShopActivity.this, GestureMotifyActivity.class);

                            intent.putExtra("str1", str1);
                            startActivity(intent);

//                    startActivity(new Intent(MainActivity.this, GestureMotifyActivity.class));


                        } else if (str1.equals("modify")) {
//                    startActivity(new Intent(MainActivity.this, GestureVerify_checkActivity.class));

                            Intent intent = new Intent(ShopActivity.this, GestureVerify_checkActivity.class);

                            intent.putExtra("str1", str1);
                            startActivity(intent);

                        }


                        Log.d(data, "3333333333333");
                        Log.d(data, "3333333333333");
                    }
                });


        mWebView.registerHandler("toExternalLink", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
//                Log.d(data.toString(), "3333333");
                Gson gson = new Gson();
                Wailian wailian = new Wailian();
                wailian = gson.fromJson(data.toString(), Wailian.class);
//                Intent intent = new Intent(MainActivity.this, MainActivity5.class);
                Intent intent = new Intent(ShopActivity.this, MainActivity6.class);
                intent.putExtra("url", wailian.getExternalLink());
                intent.putExtra("title", wailian.getExternalTitle());
                startActivity(intent);
            }

        });
    }

    public void stopLocation() {
        mLocationManager.removeUpdates(ShopActivity.this);
    }

    public String getdeviceId() {
        try {
            MySharePreferenceUtil.put(ShopActivity.this, "deviceid", tm.getDeviceId());
//            Toast.makeText(MainActivity.this,  tm.getDeviceId()+"", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            MySharePreferenceUtil.put(ShopActivity.this, "deviceid", "111111111");
        }
//        return tm.getDeviceId();
        String string = (String) MySharePreferenceUtil.get(ShopActivity.this, "deviceid", "");
        return string;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "onresuem", Toast.LENGTH_SHORT).show();
        if (!network()) {
//            startActivity(new Intent(MainActivity.this, NoNetActivity.class));
        }
    }

    public void startLocation(CallBackFunction callBackFunction) {

        // 创建定位请求
        TencentLocationRequest request = TencentLocationRequest.create();

        // 修改定位请求参数, 定位周期 3000 ms

        mLocationManager.requestLocationUpdates(TencentLocationRequest.create().setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_NAME).setInterval(500).setAllowDirection(true), this);
    }


    public Uri createImagePathUri(Activity activity) {
        //文件目录可以根据自己的需要自行定义
        Uri imageFilePath;
        File file = new File(activity.getExternalCacheDir(), USER_IMAGE_NAME);
        imageFilePath = Uri.fromFile(file);
        return imageFilePath;
    }

    public File createImagePathFile(Activity activity) {
        //文件目录可以根据自己的需要自行定义
        Uri imageFilePath;
        File file = new File(activity.getExternalCacheDir(), USER_IMAGE_NAME);
        imageFilePath = Uri.fromFile(file);
        return file;
    }

    public File createImagePathFile1(Activity activity) {
        //文件目录可以根据自己的需要自行定义
        Uri imageFilePath;
        File file = new File(activity.getExternalCacheDir(), USER_IMAGE_NAME);
        imageFilePath = Uri.fromFile(file);
        return file;
    }

    private Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_PHOTO) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表   这是相册的
                String path1;

                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
                if (path != null && path.size() > 0) {
                    p = path.get(0);
//                    onSelected();
                    path1 = p;
                    bitmap = getImage(p);
//                    imageView.setImageBitmap(bitmap);
                    String a = BitmapUtils.bitmapToBase64(BitmapUtils.compressImage(bitmap));
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("img", "data:image/png;base64," + a);
//                        photoFunction.onCallBack(jsonObject.toString());
                    } catch (JSONException e) {
                        //TODO
                    }

                    File file = new File(path1);
//                    imageUriFromPhoto=FileProvider.getUriForFile(MainActivity.this, "com.lyk.jsbridge.fileprovider", file);
                    imageUriFromPhoto = Uri.fromFile(file);

                    cropImage(imageUriFromPhoto, 1, 1, CROP_IMAGE_U2);


                }
            }
        }
        if (resultCode != this.RESULT_CANCELED) {
            switch (requestCode) {
                case GET_IMAGE_BY_CAMERA_U:
                    /*
                     * 这里我做了一下调用系统切图，高版本也有需要注意的地方
                     * */
                    if (imageUriFromCamera != null) {
                        cropImage(imageUriFromCamera, 1, 1, CROP_IMAGE_U);
                        break;
                    }
                    break;
                case CROP_IMAGE_U:
                    final String s = getExternalCacheDir() + "/" + USER_CROP_IMAGE_NAME;

                    Bitmap imageBitmap = GetBitmap(s, 320, 320);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
                    String a = BitmapUtils.bitmapToBase64(BitmapUtils.compressImage(imageBitmap));
                    if (!TextUtils.isEmpty(a)) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("img", "data:image/png;base64," + a);
                            cameraFunction.onCallBack(jsonObject.toString());
                        } catch (JSONException e) {
                            //TODO
                        }
                    }


                    break;
                case CROP_IMAGE_U2:
//

                    final String s1 = getExternalCacheDir() + "/" + USER_CROP_IMAGE_NAME;

                    Bitmap imageBitmap2 = GetBitmap(s1, 320, 320);
                    ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                    imageBitmap2.compress(Bitmap.CompressFormat.PNG, 70, baos1);
                    String a1 = BitmapUtils.bitmapToBase64(BitmapUtils.compressImage(imageBitmap2));
                    if (!TextUtils.isEmpty(a1)) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("img", "data:image/png;base64," + a1);
                            photoFunction.onCallBack(jsonObject.toString());
                        } catch (JSONException e) {
                            //TODO
                        }
                    }
                default:
                    break;
            }


        }


        if (requestCode == PhotoPickActivity.REQUEST_PHOTO_CROP && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String string = bundle.getString(PhotoPickActivity.EXTRA_RESULT_CROP_PHOTO);
            String path = ImageInfo.pathAddPreFix(string);
            String temPath = path.substring(path.indexOf("file://") + 7);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(temPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            String b = BitmapUtils.bitmapToBase64(BitmapUtils.compressImage(bitmap));
            if (!TextUtils.isEmpty(b)) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("img", "data:image/png;base64," + b);
                    photoFunction.onCallBack(jsonObject.toString());
                } catch (JSONException e) {
                    //TODO
                }
            }


        }

        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap GetBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    public void cropImage(Uri imageUri, int aspectX, int aspectY, int return_flag) {
        File file = new File(this.getExternalCacheDir(), USER_CROP_IMAGE_NAME);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= 24) {
            //高版本一定要加上这两句话，做一下临时的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            FileProvider.getUriForFile(MainActivity.this, "com.xuezj.fileproviderdemo.fileprovider", file);
//            com.xmrxcaifu.jsbridge.fileprovider
//            FileProvider.getUriForFile(MainActivity.this, "com.lyk.jsbridge.fileprovider", file);
            FileProvider.getUriForFile(ShopActivity.this, "com.xmrxcaifu.jsbridge.fileprovider", file);
        }
        cropImageUri = Uri.fromFile(file);

        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
  /*      intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);*/
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 640);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);

        startActivityForResult(intent, return_flag);
    }


    public void cropImage1(Uri imageUri, int aspectX, int aspectY, int return_flag) {
        File file = new File(this.getExternalCacheDir(), USER_CROP_IMAGE_NAME);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= 24) {
            //高版本一定要加上这两句话，做一下临时的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            FileProvider.getUriForFile(MainActivity.this, "com.xuezj.fileproviderdemo.fileprovider", file);

            FileProvider.getUriForFile(ShopActivity.this, "com.lyk.jsbridge.fileprovider", file);
        }
        cropImageUri = Uri.fromFile(file);

        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
/*        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);*/
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 640);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);

        startActivityForResult(intent, return_flag);
    }

    private void requestContactPermission(String permission, CallBackFunction function) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            //申请 WRITE_CONTACTS 权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            initdata();

        }
    }

    public void initdata() {
        ContactInfoService mContactInfoService = new ContactInfoService(ShopActivity.this);
        mContactBeanList = mContactInfoService.getContactList();//返回手机联系人对象集合
        mWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                contactFunction.onCallBack(ContactInfoService.changeArrayDateToJson(mContactBeanList));
            }
        }, 1000);
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("location", tencentLocation.getAddress());
            jsonObject.put("Latitude", tencentLocation.getLatitude());
            jsonObject.put("Longitude", tencentLocation.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        locationFunction.onCallBack(jsonObject.toString());

    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    /**
     * 自定义的WebViewClient
     */
    class MyWebViewClient extends BridgeWebViewClient {

        public MyWebViewClient(BridgeWebView webView) {
            super(webView);
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            loadError = false;
            errmyCode = 0;
            super.onPageStarted(view, url, favicon);
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            // 这个方法在6.0才出现
            // 这个方法在6.0才出现


            if (url.contains("xmrxcaifu")) {
                int statusCode = errorResponse.getStatusCode();

                System.out.println("onReceivedHttpError code = " + statusCode);
                Log.d("333333333", url);
                loadError = true;
                //出现404页面
//            || 500 == statusCode

            }


        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            Log.d(error.toString(), "66666");

            handler.proceed();
        }

        @Override
        public void onPageFinished(WebView view, String url1) {
//
            Log.d(url1, "333333333333333333");
            Log.d(url1, "333333333333333333");
            if (!network()) {
                loadError = true;
//                ToastUtil.showCenterToast(MainActivity.this, "网络不可用");
            }


            if (url1.equals("about:blank")) {
                url = Constant.URL;
            } else {
                url = url1;
            }

            if (mynetworkflag == 1) {
//                ToastUtil.showCenterToast(MainActivity.this,"到这里了么");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myNewLoadingDialog6.dismiss();
                        mWebView.setVisibility(View.VISIBLE);

                    }
                }, 3000);
            } else {

                if (errmyCode == -2) {
                    layout_network.setVisibility(View.VISIBLE);
                    mWebView.setVisibility(View.GONE);
                } else if (errmyCode == -8) {
                    layout_network.setVisibility(View.VISIBLE);
                    mWebView.setVisibility(View.GONE);


                } else {

                    if (errmyCode != -2 && myflag == 1) {
                        layout_network.setVisibility(View.GONE);
                        mWebView.setVisibility(View.VISIBLE);
                        errmyCode = 0;
                    } else if (errmyCode != -8 && myflag == 1) {
                        layout_network.setVisibility(View.GONE);
                        mWebView.setVisibility(View.VISIBLE);
                        errmyCode = 0;
                    } else {
                        mWebView.setVisibility(View.VISIBLE);
                    }
                }
            }


            MySharePreferenceUtil.put(ShopActivity.this, "flagloading", "1");

            if (myIntentFlag.equals("10")) {

                String updateUrl = Constant.URL + "/api/v1/appVersion/getCurrentVersion/1";

                MyOkhttp.get(updateUrl)
                        .tag(this)
                        .execute(new StringNoDialogCallback() {
                            @Override
                            public void onBefore(BaseRequest request) {
                                super.onBefore(request);

                            }

                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
//                        Log.d(s, "eeeeeeeeeeeeeeeeeeeee");
                                UpDateBean upDateBean = new UpDateBean();
                                Gson gson = new Gson();
                                upDateBean = gson.fromJson(s, UpDateBean.class);
                                int intVer = Integer.parseInt(upDateBean.getData().getVersion().replace(".", ""));
                                int appVer = Integer.parseInt(getAppVersion().replace(".", ""));
                                if (appVer < intVer) {
                                    showUpVersionDialog(upDateBean.getData().getDescription(), upDateBean.getData().getAppUrl(), upDateBean.getData().getForceUpdate(), upDateBean.getData().getVersion());
                                } else {
                                }
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                Log.d(response.toString(), "333333333333");
//                        showUpVersionDialog("1、 全新UI风格设计，交互和体验全面升级\n2、 投资流程优化，操作更便捷\n3、 账户管理全新设计，管理资产一目了然\n4、 其他功能细节优化", url, true, "1.10");
//                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onAfter(String s, Exception e) {
                                super.onAfter(s, e);

                            }
                        });
            }
            myIntentFlag = "1";

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWebView.setVisibility(View.VISIBLE);
                }
            }, 1500);
            layout_network.setVisibility(View.GONE);

            super.onPageFinished(view, url);

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url1) {
            Log.d(url1, "555555555555555555555");

            if (url1.contains("isBackToApp=true")) {
                finish();
                mWebView.destroy();
            }

       /*   if (url1.contains("checkLoginPage")) {
                String shopUrl2 = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl2", "");
                mWebView.loadUrl(shopUrl2);
            }*/
//            return super.shouldOverrideUrlLoading(view, url);

            return false;
        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String
                failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.i(TAG, "onReceivedError: 失败了" + errorCode);
            Log.d("555555555", errorCode + "");

            errmyCode = errorCode;
            if (!network()) {
                layout_network.setVisibility(View.VISIBLE);
                ImmersionBar.with(ShopActivity.this)

                        .statusBarDarkFont(true, 1f)
                        .init();
                mWebView.setVisibility(View.GONE);

                img_network.setImageResource(R.drawable.network);
                text_network.setText("数据加载失败,请检查一下你的网络");
                text_shijian.setText("点击重试");
            } else {
                if (errorCode == -2) {
                    layout_network.setVisibility(View.VISIBLE);
                    ImmersionBar.with(ShopActivity.this)
                            .statusBarDarkFont(true, 1f)
                            .init();
                    img_network.setImageResource(R.drawable.silingsi);
                    text_network.setText("没有找到您要访问的页面");
                    text_shijian.setText("返回上一页");
                    mWebView.setVisibility(View.GONE);
                } else if (errorCode == -8) {
                    layout_network.setVisibility(View.VISIBLE);
                    ImmersionBar.with(ShopActivity.this)

                            .statusBarDarkFont(true, 1f)
                            .init();
                    mWebView.setVisibility(View.GONE);
                    img_network.setImageResource(R.drawable.network);
                    text_network.setText("数据加载失败,请检查一下你的网络");
                    text_shijian.setText("点击重试");
                } else {
                    ImmersionBar.with(ShopActivity.this)
                            .statusBarDarkFont(true, 1f)
                            .init();
                    layout_network.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);

                }

            }

        }
    }


    /**
     * 自定义回调
     */
    class myHadlerCallBack extends DefaultHandler {

        @Override
        public void handler(String data, CallBackFunction function) {
            if (function != null) {

                function.onCallBack("沈少");
            }
        }

    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//

//        Toast.makeText(this, "onrestart", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        Toast.makeText(this, "onstart", Toast.LENGTH_SHORT).show();
//        mWebView.clearCache(true);
//        mWebView.loadData("", "text/html", "UTF-8");

    }

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<ShopActivity> mActivity;

        private CustomShareListener(ShopActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                if (t != null) {
                    com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

        }
    }


    private Dialog dialog;


    private CustomDialog_view_new.Builder builder;

    private void showUpVersionDialog(String content, final String url,
                                     final int forceUpdate, String version) {
        dialog = new Dialog(ShopActivity.this);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
                R.color.float_transparent)));
//        window.setGravity(Gravity.CENTER);
        window.setGravity(Gravity.TOP);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setContentView(R.layout.item_splash_upversion_dialog);
        dialog.show();
        TextView tv_upversion_dialog_content = (TextView) window
                .findViewById(R.id.tv_upversion_dialog_content);
        TextView tv_version = (TextView) window
                .findViewById(R.id.tv_version);
        ImageView tv_upversion_dialog_clean = (ImageView) window
                .findViewById(R.id.tv_upversion_dialog_clean);
        TextView tv_upversion_dialog_up = (TextView) window
                .findViewById(R.id.tv_upversion_dialog_up);
        tv_upversion_dialog_content.setText(content);
//        tv_version.setText("v" + this.version);
        tv_version.setText(version);


        tv_upversion_dialog_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    UpdateManager manager = new UpdateManager(MainActivity.this,
                        url);
                manager.showDownloadDialog();*/

                if (forceUpdate == 1) {
                    finish();
                } else {
                    dialog.dismiss();
                }


            }
        });

        tv_upversion_dialog_up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (dialog != null) {
                    dialog.cancel();
//					initData2();
                }
                UpdateManager manager = new UpdateManager(ShopActivity.this,
                        url);
                manager.showDownloadDialog();


            }
        });
//
    }


    private boolean isAppInstalled(String uri) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }


    String myLoadurl;
    CustomDialog.Builder customDialog;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(ShopActivity.this, ShopActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            addMessageToIntent(intent, (UMessage) msg.obj);


            Log.d("TEST", "uMessage：" + msg.obj.toString());
//            startActivity(intent);
//            finish();
            mWebView.loadUrl(myLoadurl);

        }
    };

    /**
     * 用于将UMessage中自定义参数的值放到intent中传到SplashActivity中，SplashActivity中对友盟推送时自定义消息作了专门处理
     *
     * @param intent 需要增加值得intent
     * @param msg    需要增加到intent中的msg
     */
    private void addMessageToIntent(Intent intent, UMessage msg) {

        if (intent == null || msg == null || msg.extra == null) {
            return;
        }

        for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null) {
                intent.putExtra(key, value);
                intent.putExtra("url", msg.extra.get("linkUrl"));
                mWebView.loadUrl(msg.extra.get("linkUrl"));
                myLoadurl = msg.extra.get("linkUrl");
            }
        }

    }

    @Override
    public void onMessage(Intent intent) {

        try {
            super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
            String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
            UmLog.i(TAG, body);
            Log.d("TEST", "body：" + body);
            UMessage uMessage = new Gson().fromJson(body, UMessage.class);
            Message message = Message.obtain();
            message.obj = uMessage;
            handler.sendMessage(message);
        } catch (Exception e) {

        }


    }


    private FingerprintCore mFingerprintCore;
    private KeyguardLockScreenManager mKeyguardLockScreenManager;

    public void onEventMainThread(Integer type) {
        Log.d("eventbus", type + "");

        myphone = (String) MySharePreferenceUtil.get(ShopActivity.this, "phone1", "");
        token = (String) MySharePreferenceUtil.get(ShopActivity.this, "token", "");
        flag = type;
        if (type == 222) {

//            ToastUtil.showCenterToast(ShopActivity.this, "3434");
            token = (String) MySharePreferenceUtil.get(ShopActivity.this, "token", "");
            mWebView.loadUrl("javascript:localStorage.setItem('" + "token" + "','" + token + "');");
            if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(ShopActivity.this, "idCard", ""))) {
                //未实名
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(ShopActivity.this, "phone", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(ShopActivity.this, "phone", "") + "');");
            } else {
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(ShopActivity.this, "username", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(ShopActivity.this, "phone", "") + "');");
            }
            mWebView.loadUrl("javascript:updateData()");

            String updateUrl = Constant.URL + "api/v1/shoppingMall/getMallToken";
            MyOkhttp.get(updateUrl)
                    .tag(this)
                    .headers("token", token)
                    .execute(new StringNoDialogCallback() {
                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.d(s, "5656565656");
//                                        ShopTokenModel shopTokenModel  = new Gson().fromJson(s, ShopTokenModel.class);
                            Gson gson1 = new Gson();
                            ShopTokenModel shopTokenModel = new ShopTokenModel();
                            shopTokenModel = gson1.fromJson(s, ShopTokenModel.class);
//                                    Log.d("34343434",finalShopModel.getData() + "?token=" + shopTokenModel.getData().getToken());
//                            ToastUtil.showCenterToast(ShopActivity2.this, "成功");
                            MySharePreferenceUtil.put(ShopActivity.this, "shopToken", shopTokenModel.getData().getToken());
                            String myurl = (String) MySharePreferenceUtil.get(ShopActivity.this, "shopUrl", "");

                            Log.d(myurl, "767676767");
                            String shopToken = (String) MySharePreferenceUtil.get(ShopActivity.this, "shopToken", "");
                            Log.d(shopToken, "alskdf234234324");
                            if (myurl.contains("?")) {
                                url = myurl + "&token=" + shopToken;
                                mWebView.loadUrl(myurl + "&token=" + shopToken);
                                String myurl2 = (String) MySharePreferenceUtil.get(ShopActivity.this, "shopUrl2", "");
//            https://t2wealth.xmrxcaifu.com/#/checkLoginPage?redirectUrl=https%3A%2F%2Fxindou.xmrxcaifu
//                                mWebView.loadUrl(myurl2);


                                Log.d("3223323", myurl + "&token=" + shopToken);
                            } else {
//                                mWebView.loadUrl(myurl + "?token=" + shopToken);
                                url = myurl + "&token=" + shopToken;
                                mWebView.loadUrl(myurl + "?token=" + shopToken);
                                String myurl2 = (String) MySharePreferenceUtil.get(ShopActivity.this, "shopUrl2", "");
//            https://t2wealth.xmrxcaifu.com/#/checkLoginPage?redirectUrl=https%3A%2F%2Fxindou.xmrxcaifu
//                                mWebView.loadUrl(myurl2);
                                Log.d("3223323", myurl + "&token=" + shopToken);
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);

                        }

                        @Override
                        public void onAfter(String s, Exception e) {
                            super.onAfter(s, e);

                        }
                    });


        }
        if (type == 349) {

            String myurl = (String) MySharePreferenceUtil.get(ShopActivity.this, "shopUrl", "");
//            https://t2wealth.xmrxcaifu.com/#/checkLoginPage?redirectUrl=https%3A%2F%2Fxindou.xmrxcaifu
            mWebView.loadUrl(myurl);
         /*   if (myCheckUrl.contains("checkLoginPage")) {
                String shopUrl2 = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl2", "");
                mWebView.loadUrl(shopUrl2);
            } else {
                mWebView.loadUrl(myurl);
            }*/
            /* if (myurl.contains("checkLoginPage")) {
                mWebView.loadUrl(url);
            } else {
                if (myurl.contains("checkLoginPage")) {
                    mWebView.loadUrl(myurl);
                } else {
                    mWebView.loadUrl(myurl);
                }
            }*/

        } else if (type == 348) {
            String myurl = (String) MySharePreferenceUtil.get(ShopActivity.this, "shopUrl2", "");
//            https://t2wealth.xmrxcaifu.com/#/checkLoginPage?redirectUrl=https%3A%2F%2Fxindou.xmrxcaifu
            mWebView.loadUrl(myurl);
        }

    }

}
