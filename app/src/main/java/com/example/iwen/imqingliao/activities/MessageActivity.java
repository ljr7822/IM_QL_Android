package com.example.iwen.imqingliao.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iwen.common.factory.model.Author;
import com.example.iwen.imqingliao.R;

public class MessageActivity extends AppCompatActivity {

    /**
     * 显示某个人的连天界面
     *
     * @param context 上下文
     * @param author  某个用户
     */
    public static void show(Context context, Author author) {
        context.startActivity(new Intent(context, MessageActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }
}