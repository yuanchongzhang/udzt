package com.xmrxcaifu;

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
import android.graphics.drawable.BitmapDrawable;
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
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeUtil;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.QbSdk;
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
import com.xmrxcaifu.activity.GestureMotifyActivity;
import com.xmrxcaifu.activity.GestureVerifyActivity;
import com.xmrxcaifu.activity.GestureVerify_checkActivity;
import com.xmrxcaifu.activity.HongBaoWebActivity;
import com.xmrxcaifu.activity.LoginActivity;
import com.xmrxcaifu.activity.MainActivity3;
import com.xmrxcaifu.activity.MainActivity5;
import com.xmrxcaifu.activity.MainActivity6;
import com.xmrxcaifu.activity.PDFWebActivity;
import com.xmrxcaifu.activity.ShopActivity2;
import com.xmrxcaifu.activity.SplashActivity;
import com.xmrxcaifu.activity.V6_Finger_Check_NewActivity;
import com.xmrxcaifu.common.LoadingDialog;
import com.xmrxcaifu.core.FingerprintCore;
import com.xmrxcaifu.dialog.CustomDialog;
import com.xmrxcaifu.dialog.CustomDialog_view_new;
import com.xmrxcaifu.http.MyOkhttp;
import com.xmrxcaifu.http.callback.StringNoDialogCallback;
import com.xmrxcaifu.http.request.BaseRequest;
import com.xmrxcaifu.modle.BeanGesture;
import com.xmrxcaifu.modle.ClipboardBean;
import com.xmrxcaifu.modle.DianhuaModel;
import com.xmrxcaifu.modle.EventBean;
import com.xmrxcaifu.modle.FaceModel;
import com.xmrxcaifu.modle.PdfModel;
import com.xmrxcaifu.modle.ShareModel;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivty implements TencentLocationListener {

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
    private CallBackFunction contactFunction, cameraFunction,
            photoFunction, locationFunction, idCardFunction,
            driveCardFunction, bankCardFunction, openGestVCFuction, FigerVC;
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
    public static MainActivity instance = null;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME)) {
                newmyflag = 1;
                //ToastUtil.showCenterToast(MainActivity.this,"到这里了么");
                myNewLoadingDialog2 = new MyNewLoadingDialog2(MainActivity.this);
                myNewLoadingBuilder2 = new MyNewLoadingDialog2.Builder(MainActivity.this);
                myNewLoadingDialog2 = myNewLoadingBuilder.create();
                token = (String) MySharePreferenceUtil.get(MainActivity.this, "token", "");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "token" + "','" + token + "');");
                if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(
                        MainActivity.this, "idCard", ""))) {
                    //未实名
                    mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
                    mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
                } else {
                    mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "username", "") + "');");
                    mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
                }
//                mWebView.loadUrl("javascript:updateData()");

                myIntentFlag = "1";

                String advtisement = (String) MySharePreferenceUtil.get(MainActivity.this, "advtisement", "");

                if (advtisement.equals("0")) {
                    mWebView.loadUrl(Constant.URL);
                } else if (advtisement.equals("1")) {
                    mWebView.loadUrl(getIntent().getStringExtra("url"));
                } else {
                    mWebView.loadUrl(Constant.URL);
                }


            }
            myphone = (String) MySharePreferenceUtil.get(MainActivity.this, "phone1", "");
        }

    };
    private BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME1)) {
                android.os.Process.killProcess(android.os.Process.myPid());
                ToastUtil.showCenterToast(MainActivity.this, "这里了");

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
    /**
     * 监听网页进度
     */
    WebChromeClient chromeClient = new WebChromeClient() {
        private View mCustomView;
        private IX5WebChromeClient.CustomViewCallback mCustomViewCallback;

        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
        }

        @Override
        public void onProgressChanged(WebView webView, int i) {
            Log.e("进度", i + "");
//            Toast.makeText(MainActivity.this, newmyflag+"", Toast.LENGTH_SHORT).show();
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.imganmi);
            LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
//            animation.setInterpolator(lin);
            if (newmyflag == 1) {
                animation.setDuration(500);
                imageView_loading.startAnimation(animation);
                if (i == 100) {
                    try {
                      /*  myNewLoadingDialog.cancel();
                        myNewLoadingDialog.dismiss();*/
                        if (myNewLoadingDialog != null && myNewLoadingDialog.isShowing()) {
                            myNewLoadingDialog.dismiss();
                        }
                        newmyflag = 3;
                    } catch (Exception e) {
                        newmyflag = 3;
//                        myNewLoadingDialog.dismiss();
                        mWebView.loadUrl(Constant.URL);
                    }
                    mWebView.setVisibility(View.VISIBLE);
                    layout_network.setVisibility(View.GONE);
                } else {
                    try {
                        myNewLoadingDialog.show();
                    } catch (Exception e) {

                    }

                }
            } else if (newmyflag == 2) {

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
    String newToken;
    private HomekeyReceiver receiver;
    private static final String TIME_SET = "android.intent.action.TIME_SET";
    private static final String SYSTEM_REASON = "reason";
    private static final String SYSTEM_HOME_KEY = "homekey";

    private long start;
    private long end;
    private String flaggesture;
    private String zhiwendenglu;
    /**
     * 监控是否按了HOME键
     */
    class HomekeyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason != null) {
                    if (reason.equals(SYSTEM_HOME_KEY)) {
                        Log.e("ggg", "我是HOEM");
                        start = System.currentTimeMillis();
                        //如果按了HOME那么就是0，否则就没按
                        MySharePreferenceUtil.put(
                                MainActivity.this, "isHome", "0");
                        Log.e("ggg", start + "按了返回键时候的时间");
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setImmersionStatus();

        receiver = new HomekeyReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

        img_network = (ImageView) findViewById(R.id.img_network);
        text_network = (TextView) findViewById(R.id.text_network);
        phone = (String) MySharePreferenceUtil.get(this, "phone1", "");
        String wailianfou = (String) MySharePreferenceUtil.get(MainActivity.this, "wailianfou", "");
         flaggesture = (String) MySharePreferenceUtil.get(MainActivity.this, phone + "flaggesture", "");
         zhiwendenglu = (String) MySharePreferenceUtil.get(MainActivity.this, phone + "zhiwendenglu", "");
//        ToastUtil.showCenterToast(this, "wailianfou" + wailianfou + "flaggesture" + flaggesture + "zhiwendenglu" + zhiwendenglu);
        Log.d("dadfkasdf", "wailianfou" + wailianfou + "flaggesture" + flaggesture + "zhiwendenglu" + zhiwendenglu);
        HashMap map = new HashMap();
// 配置不用多进程策略，即该方案仅在Android 5.1+系统上生效。
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, false);
        QbSdk.initTbsSettings(map);
        if (wailianfou.equals("1") && flaggesture.equals("0") && zhiwendenglu.equals("0")) {
            Log.e("ggg", "我是外连接");
            Intent intent = new Intent(MainActivity.this, MainActivity6.class);
            intent.putExtra("url", (String) MySharePreferenceUtil.get(MainActivity.this, "getLinkurl", ""));
            startActivity(intent);
            MySharePreferenceUtil.put(MainActivity.this, "newintentflag", "2");
            MySharePreferenceUtil.put(MainActivity.this, "wailianfou", "0");
        } else if (wailianfou.equals("1") && flaggesture.equals("") && zhiwendenglu.equals("")) {
            Log.e("ggg", "我是外连接");
            Intent intent = new Intent(MainActivity.this, MainActivity6.class);
            intent.putExtra("url", (String) MySharePreferenceUtil.get(MainActivity.this, "getLinkurl", ""));
            startActivity(intent);
            MySharePreferenceUtil.put(MainActivity.this, "newintentflag", "2");
            MySharePreferenceUtil.put(MainActivity.this, "wailianfou", "0");
        }
        registerBoradcastReceiver();//注册广播
        tm = (TelephonyManager) getApplication().getSystemService(TELEPHONY_SERVICE);
        mFrameLayout = (FrameLayout) findViewById(R.id.mFrameLayout);
        serviceIntent = new Intent(MainActivity.this, MyService.class);
        startService(serviceIntent);
        Log.d("devicetoken", getDeviceToken());
        mLocationManager = TencentLocationManager.getInstance(this);
        EventBus.getDefault().register(this);
        myphone = (String) MySharePreferenceUtil.get(MainActivity.this, "phone1", "");
        ImmersionBar.with(MainActivity.this)
                .statusBarDarkFont(false, 1f)
                .init();
        initFingerprintCore();
        layout_network = (LinearLayout) findViewById(R.id.layout_network);
        immersionBar = ImmersionBar.with(this);
        title_view_new = findViewById(R.id.title_view_new);
        text_Devicetoken = (NewTextView) findViewById(R.id.text_Devicetoken);
        text_Devicetoken.setText(getDeviceToken());
        newToken = (String) MySharePreferenceUtil.get(MainActivity.this, "token1", "");

        text_Devicetoken.setTextIsSelectable(true);
        ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
//        cmb.setText(content.trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        cmb.getText();

        try {
            if (getIntent().getStringExtra("url").equals(Constant.URL)) {
                title_view_new.setVisibility(View.GONE);
            } else {
                ImmersionBar.with(MainActivity.this)
                        .statusBarDarkFont(false, 1f)
                        .init();
                title_view_new.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }


        title_view_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mynetworkflag == 1) {
                } else {
                    newmyflag = 1;
                    mWebView.loadUrl(Constant.URL);
                    title_view_new.setVisibility(View.GONE);
                }
            }
        });
        newmyflag = 1;
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        mWebView = (BridgeWebView) findViewById(R.id.webView);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        token = (String) MySharePreferenceUtil.get(MainActivity.this, "token", "");
        Log.d("7827838238", token);

        String flagloading = (String) MySharePreferenceUtil.get(MainActivity.this, "flagloading", "");

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
//        mWebView.flushMessageQueue();
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
        //更新请求接口
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
//                                showUpVersionDialog("1、新增鑫积分体系\n2、新增任务中心\n3、新增鑫享会商城\n4、鑫享会全新改版\n5、会员中心全新改版", upDateBean.getData().getAppUrl(), upDateBean.getData().getForceUpdate(), upDateBean.getData().getVersion());
//                                showUpVersionDialog("1、新增鑫积分体系\n2、新增任务中心\n3、新增鑫享会商城", upDateBean.getData().getAppUrl(), upDateBean.getData().getForceUpdate(), upDateBean.getData().getVersion());
                                showUpVersionDialog(upDateBean.getData().getDescription(), upDateBean.getData().getAppUrl(), upDateBean.getData().getForceUpdate(), upDateBean.getData().getVersion());
