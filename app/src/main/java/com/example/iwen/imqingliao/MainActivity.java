package com.example.iwen.imqingliao;

import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.iwen.common.app.Activity;
import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.imqingliao.fragments.main.ActiveFragment;
import com.example.iwen.imqingliao.fragments.main.ContactFragment;
import com.example.iwen.imqingliao.fragments.main.GroupFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_title)
    TextView mTitle;

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

        mNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick() {

    }

    @OnClick(R.id.btn_action)
    void onActionClick() {

    }

    boolean isFirst = true;

    /**
     * Navigation选中事件
     *
     * @param item 哪个Item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            // 改变Title
            mTitle.setText(R.string.title_home);
            ActiveFragment activeFragment = new ActiveFragment();
            if (isFirst) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.lay_container, activeFragment)
                        .commit();
                isFirst = false;
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.lay_container, activeFragment)
                        .commit();

            }
        } else if (item.getItemId() == R.id.action_group) {
            // 改变Title
            mTitle.setText(R.string.action_group);
            GroupFragment groupFragment = new GroupFragment();

            if (isFirst) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.lay_container, groupFragment)
                        .commit();
                isFirst = false;
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.lay_container, groupFragment)
                        .commit();
            }
        }else if (item.getItemId() == R.id.action_contact){
            // 改变Title
            mTitle.setText(R.string.action_contact);
            ContactFragment contactFragment = new ContactFragment();

            if (isFirst){
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.lay_container, contactFragment)
                        .commit();
                isFirst = false;
            }else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.lay_container, contactFragment)
                        .commit();
            }
        }
        // 改变Title
        mTitle.setText(item.getTitle());
        // 变更内容

        return true;
    }
}