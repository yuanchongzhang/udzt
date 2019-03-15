package com.xmrxcaifu.volleryhttp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.xmrxcaifu.common.LoadingDialog;
import com.xmrxcaifu.vo.AuthFailureError;
import com.xmrxcaifu.vo.DefaultRetryPolicy;
import com.xmrxcaifu.vo.RequestQueue;
import com.xmrxcaifu.vo.Response;
import com.xmrxcaifu.vo.VolleyError;
import com.xmrxcaifu.vo.toolbox.JsonObjectRequest;
import com.xmrxcaifu.vo.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 沈少
 *
 */
public abstract class VolleryUtil {

	private LoadingDialog ld;
	private RequestQueue mQueue;
	private Map<String, String> headers = new HashMap<String, String>();
	private boolean isShow;
	private ConnectivityManager manager;
	private int requestMethod = 1;
	private OnButtonListener onButtonListener;
//   private ProgressLayout view;
   private SharedPreferences sp ;
	public void setRequestMethod(int requestMethod) {
		this.requestMethod = requestMethod;
	}
   
	public boolean isShow() {
		return isShow;
	}
	
	public interface OnButtonListener{
		public void change();
	}
	public void OnButtonListener(OnButtonListener OnButtonListener){
		this.onButtonListener=OnButtonListener;
		
	}
	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public void setHeaders(String key, String values) {
		headers.put(key, values);
	}
	
	      
	/**
	 * 网络访问错误
	 */
	public abstract void onError();

	/**
	 * loading框
	 */
	// public abstract void onLoading();

	/**
	 * 更新界面
	 */
	public abstract void onUpDate(JSONObject jsonObject);

	private void doInBackground(final Activity context, String url,
			String params) {
		 
//	            isNetworkAvailable();  
	       
		StringBuffer sb = new StringBuffer();
		if (ld == null) {
			ld = new LoadingDialog(context);
//			view.showProgress();
		}
		if (params.equals("")) {
			sb.append(url);
		} else {
			sb.append(url);
			sb.append(params);
		}
		if (isShow) {
			try {
				ld.show();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(context);
//			File cacheDir = new File(context.getCacheDir(), volley);
//	        DiskBasedCache cache = new DiskBasedCache(cacheDir);
//	        mQueue.start();
//
//	        // clear all volley caches.
//	        mQueue.add(new ClearCacheRequest(cache, null));
		}
		// System.out.println("vollery====" + sb.toString());
		// get 0 post 1
		JsonObjectRequest json = new JsonObjectRequest(requestMethod,
				sb.toString(), null, new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						onUpDate(response);
//						ld.close();
						
						
					}
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						onError();
						Log.e("错误", error.toString());
//						ld.close();
//						view.showContent();
					}
				}) { 
			public Map<String, String> getHeaders() throws AuthFailureError {
				headers.put("UserAgent", "rmbbox_android");
				try {
					headers.put("token", sp.getString("token", ""));
				}catch (Exception e){

				}



				headers.put("client", "android");
				return headers;
			}
		};
//		BbtApplication.getInstance().getRequestQueue().add(json);
		mQueue.add(json);
//		try {
//			LogUtils.e(VolleryUtil.this, json.getHeaders().toString());
//		} catch (AuthFailureError e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		json.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	        
	        }
	private void doInBackgroundlayout(final Activity context, String url,
			String params) {
		 
//	            isNetworkAvailable();  
	       
		StringBuffer sb = new StringBuffer();
		if (ld == null) {
//			ld = new LoadingDialog(context);
//			view.showProgress();
		}
		if (params.equals("")) {
			sb.append(url);
		} else {
			sb.append(url);
			sb.append(params);
		}
		if (isShow) {
			try {
//				ld.show();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(context);
//			File cacheDir = new File(context.getCacheDir(), volley);
//	        DiskBasedCache cache = new DiskBasedCache(cacheDir);
//	        mQueue.start();
//
//	        // clear all volley caches.
//	        mQueue.add(new ClearCacheRequest(cache, null));
		}
		// System.out.println("vollery====" + sb.toString());
		// get 0 post 1
		JsonObjectRequest json = new JsonObjectRequest(requestMethod,
				sb.toString(), null, new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						onUpDate(response);
//						ld.close();
					
						
						
					}
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						onError();
						Log.e("错误", error.toString());
//						ld.close();
//						view.showContent();
					}
				}) { 
			public Map<String, String> getHeaders() throws AuthFailureError {
				headers.put("UserAgent", "rmbbox_android");
				headers.put("token", sp.getString("token", ""));
				return headers;
			}
		};
//		BbtApplication.getInstance().getRequestQueue().add(json);
		mQueue.add(json);
		
		json.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   
	        
	        }

	public void execute(Activity context, String url, String params) {
		boolean flag = false;  
		ld = null;
        //得到网络连接信息  
        manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        //去进行判断网络是否连接  
        if (manager.getActiveNetworkInfo() != null) {  
            flag = manager.getActiveNetworkInfo().isAvailable();  
        }  
        if (!flag) {  
        	Toast.makeText(context, "网络未连接", 2000).show();
        	Log.e("onbutton", onButtonListener+"--");
        	if(onButtonListener!=null){
//        		Log.e("onbutton", "不为空");
        		onButtonListener.change();
        	}
        	return;
//            setNetwork();  
        	
        	
        }else{
        	doInBackground(context, url, params);
        	sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
//        	Log.e("走doinback", "走doinback");
        }
        
	}
//	public void executelayout(Activity context, String url, String params,ProgressLayout view) {
//		boolean flag = false;
//		ld = null;
//		this.view=view;
//        //得到网络连接信息
//        manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        //去进行判断网络是否连接
//        if (manager.getActiveNetworkInfo() != null) {
//            flag = manager.getActiveNetworkInfo().isAvailable();
//        }
//        if (!flag) {
//        	Toast.makeText(context, "网络未连接", 2000).show();
//        	Log.e("onbutton", onButtonListener+"--");
//        	if(onButtonListener!=null){
////        		Log.e("onbutton", "不为空");
//        		onButtonListener.change();
//        	}
//        	return;
////            setNetwork();
//
//
//        }else{
//        	sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
//        	doInBackgroundlayout(context, url, params);
////        	Log.e("走doinback", "走doinback");
//        }
//
//	}
}
