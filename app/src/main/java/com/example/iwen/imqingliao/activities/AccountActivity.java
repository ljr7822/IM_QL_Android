package com.example.iwen.imqingliao.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.example.iwen.common.app.Activity;
import com.example.iwen.common.app.Fragment;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.fragments.account.UpdateInfoFragment;

/**
 *
 * <p>
 * author : Iwen大大怪
 * create : 2020/11/14 18:20
 */
public class AccountActivity extends Activity {
    private Fragment mFragment;

    /**
     * 账户Activity的显示入口
     * @param context 上下文
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // 初始化Fragment
        mFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,mFragment)
                .commit();
    }

    // 收到从Activity传过来的回调，取出其中的值进行图片加载
    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFragment.onActivityResult(requestCode,resultCode,data);
    }
}