//
//  showPopupWindow(mWebView);
//                                ToastUtil.showToast(MainActivity.this, upDateBean.getData().getForceUpdate() + "");
                            } else {
//                                showUpVersionDialog("1、新增鑫积分体系\n2、新增任务中心\n3、新增鑫享会商城\n4、鑫享会全新改版\n5、会员中心全新改版", upDateBean.getData().getAppUrl(), upDateBean.getData().getForceUpdate(), upDateBean.getData().getVersion());
                            }
                        } catch (Exception e) {
//                            showPopupWindow(mWebView);
//                            showUpVersionDialog("1、新增鑫积分体系\n2、新增任务中心\n3、新增鑫享会商城\n4、鑫享会全新改版\n5、会员中心全新改版", upDateBean.getData().getAppUrl(), upDateBean.getData().getForceUpdate(), upDateBean.getData().getVersion());
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

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (errmyCode == -2) {
            long now = new Date().getTime();
            if (now - mLastBackTime < TIME_DIFF) {
                MainActivity.this.finish();
                finish();
//                                        System.exit(0);
            } else {
                mLastBackTime = now;
                Toast.makeText(MainActivity.this, "再按一次退出鑫茂荣信财富", 2000).show();
            }
        }
        if (errmyCode == -8) {
            long now = new Date().getTime();
            if (now - mLastBackTime < TIME_DIFF) {
                MainActivity.this.finish();
                finish();
//                                        System.exit(0);
            } else {
                mLastBackTime = now;
                Toast.makeText(MainActivity.this, "再按一次退出鑫茂荣信财富", 2000).show();
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
                                        MainActivity.this.finish();
                                        finish();
//                                        System.exit(0);
                                    } else {
                                        mLastBackTime = now;
                                        Toast.makeText(MainActivity.this, "再按一次退出鑫茂荣信财富", 2000).show();
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

        if (myNewLoadingDialog != null && myNewLoadingDialog.isShowing()) {
            myNewLoadingDialog.dismiss();
        }

   /*     myNewLoadingDialog.cancel();
        myNewLoadingDialog = null;*/
    }


    public void onEventMainThread(Integer type) {
        Log.d("eventbus", type + "");
        layout_network.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
//        ToastUtil.showToast(MainActivity.this, type + "");
        myphone = (String) MySharePreferenceUtil.get(MainActivity.this, "phone1", "");
        flag = type;
        if (type == 10) {
            token = (String) MySharePreferenceUtil.get(MainActivity.this, "token", "");
            mWebView.loadUrl("javascript:localStorage.setItem('" + "token" + "','" + token + "');");
            if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(MainActivity.this, "idCard", ""))) {
                //未实名
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
            } else {
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "username", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
            }
            mWebView.loadUrl("javascript:updateData()");
//            mWebView.setVisibility(View.VISIBLE);
            mWebView.loadUrl(Constant.URL);
        } else if (type == 2) {
            token = (String) MySharePreferenceUtil.get(MainActivity.this, "token", "");
            mWebView.loadUrl("javascript:localStorage.setItem('" + "token" + "','" + token + "');");

            if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(MainActivity.this, "idCard", ""))) {
                //未实名
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
            } else {
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "username", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");

            }
            mWebView.loadUrl("javascript:updateData()");
            mWebView.loadUrl(Constant.URL);
