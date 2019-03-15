package com.xmrxcaifu.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import com.xmrxcaifu.R;
import com.xmrxcaifu.statusbar.ImmersionBar;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.webkit.WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE;


/**
 *
 */

public class HongBaoWebActivity extends BaseActivty2 {
    WebView v4Webview;
    WebChromeClient chromeClient = new WebChromeClient() {


        @Override
        public void onProgressChanged(WebView webView, int i) {
            Log.e("进度", i + "");
            if (i == 100) {
                mydismissloading();
            } else {
                myshowloading();
            }
        }
    };
    private final String ACTION_NAME = "发送广播";
    String url;
    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            errmyCode = error.getErrorCode();
            if (errmyCode == -2) {
                layout_network.setVisibility(View.VISIBLE);
                ImmersionBar.with(HongBaoWebActivity.this)
                        .statusBarDarkFont(true, 1f)
                        .init();
                v4Webview.setVisibility(View.GONE);
            } else if (errmyCode == -8) {
                layout_network.setVisibility(View.VISIBLE);
                ImmersionBar.with(HongBaoWebActivity.this)
                        .statusBarDarkFont(true, 1f)
                        .init();
                v4Webview.setVisibility(View.GONE);
            } else {
                ImmersionBar.with(HongBaoWebActivity.this)
                        .statusBarDarkFont(true, 1f)
                        .init();
                layout_network.setVisibility(View.GONE);
                v4Webview.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            errmyCode = 0;
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
    @BindView(R.id.text_cancel)
    TextView textCancel;
    @BindView(R.id.text_name)
    TextView mTextName;
    @BindView(R.id.text_title)
    TextView textTitle;
    TextView text_share;
    int errmyCode = 0;
    LinearLayout layout_network;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    RelativeLayout top_layout1;
    RelativeLayout rl_back;
    ImageView img_back;
    TextView text_shijian;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hongbao);
        ButterKnife.bind(this);
        ImmersionBar.with(HongBaoWebActivity.this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        v4Webview = (WebView) findViewById(R.id.v4_webview);
        top_layout1 = (RelativeLayout) findViewById(R.id.top_layout1);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        text_shijian= (TextView) findViewById(R.id.text_shijian);

//        initWebView();
//        preView("https://xm-file.oss-cn-beijing.aliyuncs.com/file/bYeXc4CJH5.pdf");
        String mobile = getIntent().getStringExtra("teleNum");
        mShareListener = new HongBaoWebActivity.CustomShareListener(this);
        String maskNumber = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        mTextName.setText(getIntent().getStringExtra("name") + "\n" + maskNumber + "\n");
        textTitle.setText(getIntent().getStringExtra("title"));
        v4Webview.loadUrl(getIntent().getStringExtra("url"));
        layout_network = (LinearLayout) findViewById(R.id.layout_network);
        text_share = (TextView) findViewById(R.id.text_share);

        if (getIntent().getStringExtra("isShare").equals("0")) {
            text_share.setVisibility(View.GONE);
        } else {
            text_share.setVisibility(View.VISIBLE);
        }

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("PDF", "PDF");

        // 设置控件附加属性，用于无埋点对控件添加数据；控件绑定事件附加属性依赖于view.setTag(key, value)支持，使用key值：-96000；
        // 如果view已经绑定过此key值，则此设置不生效
        StatService.setAttributes(v4Webview, hashMap);
        text_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final ShareModel finalShareModel = shareModel;
                mShareAction = new ShareAction(HongBaoWebActivity.this).setDisplayList(
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ)

                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                UMWeb web = new UMWeb(getIntent().getStringExtra("pdfShareUrl"));
                                web.setTitle(getIntent().getStringExtra("title"));
                                web.setDescription(getIntent().getStringExtra("title"));
//                                    web.setThumb(new UMImage(MainActivity.this, R.mipmap.index));
//                                    web.setThumb(new UMImage(MainActivity.this, R.mipmap.index));
                                web.setThumb(new UMImage(HongBaoWebActivity.this, getIntent().getStringExtra("shareImg")));  //网络缩略图
                                new ShareAction(HongBaoWebActivity.this).withMedia(web)
                                        .setPlatform(share_media)
                                        .setCallback(mShareListener)
                                        .share();
                            }
                        });


                mShareAction.open();
            }
        });

        WebSettings ws = v4Webview.getSettings();
        screenSetting();
        ws.setUseWideViewPort(true);
        v4Webview.getSettings().setDomStorageEnabled(true);
        ws.setJavaScriptEnabled(true);
