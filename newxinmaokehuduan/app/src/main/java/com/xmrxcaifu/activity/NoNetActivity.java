package com.xmrxcaifu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.xmrxcaifu.R;
import com.xmrxcaifu.statusbar.ImmersionBar;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2018/4/16.
 */
public class NoNetActivity extends BaseActivty2 {

    @BindView(R.id.rl_no_net)
    RelativeLayout mRlNoNet;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_top_layout)
    RelativeLayout rlTopLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        ImmersionBar.with(NoNetActivity.this)
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        EventBus.getDefault().unregister(this);

    }

    @OnClick(R.id.rl_no_net)
    public void onViewClicked() {
        EventBus.getDefault().post(3);
//        finish();
    }

    private long mLastBackTime = 0;
    private static final long TIME_DIFF = 2 * 1000;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            long now = new Date().getTime();
            if (now - mLastBackTime < TIME_DIFF) {
                EventBus.getDefault().post(106);
//                finish();
                return super.onKeyDown(keyCode, event);
            } else {
                mLastBackTime = now;
                Toast.makeText(NoNetActivity.this, "再按一次退出鑫茂理财师", 2000).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onEventMainThread(Integer type) {
        if (type == 1012) {
            finish();
        }
    }

    @OnClick({R.id.img_back, R.id.rl_back, R.id.tv_title, R.id.rl_top_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                EventBus.getDefault().post(201);
                finish();

                break;
            case R.id.rl_back:
                EventBus.getDefault().post(201);
                finish();
                break;
            case R.id.tv_title:
                break;
            case R.id.rl_top_layout:
                break;
        }
    }
}
