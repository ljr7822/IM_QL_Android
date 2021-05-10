package com.example.iwen.LightChat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.iwen.LightChat.R;
import com.example.iwen.LightChat.fragments.personal.PersonalFragment;
import com.example.iwen.common.app.Activity;
import com.example.iwen.common.app.Fragment;

/**
 * 个人详情页面Activity
 */
public class PersonalActivity
        extends Activity {
    private static final String BOUND_KEY_ID = "BOUND_KEY_ID";
    private String userId;

    private Fragment mFragment;
    // 基本信息Fragment
    private PersonalFragment mPersonalFragment;
    // 修改密码Fragment
    private Fragment mChangePwdFragment;

    /**
     * show 方法
     *
     * @param context 上下文
     * @param userId  用户的id
     */
    public static void show(Context context, String userId) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra(BOUND_KEY_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        userId = bundle.getString(BOUND_KEY_ID);
        return !TextUtils.isEmpty(userId);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        Fragment fragment;
        fragment = new PersonalFragment();
        // 从Activity传递参数到Fragment中去
        Bundle bundle = new Bundle();
        bundle.putString(BOUND_KEY_ID, userId);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.lay_personal_container, fragment).commit();
    }
}
