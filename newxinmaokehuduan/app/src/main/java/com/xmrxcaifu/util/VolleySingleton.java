package com.xmrxcaifu.util;

import android.content.Context;

import com.xmrxcaifu.vo.Request;
import com.xmrxcaifu.vo.RequestQueue;
import com.xmrxcaifu.vo.toolbox.HttpClientStack;
import com.xmrxcaifu.vo.toolbox.HttpStack;
import com.xmrxcaifu.vo.toolbox.ImageLoader;
import com.xmrxcaifu.vo.toolbox.Volley;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;


public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;

    private VolleySingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();

        
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // 如果你的应用时常使用volley，那么传入的context最好是application的context
        	DefaultHttpClient httpclient = new DefaultHttpClient();
          CookieStore cookieStore = new BasicCookieStore();
          httpclient.setCookieStore(cookieStore);
          HttpStack httpStack = new HttpClientStack(httpclient);
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext(),httpStack);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}