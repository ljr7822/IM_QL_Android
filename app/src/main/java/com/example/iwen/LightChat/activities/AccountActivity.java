package com.example.iwen.LightChat.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.iwen.common.app.Activity;
import com.example.iwen.common.app.Fragment;
import com.example.iwen.LightChat.R;
import com.example.iwen.LightChat.fragments.account.AccountTrigger;
import com.example.iwen.LightChat.fragments.account.LoginFragment;
import com.example.iwen.LightChat.fragments.account.RegisterFragment;

/**
 * 账户
 *
 * @author : Iwen大大怪
 * create : 2020/11/14 18:20
 */
public class AccountActivity extends Activity implements AccountTrigger {
    private Fragment mFragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;
    private ImageView mBg;

    /**
     * 账户Activity的显示入口
     *
     * @param context 上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // 初始化Fragment
        mFragment = mLoginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mFragment)
                .commit();
        initView();
        // 初始化背景
        Glide.with(this)
                .load(R.mipmap.ic_img3)
                .centerCrop()
                .into(new CustomViewTarget<ImageView, Drawable>(mBg) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        // 拿到当前的drawable
//                        Drawable drawable = resource.getCurrent();
//                        // 使用适配包进行包装
//                        drawable = DrawableCompat.wrap(drawable);
//                        // 设置着色颜色和效果、蒙版模式
//                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent),
//                                PorterDuff.Mode.SCREEN);
//                        this.view.setImageDrawable(drawable);
                        this.view.setImageDrawable(resource.getCurrent());
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    // 切换具体的fragment
    @Override
    public void triggerView() {
        Fragment fragment;
        if (mFragment == mLoginFragment) {
            if (mRegisterFragment == null) {
                // 默认情况下为null
                // 第一次之后就不为null了
                mRegisterFragment = new RegisterFragment();
            }
            fragment = mRegisterFragment;
        } else {
            // 因为默认情况已经赋值，不需要判空
            fragment = mLoginFragment;
        }
        // 重新赋值当前正在显示的fragment
        mFragment = fragment;
        // 切换显示
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lay_container, fragment)
                .commit();
    }

    private void initView() {
        mBg = findViewById(R.id.im_bg);
    }
}