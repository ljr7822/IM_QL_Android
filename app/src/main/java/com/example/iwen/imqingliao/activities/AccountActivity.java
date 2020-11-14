package com.example.iwen.imqingliao.activities;

import android.content.Context;
import android.content.Intent;

import com.example.iwen.common.app.Activity;
import com.example.iwen.imqingliao.R;
import com.example.iwen.imqingliao.fragments.account.UpdateInfoFragment;

/**
 *
 * <p>
 * author : Iwen大大怪
 * create : 2020/11/14 18:20
 */
public class AccountActivity extends Activity {

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
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,new UpdateInfoFragment())
                .commit();
    }
}