//            initWebView();

        } else if (type == 1) {

            mWebView.loadUrl(Constant.URL + "#/regisxy");

        } else if (type == 200) {
            mWebView.clearHistory();

//            finish();
        } else if (type == 74) {

/*
            String gesture = (String) MySharePreferenceUtil.get(MainActivity.this, "flag_shoushi", "");

//            String gesture = (String) MySharePreferenceUtil.get(MainActivity.this, "flaggesture", "");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("success", gesture);

            } catch (Exception e) {

            }
            openGestVCFuction.onCallBack(jsonObject.toString());*/
            String myphone2 = (String) MySharePreferenceUtil.get(MainActivity.this, "phone1", "");
            String gesture = (String) MySharePreferenceUtil.get(MainActivity.this, myphone2 + "flaggesture", "");

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("success", gesture);

            } catch (Exception e) {

            }
            openGestVCFuction.onCallBack(jsonObject.toString());

        } else if (type == 75) {
            String myphone2 = (String) MySharePreferenceUtil.get(MainActivity.this, "phone1", "");
//            String gesture = (String) MySharePreferenceUtil.get(MainActivity.this, "flaggesture", "");
            String gesture = (String) MySharePreferenceUtil.get(MainActivity.this, myphone2 + "flaggesture", "");


            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("success", gesture);

            } catch (Exception e) {

            }
            openGestVCFuction.onCallBack(jsonObject.toString());
            /*         String gesture = (String) MySharePreferenceUtil.get(MainActivity.this, "flag_shoushi", "");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("success", gesture);
            } catch (Exception e) {

            }
            openGestVCFuction.onCallBack(jsonObject.toString());*/
        } else if (type == 76) {
//            String gesture = (String) MySharePreferenceUtil.getout(MainActivity.this, "flaggesture", "");
            String gesture = (String) MySharePreferenceUtil.get(MainActivity.this, "flag_shoushi", "");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("success", gesture);

            } catch (Exception e) {

            }
            openGestVCFuction.onCallBack(jsonObject.toString());
        } else if (type == 101) {

            mWebView.clearHistory();
            flag = 8;
//            url = Constant.URL + "#/verIdentity";
//实名认证
            url = Constant.URL + "#/verIdentity?isRegister=true";
//            mWebView.setVisibility(View.GONE);
            mWebView.loadUrl(url);
            mWebView.reload();


        } else if (type == 301) {
            String gesture = (String) MySharePreferenceUtil.get(MainActivity.this, myphone + "flaggesture", "");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("success", gesture);

            } catch (Exception e) {

            }
            openGestVCFuction.onCallBack(jsonObject.toString());

//            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("success", myphone);

            } catch (Exception e) {

            }

            myNewLoadingDialog4 = new MyNewLoadingDialog2(MainActivity.this);
            myNewLoadingBuilder4 = new MyNewLoadingDialog2.Builder(MainActivity.this);
            myNewLoadingDialog4 = myNewLoadingBuilder4.create();

//            myNewLoadingDialog4.show();
//            mWebView.reload();
            openGestVCFuction.onCallBack(jsonObject.toString());


        } else if (type == 201) {
            mWebView.reload();
        } else if (type == 999) {
            mWebView.loadUrl(url);
            token = (String) MySharePreferenceUtil.get(MainActivity.this, "token", "");
            mWebView.loadUrl("javascript:localStorage.setItem('" + "token" + "','" + token + "');");
            if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(MainActivity.this, "idCard", ""))) {
                //未实名
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
            } else {
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "username", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
            }
            mWebView.loadUrl("javascript:updateData()");

        } else if (type == 998) {
            token = (String) MySharePreferenceUtil.get(MainActivity.this, "token", "");
            Log.d("34534534534", token);
            mWebView.loadUrl("javascript:localStorage.setItem('" + "token" + "','" + token + "');");
            if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(MainActivity.this, "idCard", ""))) {
                //未实名
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
            } else {
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "username", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "phone", "") + "');");
            }
            mWebView.loadUrl("javascript:updateData()");
            initWebView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    myIntentFlag = "1";
                    String advtisement = (String) MySharePreferenceUtil.get(MainActivity.this, "advtisement", "");
                    if (advtisement.equals("0")) {
                        mWebView.loadUrl(Constant.URL);
                    } else if (advtisement.equals("1")) {
//                        mWebView.loadUrl(Constant.URL);
//                        initWebView();

                        mWebView.loadUrl(getIntent().getStringExtra("url"));
                    } else {
                        mWebView.loadUrl(Constant.URL);
                    }
                }
            }, 3000);


            mWebView.setVisibility(View.VISIBLE);
        } else if (type == 886) {
            MainActivity.this.finish();
            finish();
        } else if (type == 666) {
            layout_network.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            mynetworkflag = 1;

            myNewLoadingDialog6 = new MyNewLoadingDialog2(MainActivity.this);
            myNewLoadingBuilder6 = new MyNewLoadingDialog2.Builder(MainActivity.this);
            myNewLoadingDialog6 = myNewLoadingBuilder6.create();
            Window window = myNewLoadingDialog6.getWindow();
            imageView_loading = (ImageView) window.findViewById(R.id.img_zhuanquan);

            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.imganmi);
            animation.setDuration(500);
            imageView_loading.startAnimation(animation);
            myNewLoadingDialog6.show();
            mWebView.setVisibility(View.VISIBLE);
            layout_network.setVisibility(View.GONE);
            ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
//        cmb.setText(content.trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
            cmb.getText();
            //定义与事件相关的属性信息
