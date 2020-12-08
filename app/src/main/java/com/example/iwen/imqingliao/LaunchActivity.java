package com.example.iwen.imqingliao;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import com.example.iwen.common.app.Activity;
import com.example.iwen.factory.persistence.Account;
import com.example.iwen.imqingliao.activities.AccountActivity;
import com.example.iwen.imqingliao.activities.MainActivity;
import com.example.iwen.imqingliao.fragments.assist.PermissionsFragment;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

public class LaunchActivity extends Activity {
    private ColorDrawable mBgDrawable;
    // 是否已经得到PushId
    private boolean mAlreadyGotPushReceiverId = false;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        View root = findViewById(R.id.activity_launch); // 拿到根布局
        int color = UiCompat.getColor(getResources(), R.color.colorPrimary); // 获取颜色
        ColorDrawable drawable = new ColorDrawable(color);// 创建一个Drawable
        root.setBackground(drawable);// 设置给背景
        mBgDrawable = drawable;
    }

    @Override
    protected void initData() {
        super.initData();
        // 动画进入到50%等待pushId获取到
        startAnim(0.5f, new Runnable() {
            @Override
            public void run() {
                // 检查等待
                waitPushReceiverId();
            }
        });
    }

    /**
     * 等待个推框架对我们的pushId设置好值
     */
    private void waitPushReceiverId(){
        if (Account.isLogin()){
            // 已经登录，判断是否绑定
            // 如果没有绑定则等待广播接收器进行绑定
            if (Account.isBind()){
                // 已经绑定
                skip();
                return;
            }
        }else {
            // 没有登录
            // 如果拿到了pushId,没有登录不能绑定
            if (!TextUtils.isEmpty(Account.getPushId())){
                skip();
                return;
            }
        }

        // 循环等待
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        },500);
    }

    /**
     * 跳转之前需要将剩下的50%进行完成
     */
    private void skip(){
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                reallySkip();
            }
        });
    }

    /**
     * 跳转方法
     */
    private void reallySkip(){
        // TODO 权限检查，跳转
        if (PermissionsFragment.haveAll(this, getSupportFragmentManager())) {
            if (Account.isLogin()){
                MainActivity.show(this);
            }else {
                AccountActivity.show(this);
                //MainActivity.show(this);
            }
            finish();
        }
    }

    /**
     * 给背景设置一个动画
     *
     * @param endProgress 动画结束进度
     * @param endCallback 动画结束时触发
     */
    private void startAnim(float endProgress, final Runnable endCallback) {
        // 获取一个最终的颜色
        int finalColor = Resource.Color.WHITE;
        // 运算当前进度的颜色
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        int endColor = (int) argbEvaluator.evaluate(endProgress, mBgDrawable.getColor(), finalColor);
        // 构建一个属性动画
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, argbEvaluator, endColor);
        valueAnimator.setDuration(1500);
        valueAnimator.setIntValues(mBgDrawable.getColor(), endColor);//开始结束设置
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 结束时触发
                endCallback.run();
            }
        });
        valueAnimator.start();
    }

    /**
     * 参数的变化
     */
    private final Property<LaunchActivity, Object> property
            = new Property<LaunchActivity, Object>(Object.class, "color") {
        @Override
        public Object get(LaunchActivity launchActivity) {
            return launchActivity.mBgDrawable.getColor();
        }

        @Override
        public void set(LaunchActivity object, Object value) {
            object.mBgDrawable.setColor((Integer) value);
        }
    };
}