package com.example.iwen.imqingliao.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.iwen.common.app.Activity;
import com.example.iwen.common.widget.PortraitView;
import com.example.iwen.factory.persistence.Account;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.fragments.main.ActiveFragment;
import com.example.iwen.imqingliao.fragments.main.ContactFragment;
import com.example.iwen.imqingliao.fragments.main.GroupFragment;
import com.example.iwen.imqingliao.helper.NavHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer> {
    // 标题
    @BindView(R.id.appbar)
    View mLayAppbar;
    // 头像
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    // 居中的文字
    @BindView(R.id.txt_title)
    TextView mTitle;
    // 显示内容部分
    @BindView(R.id.lay_container)
    FrameLayout mContainer;
    // 底部导航
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.btn_action)
    FloatActionButton mAction;

    private NavHelper<Integer> mNavHelper;

    /**
     * mainActivity显示入口
     *
     * @param context 上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (Account.isComplete()) {
            // 判断用户信息是否完善，完成则走正常流程
            return super.initArgs(bundle);
        } else {
            // 没有完善，就要更新
            UserActivity.show(this);
            return false;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // 初始化底部辅助工具类
        mNavHelper = new NavHelper<Integer>(this, R.id.lay_container, getSupportFragmentManager(), this);
        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));
        // 添加对底部导航按钮的监听
        mNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        // 第一次进去触发选择
        // 从底部导航接管Menu，进行手动触发第一次点击
        Menu menu = mNavigation.getMenu();
        // 触发首次选中home
        menu.performIdentifierAction(R.id.action_home, 0);
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick() {
        // 在群界面的时候，点击顶部的搜索就进入群搜索界面
        // 其他为人搜索界面
        int type = Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group)
                ? SearchActivity.TYPE_GROUP : SearchActivity.TYPE_USER;
        SearchActivity.show(this, type);
    }

    /**
     * 浮动按钮点击
     */
    @OnClick(R.id.btn_action)
    void onActionClick() {
        // 判断当前界面是群还是联系人
        // 如果是群，则打开群创建的界面
        if (Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group)) {
            // TODO 打开群创建界面
        } else {
            // 如果是其他，都打开添加用户的界面
            SearchActivity.show(this, SearchActivity.TYPE_USER);
        }
    }

    boolean isFirst = true;

    /**
     * Navigation选中事件
     * 当底部导航点击时触发
     *
     * @param item MenuItem
     * @return True 代表我们能够处理点击
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // 转接事件工具流到攻击类
        return mNavHelper.performClickMenu(item.getItemId());
    }

    /**
     * navHelper处理后回调的方法
     *
     * @param newTab 新Tab
     * @param oldTab 旧Tab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        // 从额外字段中取出我的title资源id
        mTitle.setText(newTab.extra);

        // 对浮动按钮进行隐藏、显示
        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            // 主界面时隐藏
            transY = Ui.dipToPx(getResources(), 76);
        } else {
            // 群
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else {
                // 联系人
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }
        // 开始动画
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(480)
                .start();
    }
}