//            mWebView.setVisibility(View.GONE);

            mWebView.loadUrl(Constant.URL);
            initWebView();

            token = (String) MySharePreferenceUtil.get(MainActivity.this, "token", "");

            Log.d("newstoken1", token);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    mWebView.loadUrl("javascript:localStorage.setItem('" + "token" + "','" + token + "');");
                    if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(MainActivity.this, "idCard", ""))) {
                        //未实名
                        mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "mPhone", "") + "');");
                        mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "mPhone", "") + "');");
                    } else {
                        mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "username", "") + "');");
                        mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "mPhone", "") + "');");
                    }
                    mWebView.loadUrl("javascript:updateData()");

                    mWebView.loadUrl(Constant.URL);
                }
            }, 1000);
        } else if (type == 250) {
//            mWebView.loadUrl(Constant.URL);
            initWebView();

            token = (String) MySharePreferenceUtil.get(MainActivity.this, "token", "");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    mWebView.loadUrl("javascript:localStorage.setItem('" + "token" + "','" + token + "');");
                    if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(MainActivity.this, "idCard", ""))) {
                        //未实名
                        mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "mPhone", "") + "');");
                        mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "mPhone", "") + "');");
                    } else {
                        mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(MainActivity.this, "username", "") + "');");
                        mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(MainActivity.this, "mPhone", "") + "');");
                    }
                    mWebView.loadUrl("javascript:updateData()");

                    mWebView.loadUrl(Constant.URL);
                }
            }, 1000);
        }
    }

    public void onEventMainThread(EventBean eventBean) {
        if (eventBean.getPostNum() == 300) {
            MySharePreferenceUtil.put(MainActivity.this, "newintentflag", "2");
            Log.i(TAG, "onEventMainThread: " + eventBean.getMsg());
            String serviceFlag = (String) MySharePreferenceUtil.get(MainActivity.this, "serviceflag", "");

            mWebView.loadUrl(eventBean.getMsg());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (url.equals(Constant.URL)) {
                    } else if (!url.equals(Constant.URL)) {
                        mWebView.reload();
                    } else {
                    }
                }
            }, 100);
        }
    }

    public void setImmersionStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void selectImage(int result) {
        MultiImageSelector.create(MainActivity.this).showCamera(true) // 是否显示相机. 默认为显示
//                .count(1) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .single() // 单选模式
//                .multi() // 多选模式, 默认模式;
//                .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
                .start(MainActivity.this, result);
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
        token = (String) MySharePreferenceUtil.get(MainActivity.this, "token", "");
//        token = (String) MySharePreferenceUtil.get(MainActivity.this, "token", "");
        url = getIntent().getStringExtra("url");
         flaggesture = (String) MySharePreferenceUtil.get(MainActivity.this, phone + "flaggesture", "");
         zhiwendenglu = (String) MySharePreferenceUtil.get(MainActivity.this, phone + "zhiwendenglu", "");
        if (flaggesture.equals("0") && zhiwendenglu.equals("1")) {
        } else {
            if (TextUtils.isEmpty(url)) {
                mWebView.loadUrl(Constant.URL);
            } else {
//                mWebView.loadUrl(Constant.URL);
                mWebView.loadUrl(url);
            }
        }
        text_shijian.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        text_shijian.getPaint().setAntiAlias(true);//抗锯齿
        text_shijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myflag = 1;
                if (!network()) {
                    mWebView.setVisibility(View.GONE);
                    ImmersionBar.with(MainActivity.this)
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

        builder5 = new MyLoadingDialog.Builder(MainActivity.this)
                .setShowMessage(false)
                .setCancelable(false);
        dialog5 = builder5.create();


        myNewLoadingDialog = new MyNewLoadingDialog2(MainActivity.this);
        myNewLoadingBuilder = new MyNewLoadingDialog2.Builder(MainActivity.this);
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

//        StatService.trackWebView(this, mWebView, chromeClient);


        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("首页", "首页");

        // 设置控件附加属性，用于无埋点对控件添加数据；控件绑定事件附加属性依赖于view.setTag(key, value)支持，使用key值：-96000；
        // 如果view已经绑定过此key值，则此设置不生效
        StatService.setAttributes(mWebView, hashMap);


        mWebView.registerHandler("submitFromWeb", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

                String str = "这是html返回给java的数据:" + data;
                // 例如你可以对原始数据进行处理
                str = str + ",Java经过处理后截取了一部分：" + str.substring(0, 5);
                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                //回调返回给Js
                function.onCallBack(str + ",Java经过处理后截取了一部分：" + str.substring(0, 5));
            }

        });