//        ws.setAllowFileAccess(true);
//        ws.setAllowFileAccessFromFileURLs(true);
//        ws.setAllowUniversalAccessFromFileURLs(true);
        ws.setSupportZoom(true); //设置可以支持缩放

        ws.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        ws.setSavePassword(false);
        ws.setBuiltInZoomControls(true);//设置出现缩放工具

        v4Webview.setWebViewClient(new MyWebViewClient());//设置用WebView打开内部链接


        v4Webview.clearCache(true);

//        v4Webview.setWebChromeClient(chromeClient);
        v4Webview.setWebChromeClient(chromeClient);
        v4Webview.getSettings().setUseWideViewPort(true);

        v4Webview.getSettings().setLoadWithOverviewMode(true);
        v4Webview.getSettings().setSavePassword(true);
        v4Webview.getSettings().setSaveFormData(true);
        v4Webview.getSettings().setJavaScriptEnabled(true);

        // enable navigator.geolocation
        v4Webview.getSettings().setGeolocationEnabled(true);
        v4Webview.getSettings().setGeolocationDatabasePath(
                "/data/data/org.itri.html5webview/databases/");

        v4Webview.requestFocus();
        v4Webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // v4_webview.setScrollBarStyle(0);
        String ua = v4Webview.getSettings().getUserAgentString();
        v4Webview.getSettings().setUserAgentString(
                ua + "; " + "mingtang_android");
        v4Webview.addJavascriptInterface(new HongBaoWebActivity.JsInterfaces(this),
                "AndroidWebView");
        v4Webview.getSettings().setBuiltInZoomControls(true);
        v4Webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        v4Webview.getSettings().setDomStorageEnabled(true);

        v4Webview.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v4Webview.getSettings().setMixedContentMode(
                    MIXED_CONTENT_COMPATIBILITY_MODE);
        }
//        v4Webview.getSettings().setAllowFileAccess(true);
        v4Webview.getSettings().setAppCacheEnabled(true);
        v4Webview.getSettings().setJavaScriptEnabled(true);
//        v4Webview.getSettings().setAppCachePath(appCachePath);
        v4Webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        v4Webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        v4Webview.getSettings().setSupportZoom(true);
        v4Webview.getSettings().setDefaultTextEncodingName("utf-8");
        v4Webview.getSettings().setDisplayZoomControls(false);

        v4Webview.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("newurl", url);

                view.loadUrl(url);
                return true;
            }

/*            @Override
            public void onReceivedSslError(WebView arg0, SslErrorHandler arg1,
                                           com.tencent.smtt.export.external.interfaces.SslError arg2) {
                // TODO Auto-generated method stub
//				super.onReceivedSslError(arg0, arg1, arg2);
                arg1.proceed();

            }*/

        });

        text_shijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!network()){
                    finish();
                }else {
                    v4Webview.loadUrl(getIntent().getStringExtra("url"));
                }
            }
        });
    }


    public void screenSetting() {
        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
        switch (screenDensity) {

            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;

            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;

            case DisplayMetrics.DENSITY_HIGH:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
        }
        v4Webview.getSettings().setDefaultZoom(zoomDensity);//webSettings.setDefaultZoom(zoomDensity);

    }

    /**
     * 预览pdf
     *
     * @param pdfUrl url或者本地文件路径
     */
    private void preView(String pdfUrl) {
        //1.只使用pdf.js渲染功能，自定义预览UI界面
        v4Webview.loadUrl("file:///android_asset/index.html?" + pdfUrl);
        //2.使用mozilla官方demo加载在线pdf
//        mWebView.loadUrl("http://mozilla.github.io/pdf.js/web/viewer.html?file=" + pdfUrl);
        //3.pdf.js放到本地
//        mWebView.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + pdfUrl);
        //4.使用谷歌文档服务
//        mWebView.loadUrl("http://docs.google.com/gviewembedded=true&url=" + pdfUrl);
    }

    public class JsInterfaces {
        public JsInterfaces(HongBaoWebActivity hongBaoWebActivity) {

        }
    }

    @OnClick({R.id.text_title, R.id.text_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_title:
                break;

            case R.id.text_cancel:
                finish();
                break;
        }
    }


    private static class CustomShareListener implements UMShareListener {

        private WeakReference<HongBaoWebActivity> mActivity;

        private CustomShareListener(HongBaoWebActivity activity) {
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
}
