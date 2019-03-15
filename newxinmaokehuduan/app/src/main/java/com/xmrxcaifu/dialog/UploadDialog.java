package com.xmrxcaifu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xmrxcaifu.R;


/**
 * 创建自定义的dialog，主要学习其实现原理
 * Created by chengguo on 2016/3/22.
 */
public class UploadDialog extends Dialog {

    private TextView titleTv;//消息标题文本
    private TextView messageTv;//消息提示文本
    private String titleStr;//从外界设置的title文本


    private TextView versionName;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;
    private View mViewLine;
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器
    private TextView mBtnUpdata;
    private String mVersionName;


    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(onYesOnclickListener onYesOnclickListener) {

        this.yesOnclickListener = onYesOnclickListener;
    }

    public UploadDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_dialog_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        mBtnUpdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听

    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {

        if (messageStr != null && messageStr.length() > 0) {
            messageTv.setText(messageStr);
        }
        if (mVersionName != null && mVersionName.length() > 0) {
            versionName.setText(mVersionName);
        }

    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        versionName = (TextView) findViewById(R.id.tv_version_name);
        messageTv = (TextView) findViewById(R.id.message);
        mBtnUpdata = (TextView) findViewById(R.id.btn_updata);
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    public void setVersionName(String versionName) {
        mVersionName = versionName;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

}