//        mWebView.addBridgeInterface(new MyJavaSctiptInterface(mWebView, this));//注册桥梁类，该类负责H5和android通信


        mWebView.registerHandler("functionOpen", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(MainActivity.this, "网页在打开你的下载文件预览", Toast.LENGTH_SHORT).show();
                pickFile();

            }

        });

        /**
         * 前端页面需要发送短信时触发
         */
        mWebView.registerHandler("msgSend", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Gson gson = new Gson();
                DianhuaModel dianhuaModel = new DianhuaModel();
                dianhuaModel = gson.fromJson(data, DianhuaModel.class);//拿到数据
                Uri smsToUri = Uri.parse("smsto:" + dianhuaModel.getTelphone());
                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);//调用系统短信发送
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

                Toast.makeText(MainActivity.this, data + "", Toast.LENGTH_SHORT).show();


            }
        });


        mWebView.registerHandler("goBackForAndroid", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(MainActivity.this, "返回上一级页面", Toast.LENGTH_SHORT).show();
                mWebView.loadUrl(String.format("javascript:WebViewJavascriptBridge._handleMessageFromNative(%s)", data));

            }

        });

        mWebView.registerHandler("toPastString", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Log.d(data.toString(), "787878");

//                Toast.makeText(MainActivity.this, data.toString(), Toast.LENGTH_SHORT).show();

                Gson gson = new Gson();
                ClipboardBean clipboardBean = new ClipboardBean();
                clipboardBean = gson.fromJson(data.toString(), ClipboardBean.class);
                String content = clipboardBean.getPastString();

                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(content);
            }

        });

        //跳转积分商城外链
        mWebView.registerHandler("goXindouWebView", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
//                ToastUtil.showCenterToast(MainActivity.this, "goXindouWebView");

            /*    mWebView.evaluateJavascript("javascript:goXindouWebView()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d("mmmmmm", "--s--=" + s);
                    }
                });*/

                Log.d(data, "323223232");
                Gson gson = new Gson();
                ShopModel shopModel = new ShopModel();
                shopModel = gson.fromJson(data, ShopModel.class);
                String token1 = (String) MySharePreferenceUtil.get(MainActivity.this, "token1", "");
                MySharePreferenceUtil.put(MainActivity.this, "shopUrl", shopModel.getData());
                MySharePreferenceUtil.put(MainActivity.this, "shopUrl2", shopModel.getData());
                Log.d(token1, "3242342342342688hgh");
                if (TextUtils.isEmpty(token1)) {
                    Intent intent = new Intent(MainActivity.this, ShopActivity2.class);
//                    Intent intent = new Intent(MainActivity.this, ShopActivity.class);
                    intent.putExtra("url", shopModel.getData());
                    intent.putExtra("url2", shopModel.getData());
                    MySharePreferenceUtil.put(MainActivity.this, "shopUrl", shopModel.getData());
                    MySharePreferenceUtil.put(MainActivity.this, "shopUrl2", shopModel.getData());
                    startActivity(intent);
                } else if (token1.length() < 5) {
                    Intent intent = new Intent(MainActivity.this, ShopActivity2.class);
//                    Intent intent = new Intent(MainActivity.this, ShopActivity.class);
                    intent.putExtra("url", shopModel.getData());
                    intent.putExtra("url2", shopModel.getData());
                    MySharePreferenceUtil.put(MainActivity.this, "shopUrl", shopModel.getData());
                    MySharePreferenceUtil.put(MainActivity.this, "shopUrl2", shopModel.getData());
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

                                    Intent intent = new Intent(MainActivity.this, ShopActivity2.class);
//                                    Intent intent = new Intent(MainActivity.this, ShopActivity.class);
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
                    dialog_udrmb = new Dialog(MainActivity.this);
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
                    Intent intent = new Intent(MainActivity.this, MainActivity3.class);
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
                    //APP数据给前端传值，
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
                    // 接收网页传过来的数据，跳转页面
                    udrmbBean = gson.fromJson(data.toString(), UdrmbBean.class);
                    Intent intent = new Intent(MainActivity.this, MainActivity5.class);
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
                        Toast.makeText(MainActivity.this, "返回上一级页面", Toast.LENGTH_SHORT).show();

                    }

                });


        mWebView.registerHandler("agreementBack", new

                BridgeHandler() {

                    @Override
                    public void handler(String data, CallBackFunction function) {
                        Toast.makeText(MainActivity.this, "返回", Toast.LENGTH_SHORT).show();
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


        /**
         * 前端PDF点击处理
         */
        mWebView.registerHandler("toPDFVC", new
                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        Log.d(data.toString(), "333333333333333333");
                        Log.d(data.toString(), "333333333333333333");
                        Gson gson = new Gson();
                        PdfModel pdfModel = new PdfModel();
                        pdfModel = gson.fromJson(data, PdfModel.class);//前端返回的PDF数据
                        Intent intent;
                        if (pdfModel.getPdfUrl().contains("viewContract.api")) {
                            intent = new Intent(MainActivity.this, HongBaoWebActivity.class);
                        } else {
//                            intent = new Intent(MainActivity.this, PDFActivity6.class);
                            intent = new Intent(MainActivity.this, PDFWebActivity.class);
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
                        ImmersionBar.with(MainActivity.this)

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


                        ImmersionBar.with(MainActivity.this)

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

        /**
         * 调用原生相册
         */
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

        //所有验证登录都会走这
        mWebView.registerHandler("toLogin", new
                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                            Log.e("ggg","调取登录");
                            MySharePreferenceUtil.remove(MainActivity.this, "token");
                            MySharePreferenceUtil.remove(MainActivity.this, "username");
                            MySharePreferenceUtil.put(MainActivity.this, "newintentflag", "2");
                            MySharePreferenceUtil.put(MainActivity.this, "token", "");
                            MySharePreferenceUtil.put(MainActivity.this, "token1", "");
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                        }

                    }
                });

        mWebView.registerHandler("loginOut", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {


//                Toast.makeText(MainActivity.this, "loginOut", Toast.LENGTH_SHORT).show();
                        MySharePreferenceUtil.remove(MainActivity.this, "token");
                        MySharePreferenceUtil.remove(MainActivity.this, "username");
                        MySharePreferenceUtil.remove(MainActivity.this, "phone");
                        MySharePreferenceUtil.get(MainActivity.this, phone + "flaggesture", "2");
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
                        MySharePreferenceUtil.put(MainActivity.this, "appHeadUrl", string);


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
                        Log.e("ggg", "我是设置");
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                });
        mWebView.registerHandler("activity", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        try {
                            startActivity(new Intent(MainActivity.this, Class.forName(data)));
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

                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();

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
                                        File photoFile = createImagePathFile(MainActivity.this);
                                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

//                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

//                                com.xmrxcaifu.jsbridge.fileprovider
                                        imageUriFromCamera = FileProvider.getUriForFile(MainActivity.this, "com.xmrxcaifu.jsbridge.fileprovider", photoFile);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);

                                        startActivityForResult(intent, GET_IMAGE_BY_CAMERA_U);
                                    } else {
                                        Toast.makeText(MainActivity.this, "扫码拍照或无法正常使用", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            imageUriFromCamera = createImagePathUri(MainActivity.this);
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
                        Log.e("ggg", "获取手势密码信息");
                        Log.d(data, "3333333333333");
                        String gesture = (String) MySharePreferenceUtil.get(MainActivity.this, myphone + "flaggesture", "");
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
                        Log.e("ggg", "关闭手势密码");
                        String gesture = (String) MySharePreferenceUtil.get(MainActivity.this, myphone + "flaggesture", "");
                        JSONObject jsonObject = new JSONObject();
                        try {

                            if (gesture.equals("1")) {
                                jsonObject.put("success", gesture);
                                MySharePreferenceUtil.put(MainActivity.this, myphone + "flaggesture", "0");
                            } else {
                                jsonObject.put("success", "0");
                                MySharePreferenceUtil.put(MainActivity.this, myphone + "flaggesture", "0");
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
                        Log.e("ggg", "打开手势密码");
                        Log.d(data, "3333333333333");
//
                        openGestVCFuction = function;
                        Gson gson = new Gson();
                        BeanGesture beanGesture = new BeanGesture();
                        beanGesture = gson.fromJson(data, BeanGesture.class);
                        String str1 = beanGesture.getOpentype();

                        if (str1.equals("oncreate")) {

                            Intent intent = new Intent(MainActivity.this, GestureMotifyActivity.class);

                            intent.putExtra("str1", str1);
                            startActivity(intent);

//                    startActivity(new Intent(MainActivity.this, GestureMotifyActivity.class));


                        } else if (str1.equals("modify")) {
//                    startActivity(new Intent(MainActivity.this, GestureVerify_checkActivity.class));

                            Intent intent = new Intent(MainActivity.this, GestureVerify_checkActivity.class);

                            intent.putExtra("str1", str1);
                            startActivity(intent);

                        }
                        Log.d(data, "3333333333333");
                        Log.d(data, "3333333333333");
                    }
                });


     /*   getFingermark   获取指纹
        closeFingermark    关闭指纹

        openFingermark   开启指纹*/
        /**
         * 获取指纹
         */
        mWebView.registerHandler("getFingermark", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        Log.e("ggg", "获取指纹");
                        String finger = (String) MySharePreferenceUtil.get(MainActivity.this, phone + "zhiwendenglu", "");
//                function.onCallBack(finger.toString());
                        String zhichi = (String) MySharePreferenceUtil.get(MainActivity.this, "figer", "");

                        FigerVC = function;

                        if (zhichi.equals("0")) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("success", "2");
                                function.onCallBack(jsonObject.toString());
                            } catch (Exception e) {
                            }
                        } else {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("success", finger);
                                function.onCallBack(jsonObject.toString());
                            } catch (Exception e) {

                            }
                        }


                    }
                });

        /**
         * 关闭指纹
         */
        mWebView.registerHandler("closeFingermark", new

                BridgeHandler() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        Log.e("ggg", "关闭指纹");
                        FigerVC = function;
                        flag_open_close = "0";
                        startFingerprintRecognition();
                        builder = new CustomDialog_view_new.Builder(MainActivity.this);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FigerVC.onCallBack("1");
                                builder.create().dismiss();
                                cancelFingerprintRecognition();
                            }
                        });
                        if (flag_open_close.equals("0")) {
                            //关闭指纹
                            builder.setMessage("关闭指纹登录");
                        } else {
                            builder.setMessage("开启指纹登录");
                        }
                        builder.setMessage2("请验证已有手机指纹");
                        builder.create().setCancelable(false);
                        builder.create().show();
