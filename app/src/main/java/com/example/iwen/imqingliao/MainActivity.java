package com.example.iwen.imqingliao;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.iwen.common.app.Activity;
import com.example.iwen.common.widget.PortraitView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends Activity {
    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_title)
    TextView mText;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

    }

    @Override
    protected void initData() {
        super.initData();
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick(){

    }

    @OnClick(R.id.btn_action)
    void onActionClick(){

    }
}