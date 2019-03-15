package com.xmrxcaifu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

import android.support.multidex.MultiDex;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.baidu.mobstat.StatService;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsDownloader;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.common.UmLog;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;
import com.xmrxcaifu.http.MyOkhttp;
import com.xmrxcaifu.http.cache.CacheEntity;
import com.xmrxcaifu.http.cache.CacheMode;
import com.xmrxcaifu.http.cookie.store.PersistentCookieStore;
import com.xmrxcaifu.http.model.HttpHeaders;
import com.xmrxcaifu.http.model.HttpParams;
import com.xmrxcaifu.modle.EventBean;
import com.xmrxcaifu.service.FirstLoadingX5Service;
import com.xmrxcaifu.service.MeizuTestReceiver;
import com.xmrxcaifu.util.MySharePreferenceUtil;
import com.xmrxcaifu.util.ToastUtil;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.mezu.MeizuRegister;
import org.android.agoo.xiaomi.MiPushRegistar;
import org.xutils.x;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import de.greenrobot.event.EventBus;

/**
 * Created by shenshao on 2018/2/23.
 */
public class App extends Application {

    private Handler handler;
    private static final String TAG = Application.class.getName();
    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";
    private List<Activity> oList;//用于存放所有启动的Activity的集合
    public static App application;
    private static Context context;
    private static final String TAG1 = "ggg";
    private static int mActivityCount;
    Timer timer = new Timer();
    private int recLen = 10000;
    private static int activityCounter = 0;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        //tbs 内核下载跟踪
        //判断是否要自行下载内核
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            recLen--;
            Message message = new Message();
            message.what = 1;
            handler11.sendMessage(message);
        }
    };

    @SuppressLint("HandlerLeak")
    final Handler handler11 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (recLen <= 0) {//倒计时结束
                        Log.e("ggg", "我倒计时结束了");
                    }
            }
        }
    };

    @Override
    public void onCreate() {
//        MultiDex.install(this);
        super.onCreate();
        closeAndroidPDialog();
        application = this;
        context = getApplicationContext();
        MySharePreferenceUtil.put(application, "isSpalash", "");
        //监控APP是在后台运行还是前台显示运行
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivityCount++;
                if(mActivityCount == 1) {
                    Log.e("ggg", "后台->前台");
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mActivityCount--;
                if(mActivityCount == 0) {
                    Log.e("ggg", "前台->后台");
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
      /*  HashMap map = new HashMap();

        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);

        QbSdk.initTbsSettings(map);*/


        CrashReport.initCrashReport(getApplicationContext(), "c2c0928b14", false);
   /*     preinitX5WithService();
        preinitX5WebCore();*/
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
//                ToastUtil.showToast(context, "onViewInitFinished");
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(this, cb);
        TbsDownloader.startDownload(context);
        //增加这句话56f3d7df6a
//        QbSdk.initX5Environment(this,null);
//        ExceptionHandler.getInstance().initConfig(this);


//        boolean needDownload = TbsDownloader.needDownload(this, TbsDownloader.DOWNLOAD_OVERSEA_TBS);


        // 打开调试开关，可以查看logcat日志。版本发布前，为避免影响性能，移除此代码
        // 查看方法：adb logcat -s sdkstat
//        StatService.setDebugOn(true);

        // 开启自动埋点统计，为保证所有页面都能准确统计，建议在Application中调用。
        // 第三个参数：autoTrackWebview：
        // 如果设置为true，则自动track所有webview；如果设置为false，则不自动track webview，
        // 如需对webview进行统计，需要对特定webview调用trackWebView() 即可。
        // 重要：如果有对webview设置过webchromeclient，则需要调用trackWebView() 接口将WebChromeClient对象传入，
        // 否则开发者自定义的回调无法收到。
        StatService.autoTrace(this, true, false);
        UMConfigure.init(this, "5913c832677baa7b9f000631", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "2e606d74d3f38f70d38404ad73583f7f");
//        MiPushRegistar.register(this, XIAOMI_ID, XIAOMI_KEY);

        FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(this);


        if (!managerCompat.isHardwareDetected()) { //判断设备是否支持
            MySharePreferenceUtil.put(this, "figer", "0");

        } else {


            MySharePreferenceUtil.put(this, "finger", "1");
        }
        //小米的配置
        MiPushRegistar.register(this, "2882303761517832762", "5761783293762");
//        MeizuRegister.register(Context context, String meizuAppId, String meizuAppKey);
//
        //魅族
        MeizuRegister.register(this, "114407", "5f310c1a504d4b86af14a39cfd9d3cfc");
        HuaWeiRegister.register(this);
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
        //-----------------------------------------------------------------------------------//
//        Fabric.with(this, new Crashlytics());
//        MeizuRegister.register(Context context, String meizuAppId, String meizuAppKey);

        MyOkhttp.init(this);
        UMShareAPI.get(this);
        Intent intent2 = new Intent(this, MeizuTestReceiver.class);
        getApplicationContext().startService(intent2);
        com.umeng.socialize.Config.DEBUG = true;
        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(this);
        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            MyOkhttp.getInstance()
                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    .debug("MyOkhttp", Level.INFO, true)

                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(MyOkhttp.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(MyOkhttp.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(MyOkhttp.DEFAULT_MILLISECONDS)    //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 QQ:91238141
                    .setCacheMode(CacheMode.NO_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3)

                    //如果不想让框架管理cookie（或者叫session的保持）,以下不需要
//                .setCookieStore(new MemoryCookieStore())            //cookie使用内存缓存（app退出后，cookie消失）
                    .setCookieStore(new PersistentCookieStore())        //cookie持久化存储，如果cookie不过期，则一直有效

                    //可以设置https的证书,以下几种方案根据需要自己设置
                    .setCertificates();                              //方法一：信任所有证书,不安全有风险
//                    .setCertificates(new SafeTrustManager())       ;     //方法二：自定义信任规则，校验服务端证书
//                    .setCertificates(getAssets().open("srca.cer"))      //方法三：使用预埋证书，校验服务端证书（自签名证书）
//                    //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//                    .setCertificates(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"))//

            //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//                    .setHostnameVerifier(new SafeHostnameVerifier())

            //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

            //这两行同上，不需要就不要加入
//                    .addCommonHeaders(headers)  //设置全局公共头
//                    .addCommonParams(params);   //设置全局公共参数

        } catch (Exception e) {
            e.printStackTrace();
        }
        hookWebView();
//        UMConfigure.init(this, "59892f08310c9307b60023d0", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "669c30a9584623e70e8cd01b0381dcb4");
        //开启ShareSDK debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式

        // PushSDK初始化(如使用推送SDK，必须调用此方法)

        PlatformConfig.setWeixin("wx5cced285560abcf0", "fb69d7a504f7c04e90d1800caaf3d8b6");
        PlatformConfig.setQQZone("1106083191", "OTduRMzZfbNRuoXb");
        oList = new ArrayList<Activity>();


        initUpush();
        x.Ext.init(this);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }


    public static void hookWebView() {
        int sdkInt = Build.VERSION.SDK_INT;
        try {
            Class<?> factoryClass = Class.forName("android.webkit.WebViewFactory");
            Field field = factoryClass.getDeclaredField("sProviderInstance");
            field.setAccessible(true);
            Object sProviderInstance = field.get(null);
            if (sProviderInstance != null) {
                Log.i(TAG, "sProviderInstance isn't null");
                return;
            }

            Method getProviderClassMethod;
            if (sdkInt > 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass");
            } else if (sdkInt == 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getFactoryClass");
            } else {
                Log.i(TAG, "Don't need to Hook WebView");
                return;
            }
            getProviderClassMethod.setAccessible(true);
            Class<?> factoryProviderClass = (Class<?>) getProviderClassMethod.invoke(factoryClass);
            Class<?> delegateClass = Class.forName("android.webkit.WebViewDelegate");
            Constructor<?> delegateConstructor = delegateClass.getDeclaredConstructor();
            delegateConstructor.setAccessible(true);
            if (sdkInt < 26) {//低于Android O版本
                Constructor<?> providerConstructor = factoryProviderClass.getConstructor(delegateClass);
                if (providerConstructor != null) {
                    providerConstructor.setAccessible(true);
                    sProviderInstance = providerConstructor.newInstance(delegateConstructor.newInstance());
                }
            } else {
                Field chromiumMethodName = factoryClass.getDeclaredField("CHROMIUM_WEBVIEW_FACTORY_METHOD");
                chromiumMethodName.setAccessible(true);
                String chromiumMethodNameStr = (String) chromiumMethodName.get(null);
                if (chromiumMethodNameStr == null) {
                    chromiumMethodNameStr = "create";
                }
                Method staticFactory = factoryProviderClass.getMethod(chromiumMethodNameStr, delegateClass);
                if (staticFactory != null) {
                    sProviderInstance = staticFactory.invoke(null, delegateConstructor.newInstance());
                }
            }

            if (sProviderInstance != null) {
                field.set("sProviderInstance", sProviderInstance);
                Log.i(TAG, "Hook success!");
            } else {
                Log.i(TAG, "Hook failed!");
            }
        } catch (Throwable e) {
            Log.w(TAG, e);
        }
    }

    private void initUpush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        handler = new Handler(getMainLooper());

        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        // sdk关闭通知声音
//		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        // 通知声音由服务端控制
//		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);
//		mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
//		mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
//                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();

                    }
                });
            }


            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public Notification getNotification(Context context, UMessage msg) {

                int requestCode = (int) System.currentTimeMillis();

                switch (msg.builder_id) {
                    case 1:
                        Notification.Builder builder = new Notification.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                                R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(
                                R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(
                                R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView)
                                .setSmallIcon(getSmallIconId(context, msg))
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);

                        return builder.getNotification();
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。

                        return super.getNotification(context, msg);
                }

            }


        };
        mPushAgent.setMessageHandler(messageHandler);