//                onFingerprint(null);
                    }
                });
        /**
         * 开启指纹
         */
        mWebView.registerHandler("openFingermark", new

                BridgeHandler() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        Log.e("ggg", "开启指纹");
                        FigerVC = function;
                        flag_open_close = "1";
//                onFingerprint(null);
                        startFingerprintRecognition();
                        builder = new CustomDialog_view_new.Builder(MainActivity.this);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FigerVC.onCallBack("0");
                                builder.create().dismiss();
                                cancelFingerprintRecognition();
                            }
                        });
                        if (flag_open_close.equals("0")) {
                            //关闭指纹
                            builder.setMessage("关闭指纹登录");
                        } else {
                            builder.setMessage("开启指纹登录");
                        }
                        builder.setMessage2("请验证已有手机指纹");
                        builder.create().setCancelable(false);
                        builder.create().show();
                    }
                });
        mWebView.registerHandler("share", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        Log.e("ggg", "分享走我");
                        Log.d("data", data);
                        Log.d("data", data);
                        ShareModel shareModel = new ShareModel();
                        Gson gson = new Gson();
                        shareModel = gson.fromJson(data, ShareModel.class);
                        final ShareModel finalShareModel = shareModel;
                        mShareAction = new ShareAction(MainActivity.this).setDisplayList(
                                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ)
                                .setShareboardclickCallback(new ShareBoardlistener() {
                                    @Override
                                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                        if (snsPlatform.mShowWord.equals("umeng_sharebutton_copy")) {
//                                    Toast.makeText(MainActivity.this, "复制文本按钮", Toast.LENGTH_LONG).show();
                                        } else if (snsPlatform.mShowWord.equals("umeng_sharebutton_copyurl")) {
//                                    Toast.makeText(MainActivity.this, "复制链接按钮", Toast.LENGTH_LONG).show();
                                        } else if (share_media == SHARE_MEDIA.SMS) {
                                            new ShareAction(MainActivity.this).withText(finalShareModel.getTitle())
                                                    .setPlatform(share_media)
                                                    .setCallback(mShareListener)
                                                    .share();
                                        } else {
                                            UMWeb web = new UMWeb(finalShareModel.getUrlStr());
                                            web.setTitle(finalShareModel.getTitle());
                                            web.setDescription(finalShareModel.getDesc());
//                                    web.setThumb(new UMImage(MainActivity.this, R.mipmap.index));
//                                    web.setThumb(new UMImage(MainActivity.this, R.mipmap.index));
                                            web.setThumb(new UMImage(MainActivity.this, finalShareModel.getImageUrlStr()));  //网络缩略图
                                            new ShareAction(MainActivity.this).withMedia(web)
                                                    .setPlatform(share_media)
                                                    .setCallback(mShareListener)
                                                    .share();
                                        }
                                    }
                                });
                        mShareAction.open();
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
                Intent intent = new Intent(MainActivity.this, MainActivity6.class);
                intent.putExtra("url", wailian.getExternalLink());
                intent.putExtra("title", wailian.getExternalTitle());
                startActivity(intent);
            }

        });

    }

    public void stopLocation() {
        mLocationManager.removeUpdates(MainActivity.this);
    }

    public String getdeviceId() {
        try {
            MySharePreferenceUtil.put(MainActivity.this, "deviceid", tm.getDeviceId());
//            Toast.makeText(MainActivity.this,  tm.getDeviceId()+"", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            MySharePreferenceUtil.put(MainActivity.this, "deviceid", "111111111");
        }
//        return tm.getDeviceId();
        String string = (String) MySharePreferenceUtil.get(MainActivity.this, "deviceid", "");
        return string;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        //获取是否点击了HOME键的标识0是点击了其它没点home
//        String ishome = (String) MySharePreferenceUtil.get(
//                MainActivity.this, "isHome", "");
//
//        //如果是0那么就是按了HOME键，需要判断时间是否大于显示指纹和
//        if ("0".equals(ishome)) {
//            Log.e("ggg", ishome + "是否点了0点，其它不点");
//            MySharePreferenceUtil.put(
//                    MainActivity.this, "isHome", "1");
//            end = System.currentTimeMillis();
//            if (end - start >= 10 * 1000) {
//                Log.e("ggg", end - start + "在后台超过一分钟，我要干活");
//                String flaggesture = (String) MySharePreferenceUtil.get(MainActivity.this, phone + "flaggesture", "");
//                String newintentflag = (String) MySharePreferenceUtil.get(MainActivity.this, "newintentflag", "");
//                String zhiwendenglu = (String) MySharePreferenceUtil.get(MainActivity.this, phone + "zhiwendenglu", "");
//                if (flaggesture.equals("1")) {
//                    Log.e("ggg", "我是手势");
//                    Intent intent = new Intent(MainActivity.this, GestureVerifyActivity.class);
//                    intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));
//
//                    intent.putExtra("url", getIntent().getStringExtra("url"));
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in,
//                            R.anim.slide_out);
//                    MySharePreferenceUtil.put(MainActivity.this, "newintentflag", "2");
//                    MySharePreferenceUtil.put(MainActivity.this, "flagloading", "1");
////                finish();
//
//                    Intent mIntent = new Intent(ACTION_NAME);
//                    mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
//                    //发送广播
//                    sendBroadcast(mIntent);
//
//                } else if (flaggesture.equals("0") && zhiwendenglu.equals("1")) {
//                    Log.e("ggg", "我是指纹我是指纹");
//                    Intent intent = new Intent(MainActivity.this, V6_Finger_Check_NewActivity.class);
//                    intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));
//                    intent.putExtra("url", getIntent().getStringExtra("url"));
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in,
//                            R.anim.slide_out);
//                    MySharePreferenceUtil.put(MainActivity.this, "newintentflag", "2");
//                    MySharePreferenceUtil.put(MainActivity.this, "flagloading", "1");
//                    MySharePreferenceUtil.put(MainActivity.this, "newintentflag", "2");
//                    MySharePreferenceUtil.put(MainActivity.this, "flagloading", "1");
//                    Intent mIntent = new Intent(ACTION_NAME);
//                    mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
//                    //发送广播
//                    sendBroadcast(mIntent);
//                } else if (newintentflag.equals("5")) {
//                    Log.e("ggg", "我是什么");
//                    Intent intent = new Intent(MainActivity.this, GestureVerifyActivity.class);
//                    intent.putExtra("flag_gest", getIntent().getStringExtra("flag_gest"));
//                    intent.putExtra("url", getIntent().getStringExtra("url"));
//                    startActivity(intent);
//
//                }
//            }
//        }

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
            FileProvider.getUriForFile(MainActivity.this, "com.xmrxcaifu.jsbridge.fileprovider", file);
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

            FileProvider.getUriForFile(MainActivity.this, "com.lyk.jsbridge.fileprovider", file);
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
        ContactInfoService mContactInfoService = new ContactInfoService(MainActivity.this);
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
            super.onPageFinished(view, url);
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
//                    mWebView.setVisibility(View.GONE);
                } else if (errmyCode == -8) {
                    layout_network.setVisibility(View.VISIBLE);
//                    mWebView.setVisibility(View.GONE);
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


            MySharePreferenceUtil.put(MainActivity.this, "flagloading", "1");
            //更新请求接口
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


        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url1) {
//            mWebView.flushMessageQueue();
//            goXindouWebView
            Log.d(url1, "555555555555555555555");
            String mystringurl = BridgeUtil.getFunctionFromReturnUrl(url1);

            if (url1.contains("goXindouWebView")) {
//                mWebView.doSend("goXindouWebView","data",contactFunction );

                BridgeUtil.webViewLoadJs(mWebView, "goXindouWebView");
            }

            if (url1.contains("xmrxcaifu")) {
                return true;
            }
            if (url1.equals("about:blank")) {
                url = Constant.URL;
            } else if (url1.contains("__BRIDGE_LOADED__")) {
                url = Constant.URL;
            } else if (url1.equals("wvjbscheme://__BRIDGE_LOADED__")) {
                url = Constant.URL;
            } else {
                url = url1;
            }

            if (!network()) {
//                startActivity(new Intent(MainActivity.this, NoNetActivity.class));
            } else {
                //弹出拨打电话，原生拨打
                if (url.startsWith("tel:")) {
                    final Dialog dialog_tel;
                    dialog_tel = new Dialog(MainActivity.this);
                    dialog_tel.setCancelable(false);
                    Window window = dialog_tel.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
                            R.color.float_transparent)));
                    window.setGravity(Gravity.CENTER);
                    dialog_tel.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    window.setContentView(R.layout.item_phone_dialog);
                    dialog_tel.show();
                    final String phone_url = url;
                    String str1 = phone_url.replaceAll("tel:", "");
                    TextView text_phone = (TextView) window.findViewById(R.id.text_phone);


                    int start = 0;
                    int before = 0;
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < str1.length(); i++) {
                        if (i != 3 && i != 8 && str1.charAt(i) == ' ') {
                            continue;
                        } else {
                            sb.append(str1.charAt(i));
                            if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                                sb.insert(sb.length() - 1, ' ');
                            }
                        }
                    }
                    if (!sb.toString().equals(str1.toString())) {
                        int index = start + 1;
                        if (sb.charAt(start) == ' ') {
                            if (before == 0) {
                                index++;
                            } else {
                                index--;
                            }
                        } else {
                            if (before == 1) {
                                index--;
                            }
                        }


                    }


                    text_phone.setText(sb.toString());
                    TextView text_cancel = (TextView) window.findViewById(R.id.text_cancel);
                    TextView text_submit = (TextView) window.findViewById(R.id.text_submit);
                    text_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_tel.dismiss();
                        }
                    });
                    text_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Toast.makeText(MainActivity.this, "点击拨号", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phone_url));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            dialog_tel.dismiss();
                        }
                    });


                    return true;
                } else if (url.equals(Constant.URL + "#/verIdentity")) {
                } else if (url.contains("statusWhite")) {
//黑的么
                    ImmersionBar.with(MainActivity.this)
                            .statusBarDarkFont(false, 1f)
                            .init();
                } else if (url.contains("statusBlack")) {
                    //白的么
                    ImmersionBar.with(MainActivity.this)
                            .statusBarDarkFont(true, 1f)
                            .init();
                } else if (url.equals(Constant.URL)) {
                    ImmersionBar.with(MainActivity.this)

                            .statusBarDarkFont(false, 1f)
                            .init();
                } else {

                }

            }


            return super.shouldOverrideUrlLoading(view, url);


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
                ImmersionBar.with(MainActivity.this)

                        .statusBarDarkFont(true, 1f)
                        .init();
                mWebView.setVisibility(View.GONE);

                img_network.setImageResource(R.drawable.network);
                text_network.setText("数据加载失败,请检查一下你的网络");
                text_shijian.setText("点击重试");
            } else {
                if (errorCode == -2) {
                    layout_network.setVisibility(View.VISIBLE);
                    ImmersionBar.with(MainActivity.this)
                            .statusBarDarkFont(true, 1f)
                            .init();
                    img_network.setImageResource(R.drawable.silingsi);
                    text_network.setText("没有找到您要访问的页面");
                    text_shijian.setText("返回上一页");
//                    mWebView.setVisibility(View.GONE);
                } else if (errorCode == -8) {
                    layout_network.setVisibility(View.VISIBLE);
                    ImmersionBar.with(MainActivity.this)

                            .statusBarDarkFont(true, 1f)
                            .init();
//                    mWebView.setVisibility(View.GONE);
                    img_network.setImageResource(R.drawable.network);
                    text_network.setText("数据加载失败,请检查一下你的网络");
                    text_shijian.setText("点击重试");
                } else {
                    ImmersionBar.with(MainActivity.this)
                            .statusBarDarkFont(true, 1f)
                            .init();
                    layout_network.setVisibility(View.GONE);
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

        private WeakReference<MainActivity> mActivity;

        private CustomShareListener(MainActivity activity) {
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

    /**
     * 更新弹出的Dialog
     * @param content
     * @param url
     * @param forceUpdate
     * @param version
     */
    private void showUpVersionDialog(String content, final String url,
                                     final int forceUpdate, String version) {
        dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
                R.color.float_transparent)));
        window.setGravity(Gravity.CENTER);
//        window.setGravity(Gravity.TOP);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

//四个是正常的

        window.setContentView(R.layout.item_splash_upversion_dialog2);
//        window.setContentView(R.layout.item_splash_upversion_dialog);
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
        View view_touming = window.findViewById(R.id.view_touming);
        LinearLayout layout_dialog = (LinearLayout) window.findViewById(R.id.layout_dialog);

        View view_5 = window.findViewById(R.id.view_5);
        View view_6 = window.findViewById(R.id.view_6);
        String[] len = content.split("\n");
        Log.d("lenlenlenlen", len.length + "");
        View view_4 = window.findViewById(R.id.view_4);
        View view_3 = window.findViewById(R.id.view_3);

        View view_2 = window.findViewById(R.id.view_2);
        if (len.length == 2) {
            view_2.setVisibility(View.VISIBLE);
        } else {
            view_2.setVisibility(View.GONE);
        }
        if (len.length == 3) {
            view_3.setVisibility(View.VISIBLE);
            view_2.setVisibility(View.VISIBLE);
        } else {
            view_3.setVisibility(View.GONE);
        }

        if (len.length == 4) {
            view_3.setVisibility(View.VISIBLE);
            view_2.setVisibility(View.VISIBLE);
            view_4.setVisibility(View.VISIBLE);
        } else {
            view_4.setVisibility(View.GONE);
        }

        if (len.length == 5) {
            view_3.setVisibility(View.VISIBLE);
            view_2.setVisibility(View.VISIBLE);
            view_4.setVisibility(View.VISIBLE);
            view_5.setVisibility(View.VISIBLE);
        } else {
            view_5.setVisibility(View.GONE);
        }
        if (len.length == 6) {
            view_3.setVisibility(View.VISIBLE);
            view_2.setVisibility(View.VISIBLE);
            view_4.setVisibility(View.VISIBLE);
            view_5.setVisibility(View.VISIBLE);
            view_6.setVisibility(View.VISIBLE);
        } else {
            view_6.setVisibility(View.GONE);
        }

        WindowManager m = getWindowManager();
        Display display = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值

        p.height = (int) (display.getHeight() * 0.8);   //高度设置为屏幕的0.3
        p.width = (int) (display.getWidth() * 0.8);    //宽度设置为屏幕的0.5


        dialog.getWindow().setAttributes(p);     //设置生效


        if (forceUpdate == 1) {
            tv_upversion_dialog_clean.setVisibility(View.GONE);
        } else {
            tv_upversion_dialog_clean.setVisibility(View.VISIBLE);
        }

        tv_upversion_dialog_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

//					initData2();
                    if (forceUpdate == 1) {

                    } else {
                        dialog.cancel();
                    }

                }
                UpdateManager manager = new UpdateManager(MainActivity.this,
                        url);
                manager.showDownloadDialog();


            }
        });
