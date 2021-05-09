package com.example.iwen.LightChat;

import android.content.Context;

import com.example.iwen.LightChat.activities.AccountActivity;
import com.example.iwen.common.app.Application;
import com.example.iwen.factory.Factory;
import com.igexin.sdk.PushManager;

/**
 * @author : Iwen大大怪
 * @create : 2020/11/15 2:05
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 调用Factory进行初始化
        Factory.setup();
        // 推送初始化
        PushManager.getInstance().initialize(this);
    }

    /**
     * 复写方法：退出登录后跳转到登录界面
     *
     * @param context 上下文
     */
    @Override
    protected void showAccountView(Context context) {
        // 进行登录界面的显示
        AccountActivity.show(context);
    }
}
