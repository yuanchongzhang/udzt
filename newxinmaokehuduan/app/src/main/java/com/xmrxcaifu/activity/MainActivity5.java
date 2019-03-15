package com.xmrxcaifu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;

import com.tencent.map.geolocation.TencentLocationManager;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.xmrxcaifu.BaseActivty;
import com.xmrxcaifu.R;
import com.xmrxcaifu.Rom;
import com.xmrxcaifu.StatusBarUtil;
import com.xmrxcaifu.modle.User;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.view.MyNewLoadingDialog;

import java.io.File;
import java.util.Date;

public class MainActivity5 extends BaseActivty {

    private BridgeWebView mWebView;

    ValueCallback<Uri> mUploadMessage;

    int RESULT_CODE = 0;

    private TencentLocationManager mLocationManager;
    public final String USER_IMAGE_NAME = "image.png";
    public final String USER_CROP_IMAGE_NAME = "temporary.png";
    private static final String TAG = "MainActivity";

    private long mLastBackTime = 0;
    private long TIME_DIFF = 2 * 1000;
    private String p = null;
    private TelephonyManager tm;


    View title_view_new;

    MyNewLoadingDialog myNewLoadingDialog2;

    MyNewLoadingDialog.Builder myNewLoadingBuilder2;
    TextView text_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainnew3);
//        setImmersionStatus();
        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        ImmersionBar.with(MainActivity5.this)
                .statusBarDarkFont(true, 1f)
                .init();
        mLocationManager = TencentLocationManager.getInstance(this);
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        mWebView = (BridgeWebView) findViewById(R.id.webView);
        title_view_new = findViewById(R.id.title_view_new);
        title_view_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CookieSyncManager.createInstance(MainActivity5.this);
                CookieManager.getInstance().removeAllCookie();
                finish();
            }
        });
        text_cancel = (TextView) findViewById(R.id.text_cancel);
        text_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CookieSyncManager.createInstance(MainActivity5.this);
                CookieManager.getInstance().removeAllCookie();
                finish();
//                /data/data/com.xmrxcaifu/app_webview
                Log.d("3333333333333333333", "44444444444444444");
//                String path = getFilesDir().getParent();
//                deleteFolder(new File(path + "/app_webview"));


            }
        });
        getdeviceId();
        initWebView();

        myNewLoadingDialog2 = new MyNewLoadingDialog(MainActivity5.this);
        myNewLoadingBuilder2 = new MyNewLoadingDialog.Builder(MainActivity5.this);
        myNewLoadingDialog2 = myNewLoadingBuilder2.create();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myNewLoadingDialog2.dismiss();
        myNewLoadingDialog2 = null;

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
    public void setImmersionStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    private void initWebView() {
        // 设置具体WebViewClient
        mWebView.setWebViewClient(new MyWebViewClient(mWebView));
        // set HadlerCallBack
        mWebView.setDefaultHandler(new myHadlerCallBack());
        // setWebChromeClient
        mWebView.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.e("进度", newProgress + "");
//                dialog2.show();
//progeress
//                Toast.makeText(MainActivity.this, flag + "", Toast.LENGTH_SHORT).show();
                try {
                    if (newProgress == 100) {
//                    dialog2.dismiss();
                        myNewLoadingDialog2.dismiss();

                    } else {
                        myNewLoadingDialog2.show();
                    }


                } catch (Exception e) {

                }

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
                pickFile();
            }
        });

//        Toast.makeText(this, "他和eventbus哪个在前面", Toast.LENGTH_SHORT).show();

        mWebView.loadUrl(getIntent().getStringExtra("url"));
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);

    /*    String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();

        mWebView.getSettings().setAllowFileAccess(false);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCachePath(appCachePath);*/
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSavePassword(false);
        mWebView.registerHandler("submitFromWeb", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

                String str = "这是html返回给java的数据:" + data;
                // 例如你可以对原始数据进行处理
                str = str + ",Java经过处理后截取了一部分：" + str.substring(0, 5);
                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                Toast.makeText(MainActivity5.this, str, Toast.LENGTH_SHORT).show();
                //回调返回给Js
                function.onCallBack(str + ",Java经过处理后截取了一部分：" + str.substring(0, 5));
            }

        });
//        mWebView.addBridgeInterface(new MyJavaSctiptInterface(mWebView, this));//注册桥梁类，该类负责H5和android通信


        mWebView.registerHandler("functionOpen", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(MainActivity5.this, "网页在打开你的下载文件预览", Toast.LENGTH_SHORT).show();
                pickFile();

            }

        });


        mWebView.registerHandler("agreementBack", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

//                pickFile();
//                sendBroadcast(new Intent());
                Intent mIntent = new Intent(ACTION_NAME);
                mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
                //发送广播
                sendBroadcast(mIntent);
                MainActivity5.this.finish();
//                EventBus.getDefault().post(100);
            }

        });


        mWebView.registerHandler("useragent", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack("xxc_android");

            }

        });

        mWebView.registerHandler("deviceId", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

                function.onCallBack(getdeviceId());

            }

        });
        mWebView.registerHandler("mobileType", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Build build = new Build();

                function.onCallBack(build.MODEL);

            }

        });
        mWebView.registerHandler("statusBlack", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                StatusBarUtil.StatusBarLightMode(MainActivity5.this);
            }
        });


        mWebView.registerHandler("statusWhite", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                int a = 0;
                if (Rom.check(Rom.ROM_MIUI)) {
                    a = 1;
                } else if (Rom.check(Rom.ROM_FLYME)) {
                    a = 2;
                } else {
                    a = 3;
                }
                StatusBarUtil.StatusBarDarkMode(MainActivity5.this, a);
            }
        });


        mWebView.registerHandler("settings", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        mWebView.registerHandler("share", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        mWebView.registerHandler("activity", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    startActivity(new Intent(MainActivity5.this, Class.forName(data)));
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
        mWebView.callHandler("toast", new Gson().toJson(user), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

                Toast.makeText(MainActivity5.this, data, Toast.LENGTH_SHORT).show();

            }
        });
        mWebView.send("hello");

    }


    private final String ACTION_NAME = "注册协议";

    public String getdeviceId() {

        MySharePreferenceUtil.put(MainActivity5.this, "deviceid", tm.getDeviceId());

        return tm.getDeviceId();

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtil.StatusBarLightMode(this);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        CookieSyncManager.createInstance(MainActivity5.this);
        CookieManager.getInstance().removeAllCookie();
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            mWebView.evaluateJavascript("javascript:goBackForAndroid()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    Log.d("zzp", "--s--=" + s);


                    if ("\"finish\"".equals(s)) {

                        long now = new Date().getTime();
                        if (now - mLastBackTime < TIME_DIFF) {
                            CookieSyncManager.createInstance(MainActivity5.this);
                            CookieManager.getInstance().removeAllCookie();
                            finish();

                        } else {
                            mLastBackTime = now;
//                            Toast.makeText(MainActivity2.this, "再按一次退出鑫茂荣信财富", 2000).show();
                        }

                    } else if (s.equals("null")) {
                        finish();
                    }
                }
            });

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 自定义的WebViewClient
     */
    class MyWebViewClient extends BridgeWebViewClient {

        public MyWebViewClient(BridgeWebView webView) {
            super(webView);
        }
    }


    /**
     * 自定义回调
     */
    class myHadlerCallBack extends DefaultHandler {

        @Override
        public void handler(String data, CallBackFunction function) {
            if (function != null) {

                Toast.makeText(MainActivity5.this, data, Toast.LENGTH_SHORT).show();
                function.onCallBack("沈少");
            }
        }
    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    String url;


}
