package com.itheima09.wxface;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager pager;
    List<IconWithTextView> tabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = (ViewPager) findViewById(R.id.view_pager);
        IconWithTextView tab = (IconWithTextView) findViewById(R.id.tab_weixin);
        tab.setIconTextAlpha(1.0f);
        tab.setOnClickListener(this);
        tabs.add(tab);
        tab = (IconWithTextView) findViewById(R.id.tab_address);
        tabs.add(tab);
        tab.setOnClickListener(this);
        tab = (IconWithTextView) findViewById(R.id.tab_find);
        tabs.add(tab);
        tab.setOnClickListener(this);
        tab = (IconWithTextView) findViewById(R.id.tab_setting);
        tabs.add(tab);
        tab.setOnClickListener(this);


        //设置数据源
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        //设置滑动监听
        pager.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i("tag", "position:" + position);
        Log.i("tag", "positionOffset:" + positionOffset);//0.0f -> 1.0f
        if (positionOffset > 0) {
            //获取当前tab
            IconWithTextView currentTab = tabs.get(position);
            //获取下一个tab
            IconWithTextView nextTab = tabs.get(position + 1);
            currentTab.setIconTextAlpha(1 - positionOffset);//1.0f->0.0
            nextTab.setIconTextAlpha(positionOffset);
        }


    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        //处理tab的点击事件
        int position = 0;
        switch (v.getId()){
            case R.id.tab_weixin:
                position = 0;
                break;
            case R.id.tab_address:
                position = 1;
                break;
            case R.id.tab_find:
                position = 2;
                break;
            case R.id.tab_setting:
                position = 3;
                break;
        }
        //遍历tabs
        for (int i = 0; i < tabs.size(); i++) {
            IconWithTextView tab = tabs.get(i);
            if(position == i){
                tab.setIconTextAlpha(1.0f);
            }else{
                tab.setIconTextAlpha(0.0f);
            }
        }
        //设置pager当前页
        pager.setCurrentItem(position,false);

    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            if (position == 0) {
                f = new WeixinFragment();
            } else {
                f = new TabFragment();
            }

            return f;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
