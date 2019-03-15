package com.xmrxcaifu.mydifinedialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.xmrxcaifu.R;


public class WebViewDialog extends Dialog {

    private Activity mActivity;
    //private TextView loadingText;
    AVLoadingIndicatorView avLoadingIndicatorView;
    public WebViewDialog(Activity context) {
        super(context, R.style.dialog_normal);
        mActivity = context;
        setContentView(R.layout.loading);//loading

        setProperty();
        setCancelable(true);
        Window window = context.getWindow();
        avLoadingIndicatorView= (AVLoadingIndicatorView) findViewById(R.id.progress_wheel);
        avLoadingIndicatorView.setIndicator("BallClipRotateIndicator");

//		getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);  
        //loadingText = (TextView) findViewById(R.id.loading_text);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

	/*public void setLoadingMessage(String message){
		if(!TextUtils.isEmpty(message)){
			loadingText.setText(message);
		}
	}*/

    /*public void show(String message) {
        if(!TextUtils.isEmpty(message)){
            loadingText.setText(message);
        }
        super.show();
    }
*/
    @Override
    public void show() {
        // TODO Auto-generated method stub
        super.show();
        handler.sendEmptyMessageDelayed(1, 1000);
//        show();
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            show();
        }

        ;
    };

    public void close() {
       /* if (!mActivity.isFinishing()) {
            mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    if (isShowing()) {

                    }
                }
            });
        }*/
        dismiss();
        avLoadingIndicatorView.hide();
    }

    private void setProperty() {
        Window window = getWindow();
        WindowManager.LayoutParams p = window.getAttributes();
        Display d = getWindow().getWindowManager().getDefaultDisplay();
        p.height = (int) (d.getHeight() * 1);
        p.width = (int) (d.getWidth() * 1);
        window.setAttributes(p);
    }
}
