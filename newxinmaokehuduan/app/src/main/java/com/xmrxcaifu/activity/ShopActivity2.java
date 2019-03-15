package com.xmrxcaifu.activity;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.google.gson.Gson;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
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
import com.xmrxcaifu.MainActivity;
import com.xmrxcaifu.R;
import com.xmrxcaifu.Rom;
import com.xmrxcaifu.common.LoadingDialog;
import com.xmrxcaifu.core.FingerprintCore;
import com.xmrxcaifu.dialog.CustomDialog;
import com.xmrxcaifu.dialog.CustomDialog_view_new;
import com.xmrxcaifu.http.MyOkhttp;
import com.xmrxcaifu.http.callback.StringNoDialogCallback;
import com.xmrxcaifu.http.request.BaseRequest;
import com.xmrxcaifu.modle.FaceModel;
import com.xmrxcaifu.modle.ShareModel;
import com.xmrxcaifu.modle.ShopDetialModel;
import com.xmrxcaifu.modle.ShopModel;
import com.xmrxcaifu.modle.ShopTokenModel;
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
import com.xmrxcaifu.view.MyLoadingDialog;
import com.xmrxcaifu.view.MyNewLoadingDialog2;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;
import okhttp3.Response;

public class ShopActivity2 extends BaseActivty implements TencentLocationListener {

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

    private final String ACTION_NAME1 = "结束程序";
    View title_view_new;

    String str = "0";
    String flag_open_close = "";
    String imei = "";
    String ANDROID_ID = "";
    String mytoken;
    public static ShopActivity2 instance = null;

    private final String ACTION_NAME = "回调";