//        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                Log.d(msg.msg_id, "3333333333333333");
            }

            @Override
            public void openActivity(Context context, UMessage uMessage) {
                super.openActivity(context, uMessage);

                Log.d(uMessage.msg_id, "3333333333333333");
                for (Map.Entry<String, String> entry : uMessage.extra.entrySet()) {
                    String key = entry.getKey();

                    String value = entry.getValue();
                    if (key.equals("linkUrl")) {
                        if (TextUtils.isEmpty(value)) {
                        } else {
                            String serviceFlag = (String) MySharePreferenceUtil.get(context, "serviceflag", "");
                            if (serviceFlag.equals("1")) {
                                EventBean eventBean = new EventBean();
                                eventBean.setPostNum(300);
                                eventBean.setMsg(uMessage.extra.get("linkUrl"));
                                EventBus.getDefault().post(eventBean);
                            } else {
//                                Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                                //activity在外面的时候
                            /*    Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("url", uMessage.extra.get("linkUrl"));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);*/
                                EventBean eventBean = new EventBean();
                                eventBean.setPostNum(300);
                                eventBean.setMsg(uMessage.extra.get("linkUrl"));
                                EventBus.getDefault().post(eventBean);
                            }
                        }
                    }
                }
                if ("openWithWeb".equals(uMessage.extra.get("openRule"))) {
                }
            }


            @Override
            public void launchApp(Context context, UMessage uMessage) {
                super.launchApp(context, uMessage);
                Log.d(uMessage.msg_id, "3333333333333333");


                for (Map.Entry<String, String> entry : uMessage.extra.entrySet()) {
                    String key = entry.getKey();

                    String value = entry.getValue();
                    if (key.equals("linkUrl")) {
                        if (TextUtils.isEmpty(value)) {
                        } else {
                            String serviceFlag = (String) MySharePreferenceUtil.get(context, "serviceflag", "");
                            if (serviceFlag.equals("1")) {
                                EventBean eventBean = new EventBean();
                                eventBean.setPostNum(300);
                                eventBean.setMsg(uMessage.extra.get("linkUrl"));
                                EventBus.getDefault().post(eventBean);

//                                Toast.makeText(context, "传递的URL" + uMessage.extra.get("linkUrl"), Toast.LENGTH_SHORT).show();
                            } else {
//                                Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                                //activity在外面的时候
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("url", uMessage.extra.get("linkUrl"));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);

                            }


                        }


                    } else {

                    }


                }
                if ("openWithWeb".equals(uMessage.extra.get("openRule"))) {

                }
            }
        };
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知，参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);


        //注册推送服务 每次调用register都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
