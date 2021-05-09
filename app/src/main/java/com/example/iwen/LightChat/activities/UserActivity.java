package com.example.iwen.LightChat.activities;

import android.annotation.SuppressLint;
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
import com.example.iwen.LightChat.fragments.user.UpdateInfoFragment;

/**
 * 用户的Activity
 */
public class UserActivity extends Activity {
    private Fragment mFragment;
    private ImageView mBg;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    /**
     * UserActivity显示入口
     *
     * @param context 上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, UserActivity.class));
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // 初始化Fragment
        mFragment = new UpdateInfoFragment();
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
//                        drawable.setColorFilter(UiCompat.getColor(getResources(),R.color.colorAccent),
//                                PorterDuff.Mode.SCREEN);
//                        this.view.setImageDrawable(drawable);
                        this.view.setImageDrawable(resource.getCurrent());

                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    // 收到从Activity传过来的回调，取出其中的值进行图片加载
    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFragment.onActivityResult(requestCode, resultCode, data);
    }

    private void initView(){
        mBg = findViewById(R.id.im_bg);
    }
}