package com.xmrxcaifu.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
	static Toast mToast = null;
	public static void showToast(Context context, String text) {
        if(mToast == null) {  
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {  
            mToast.setText(text);    
            mToast.setDuration(Toast.LENGTH_SHORT);
        }  
        mToast.show();  
    }  
	
	public static void showCenterToast(Context context, String text){
		if(mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(text);    
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();  
	}
}