    public void registerBoradcastReceiver1() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME1);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);


    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME)) {
                ToastUtil.showCenterToast(ShopActivity2.this, "这里");
                token = (String) MySharePreferenceUtil.get(ShopActivity2.this, "token", "");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "token" + "','" + token + "');");
                if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(ShopActivity2.this, "idCard", ""))) {
                    //未实名
                    mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(ShopActivity2.this, "phone", "") + "');");
                    mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(ShopActivity2.this, "phone", "") + "');");
                } else {
                    mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(ShopActivity2.this, "username", "") + "');");
                    mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(ShopActivity2.this, "phone", "") + "');");
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
                                MySharePreferenceUtil.put(ShopActivity2.this, "shopToken", shopTokenModel.getData().getToken());
                                String myurl = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl", "");
                                Log.d(myurl, "767676767");
                                String shopToken = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopToken", "");
                                Log.d(shopToken, "alskdf234234324");
                                if (myurl.contains("?")) {
                                    url = myurl + "&token=" + shopToken;
                                    mWebView.loadUrl(myurl + "&token=" + shopToken);
                                    String myurl2 = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl2", "");


                                    Log.d("3223323", myurl + "&token=" + shopToken);
                                } else {
//                                mWebView.loadUrl(myurl + "?token=" + shopToken);
                                    url = myurl + "&token=" + shopToken;
                                    mWebView.loadUrl(myurl + "?token=" + shopToken);
                                    String myurl2 = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl2", "");
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
        }
    };
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
        public void onProgressChanged(WebView webView, int i) {
            Log.e("进度", i + "");
//            Toast.makeText(MainActivity.this, newmyflag+"", Toast.LENGTH_SHORT).show();


            Animation animation = AnimationUtils.loadAnimation(ShopActivity2.this, R.anim.imganmi);

            LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
//            animation.setInterpolator(lin);


            if (newmyflag == 1) {

                animation.setDuration(500);
                imageView_loading.startAnimation(animation);
                if (i >= 50) {
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
        setContentView(R.layout.activity_shop);
        setImmersionStatus();

        img_network = (ImageView) findViewById(R.id.img_network);
        text_network = (TextView) findViewById(R.id.text_network);


        tm = (TelephonyManager) getApplication().getSystemService(TELEPHONY_SERVICE);
        mFrameLayout = (FrameLayout) findViewById(R.id.mFrameLayout);
        serviceIntent = new Intent(ShopActivity2.this, MyService.class);
        startService(serviceIntent);
        Log.d("devicetoken", getDeviceToken());
        mLocationManager = TencentLocationManager.getInstance(this);
        EventBus.getDefault().register(this);
        myphone = (String) MySharePreferenceUtil.get(ShopActivity2.this, "phone1", "");
        ImmersionBar.with(ShopActivity2.this)
                .statusBarDarkFont(false, 1f)
                .init();

        layout_network = (LinearLayout) findViewById(R.id.layout_network);
        immersionBar = ImmersionBar.with(this);
        title_view_new = findViewById(R.id.title_view_new);
        text_Devicetoken = (NewTextView) findViewById(R.id.text_Devicetoken);
        text_Devicetoken.setText(getDeviceToken());

        registerBoradcastReceiver1();
        text_Devicetoken.setTextIsSelectable(true);
        ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
//        cmb.setText(content.trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        cmb.getText();
        ImmersionBar.with(ShopActivity2.this)
                .statusBarDarkFont(true, 1f)
                .init();

        newmyflag = 1;
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        mWebView = (BridgeWebView) findViewById(R.id.webView);


        token = (String) MySharePreferenceUtil.get(ShopActivity2.this, "token", "");

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
            mWebView.setVisibility(View.VISIBLE);

            img_network.setImageResource(R.drawable.network);
            text_network.setText("数据加载失败,请检查一下你的网络");
            text_shijian.setText("点击重试");
        } else {
            layout_network.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
        }


    }

    public static int count = 0;


    public void method() {

        count++;


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


//        mWebView.destroy();

        String path = getFilesDir().getParent();
//        Toast.makeText(this, path+"", Toast.LENGTH_SHORT).show();
        stopService(serviceIntent);


    }


    public void onEventMainThread(Integer type) {
        Log.d("eventbus", type + "");

        myphone = (String) MySharePreferenceUtil.get(ShopActivity2.this, "phone1", "");
        token = (String) MySharePreferenceUtil.get(ShopActivity2.this, "token", "");
        flag = type;
        if (type == 222) {

//            ToastUtil.showCenterToast(ShopActivity.this, "3434");
            token = (String) MySharePreferenceUtil.get(ShopActivity2.this, "token", "");
            mWebView.loadUrl("javascript:localStorage.setItem('" + "token" + "','" + token + "');");
            if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(ShopActivity2.this, "idCard", ""))) {
                //未实名
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(ShopActivity2.this, "phone", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(ShopActivity2.this, "phone", "") + "');");
            } else {
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(ShopActivity2.this, "username", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(ShopActivity2.this, "phone", "") + "');");
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
                            MySharePreferenceUtil.put(ShopActivity2.this, "shopToken", shopTokenModel.getData().getToken());
                            String myurl = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl", "");
                            Log.d(myurl, "767676767");
                            String shopToken = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopToken", "");
                            Log.d(shopToken, "alskdf234234324");
                            if (myurl.contains("?")) {
                                url = myurl + "&token=" + shopToken;
                                mWebView.loadUrl(myurl + "&token=" + shopToken);
                                String myurl2 = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl2", "");


                                Log.d("3223323", myurl + "&token=" + shopToken);
                            } else {
//                                mWebView.loadUrl(myurl + "?token=" + shopToken);
                                url = myurl + "&token=" + shopToken;
                                mWebView.loadUrl(myurl + "?token=" + shopToken);
                                String myurl2 = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl2", "");
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

            String myurl = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl", "");
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
            String myurl = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl2", "");
//            https://t2wealth.xmrxcaifu.com/#/checkLoginPage?redirectUrl=https%3A%2F%2Fxindou.xmrxcaifu
            mWebView.loadUrl(myurl);
        }

    }


    public void setImmersionStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void selectImage(int result) {
        MultiImageSelector.create(ShopActivity2.this).showCamera(true) // 是否显示相机. 默认为显示
//                .count(1) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .single() // 单选模式
//                .multi() // 多选模式, 默认模式;
//                .origin(ArrayList<String>) // 默认已选择图片. 只有在选择模式为多选时有效
                .start(ShopActivity2.this, result);
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
        token = (String) MySharePreferenceUtil.get(ShopActivity2.this, "token", "");
        token = (String) MySharePreferenceUtil.get(ShopActivity2.this, "token", "");
        url = getIntent().getStringExtra("url");
//        mWebView.loadUrl("http://t2personal.xmrxcaifu.com:9081/#/checkLoginPage?redirectUrl=https%3a%2f%2fxindou.xmrxcaifu.com%2ftest%2fm%2fUsers.aspx");
        Log.d(url, "urlurl");
        mWebView.loadUrl(url);

//        mWebView.loadUrl("http://192.168.1.124:8077/#/checkLoginPage?redirectUrl=https://xindou.xmrxcaifu.com/test/m/GiftExchangeDetail.aspx?giftId=1792");
        text_shijian.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        text_shijian.getPaint().setAntiAlias(true);//抗锯齿
        text_shijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myflag = 1;
                if (!network()) {
                    mWebView.setVisibility(View.VISIBLE);
                    ImmersionBar.with(ShopActivity2.this)
                            .statusBarDarkFont(true, 1f)
                            .init();
                    img_network.setImageResource(R.drawable.network);
                    text_network.setText("数据加载失败,请检查一下你的网络");
                    text_shijian.setText("点击重试");
                    layout_network.setVisibility(View.VISIBLE);
                } else {
                    myflag = 1;
//                    mWebView.loadUrl(Constant.URL);
                }
            }
        });
        mWebView.setWebViewClient(new MyWebViewClient(mWebView));
        // set HadlerCallBack


        builder5 = new MyLoadingDialog.Builder(ShopActivity2.this)
                .setShowMessage(false)
                .setCancelable(false);
        dialog5 = builder5.create();


        myNewLoadingDialog = new MyNewLoadingDialog2(ShopActivity2.this);
        myNewLoadingBuilder = new MyNewLoadingDialog2.Builder(ShopActivity2.this);
        myNewLoadingDialog = myNewLoadingBuilder.create();


        Window window = myNewLoadingDialog.getWindow();
        imageView_loading = (ImageView) window.findViewById(R.id.img_zhuanquan);

        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);

        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();