//
    }

    private PopupWindow popWindow;

    private void showPopupWindow(View parent) {
        if (popWindow == null) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.item_splash_upversion_pop, null);
            //LayoutParams相当于一个Layout的信息包，它封装了Layout的位置、高、宽等信息。
            popWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            initPop(view);
        }
        WindowManager m = getWindowManager();
        Display display = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值

        p.height = (int) (display.getHeight() * 0.8);   //高度设置为屏幕的0.3
        p.width = (int) (display.getWidth() * 0.8);    //宽度设置为屏幕的0.5


//        popWindow.getWindow().setAttributes(p);     //设置生效

        //设置动画效果
        popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        //获取popwindow焦点
        popWindow.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        //实现软键盘不自动弹出,ADJUST_RESIZE属性表示Activity的主窗口总是会被调整大小，从而保证软键盘显示空间。
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popwindow显示位置
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }


    public void initPop(View view) {

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
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onFingerprint(View v) {
        FingerprintUtil.callFingerPrint(new FingerprintUtil.OnCallBackListenr() {
            @Override
            public void onSupportFailed() {
//                showToast("当前设备不支持指纹");

                ToastUtil.showCenterToast(MainActivity.this, "当前设备不支持指纹");

                FigerVC.onCallBack("0");

            }

            @Override
            public void onInsecurity() {

//                ToastUtil.showCenterToast(MainActivity.this,"请在系统设置中开启指纹功能");


            }

            @Override
            public void onEnrollFailed() {
//                showToast("请到设置中设置指纹");
                ToastUtil.showCenterToast(MainActivity.this, "请到设置中设置指纹");
            }

            @Override
            public void onAuthenticationStart() {
                Log.e("start", "finger_start");
                builder = new CustomDialog_view_new.Builder(MainActivity.this);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FingerprintUtil.cancel();
                        builder.create().dismiss();
                    }
                });
                if (flag_open_close.equals("0")) {
                    //关闭指纹
                    builder.setMessage("关闭指纹登录");
                } else {
                    builder.setMessage("开启指纹登录");
                }
                builder.setMessage2("请验证已有手机指纹");
                builder.create().setCancelable(false);
                builder.create().show();
            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {


                String finger = (String) MySharePreferenceUtil.get(MainActivity.this, phone + "zhiwendenglu", "");
//                ToastUtil.showCenterToast(MainActivity.this, errString.toString());
                if (finger.equals("1")) {
                    MySharePreferenceUtil.put(MainActivity.this, phone + "zhiwendenglu", "1");
                    FigerVC.onCallBack("1");
                } else {
                    MySharePreferenceUtil.put(MainActivity.this, phone + "zhiwendenglu", "0");
                    FigerVC.onCallBack("0");
                }

                if (builder.create() != null) {
                    builder.create().cancel();
                    builder.create().mdissmis();
                    FingerprintUtil.cancel();
                }


            }

            @Override
            public void onAuthenticationFailed() {

                ToastUtil.showCenterToast(MainActivity.this, "认证失败");

            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {


                ToastUtil.showCenterToast(MainActivity.this, helpString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {

//                ToastUtil.showCenterToast(MainActivity.this, "认证成功");
                builder.create().dismiss();
                String finger = (String) MySharePreferenceUtil.get(MainActivity.this, phone + "zhiwendenglu", "");

                if (finger.equals("1")) {
                    MySharePreferenceUtil.put(MainActivity.this, phone + "zhiwendenglu", "0");
                    FigerVC.onCallBack("0");
                } else {
                    MySharePreferenceUtil.put(MainActivity.this, phone + "zhiwendenglu", "1");
                    FigerVC.onCallBack("1");
                }

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

//            ToastUtil.showCenterToast(MainActivity.this, "认证成功");
            builder.create().dismiss();
            String finger = (String) MySharePreferenceUtil.get(MainActivity.this, phone + "zhiwendenglu", "");

            if (finger.equals("1")) {
                MySharePreferenceUtil.put(MainActivity.this, phone + "zhiwendenglu", "0");
                FigerVC.onCallBack("0");
            } else {
                MySharePreferenceUtil.put(MainActivity.this, phone + "zhiwendenglu", "1");
                FigerVC.onCallBack("1");
            }

        }

        @Override
        public void onAuthenticateFailed(int helpId) {
//            ToastUtil.showCenterToast(MainActivity.this, "识别失败");
            builder.create().dismiss();

            String finger = (String) MySharePreferenceUtil.get(MainActivity.this, phone + "zhiwendenglu", "");
//                ToastUtil.showCenterToast(MainActivity.this, errString.toString());
            if (finger.equals("1")) {
                MySharePreferenceUtil.put(MainActivity.this, phone + "zhiwendenglu", "1");
                FigerVC.onCallBack("1");
            } else {
                MySharePreferenceUtil.put(MainActivity.this, phone + "zhiwendenglu", "0");
                FigerVC.onCallBack("0");
            }

            if (builder.create() != null) {
                builder.create().cancel();
                builder.create().mdissmis();
                cancelFingerprintRecognition();
            }

        }

        @Override
        public void onAuthenticateError(int errMsgId) {
//            ToastUtil.showCenterToast(MainActivity.this, "识别错误");
            String finger = (String) MySharePreferenceUtil.get(MainActivity.this, phone + "zhiwendenglu", "");
//                ToastUtil.showCenterToast(MainActivity.this, errString.toString());
            if (finger.equals("1")) {
                MySharePreferenceUtil.put(MainActivity.this, phone + "zhiwendenglu", "1");
                FigerVC.onCallBack("1");
            } else {
                MySharePreferenceUtil.put(MainActivity.this, phone + "zhiwendenglu", "0");
                FigerVC.onCallBack("0");
            }

            if (builder.create() != null) {
                builder.create().cancel();
                builder.create().mdissmis();
                cancelFingerprintRecognition();
//                FingerprintUtil.cancel();
            }

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
//            resetGuideViewState();
        }
    }

}
