package com.example.iwen.LightChat.activities;

import android.content.Context;
import android.content.Intent;

import com.example.iwen.LightChat.R;
import com.example.iwen.common.app.Activity;

public class ChangeDataActivity extends Activity {

    /**
     * Show 方法
     * @param context 上下文
     */
    public static void show(Context context){
        Intent intent = new Intent(context, ChangeDataActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_change_data;
    }
}