//                Toast.makeText(App.this, "成功了", Toast.LENGTH_SHORT).show();
//                Toast.makeText(App.this, deviceToken, Toast.LENGTH_SHORT).sho w();
//               AggqlPyqaTVz_d1m1emNWO42G56JypFjbQe9FbYDwhyT  //华为 AlEBBfselEzEyioNmphmQlwmlcPbqoZvgFNN4_x-uuBd  //小米AgSx3NTMz7Im5f5spaE_51bA_MYPVwQ94ggdvuB-732s
                Log.e("ggg", deviceToken);
                UmLog.i(TAG, "device token: " + deviceToken);
                MySharePreferenceUtil.put(App.this, "devicetoken", deviceToken);
                Log.d(deviceToken, deviceToken);
//                onViewInitFinished

                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }

            @Override
            public void onFailure(String s, String s1) {
                UmLog.i(TAG, "c: " + s + " " + s1);
                sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }
        });
    }


    @Override
    public void onTerminate() {
//        Toast.makeText(this, "结尾了", Toast.LENGTH_SHORT).show();
        String path = getFilesDir().getParent();
//        Toast.makeText(this, "onTerminate", Toast.LENGTH_SHORT).show();
        Log.d(path, "555555555555555555");

        super.onTerminate();

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);


    }


    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
// 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
//判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();

//        Toast.makeText(this, "2345", Toast.LENGTH_SHORT).show();
    }
//    dispatchActivityDestroyed

    private Intent addMessageToIntent(Intent intent, UMessage msg) {
        if (intent == null || msg == null || msg.extra == null)
            return intent;
        for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null)
                intent.putExtra(key, value);
        }
        return intent;
    }

    public static Context getContext() {
        return context;
    }


    private void preinitX5WithService() {

        Intent intent = new Intent(this, FirstLoadingX5Service.class);

        startService(intent);

    }


    private void preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {

            // preinit只需要调用一次，如果已经完成了初始化，那么就直接构造viewQbSdk.preInit(MainActivity.this, null);// 设置X5初始化完成的回调接口}
            QbSdk.preInit(context, null);
        }
    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
