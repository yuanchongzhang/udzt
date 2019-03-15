package com.xmrxcaifu.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;


import com.xmrxcaifu.R;
import com.xmrxcaifu.mydifinedialog.AVLoadingIndicatorView;

/**
 * Created by tjy on 2017/6/19.
 */
public class MyNewLoadingDialog2 extends Dialog {


    public MyNewLoadingDialog2(Context context) {

//        super(context, R.style.myloading_dialog);
        super(context, R.style.dialog_normal);
    }

    public MyNewLoadingDialog2(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        private Context context;
        private String message;
        private boolean isShowMessage = true;
        private boolean isCancelable = false;
        private boolean isCancelOutside = false;


        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置提示信息
         *
         * @param message
         * @return
         */

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置是否显示提示信息
         *
         * @param isShowMessage
         * @return
         */
        public Builder setShowMessage(boolean isShowMessage) {
            this.isShowMessage = isShowMessage;
            return this;
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */

        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        /**
         * 设置是否可以取消
         *
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }

        public MyNewLoadingDialog2 create() {

            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.loading, null);




            MyNewLoadingDialog2 loadingDailog = new MyNewLoadingDialog2(context, R.style.dialog_normal);
//            TextView msgText = (TextView) view.findViewById(R.id.tipTextView);
            AVLoadingIndicatorView avLoadingIndicatorView= (AVLoadingIndicatorView) view.findViewById(R.id.progress_wheel);

            ImageView imageView= (ImageView) view.findViewById(R.id.img_zhuanquan);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.imganmi);
            LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
//            animation.setInterpolator(lin);
            animation.setDuration(400);

//            imageView.startAnimation(animation);

            avLoadingIndicatorView.setIndicator("BallBeatIndicator");


            if (isShowMessage) {
//                msgText.setText(message);
            } else {
//                msgText.setVisibility(View.GONE);
            }
            loadingDailog.setContentView(view);
            loadingDailog.setCancelable(isCancelable);
            loadingDailog.setCanceledOnTouchOutside(isCancelOutside);
            return loadingDailog;

        }


    }
}