//        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setDomStorageEnabled(true);//设置适应Html5 //重点是这个设置
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setUseWideViewPort(true);
//自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setSavePassword(false);

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


        mWebView.registerHandler("getAndroidExit", new

                BridgeHandler() {

                    @Override
                    public void handler(String data, CallBackFunction function) {
                        Toast.makeText(ShopActivity2.this, "返回上一级页面", Toast.LENGTH_SHORT).show();


                    }

                });


        mWebView.registerHandler("agreementBack", new

                BridgeHandler() {

                    @Override
                    public void handler(String data, CallBackFunction function) {
                        Toast.makeText(ShopActivity2.this, "返回", Toast.LENGTH_SHORT).show();
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


        mWebView.registerHandler("statusBlack", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {

                        //白的

                        ImmersionBar.with(ShopActivity2.this)

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


                        ImmersionBar.with(ShopActivity2.this)

                                .statusBarDarkFont(false, 1f)
                                .init();
                    }
                });


        mWebView.registerHandler("toXinDouLogin", new

                BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
//                        ToastUtil.showCenterToast(ShopActivity2.this, "toXinDouLogin");
                        Log.d(data, "toXinDouLogintoXinDouLogin");
                        MySharePreferenceUtil.put(ShopActivity2.this, "token", "");
                        Gson gson = new Gson();
                        ShopDetialModel shopDetialModel = new ShopDetialModel();
                        shopDetialModel = gson.fromJson(data, ShopDetialModel.class);

                        MySharePreferenceUtil.put(ShopActivity2.this, "shopUrl", shopDetialModel.getExternalLink());

                        Intent intent = new Intent(ShopActivity2.this, LoginShopActivity.class);

                        intent.putExtra("loginshop", "success");
                        startActivityForResult(intent, 5000);

                    }
                });


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
                            startActivity(new Intent(ShopActivity2.this, Class.forName(data)));
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

        mWebView.send("hello");


        mWebView.registerHandler("toExternalLink", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
//                Log.d(data.toString(), "3333333");
                Gson gson = new Gson();
                Wailian wailian = new Wailian();
                wailian = gson.fromJson(data.toString(), Wailian.class);
//                Intent intent = new Intent(MainActivity.this, MainActivity5.class);
                Intent intent = new Intent(ShopActivity2.this, MainActivity6.class);
                intent.putExtra("url", wailian.getExternalLink());
                intent.putExtra("title", wailian.getExternalTitle());
                startActivity(intent);
            }

        });
    }

    public void stopLocation() {
        mLocationManager.removeUpdates(ShopActivity2.this);
    }

    public String getdeviceId() {
        try {
            MySharePreferenceUtil.put(ShopActivity2.this, "deviceid", tm.getDeviceId());
//            Toast.makeText(MainActivity.this,  tm.getDeviceId()+"", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            MySharePreferenceUtil.put(ShopActivity2.this, "deviceid", "111111111");
        }
//        return tm.getDeviceId();
        String string = (String) MySharePreferenceUtil.get(ShopActivity2.this, "deviceid", "");
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
//        ToastUtil.showCenterToast(ShopActivity2.this, "requestCode" + requestCode + "resultCode" + requestCode + data.getStringExtra(""));
        Bundle MoonBuddle = data.getExtras();
        String string = MoonBuddle.getString("loginshop");
        suc_fail = MoonBuddle.getString("loginshop");

        Log.d(string, "string");
        Log.d(string, "string");
        if (string.equals("success")) {

            token = (String) MySharePreferenceUtil.get(ShopActivity2.this, "token", "");
            mWebView.loadUrl("javascript:localStorage.setItem('" + "token" + "','" + token + "');");
            if (TextUtils.isEmpty((String) MySharePreferenceUtil.get(ShopActivity2.this, "idCard", ""))) {
                //未实名
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(ShopActivity2.this, "phone", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(ShopActivity2.this, "phone", "") + "');");
            } else {
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userName" + "','" + MySharePreferenceUtil.get(ShopActivity2.this, "username", "") + "');");
                mWebView.loadUrl("javascript:localStorage.setItem('" + "userMobile" + "','" + MySharePreferenceUtil.get(ShopActivity2.this, "phone", "") + "');");
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
//                            {"meta":{"success":false,"message":"please login","code":"AUTHENTICATION_ERROR"}}
                            if (shopTokenModel.getMeta().getMessage().equals("please login")) {
                                String myurl = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl", "");
//            https://t2wealth.xmrxcaifu.com/#/checkLoginPage?redirectUrl=https%3A%2F%2Fxindou.xmrxcaifu
                                mWebView.loadUrl(myurl);
                            } else {

                                MySharePreferenceUtil.put(ShopActivity2.this, "shopToken", shopTokenModel.getData().getToken());
                                String myurl = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl", "");
                                Log.d(myurl, "767676767");
                                String shopToken = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopToken", "");
                                Log.d(shopToken, "alskdf234234324");
                                if (myurl.contains("?")) {
                                    url = myurl + "&token=" + shopToken;
                                    mWebView.loadUrl(myurl + "&token=" + shopToken);
                                    String myurl2 = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl2", "");

                                    Log.d("3223323", myurl + "&token=" + shopToken);
                                } else {
//                                mWebView.loadUrl(myurl + "?token=" + shopToken);
                                    url = myurl + "&token=" + shopToken;
                                    mWebView.loadUrl(myurl + "?token=" + shopToken);
                                    String myurl2 = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl2", "");
//            https://t2wealth.xmrxcaifu.com/#/checkLoginPage?redirectUrl=https%3A%2F%2Fxindou.xmrxcaifu
//                                mWebView.loadUrl(myurl2);
                                    Log.d("3223323", myurl + "&token=" + shopToken);
                                }
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
        } else if (string.equals("fail")) {
            String myurl = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl", "");
//            https://t2wealth.xmrxcaifu.com/#/checkLoginPage?redirectUrl=https%3A%2F%2Fxindou.xmrxcaifu
            mWebView.loadUrl(myurl);
        } else if (string.equals("fail2")) {
            String myurl = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl2", "");
            Log.d(myurl, "myurlmyurlmyurl");

            mWebView.loadUrl(myurl);
        }
        if (requestCode == 5000) {


        }


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
            FileProvider.getUriForFile(ShopActivity2.this, "com.xmrxcaifu.jsbridge.fileprovider", file);
        }
        cropImageUri = Uri.fromFile(file);
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
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

    String myCheckUrl = "";

    public void initdata() {
        ContactInfoService mContactInfoService = new ContactInfoService(ShopActivity2.this);
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
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            Log.d(error.toString(), "66666");

            handler.proceed();
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(url, "onPageStarted");

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url1) {
//
            Log.d(url1, "333333333333333333");
            Log.d(url1, "333333333333333333");


            MySharePreferenceUtil.put(ShopActivity2.this, "finishUrl", url1);

            super.onPageFinished(view, url);

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url1) {
            Log.d(url1, "555555555555555555555");
//            me://__BRIDGE_LOADED__: 555555555555555555555
            if (url1.contains("BRIDGE_LOADED")) {

            } else {
                myCheckUrl = url1;
            }

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

        private WeakReference<ShopActivity2> mActivity;

        private CustomShareListener(ShopActivity2 activity) {
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
        dialog = new Dialog(ShopActivity2.this);
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
            Intent intent = new Intent(ShopActivity2.this, ShopActivity2.class);
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

    String suc_fail = "success";
    String getMyIntentFlag = "0";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        /*    finish();
            mWebView.destroy();*/

//        if (myCheckUrl)
            String myurl = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl", "");
            String myurl2 = (String) MySharePreferenceUtil.get(ShopActivity2.this, "shopUrl2", "");
            Log.d(myurl, "myurlmyurl");
            Log.d(myurl2, "myurlmyurl");
//            GiftExchange_Flow.aspx:
            if (myurl2.contains("GiftExchangeDetail")) {
                if (getMyIntentFlag.equals("0") && myCheckUrl.contains("GiftExchange_Flow")) {
                    if (myurl2.contains("redirectUrl=")) {
                        myurl2 = myurl2.split("redirectUrl=")[1];
                        mWebView.loadUrl(myurl2);
                        getMyIntentFlag = "1";
                    }
                } else {
                    finish();
                }
            } else {

                if (suc_fail.contains("fail") && myurl.contains("giftId")) {
                    mWebView.loadUrl(myurl2);
                    suc_fail = "success";
                } else if (myCheckUrl.contains("Index")) {
                    finish();
                } else if (suc_fail.equals("success") && myurl.contains("GiftExchange_Flow")) {
                    mWebView.loadUrl(myurl2);
                } else if (suc_fail.equals("success") && myurl.contains("Users")) {
                    mWebView.loadUrl(myurl2);
                } else if (suc_fail.equals("success") && myurl.contains("giftId")) {
                    mWebView.loadUrl(myurl2);
                    suc_fail = "success";
                } else {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                }

            }


        }
        return false;
    }
}
