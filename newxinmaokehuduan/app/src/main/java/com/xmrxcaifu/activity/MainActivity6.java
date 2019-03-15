package com.xmrxcaifu.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;


import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.xmrxcaifu.BaseActivty;
import com.xmrxcaifu.Constant;
import com.xmrxcaifu.MainActivity;
import com.xmrxcaifu.R;
import com.xmrxcaifu.StatusBarUtil;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.util.ToastUtil;
import com.xmrxcaifu.view.MyNewLoadingDialog2;
import com.zhuge.analysis.stat.ZhugeSDK;

import de.greenrobot.event.EventBus;

import static android.webkit.WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE;


public class MainActivity6 extends BaseActivty implements TencentLocationListener {


    private BridgeWebView mWebView;

    ValueCallback<Uri> mUploadMessage;

    private TencentLocationManager mLocationManager;

    private static final String TAG = "MainActivity";

    private TelephonyManager tm;
    private String mPhone = null;

    Button btn_login;
    String token;
    private final String ACTION_NAME = "智投宝结束";

    private String mShareData;


    private FrameLayout mFrameLayout;
    TextView text_cancel1;
    TextView text_title1;
    String title;
    MyNewLoadingDialog2 myNewLoadingDialog2;
    MyNewLoadingDialog2.Builder myNewLoadingBuilder2;
    ImageView imageView_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

//        ToastUtil.showCenterToast(this, "广告");
        ZhugeSDK.getInstance().openLog();
        ImmersionBar.with(this)
                .statusBarDarkFont(false, 1f)
                .init();
        try {
            title = getIntent().getStringExtra("title");
        } catch (Exception e) {

        }

        try {
            if (TextUtils.isEmpty(title)) {
                text_title1.setText("鑫茂理财师");
            } else {
                text_title1.setText(title);
            }
        } catch (Exception e) {

        }

        myNewLoadingDialog2 = new MyNewLoadingDialog2(MainActivity6.this);
        myNewLoadingBuilder2 = new MyNewLoadingDialog2.Builder(MainActivity6.this);
        myNewLoadingDialog2 = myNewLoadingBuilder2.create();
        Window window = myNewLoadingDialog2.getWindow();
        imageView_loading = (ImageView) window.findViewById(R.id.img_zhuanquan);

        Animation animation = AnimationUtils.loadAnimation(MainActivity6.this, R.anim.imganmi);
        animation.setDuration(500);
        imageView_loading.startAnimation(animation);


        text_cancel1 = (TextView) findViewById(R.id.text_cancel1);

        ImmersionBar.with(MainActivity6.this)
                .statusBarDarkFont(false, 1f)
                .init();
//        registerBoradcastReceiver();
        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
//        });toLogin跳转登录的方法

        mLocationManager = TencentLocationManager.getInstance(this);


        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        mWebView = (BridgeWebView) findViewById(R.id.webView);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity6.this, LoginActivity.class));
//                selectImage(REQUEST_PHOTO);

            }
        });

        mFrameLayout = (FrameLayout) findViewById(R.id.mFrameLayout);


