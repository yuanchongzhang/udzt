package com.xmrxcaifu.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


import com.xmrxcaifu.R;
import com.xmrxcaifu.frament.GuideFragment;
import com.xmrxcaifu.statusbar.ImmersionBar;
import com.xmrxcaifu.util.MySharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class V4GuideActivity extends FragmentActivity {

    private ViewPager v4_guide_view_pager;
    private MyPagerAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v4_guide_activity);
        ImmersionBar.with(V4GuideActivity.this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        MySharePreferenceUtil.put(V4GuideActivity.this, "guide", "3");
        MySharePreferenceUtil.put(V4GuideActivity.this, "flagloading", "10");
        initView();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    private void initView() {
        v4_guide_view_pager = (ViewPager) findViewById(R.id.v4_guide_view_pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        v4_guide_view_pager.setAdapter(adapter);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<String> catalogs = new ArrayList<String>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            catalogs.add("1");
            catalogs.add("2");
            catalogs.add("3");
            catalogs.add("4");
        }

        public CharSequence getPageTitle(int position) {
            return catalogs.get(position);
        }

        public int getCount() {
            return catalogs.size();
        }

        public Fragment getItem(int position) {
            return GuideFragment.newInstance(catalogs.get(position));
        }
    }

}