//        updateData 登录回来之后更新
        token = (String) MySharePreferenceUtil.get(MainActivity6.this, "token", "");
        text_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initWebView();

        String url = getIntent().getStringExtra("url");
        Log.d("myurlsmyurls", url);

        mWebView.loadUrl(url);


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        token = (String) MySharePreferenceUtil.get(MainActivity6.this, "token", "");

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            finish();


            return true;
        }


        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
        ImmersionBar.with(this).destroy();
    }


    //    private LoadingDialogGIF mLoadingDialog2;


    WebChromeClient chromeClient = new WebChromeClient() {
        private View mCustomView;
        private IX5WebChromeClient.CustomViewCallback mCustomViewCallback;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mFrameLayout.setBackground(getResources().getDrawable(R.color.black));
            mCustomView = view;
            mFrameLayout.addView(mCustomView);
            mCustomViewCallback = callback;
            mWebView.setVisibility(View.GONE);

//横屏
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onHideCustomView() {
            mWebView.setVisibility(View.VISIBLE);
            if (mCustomView == null) {
                return;
            }
            //竖屏
            mFrameLayout.setBackground(getResources().getDrawable(R.color.black));
            mCustomView.setVisibility(View.GONE);
            mFrameLayout.removeView(mCustomView);
            mCustomViewCallback.onCustomViewHidden();
            mCustomView = null;

//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onHideCustomView();
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newmyflag == 1) {
            }
            try {
                if (newProgress == 100) {
//                    dialog2.dismiss();
//                  mydismissloading();
                    myNewLoadingDialog2.dismiss();
                } else {
//                 myshowloading();
                    myNewLoadingDialog2.show();
                }

            } catch (Exception e) {
            }

            super.onProgressChanged(view, newProgress);
            Log.e("进度", newProgress + "");
        }

        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
            this.openFileChooser(uploadMsg);
        }

        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
            this.openFileChooser(uploadMsg);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;

        }

    };


    int newmyflag = 1;

    private void initWebView() {
        // 设置具体WebViewClient


        mWebView.setWebViewClient(new MyWebViewClient(mWebView));
        // set HadlerCallBack
        mWebView.setDefaultHandler(new myHadlerCallBack());
        // setWebChromeClient
        mWebView.setWebChromeClient(chromeClient);


        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
//		WebView.setWebContentsDebuggingEnabled(true);
        // This next one is crazy. It's the DEFAULT location for your app's
        // cache
        // But it didn't work for me without this line.
        // UPDATE: no hardcoded path. Thanks to Kevin Hawkins
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();

//        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCachePath(appCachePath);

//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.getSettings().setUseWideViewPort(true);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSavePassword(true);
        mWebView.getSettings().setSaveFormData(true);
        mWebView.getSettings().setSavePassword(false);
        // enable navigator.geolocation
        mWebView.getSettings().setGeolocationEnabled(true);
        mWebView.getSettings().setGeolocationDatabasePath(
                "/data/data/org.itri.html5webview/databases/");

        mWebView.requestFocus();
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // v4_webview.setScrollBarStyle(0);
        String ua = mWebView.getSettings().getUserAgentString();
        mWebView.getSettings().setBuiltInZoomControls(true);
//        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(
                    MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        mWebView.getSettings().setAppCacheEnabled(true);
//        v4Webview.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//		v4_webview.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.getSettings().setDisplayZoomControls(false);


    }


    public String getdeviceId() {
        String deviceId;
        if (tm.getDeviceId() != null) {
            deviceId = tm.getDeviceId();
        } else {
            //android.provider.Settings;
            deviceId = Settings.Secure.getString(this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }


        return tm.getDeviceId();

    }

    @Override
    protected void onResume() {
        super.onResume();

        StatusBarUtil.StatusBarLightMode(this);

    }

    public void startLocation(CallBackFunction callBackFunction) {

        // 创建定位请求
        TencentLocationRequest request = TencentLocationRequest.create();

        // 修改定位请求参数, 定位周期 3000 ms

        mLocationManager.requestLocationUpdates(TencentLocationRequest.create().setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_NAME).setInterval(500).setAllowDirection(true), this);
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {

    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }


    /**
     * 自定义的WebViewClient
     */
    class MyWebViewClient extends BridgeWebViewClient {
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        public MyWebViewClient(BridgeWebView webView) {
            super(webView);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //在这里执行你想调用的js函数
            if (!Constant.URL.equals(url))
                EventBus.getDefault().post(1012);


        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }
    }


    /**
     * 自定义回调
     */
    class myHadlerCallBack extends DefaultHandler {

        @Override
        public void handler(String data, CallBackFunction function) {
            if (function != null) {

                Toast.makeText(MainActivity6.this, data, Toast.LENGTH_SHORT).show();
                function.onCallBack("沈少");
            }
        }

    }


}
